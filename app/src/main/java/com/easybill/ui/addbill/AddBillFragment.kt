package com.easybill.ui.addbill

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.easybill.MainViewModel
import com.easybill.R
import com.easybill.data.model.Bill
import com.easybill.data.model.BillHeader
import com.easybill.data.model.BillItem
import com.easybill.misc.generateFakeBills
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_add.view.*
import timber.log.Timber
import java.io.IOException
import java.time.LocalDateTime
import java.util.Locale
import kotlin.math.absoluteValue

/**
 * Uses the Android-Image-Cropper library to take a picture and crop it
 * Source: https://github.com/ArthurHub/Android-Image-Cropper
 * Uses the ML Kit to process a picture of a bill
 * Source: https://developers.google.com/ml-kit/vision/text-recognition/android#top_of_page
 */
class AddBillFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    private lateinit var photoViewContainer: LinearLayout
    private lateinit var photoView: ImageView
    private lateinit var scanButton: Button
    private lateinit var confirmButton: Button

    private lateinit var image: InputImage
    private lateinit var imageBitmap: Bitmap
    private lateinit var recognizer: TextRecognizer

    private var newBill: Bill? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recognizer = TextRecognition.getClient()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_add, container, false)

        val deleteButton: Button = root.findViewById(R.id.delete_all_bills)
        deleteButton.setOnClickListener {
            val bills = viewModel.bills.value
            if (bills != null)
                for (bill in bills)
                    viewModel.deleteBillById(bill.header.headerId)
        }

        val generateButton: Button = root.findViewById(R.id.generate_random_bills)
        generateButton.setOnClickListener {
            val bills = generateFakeBills(100)
            for (bill in bills)
                viewModel.addBill(bill)
        }

        val cameraButton: Button = root.findViewById(R.id.camera_button)
        cameraButton.setOnClickListener { openCamera() }

        scanButton = root.findViewById(R.id.scan_button)
        scanButton.setOnClickListener { scanPhoto() }

        confirmButton = root.findViewById(R.id.confirm_button)
        confirmButton.setOnClickListener { confirmPhoto() }

        photoView = root.photo_image_view
        photoViewContainer = root.photo_view_container

        setInitialViewState()

        return root
    }

    /**
     * Set the views in the fragment to the initial state
     */
    private fun setInitialViewState() {
        scanButton.isEnabled = false
        confirmButton.isEnabled = false
        photoViewContainer.setBackground(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.bill_picture_container_init
            )
        )
        photoView.setImageBitmap(null)
    }

    /**
     * Set the views in the fragment to the picture ready state
     * when taking and cropping a picture was successful.
     */
    private fun setPictureReadyViewState() {
        photoViewContainer.setBackground(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.bill_picture_container
            )
        )
        scanButton.isEnabled = true
    }

    /**
     * Set the views in the fragment to the success state if the ocr scan was successful
     */
    private fun setSuccessViewState() {
        photoViewContainer.setBackground(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.bill_picture_container_success
            )
        )
        scanButton.isEnabled = false
        confirmButton.isEnabled = true
    }

    /**
     * Set the views in the fragment to the filed state if the ocr scan failed
     */
    private fun setFailedViewState() {
        photoViewContainer.setBackground(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.bill_picture_container_fail
            )
        )
        scanButton.isEnabled = false
    }

    /**
     * Uses the android image cropper library to open the camera application,
     * take a photo and pass it to the image cropper.
     */
    private fun openCamera() {
        CropImage.activity()
            .setActivityTitle("Crop Photo")
            .setAllowFlipping(false)
            .start(requireContext(), this)
        setInitialViewState()
    }

    /**
     * Uses the ML Kit to recognize the characters in the image.
     */
    private fun scanPhoto() {
        val result = recognizer.process(image)
            .addOnSuccessListener { visionText ->
                processBillText(visionText)
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                setFailedViewState()
                Toast.makeText(this.activity, "Scan failed", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * The user can confirm the scan result and add the bill to the database.
     */
    private fun confirmPhoto() {
        if (newBill != null) {
            viewModel.addBill(newBill!!)
            Toast.makeText(this.activity, "Bill added", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this.activity, "Bill added (not really)", Toast.LENGTH_LONG).show()
        }
        setInitialViewState()
    }

    /**
     * Receive the result from android image cropper and store it
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    imageBitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, result.uri)
                    photoView.setImageBitmap(imageBitmap)
                    // photoView.setImageURI(result.uri)
                    try {
                        image = InputImage.fromFilePath(requireContext(), result.uri)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    setPictureReadyViewState()
                }
            }
            else -> {
                Toast.makeText(this.activity, "Unrecognized request code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    class BillValueEntry(
        val text: String,
        val textX: Float,
        val textY: Float,
        val textSize: Float,
        var left: Float,
        val top: Float,
        val right: Float,
        val bottom: Float
    )

    private fun processBillText(visionText: Text) {

        val regexForTotal: MutableList<Regex> = mutableListOf(Regex(".*total.*"), Regex(".*sum.*"), Regex(".*summe.*"), Regex(".*eur.*"))
        val regexForAddress: MutableList<Regex> = mutableListOf(Regex(".*straße.*"), Regex(".*str.*"), Regex(".*street.*"))

        var shopName = ""
        var address = ""
        var totalPrice = -1.0

        var shopNameEntry: BillValueEntry? = null

        if (visionText.textBlocks.size != 0) {
            val shopNameBlock = visionText.textBlocks[0]
            shopName = shopNameBlock.lines[0].text
            shopNameEntry = BillValueEntry(
                text = shopNameBlock.lines[0].text,
                textX = shopNameBlock.lines[0].boundingBox?.left!!.toFloat(),
                textY = shopNameBlock.lines[0].boundingBox?.bottom!!.toFloat(),
                textSize = shopNameBlock.lines[0].boundingBox?.bottom!!.toFloat() - shopNameBlock.lines[0].boundingBox?.top!!.toFloat(),
                left = shopNameBlock.boundingBox?.left!!.toFloat(),
                right = shopNameBlock.boundingBox?.right!!.toFloat(),
                top = shopNameBlock.boundingBox?.top!!.toFloat(),
                bottom = shopNameBlock.boundingBox?.bottom!!.toFloat()
            )
        }

        val addressList: MutableList<BillValueEntry> = mutableListOf()
        val totalList: MutableList<BillValueEntry> = mutableListOf()
        val otherEntries: MutableList<BillValueEntry> = mutableListOf()

        val headLine = "#".repeat(50)
        val subLine = "-".repeat(50)
        Timber.d(headLine)

        for (block in visionText.textBlocks) {
            // Get values --------------------------------------------------------------------------
            val blockText = block.text
            val blockFrame = block.boundingBox
            // Log ---------------------------------------------------------------------------------
            Timber.d("\nBlock text: $blockText")
            Timber.d("Frame:\n\tLeft: ${blockFrame?.left}\n\tRight: ${blockFrame?.right}\n\tTop: ${blockFrame?.top}\n\tBottom: ${blockFrame?.bottom}")
            Timber.d(subLine)
            // -------------------------------------------------------------------------------------
            for (line in block.lines) {
                val lineText = line.text
                val lineFrame = line.boundingBox
                // check if line matches street regex
                for (regex in regexForAddress) {
                    if (lineText.toLowerCase(Locale.ROOT).matches(regex)) {
                        addressList.add(
                            BillValueEntry(
                                text = lineText,
                                textX = lineFrame?.left!!.toFloat(),
                                textY = lineFrame.bottom.toFloat(),
                                textSize = lineFrame.bottom.toFloat() - lineFrame.top.toFloat(),
                                left = blockFrame?.left!!.toFloat(),
                                right = blockFrame.right.toFloat(),
                                top = blockFrame.top.toFloat(),
                                bottom = blockFrame.bottom.toFloat()
                            )
                        )
                        break
                    }
                }
                // check if line matches total regex
                for (regex in regexForTotal) {
                    if (lineText.toLowerCase(Locale.ROOT).matches(regex)) {
                        totalList.add(
                            BillValueEntry(
                                text = lineText,
                                textX = lineFrame?.left!!.toFloat(),
                                textY = lineFrame.bottom.toFloat(),
                                textSize = lineFrame.bottom.toFloat() - lineFrame.top.toFloat(),
                                left = blockFrame?.left!!.toFloat(),
                                right = blockFrame.right.toFloat(),
                                top = blockFrame.top.toFloat(),
                                bottom = blockFrame.bottom.toFloat()
                            )
                        )
                        break
                    }
                }
                // add to other if does not match anything
                otherEntries.add(
                    BillValueEntry(
                        text = lineText,
                        textX = lineFrame?.left!!.toFloat(),
                        textY = lineFrame.bottom.toFloat(),
                        textSize = lineFrame.bottom.toFloat() - lineFrame.top.toFloat(),
                        left = blockFrame?.left!!.toFloat(),
                        top = blockFrame.top.toFloat(),
                        right = blockFrame.right.toFloat(),
                        bottom = blockFrame.bottom.toFloat()
                    )
                )
            }
        }

        // check if total list is not empty and try to find a fitting price
        var totalLabelEntry: BillValueEntry? = null
        var totalPriceEntry: BillValueEntry? = null
        var found = false
        if (totalList.size != 0) {
            for (totalEntry in totalList) {
                for (otherEntry in otherEntries) {
                    // if the text is on the same height, try convert to number
                    if ((otherEntry.textY - totalEntry.textY).absoluteValue < 10f) {
                        try {
                            totalPrice = otherEntry.text.replace(",", ".", false).toDouble()
                            totalLabelEntry = totalEntry
                            totalPriceEntry = otherEntry
                            found = true
                        } catch (e: NumberFormatException) {
                            // ignore
                        }
                    }
                    if (found) { break }
                }
                if (found) { break }
            }
        }

        Timber.d(headLine)
        var addressEntry: BillValueEntry? = null
        if (addressList.size != 0) {
            address = addressList[0].text
            addressEntry = addressList[0]
        }

        drawBillContentToPicture(shopNameEntry, addressEntry, totalLabelEntry, totalPriceEntry, otherEntries)

        // create new bill if required values exist
        if (!shopName.isEmpty() && totalPrice > 0) {
            createNewBill(shopName, address, totalPrice)
            setSuccessViewState()
        } else {
            setFailedViewState()
            Toast.makeText(this.activity, "Please take a new photo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createNewBill(shopName: String, address: String, totalPrice: Double) {
        newBill = Bill(
            header = BillHeader(
                companyName = shopName,
                address = address,
                dateTime = LocalDateTime.now()
            ),
            items = listOf(
                BillItem(
                    name = "unknown",
                    amount = 1.0,
                    price = totalPrice
                )
            )
        )
    }

    private fun drawBillContentToPicture(
        shopName: BillValueEntry?,
        address: BillValueEntry?,
        total: BillValueEntry?,
        totalValue: BillValueEntry?,
        other: MutableList<BillValueEntry>?
    ) {
        var bitmap: Bitmap = Bitmap.createBitmap(imageBitmap.width, imageBitmap.height, Bitmap.Config.RGB_565)
        val billCanvas = Canvas(bitmap)

        // billCanvas.drawBitmap(imageBitmap, 0f, 0f, null)

        val shopNameColor = ResourcesCompat.getColor(resources, R.color.colorShopName, null)
        val addressColor = ResourcesCompat.getColor(resources, R.color.colorAddress, null)
        val totalColor = ResourcesCompat.getColor(resources, R.color.colorTotal, null)
        val otherColor = ResourcesCompat.getColor(resources, R.color.colorOther, null)
        val backgroundColor = ResourcesCompat.getColor(resources, R.color.colorScanBackground, null)

        val paint = Paint().apply {
            // Smooths out edges of what is drawn without affecting shape.
            isAntiAlias = true
            // Dithering affects how colors with higher-precision than the device are down-sampled.
            isDither = true
            style = Paint.Style.STROKE // default: FILL
            strokeJoin = Paint.Join.ROUND // default: MITER
            strokeCap = Paint.Cap.ROUND // default: BUTT
            strokeWidth = 3f // default: Hairline-width (really thin)
        }

        // draw the background
        paint.color = backgroundColor
        paint.style = Paint.Style.FILL
        billCanvas.drawRoundRect(RectF(0f, 0f, imageBitmap.width.toFloat(), imageBitmap.height.toFloat()), 0f, 0f, paint)

        paint.style = Paint.Style.STROKE

        // draw unrecognized entries
        if (other != null) {
            paint.color = otherColor
            for (otherEntry in other) {
                paint.style = Paint.Style.STROKE
                billCanvas.drawRoundRect(RectF(otherEntry.left, otherEntry.top, otherEntry.right, otherEntry.bottom), 0f, 0f, paint)
                paint.style = Paint.Style.FILL
                paint.textSize = otherEntry.textSize
                billCanvas.drawText(otherEntry.text, otherEntry.textX, otherEntry.textY, paint)
            }
        }

        // draw shop name
        if (shopName != null) {
            paint.color = shopNameColor
            paint.style = Paint.Style.STROKE
            billCanvas.drawRoundRect(RectF(shopName.left, shopName.top, shopName.right, shopName.bottom), 0f, 0f, paint)
            paint.style = Paint.Style.FILL
            paint.textSize = shopName.textSize
            billCanvas.drawText(shopName.text, shopName.textX, shopName.textY, paint)
        }

        // draw address
        if (address != null) {
            paint.color = addressColor
            paint.style = Paint.Style.STROKE
            billCanvas.drawRoundRect(RectF(address.left, address.top, address.right, address.bottom), 0f, 0f, paint)
            paint.style = Paint.Style.FILL
            paint.textSize = address.textSize
            billCanvas.drawText(address.text, address.textX, address.textY, paint)
        }

        // draw total price
        if (total != null && totalValue != null) {
            paint.color = totalColor
            paint.style = Paint.Style.STROKE
            billCanvas.drawRoundRect(RectF(total.left, total.top, total.right, total.bottom), 0f, 0f, paint)
            billCanvas.drawRoundRect(RectF(totalValue.left, totalValue.top, totalValue.right, totalValue.bottom), 0f, 0f, paint)
            paint.style = Paint.Style.FILL
            paint.textSize = total.textSize
            billCanvas.drawText(total.text, total.textX, total.textY, paint)
            paint.textSize = totalValue.textSize
            billCanvas.drawText(totalValue.text, totalValue.textX, totalValue.textY, paint)
        }

        photoView.setImageBitmap(bitmap)
    }
}
