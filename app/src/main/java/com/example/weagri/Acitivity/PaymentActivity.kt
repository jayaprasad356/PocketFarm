package com.example.weagri.Acitivity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.canhub.cropper.CropImage
import com.example.weagri.R
import com.example.weagri.databinding.ActivityPaymentBinding
import com.example.weagri.helper.ApiConfig
import com.example.weagri.helper.Constant
import com.example.weagri.helper.Session
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class PaymentActivity : AppCompatActivity() {
    lateinit var binding: ActivityPaymentBinding
    lateinit var activity: Activity
    lateinit var session: com.example.weagri.helper.Session

    var filePath1: String? = null
    var imageUri: Uri? = null

    private val REQUEST_IMAGE_GALLERY = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPaymentBinding.inflate(layoutInflater)
        activity = this
        session = com.example.weagri.helper.Session(activity)

        binding.cvImage.setOnClickListener {
            pickImageFromGallery()

        }

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }


        binding.btnUpload.setOnClickListener {
            if (filePath1 != null) {
                recharge()
            } else {
                Toast.makeText(activity, "Please select image", Toast.LENGTH_SHORT).show()
            }
        }

        transaction()



        setContentView(binding.root)

    }

    private fun transaction() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session.getData(Constant.USER_ID)
        com.example.weagri.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    Log.d("SIGNUP_RES", response)
                    if (jsonObject.getBoolean(com.example.weagri.helper.Constant.SUCCESS)) {
                        val jsonArray = jsonObject.getJSONArray(com.example.weagri.helper.Constant.DATA)





                    } else {

                        Toast.makeText(this, "" + jsonObject.getString(com.example.weagri.helper.Constant.MESSAGE), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, java.lang.String.valueOf(response) + java.lang.String.valueOf(result), Toast.LENGTH_SHORT).show()
            }
        }, this, com.example.weagri.helper.Constant.RECHARGE_HISTORY, params, true)


    }

    private fun pickImageFromGallery() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_GALLERY
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_GALLERY) {
                imageUri = data?.data
                CropImage.activity(imageUri)
                    .start(activity)
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result: CropImage.ActivityResult = CropImage.getActivityResult(data)!!
                filePath1 = result.getUriFilePath(activity, true)
                val imgFile: File = File(filePath1)
                if (imgFile.exists()) {
                    binding.btnUpload.isEnabled = true // Corrected method name
                    val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                    binding.ivImage.setImageBitmap(myBitmap)
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun recharge() {
        val params: MutableMap<String, String> = HashMap()
        params[com.example.weagri.helper.Constant.USER_ID] = session.getData(com.example.weagri.helper.Constant.USER_ID)
        val FileParams: MutableMap<String, String> = HashMap()
        FileParams[com.example.weagri.helper.Constant.IMAGE] = filePath1!!
        com.example.weagri.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.example.weagri.helper.Constant.SUCCESS)) {



                        showCustomDialog()

                    } else {
                        Toast.makeText(activity, "" + jsonObject.getString(com.example.weagri.helper.Constant.MESSAGE), Toast.LENGTH_SHORT).show()

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, activity, com.example.weagri.helper.Constant.RECHARGE, params, FileParams)
    }


    private fun showCustomDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView: View = layoutInflater.inflate(R.layout.custom_dialog, null)

        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.setOnDismissListener {
            // This code will be executed when the dialog is dismissed
            moveToHomeActivity()
        }
        dialog.show()
    }

    private fun moveToHomeActivity() {
        val intent = Intent(this, com.example.weagri.Acitivity.HomeActivity::class.java)
        startActivity(intent)
        finish() // Optional: finish the current activity if you don't want to keep it in the stack
    }


}