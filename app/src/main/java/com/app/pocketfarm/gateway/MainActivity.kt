package com.app.pocketfarm.gateway

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.pocketfarm.R
import com.app.pocketfarm.activity.HomeActivity
import com.app.pocketfarm.helper.ApiConfig
import com.app.pocketfarm.helper.Constant
import com.app.pocketfarm.helper.Session
import com.app.pocketfarm.utils.DialogUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    var mWebView: WebView? = null
    var context: Context? = null
    var TAG = "MainActivity"
    var session: Session? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        session = Session(this)
        if (0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
        context = this
        mWebView = findViewById<View>(R.id.payment_webview) as WebView
        initWebView()

        val qrValue = intent.getStringExtra("QR_VALUE")
        // 👍 Call the Create Order API from your server and you will get the Payment URL.
        //    you will also get UPI intent if you are using Enterprise Plan.
        //    you can use upi intent in payment url and it will directly ask for UPI App.
        // 🚫 Do not Call UPIGateway API in Android App Directly
        val PAYMENT_URL = ""+qrValue
        //        String PAYMENT_URL = "upi://pay?pa=...";
        if (PAYMENT_URL.startsWith("upi:")) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(PAYMENT_URL)
            startActivity(intent)
        } else {
            mWebView!!.loadUrl(PAYMENT_URL)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        mWebView!!.settings.javaScriptEnabled = true
        mWebView!!.settings.loadWithOverviewMode = true
        mWebView!!.settings.setSupportMultipleWindows(true)
        // Do not change Useragent otherwise it will not work. even if not working uncommit below
        // mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 4.4.4; One Build/KTU84L.H4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.135 Mobile Safari/537.36");
        mWebView!!.webChromeClient = WebChromeClient()
        mWebView!!.addJavascriptInterface(WebviewInterface(), "Interface")
    }

    inner class WebviewInterface {
        @JavascriptInterface
        fun paymentResponse(client_txn_id: String, txn_id: String) {
            Log.i(TAG, client_txn_id)
            Log.i(TAG, txn_id)
            // this function is called when payment is done (success, scanning ,timeout or cancel by user).
            // You must call the check order status API in server and get update about payment.
            // 🚫 Do not Call UpiGateway API in Android App Directly.
            Toast.makeText(context, "Order ID: $client_txn_id, Txn ID: $txn_id", Toast.LENGTH_SHORT).show()

            apicall()

            // Close the Webview.
        }

        @JavascriptInterface
        fun errorResponse() {
            // this function is called when Transaction in Already Done or Any other Issue.
            Toast.makeText(context, "Transaction Error.", Toast.LENGTH_SHORT).show()
            // Close the Webview.

        }
    }


    private fun apicall() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        params[Constant.TXN_ID] = session!!.getData(Constant.TXN_ID)
        params[Constant.DATE] = session!!.getData(Constant.DATE)
        params[Constant.KEY] = session!!.getData(Constant.KEY)
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(this, ""+jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()
                    } else {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(this, ""+jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()

                }
            }
        }, this, Constant.RECHARGE_STATUS, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }

    override fun onBackPressed() {
        // Do nothing
    }




}