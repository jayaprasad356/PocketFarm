package com.example.weagri.Acitivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weagri.R
import com.example.weagri.helper.Constant
import com.example.weagri.helper.ProgressDisplay
import com.example.weagri.utils.DialogUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SplashScreenActivity : AppCompatActivity() {

    private var handler: Handler? = null
    private var session: com.example.weagri.helper.Session? = null
    private var activity: Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        activity = this
        session = com.example.weagri.helper.Session(activity)
        handler = Handler()

        val tvTitle = "<font color='#F8B328'>Pocket</font> "+"<font color='#00B251'>Farm</font>"

            val textView = findViewById<TextView>(R.id.textView)

        textView.text = Html.fromHtml(tvTitle)

        GotoActivity()

    }
    private fun GotoActivity() {
        handler?.postDelayed({
            if (session!!.getBoolean("is_logged_in")) {
                userdetails() // This should return the Intent after fetching user details
            } else {
                val intent = Intent(activity, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 2000)
    }


    private fun userdetails() {
        val params: MutableMap<String, String> = HashMap()
        params[com.example.weagri.helper.Constant.USER_ID] = session!!.getData(com.example.weagri.helper.Constant.USER_ID)
        com.example.weagri.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.example.weagri.helper.Constant.SUCCESS)) {
                        val jsonArray: JSONArray =
                            jsonObject.getJSONArray(com.example.weagri.helper.Constant.DATA)



                        session!!.setData(
                            Constant.ACCOUNT_NUM,
                            jsonArray.getJSONObject(0).getString(Constant.ACCOUNT_NUM)
                        )
                        session!!.setData(
                            Constant.HOLDER_NAME,
                            jsonArray.getJSONObject(0).getString(Constant.HOLDER_NAME)
                        )
                        session!!.setData(
                            Constant.BANK,
                            jsonArray.getJSONObject(0).getString(Constant.BANK)
                        )
                        session!!.setData(
                            Constant.BRANCH,
                            jsonArray.getJSONObject(0).getString(Constant.BRANCH)
                        )
                        session!!.setData(
                            Constant.IFSC,
                            jsonArray.getJSONObject(0).getString(Constant.IFSC)
                        )
                        session!!.setData(
                            Constant.AGE,
                            jsonArray.getJSONObject(0).getString(Constant.AGE)
                        )
                        session!!.setData(
                            Constant.CITY,
                            jsonArray.getJSONObject(0).getString(Constant.CITY)
                        )
                        session!!.setData(
                            Constant.STATE,
                            jsonArray.getJSONObject(0).getString(Constant.STATE)
                        )
                        session!!.setData(
                            Constant.DEVICE_ID,
                            jsonArray.getJSONObject(0).getString(Constant.DEVICE_ID)
                        )
                        session!!.setData(
                            Constant.RECHARGE_BALANCE,
                            jsonArray.getJSONObject(0).getString(Constant.RECHARGE_BALANCE)
                        )
                        session!!.setData(
                            Constant.TOTAL_EARNINGS,
                            jsonArray.getJSONObject(0).getString(Constant.TOTAL_EARNINGS)
                        )
                        session!!.setData(
                            Constant.TOTAL_INCOME,
                            jsonArray.getJSONObject(0).getString(Constant.TOTAL_INCOME)
                        )
                        session!!.setData(
                            Constant.BALANCE,
                            jsonArray.getJSONObject(0).getString(Constant.BALANCE)
                        )
                        session!!.setData(
                            Constant.SEVEN_DAYS_EARNINGS,
                            jsonArray.getJSONObject(0).getString(Constant.SEVEN_DAYS_EARNINGS)
                        )
                        session!!.setData(
                            Constant.WITHDRAWAL_STATUS,
                            jsonArray.getJSONObject(0).getString(Constant.WITHDRAWAL_STATUS)
                        )
                        session!!.setData(
                            Constant.SEVEN_DAYS_EARN,
                            jsonArray.getJSONObject(0).getString(Constant.SEVEN_DAYS_EARN)
                        )




                        Handler().postDelayed({
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, 1000)





                    } else {
                        DialogUtils.showCustomDialog(this, ""+jsonObject.getString(Constant.MESSAGE))

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, com.example.weagri.helper.Constant.USER_DETAILS, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }

}