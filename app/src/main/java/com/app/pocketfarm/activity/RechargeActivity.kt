package com.app.pocketfarm.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.widget.Toast
import com.app.pocketfarm.R
import com.app.pocketfarm.databinding.ActivityRechargeBinding
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder
import com.phonepe.intent.sdk.api.PhonePe
import com.phonepe.intent.sdk.api.PhonePeInitException
import com.phonepe.intent.sdk.api.UPIApplicationInfo
import com.phonepe.intent.sdk.api.models.PhonePeEnvironment
import org.json.JSONObject
import java.nio.charset.Charset
import java.security.MessageDigest

class RechargeActivity : AppCompatActivity() {

    lateinit var binding: ActivityRechargeBinding
    private lateinit var activity: Activity
    private lateinit var environment: PhonePeEnvironment
    private lateinit var session: com.app.pocketfarm.helper.Session
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRechargeBinding.inflate(layoutInflater)
        activity = this
        session = com.app.pocketfarm.helper.Session(activity)
        //PhonePe.init(activity, environment, "", "")




        binding.ibBack.setOnClickListener {
            onBackPressed()
        }

        binding.rlAmont1.setOnClickListener {
            binding.rlAmont1.setBackgroundResource(R.drawable.bg_rounded_blue)
            binding.rlAmont2.setBackgroundResource(R.drawable.edittext)
            binding.rlAmont3.setBackgroundResource(R.drawable.edittext)

            binding.tvAmount1.setTextColor(resources.getColor(R.color.white))
            binding.tvAmount2.setTextColor(resources.getColor(R.color.black))
            binding.tvAmount3.setTextColor(resources.getColor(R.color.black))

            binding.etAmount.setText(binding.tvAmount1.text.toString())



        }

        binding.rlAmont2.setOnClickListener {
            binding.rlAmont1.setBackgroundResource(R.drawable.edittext)
            binding.rlAmont2.setBackgroundResource(R.drawable.bg_rounded_blue)
            binding.rlAmont3.setBackgroundResource(R.drawable.edittext)

            binding.tvAmount1.setTextColor(resources.getColor(R.color.black))
            binding.tvAmount2.setTextColor(resources.getColor(R.color.white))
            binding.tvAmount3.setTextColor(resources.getColor(R.color.black))

            binding.etAmount.setText(binding.tvAmount2.text.toString())

        }



        binding.rlAmont3.setOnClickListener {
            binding.rlAmont1.setBackgroundResource(R.drawable.edittext)
            binding.rlAmont2.setBackgroundResource(R.drawable.edittext)
            binding.rlAmont3.setBackgroundResource(R.drawable.bg_rounded_blue)

            binding.tvAmount1.setTextColor(resources.getColor(R.color.black))
            binding.tvAmount2.setTextColor(resources.getColor(R.color.black))
            binding.tvAmount3.setTextColor(resources.getColor(R.color.white))

            binding.etAmount.setText(binding.tvAmount3.text.toString())

        }
        phonepeCall()





        setContentView(binding.root)

    }
    private fun phonepeCall() {
        PhonePe.init(this@RechargeActivity, PhonePeEnvironment.RELEASE, "M22TPYBERG1U0", "")
        val data = JSONObject()
        data.put("merchantTransactionId",System.currentTimeMillis().toString())
        data.put("merchantId", "M22TPYBERG1U0")
        data.put("merchantUserId", System.currentTimeMillis().toString())
        data.put("amount", 200)
        data.put("mobileNumber", "1234567890")
        data.put("callbackUrl", "https://api.phonepe.com/apis/hermes")
        val mPaymentInstrument = JSONObject()
        mPaymentInstrument.put("type", "PAY_PAGE")
        data.put("paymentInstrument", mPaymentInstrument)
        val base64Body: String = Base64.encodeToString(data.toString().toByteArray(Charset.defaultCharset()), Base64.NO_WRAP)
        val checksum =sha256(base64Body + "/pg/v1/pay" + "2aee1031-a822-4b78-9b97-86bc5906b3bc") + "###" + 1
        val b2BPGRequest =
            B2BPGRequestBuilder().setData(base64Body).setChecksum(checksum).setUrl("/pg/v1/pay").build()
        binding.btnLogin.setOnClickListener {
            try {
                startActivityForResult(PhonePe.getImplicitIntent(this, b2BPGRequest, "")!!, 1)
            } catch (_: PhonePeInitException) {
            }



        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            Toast.makeText(this@RechargeActivity, "Success", Toast.LENGTH_SHORT).show()
        }
    }
    fun sha256(data: String): String {
        try {
            val digest = MessageDigest.getInstance("SHA-256")
            val bytes = digest.digest(data.toByteArray(Charset.defaultCharset()))
            val hexString = StringBuilder(bytes.size * 2)
            for (byte in bytes) {
                val int = byte.toInt() and 0xff
                val hexChar = Integer.toHexString(int)
                if (hexChar.length == 1) {
                    hexString.append('0')
                }
                hexString.append(hexChar)
            }
            return hexString.toString()
        } catch (e: Exception) {
            throw RuntimeException("Error computing SHA-256 hash: $e")
        }
    }

}