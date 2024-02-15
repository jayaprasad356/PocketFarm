package com.example.weagri.Acitivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weagri.R
import com.example.weagri.databinding.ActivityOtpBinding
import com.example.weagri.helper.Constant
import com.example.weagri.helper.Session
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {
    lateinit var binding: ActivityOtpBinding

    var activity: Activity? = null
    lateinit var session: Session










    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        binding = ActivityOtpBinding.inflate(layoutInflater)

        activity = this
        session = Session(activity)


        binding.ibBack.setOnClickListener(){
            onBackPressed()
        }



        showOtp()


        setContentView(binding.root)
    }


    private fun showOtp() {
        binding.btnVerify.setOnClickListener(View.OnClickListener {


            if (binding.otpView.toString().isEmpty()) {

                Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show()
            }
            else if (binding.otpView.toString().length == 6) {
                Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
            }
            else {
                verifyOtp()
            }

        })

    }

    private fun verifyOtp() {
        //  binding.otpView =  000000

        login()


    }


    private fun login() {

        val params: MutableMap<String, String> = HashMap()
        params[Constant.MOBILE] = session!!.getData(Constant.MOBILE)
        params[Constant.DEVICE_ID] =  Constant.getDeviceId(activity)
        com.example.weagri.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    Log.d("SIGNUP_RES", response)
                    if (jsonObject.getBoolean(com.example.weagri.helper.Constant.SUCCESS)) {
                        val jsonArray = jsonObject.getJSONArray(com.example.weagri.helper.Constant.DATA)
                        session!!.setBoolean("is_logged_in", true)
                        session!!.setData(
                            com.example.weagri.helper.Constant.USER_ID, jsonArray.getJSONObject(0).getString(
                                com.example.weagri.helper.Constant.ID))
                        session.setData(
                            com.example.weagri.helper.Constant.NAME, jsonArray.getJSONObject(0).getString(
                                com.example.weagri.helper.Constant.NAME))
                        session.setData(
                            com.example.weagri.helper.Constant.MOBILE, jsonArray.getJSONObject(0).getString(
                                com.example.weagri.helper.Constant.MOBILE))
                        session.setData(
                            com.example.weagri.helper.Constant.EMAIL, jsonArray.getJSONObject(0).getString(
                                com.example.weagri.helper.Constant.EMAIL))
                        session.setData(
                            com.example.weagri.helper.Constant.AGE, jsonArray.getJSONObject(0).getString(
                                com.example.weagri.helper.Constant.AGE))
                        session.setData(
                            com.example.weagri.helper.Constant.CITY, jsonArray.getJSONObject(0).getString(
                                com.example.weagri.helper.Constant.CITY))
                        session.setData(
                            com.example.weagri.helper.Constant.STATE, jsonArray.getJSONObject(0).getString(
                                com.example.weagri.helper.Constant.STATE))
                        session.setData(
                            com.example.weagri.helper.Constant.REFER_CODE, jsonArray.getJSONObject(0).getString(
                                com.example.weagri.helper.Constant.REFER_CODE))


                        startActivity(Intent(this, com.example.weagri.Acitivity.SplashScreenActivity::class.java))
                        finish()

                    } else {

                        startActivity(Intent(this, com.example.weagri.Acitivity.ProfiledetailsActivity::class.java))
                        finish()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, java.lang.String.valueOf(response) + java.lang.String.valueOf(result), Toast.LENGTH_SHORT).show()
            }
        }, this, com.example.weagri.helper.Constant.LOGIN, params, true)


    }

}