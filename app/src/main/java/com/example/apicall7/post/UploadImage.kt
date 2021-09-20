package com.example.apicall7.post

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import com.example.apicall7.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class UploadImage : AppCompatActivity(), UploadRequestBody.UploadCallback {
    lateinit var imageView: ImageView
    lateinit var progressBar: ProgressBar
    lateinit var buttonUpload: Button
    lateinit var layoutRoot: View
    lateinit var statusResponse:TextView
    lateinit var home: ImageButton
    private val requestCode = 1



    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_image)

        imageView = findViewById(R.id.imageView)
        progressBar = findViewById(R.id.progress_bar)
        buttonUpload = findViewById(R.id.upload)
        layoutRoot = findViewById(R.id.layout_root)
        statusResponse = findViewById(R.id.textView)
        home = findViewById(R.id.homeButton)

        //permission request to upload image
        imageView.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this@UploadImage,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, Array(1) {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }, requestCode)
            } else {
                statusResponse.text = "Permission Granted! Please, choose an image"
                openImageChooser()
            }
        }

        buttonUpload.setOnClickListener {
            uploadImage()
            Thread.sleep(5000)
            buttonUpload.text = "Successful, Upload Again"
        }
        home.setOnClickListener{
            var intent = Intent(this@UploadImage, MainActivity::class.java)
            startActivity(intent)
        }

    }
    //image selector function
    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_PICK_IMAGE)
        }
    }

    //override the function onActivityResult() to get the selected image.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    selectedImageUri = data?.data
                    imageView.setImageURI(selectedImageUri)
                }
            }
        }
    }
    //function to upload image
    private fun uploadImage() {
        if (selectedImageUri == null) {
            layoutRoot?.snackbar("Select an Image First")
            return
        }

        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        progressBar.progress = 0
        val body = UploadRequestBody(file, "image", this)
        MyAPI().uploadImage(
            MultipartBody.Part.createFormData(
                "file",
                file.name,
                body
            ),
            //RequestBody.create(MediaType.parse("multipart/form-data"), "json")
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "json")
        ).enqueue(object : retrofit2.Callback<UploadResponse> {
            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                layoutRoot?.snackbar(t.message!!)
                progressBar.progress = 0
            }

            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                response.body()?.let {
                    layoutRoot?.snackbar(it.message)
                    progressBar.progress = 100

                }
                //logging on success
                Log.d("google", response.body()?.status.toString())
            }
        })
    }
    //on progress update function override
    override fun onProgressUpdate(percentage: Int) {
        progressBar.progress = percentage
    }
    //constant to use to get selected image
    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 101
    }
}