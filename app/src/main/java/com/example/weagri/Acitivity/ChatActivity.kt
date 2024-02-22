package com.example.weagri.Acitivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weagri.R
import com.zoho.commons.Fonts
import com.zoho.commons.InitConfig
import com.zoho.livechat.android.listeners.InitListener
import com.zoho.salesiqembed.ZohoSalesIQ

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        val initConfig = InitConfig()
        // Set your font configurations

        ZohoSalesIQ.init(application, "q%2FetVzn%2B7suBUISHrv%2B4MAcJ28PTqq9c8NCG12tbTKEPyGbNr2VZjxcURpltcVF97toxu8fzIm4%3D_in", "VXYedrQX8SnJI7EFCpu01dOAbrxweRFJ9LoYwisslnQwtg7o2gI5TDmhdHf%2BpskotIQR5eZeZIfAQHqtrJtg%2Fvrhon6CEgZuOhnVA8woxXjqz2ZMOkOvsw%3D%3D", initConfig, object : InitListener {
            override fun onInitSuccess() {
                ZohoSalesIQ.Launcher.show(ZohoSalesIQ.Launcher.VisibilityMode.ALWAYS)
            }

            override fun onInitError(errorCode: Int, errorMessage: String) {
                // Handle initialization errors
            }
        })
    }
}