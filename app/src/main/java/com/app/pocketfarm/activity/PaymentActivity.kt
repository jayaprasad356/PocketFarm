package com.app.pocketfarm.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.canhub.cropper.CropImage
import com.app.pocketfarm.R
import com.app.pocketfarm.databinding.ActivityPaymentBinding
import com.app.pocketfarm.helper.Constant
import com.app.pocketfarm.utils.DialogUtils
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class PaymentActivity : AppCompatActivity() {
    lateinit var binding: ActivityPaymentBinding
    lateinit var activity: Activity
    lateinit var session: com.app.pocketfarm.helper.Session

    var filePath1: String? = null
    var imageUri: Uri? = null

    private val REQUEST_IMAGE_GALLERY = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPaymentBinding.inflate(layoutInflater)
        activity = this
        session = com.app.pocketfarm.helper.Session(activity)

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
        com.app.pocketfarm.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    Log.d("SIGNUP_RES", response)
                    if (jsonObject.getBoolean(com.app.pocketfarm.helper.Constant.SUCCESS)) {
                        val jsonArray = jsonObject.getJSONArray(com.app.pocketfarm.helper.Constant.DATA)





                    } else {

                        DialogUtils.showCustomDialog(this, ""+jsonObject.getString(Constant.MESSAGE))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, java.lang.String.valueOf(response) + java.lang.String.valueOf(result), Toast.LENGTH_SHORT).show()
            }
        }, this, com.app.pocketfarm.helper.Constant.RECHARGE_HISTORY, params, true)


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
        params[com.app.pocketfarm.helper.Constant.USER_ID] = session.getData(com.app.pocketfarm.helper.Constant.USER_ID)
        val FileParams: MutableMap<String, String> = HashMap()
        FileParams[com.app.pocketfarm.helper.Constant.IMAGE] = filePath1!!
        com.app.pocketfarm.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.app.pocketfarm.helper.Constant.SUCCESS)) {



                        showCustomDialog()

                    } else {
                        Toast.makeText(activity, "" + jsonObject.getString(com.app.pocketfarm.helper.Constant.MESSAGE), Toast.LENGTH_SHORT).show()

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, activity, com.app.pocketfarm.helper.Constant.RECHARGE_URL, params, FileParams)
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
        val intent = Intent(this, com.app.pocketfarm.activity.HomeActivity::class.java)
        startActivity(intent)
        finish() // Optional: finish the current activity if you don't want to keep it in the stack
    }


}