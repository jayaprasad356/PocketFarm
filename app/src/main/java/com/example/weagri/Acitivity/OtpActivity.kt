package com.example.weagri.Acitivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weagri.R
import com.example.weagri.databinding.ActivityOtpBinding
import com.example.weagri.helper.Constant
import com.example.weagri.helper.Session
import com.example.weagri.utils.DialogUtils
import org.json.JSONException
import org.json.JSONObject

class OtpActivity : AppCompatActivity() {
    lateinit var binding: ActivityOtpBinding

    var activity: Activity? = null
    lateinit var session: Session





    private var countDownTimer: CountDownTimer? = null
    private val COUNTDOWN_TIME = 45000L // 45 seconds in milliseconds




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        binding = ActivityOtpBinding.inflate(layoutInflater)

        activity = this
        session = Session(activity)


        binding.ibBack.setOnClickListener(){
            onBackPressed()
        }

        binding.tvMobile.text = "+91 " + session!!.getData(Constant.MOBILE)



        showOtp()

        startCountdown()

        // Set click listener for the "Resend" button
        binding.btnResend.setOnClickListener {
            // Reset the timer
            resetCountdown()
            // Start the countdown timer again
            startCountdown()
            // Disable the button
            binding.btnResend.isEnabled = false
        }


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
                        //Toast.makeText(activity, jsonObject.getString(com.example.weagri.helper.Constant.MESSAGE), Toast.LENGTH_SHORT).show()
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


                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        val message = jsonObject.getString(Constant.MESSAGE)

                        if (message.equals("Your Mobile Number is not Registered")) {
                            val intent = Intent(this, ProfiledetailsActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else {
                            DialogUtils.showCustomDialog(this, ""+jsonObject.getString(com.example.weagri.helper.Constant.MESSAGE))
                        }

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
               // Toast.makeText(this, , Toast.LENGTH_SHORT).show()
                DialogUtils.showCustomDialog(this, ""+java.lang.String.valueOf(response) + java.lang.String.valueOf(result))

            }
        }, this, com.example.weagri.helper.Constant.LOGIN, params, true)


    }

    private fun startCountdown() {
        countDownTimer = object : CountDownTimer(COUNTDOWN_TIME, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                // Update UI to show remaining seconds
                binding.btnResend.text = "Resend in $secondsLeft seconds"
            }

            override fun onFinish() {
                // Enable the button when countdown finishes
                binding.btnResend.isEnabled = true
                binding.btnResend.text = "Don’t  receive any code  ? Resent"
            }
        }.start()
    }

    private fun resetCountdown() {
        countDownTimer?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        resetCountdown()
    }

}