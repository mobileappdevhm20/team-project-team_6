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
import com.easybill.misc.generateFakeBills
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_add.view.*
import java.io.IOException


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

        setInitialViewsState()

        return root
    }

    private fun setInitialViewsState() {
        scanButton.isEnabled = false
        confirmButton.isEnabled = false
        photoViewContainer.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bill_picture_container_init))
        photoView.setImageURI(null)
    }

    private fun openCamera() {
        CropImage.activity()
            .setActivityTitle("Crop Photo")
            .setAllowFlipping(false)
            .start(requireContext(), this)
        setInitialViewsState()
    }

    private fun scanPhoto() {
        // TODO use OCR to scan the photo
        val result = recognizer.process(image)
            .addOnSuccessListener { visionText ->
                // Task completed successfully
                // ...
                println("##### Success!")
                photoViewContainer.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bill_picture_container_success))
                scanButton.isEnabled = false
                confirmButton.isEnabled = true
                processBillText(visionText)
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
                println("##### Failure!")
                e.printStackTrace()
                photoViewContainer.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bill_picture_container_fail))
                Toast.makeText(this.activity, "Scan failed", Toast.LENGTH_SHORT).show()
            }
        //BillRecognizer.startCameraSource(requireContext(),
        //    Frame.Builder().setBitmap(imageBitmap).build())
    }

    private fun confirmPhoto() {
        // TODO confirm and save the bill
        Toast.makeText(this.activity, "Bill added (not really)", Toast.LENGTH_LONG).show()
        setInitialViewsState()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    photoView.setImageURI(result.uri)
                    imageBitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, result.uri)
                    //photoView.setImageBitmap(bitmap)
                    try {
                        image = InputImage.fromFilePath(requireContext(), result.uri)
                        //imageBitmap = bitmap
                        //image = InputImage.fromBitmap(bitmap, 0)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    photoViewContainer.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bill_picture_container))
                    scanButton.isEnabled = true
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
        val bottom: Float)

    private fun processBillText(visionText: Text) {

        val headLine = "#".repeat(50)
        val subLine = "-".repeat(50)

        var regexForTotal: MutableList<String> = mutableListOf(".*total.*", ".*sum.*", ".*summe.*")
        var regexForAddress: MutableList<String> = mutableListOf(".*straße.*", ".*str.*", ".*street.*")

        val shopNameBlock = visionText.textBlocks[0]
        var shopName: BillValueEntry = BillValueEntry(
            text = shopNameBlock.lines[0].text,
            textX = shopNameBlock.lines[0].boundingBox?.left!!.toFloat(),
            textY = shopNameBlock.lines[0].boundingBox?.bottom!!.toFloat(),
            textSize = shopNameBlock.lines[0].boundingBox?.bottom!!.toFloat() - shopNameBlock.lines[0].boundingBox?.top!!.toFloat(),
            left = shopNameBlock.boundingBox?.left!!.toFloat(),
            right = shopNameBlock.boundingBox?.right!!.toFloat(),
            top = shopNameBlock.boundingBox?.top!!.toFloat(),
            bottom = shopNameBlock.boundingBox?.bottom!!.toFloat()
        )

        var addressList: MutableList<BillValueEntry>
        var totalMap: MutableMap<BillValueEntry, BillValueEntry>
        var otherEntries: MutableList<BillValueEntry> = mutableListOf()

        println(headLine)

        for (block in visionText.textBlocks) {
            // Get values --------------------------------------------------------------------------
            val blockText = block.text
            val blockFrame = block.boundingBox
            // Print -------------------------------------------------------------------------------
            println("\nBlock text: $blockText")
            println("Frame:\n\tLeft: ${blockFrame?.left}\n\tRight: ${blockFrame?.right}\n\tTop: ${blockFrame?.top}\n\tBottom: ${blockFrame?.bottom}")
            println(subLine)
            // -------------------------------------------------------------------------------------
            for (line in block.lines) {
                val lineText = line.text
                val lineFrame = line.boundingBox
                // ----------
                otherEntries.add(BillValueEntry(
                    text = lineText,
                    textX = lineFrame?.left!!.toFloat(),
                    textY = lineFrame?.bottom!!.toFloat(),
                    textSize = lineFrame?.bottom!!.toFloat() - lineFrame?.top!!.toFloat(),
                    left = blockFrame?.left!!.toFloat(),
                    top = blockFrame?.top!!.toFloat(),
                    right = blockFrame?.right!!.toFloat(),
                    bottom = blockFrame?.bottom!!.toFloat()
                ))
                // ----------
            }
        }

        println(headLine)
        drawBillContentToPicture(shopName, null, null, null, otherEntries)
    }

    private fun drawBillContentToPicture(
        shopName: BillValueEntry?,
        address: BillValueEntry?,
        total: BillValueEntry?,
        totalValue: BillValueEntry?,
        other: MutableList<BillValueEntry>?)
    {
        var bitmap: Bitmap = Bitmap.createBitmap(imageBitmap.width,imageBitmap.height, Bitmap.Config.RGB_565)
        val billCanvas = Canvas(bitmap)

        billCanvas.drawBitmap(imageBitmap, 0f, 0f, null)

        val shopNameColor = ResourcesCompat.getColor(resources, R.color.colorGreenSuccess, null)
        val addressColor = ResourcesCompat.getColor(resources, R.color.colorRedFail, null)
        val totalColor = ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
        val otherColor = ResourcesCompat.getColor(resources, R.color.colorLightGrey, null)

        val paint = Paint().apply {
            // Smooths out edges of what is drawn without affecting shape.
            isAntiAlias = true
            // Dithering affects how colors with higher-precision than the device are down-sampled.
            isDither = true
            style = Paint.Style.STROKE // default: FILL
            strokeJoin = Paint.Join.ROUND // default: MITER
            strokeCap = Paint.Cap.ROUND // default: BUTT
            strokeWidth = 5f // default: Hairline-width (really thin)
        }

        // draw all other
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

        // draw address if not null
        if (address != null) {
            paint.color = addressColor
            paint.style = Paint.Style.STROKE
            billCanvas.drawRoundRect(RectF(address.left, address.top, address.right, address.bottom), 0f, 0f, paint)
            paint.style = Paint.Style.FILL
            paint.textSize = address.textSize
            billCanvas.drawText(address.text, address.textX, address.textY, paint)
        }

        // draw total
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
