package com.app.pocketfarm.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.pocketfarm.R
import com.app.pocketfarm.adapter.RechargeHistoryAdapter
import com.app.pocketfarm.adapter.ReviewlistAdapter
import com.app.pocketfarm.databinding.ActivityPaymentBinding
import com.app.pocketfarm.databinding.ActivityRatingIncomeBinding
import com.app.pocketfarm.helper.ApiConfig
import com.app.pocketfarm.helper.Constant
import com.app.pocketfarm.model.Recharge
import com.app.pocketfarm.model.Review
import com.app.pocketfarm.utils.DialogUtils
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImage
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class RatingIncomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityRatingIncomeBinding
    lateinit var activity: Activity
    lateinit var session: com.app.pocketfarm.helper.Session

    var filePath1: String? = null
    var imageUri: Uri? = null

    private val REQUEST_IMAGE_GALLERY = 2
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating_income)
        binding = ActivityRatingIncomeBinding.inflate(layoutInflater)
        activity = this
        session = com.app.pocketfarm.helper.Session(activity)

        setContentView(binding.root)

        binding.cvImage.setOnClickListener {
            pickImageFromGallery()

        }

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }



        binding.btnUpload.setOnClickListener {
            if (filePath1 != null) {
                rating()
            } else {
                Toast.makeText(activity, "Please select image", Toast.LENGTH_SHORT).show()
            }
        }
        review_list()
        apicall()
        playstorerating()


        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvHistory.layoutManager = linearLayoutManager


        binding.tvCopy.setOnClickListener {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", binding.tvReview.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(activity, "Copied", Toast.LENGTH_SHORT).show()
        }

        binding.tvGotoReview.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://play.google.com/store/apps/details?id=com.app.pocketfarm")
            startActivity(intent)
        }


    }




    private fun pickImageFromGallery() {
        Toast.makeText(activity, "Pick Image", Toast.LENGTH_SHORT).show()
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
                // Start cropping activity with the provided image URI
                CropImage.activity(imageUri)
                    //.setAspectRatio(1, 1) // Set aspect ratio to 1:1 for a square crop
                    // .setRequestedSize(512, 512) // Set output image size
                    .start(activity)
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                // Get the result URI after cropping
                val result: CropImage.ActivityResult = CropImage.getActivityResult(data)!!
                filePath1 = result.getUriFilePath(activity, true)
                val imgFile: File = File(filePath1)
                if (imgFile.exists()) {
                    binding.btnUpload.isEnabled = true // Corrected method name
                    // Decode the cropped image file into a Bitmap and display it
                    val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                    binding.ivImage.setImageBitmap(myBitmap)
                }
            }
        }
    }



    private fun playstorerating() {
        val params: MutableMap<String, String> = HashMap()
        com.app.pocketfarm.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.app.pocketfarm.helper.Constant.SUCCESS)) {

                        val jsonArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)



                        val review = jsonArray.getJSONObject(0).getString("review")


                        binding.tvReview.text = review



                    } else {

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, java.lang.String.valueOf(response) + java.lang.String.valueOf(result), Toast.LENGTH_SHORT).show()
            }
        }, this, com.app.pocketfarm.helper.Constant.PLAYSTORE_RATINGS, params, true)


    }

    private fun rating() {
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
                        DialogUtils.showCustomDialog(this, ""+jsonObject.getString(Constant.MESSAGE))

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, activity, com.app.pocketfarm.helper.Constant.REVIEW_URL, params, FileParams)
    }


    private fun showCustomDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView: View = layoutInflater.inflate(R.layout.custom_dialog, null)

        val tvMessage = dialogView.findViewById<TextView>(R.id.tvMessage)
        tvMessage.text = "Review submitted successfully."


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


    private fun apicall() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)

                        val review_video = jsonArray.getJSONObject(0).getString("review_video")

                        binding.btnDemoVideo.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(review_video))
                            startActivity(intent)
                        }


                    } else {
                        DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()

                }
            }
        }, activity, Constant.SETTINGS, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }
    private fun review_list() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session.getData(Constant.USER_ID)
        com.app.pocketfarm.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)

                    if (jsonObject.getBoolean(com.app.pocketfarm.helper.Constant.SUCCESS)) {
                        val `object` = JSONObject(response)
                        val jsonArray: JSONArray = `object`.getJSONArray(com.app.pocketfarm.helper.Constant.DATA)
                        val g = Gson()
                        val review: java.util.ArrayList<Review> = java.util.ArrayList<Review>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: Review = g.fromJson(jsonObject1.toString(), Review::class.java)
                                review.add(group)
                            } else {
                                break
                            }
                        }

                        val adapter = ReviewlistAdapter(activity, review)
                        binding.rvHistory.adapter = adapter


                    } else {

                        binding.rlHistory.visibility = View.GONE
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, java.lang.String.valueOf(response) + java.lang.String.valueOf(result), Toast.LENGTH_SHORT).show()
            }
        }, this, com.app.pocketfarm.helper.Constant.REVIEW_LIST, params, true)


    }


}