package com.app.pocketfarm.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.app.pocketfarm.databinding.ActivitySupportBinding
import com.app.pocketfarm.helper.ApiConfig
import com.app.pocketfarm.helper.Constant
import com.app.pocketfarm.utils.DialogUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SupportActivity : AppCompatActivity() {

    lateinit var binding: ActivitySupportBinding
    lateinit var activity: Activity
    lateinit var session: com.app.pocketfarm.helper.Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySupportBinding.inflate(layoutInflater)
        activity = this
        session = com.app.pocketfarm.helper.Session(activity)

        binding.ibBack.setOnClickListener {
            onBackPressed()
            finish()
        }




        apicall()

        return setContentView(binding!!.root)
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
                        val whatsapplink = jsonArray.getJSONObject(0).getString("whatsapp_group")
                        val Telegram = jsonArray.getJSONObject(0).getString("telegram_channel")

                        binding.tvJointelegram.setOnClickListener {
                            openTelegram(Telegram)

                        }
                        binding.tvJoinwhatsapp.setOnClickListener {
                            openWhatsApp(whatsapplink)

                        }

                        binding.tvSharetelegram.setOnClickListener {
                            val sendIntent = Intent()
                            sendIntent.action = Intent.ACTION_SEND
                            sendIntent.putExtra(Intent.EXTRA_TEXT, Telegram)
                            sendIntent.type = "text/plain"
                            startActivity(sendIntent)
                        }

                        binding.tvSharewhatsapp.setOnClickListener {
                            val sendIntent = Intent()
                            sendIntent.action = Intent.ACTION_SEND
                            sendIntent.putExtra(Intent.EXTRA_TEXT, whatsapplink)
                            sendIntent.type = "text/plain"
                            startActivity(sendIntent)
                        }

                    } else {
                        DialogUtils.showCustomDialog(
                            activity,
                            "" + jsonObject.getString(Constant.MESSAGE)
                        )
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()

                }
            }
        }, activity, Constant.SETTINGS, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }

    private fun openTelegram(telegram: String) {
        val url = telegram;
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun openWhatsApp(whatsapplink: String) {
        val url = whatsapplink
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)

    }

}