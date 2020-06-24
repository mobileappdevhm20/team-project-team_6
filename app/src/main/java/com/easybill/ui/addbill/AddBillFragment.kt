package com.easybill.ui.addbill

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.easybill.MainViewModel
import com.easybill.R
import com.easybill.misc.generateFakeBills
import com.google.android.gms.vision.Frame
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_add.view.*
import java.io.IOException

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
        Toast.makeText(this.activity, "Bill added", Toast.LENGTH_SHORT).show()
        setInitialViewsState()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    photoView.setImageURI(result.uri)
                    //val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, result.uri)
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
}
