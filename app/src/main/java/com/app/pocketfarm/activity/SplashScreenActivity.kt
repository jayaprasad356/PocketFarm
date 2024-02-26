package com.app.pocketfarm.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.pocketfarm.R
import com.app.pocketfarm.helper.ApiConfig
import com.app.pocketfarm.helper.ApiConfig.TAG
import com.app.pocketfarm.helper.Constant
import com.app.pocketfarm.helper.Session
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Firebase
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.dynamicLinks
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SplashScreenActivity : AppCompatActivity() {

    private var handler: Handler? = null
    private var session: Session? = null
    private var activity: Activity? = null

    private var currentVersion = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        activity = this
        session = Session(activity)
        handler = Handler()

        val tvTitle = "<font color='#F8B328'>Pocket</font> "+"<font color='#00B251'>Farm</font>"

            val textView = findViewById<TextView>(R.id.textView)

        textView.text = Html.fromHtml(tvTitle)
        setupViews()


    }


    private fun setupViews() {
        try {
            val pInfo = packageManager.getPackageInfo(packageName, 0)
            currentVersion = pInfo.versionCode.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        appupdate()
    }

//    private fun checkForUpdates() {
//          // This would come from your server
//        val currentVersionCode = packageManager.getPackageInfo(packageName, 0).versionCode
//
//        if (currentVersionCode < latestVersionCode) {
//            // Show bottom dialog prompting user to update the app
//            showUpdateDialog()
//        } else {
//            // No update needed, proceed with normal flow
//            GotoActivity()
//        }
//    }

    private fun showUpdateDialog(link: String) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_dialog_update, null)
        bottomSheetDialog.setContentView(view)

        val btnUpdate = view.findViewById<View>(R.id.btnUpdate)

        btnUpdate.setOnClickListener(View.OnClickListener {
            GotoActivity()
        })

        // Customize your bottom dialog here
        // For example, you can set text, buttons, etc.

        bottomSheetDialog.show()
    }

    private fun GotoActivity() {
        handler?.postDelayed({
            if (session!!.getBoolean("is_logged_in")) {
                val intent = Intent(activity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(activity, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 100)
    }


    private fun appupdate() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)


                        val latestVersion = jsonArray.getJSONObject(0).getString(Constant.VERSION)
                        val link = jsonArray.getJSONObject(0).getString(Constant.LINK)
                        if (currentVersion.toInt() >= latestVersion.toInt()) {
                            GotoActivity()
                        } else {
                            showUpdateDialog(link)
                        }

                    } else {
                        val jsonArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)


                        val latestVersion = jsonArray.getJSONObject(0).getString(Constant.VERSION)
                        val link = jsonArray.getJSONObject(0).getString(Constant.LINK)
                        if (currentVersion.toInt() >= latestVersion.toInt()) {
                            GotoActivity()
                        } else {
                            showUpdateDialog(link)
                        }


                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, Constant.APPUPDATE, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }

}