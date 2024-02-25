package com.example.weagri.Fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.example.weagri.Acitivity.HomeActivity
import com.example.weagri.Acitivity.InviteActivity
import com.example.weagri.Acitivity.MyProductionActivity
import com.example.weagri.Acitivity.TransactionActivity
import com.example.weagri.Acitivity.UpdateProfileActivity
import com.example.weagri.Acitivity.WithdrawalActivity
import com.example.weagri.R
import com.example.weagri.databinding.ActivityHomeBinding
import com.example.weagri.databinding.FragmentMoreBinding
import com.example.weagri.databinding.FragmentPlansBinding
import com.example.weagri.helper.ApiConfig
import com.example.weagri.helper.Constant
import com.example.weagri.helper.Session
import com.example.weagri.utils.DialogUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class MoreFragment : Fragment() {


    lateinit var binding: FragmentMoreBinding
    lateinit var activity: Activity
    lateinit var session: com.example.weagri.helper.Session


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoreBinding.inflate(inflater, container, false)




        activity = getActivity() as Activity
        session = com.example.weagri.helper.Session(activity)



        (activity as HomeActivity).binding.rlToolbar.visibility = View.GONE
//        binding.tvRecharge.text =  "₹" + session.getData(com.example.weagri.helper.Constant.RECHARGE_BALANCE)
//        binding.tvTotalEarnings.text = "₹" + session.getData(com.example.weagri.helper.Constant.TOTAL_EARNINGS)

        userdetails()

        binding.llInvite.setOnClickListener {
//            val shareBody = "Hey, I am using WeAgri App. It's a great app to earn money. Use my refer code " + session.getData(
//                com.example.weagri.helper.Constant.REFER_CODE) + " to get 100 coins on signup. Download the app from the link below.\n" + ""
//            val sharingIntent = android.content.Intent(android.content.Intent.ACTION_SEND)
//            sharingIntent.type = "text/plain"
//            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "WeAgri")
//            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
//            startActivity(android.content.Intent.createChooser(sharingIntent, "Share using"))

            startActivity(Intent(activity, InviteActivity::class.java))
        }

        binding.llScartchCard.setOnClickListener {
            startActivity(Intent(activity, com.example.weagri.Acitivity.ScartchcardActivity::class.java))
        }


        binding.tvMobile.text = session.getData(com.example.weagri.helper.Constant.MOBILE)
        binding.tvReferralCode.text = session.getData(com.example.weagri.helper.Constant.REFER_CODE)

        binding.cvWithdraw.setOnClickListener {
            startActivity(Intent(activity, WithdrawalActivity::class.java))
        }

        binding.rlmyBank.setOnClickListener {
            startActivity(Intent(activity, com.example.weagri.Acitivity.UpdatebankActivity::class.java))
        }

        binding.llMyProduction.setOnClickListener{
            startActivity(Intent(activity,MyProductionActivity::class.java))
        }

        binding.llServices.setOnClickListener{
            startActivity(Intent(activity, com.example.weagri.Acitivity.SupportActivity::class.java))
        }

        binding.cvRecharge.setOnClickListener {
            startActivity(Intent(activity, com.example.weagri.Acitivity.RechargeActivity::class.java))
        }

        binding.rlhistory.setOnClickListener {
            startActivity(Intent(activity, TransactionActivity::class.java))
        }

        binding.rlUpdateprofile.setOnClickListener{
            startActivity(Intent(activity, UpdateProfileActivity::class.java))
        }

        binding.rlwithdrawhistory.setOnClickListener{
            startActivity(Intent(activity, com.example.weagri.Acitivity.WithdrawalStatusActivity::class.java))
        }

        binding.rlCutomerSupport.setOnClickListener{


        }


        binding.rlLogout.setOnClickListener{
            session.logoutUser(activity)
        }

        binding.tvTotalIncome.text = "₹ " + session.getData(com.example.weagri.helper.Constant.TOTAL_INCOME)
        binding.tvTotalRecharge.text = "₹ " + session.getData(com.example.weagri.helper.Constant.RECHARGE)
        binding.tvTodayIncome.text = "₹ " + session.getData(com.example.weagri.helper.Constant.TODAY_INCOME)
        binding.tvTotalAssests.text = "₹ " + session.getData(com.example.weagri.helper.Constant.TOTAL_ASSETS)
        binding.tvTotalWithdrawal.text = "₹ " + session.getData(com.example.weagri.helper.Constant.TOTAL_WITHDRAWAL)
        binding.tvTeamIncome.text = "₹ " + session.getData(com.example.weagri.helper.Constant.TEAM_INCOME)



        return binding.root
    }

    private fun userdetails() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray: JSONArray =
                            jsonObject.getJSONArray(Constant.DATA)



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
                            Constant.RECHARGE, jsonArray.getJSONObject(0).getString(
                                Constant.RECHARGE))
                        session!!.setData(
                            Constant.TODAY_INCOME, jsonArray.getJSONObject(0).getString(
                                Constant.TODAY_INCOME))
                        session!!.setData(
                            Constant.TOTAL_INCOME, jsonArray.getJSONObject(0).getString(
                                Constant.TOTAL_INCOME))
                        session!!.setData(
                            Constant.BALANCE, jsonArray.getJSONObject(0).getString(
                                Constant.BALANCE))
                        session!!.setData(
                            Constant.WITHDRAWAL_STATUS, jsonArray.getJSONObject(0).getString(
                                Constant.WITHDRAWAL_STATUS))
                        session!!.setData(
                            Constant.TEAM_SIZE, jsonArray.getJSONObject(0).getString(
                                Constant.TEAM_SIZE))
                        session!!.setData(
                            Constant.VALID_TEAM, jsonArray.getJSONObject(0).getString(
                                Constant.VALID_TEAM))
                        session!!.setData(
                            Constant.TOTAL_WITHDRAWAL, jsonArray.getJSONObject(0).getString(
                                Constant.TOTAL_WITHDRAWAL))
                        session!!.setData(
                            Constant.TEAM_INCOME, jsonArray.getJSONObject(0).getString(
                                Constant.TEAM_INCOME))
                        session!!.setData(
                            Constant.TOTAL_ASSETS, jsonArray.getJSONObject(0).getString(
                                Constant.TOTAL_ASSETS))



                        binding.tvTotalIncome.text = "₹ " + session.getData(com.example.weagri.helper.Constant.TOTAL_INCOME)
                        binding.tvTotalRecharge.text = "₹ " + session.getData(com.example.weagri.helper.Constant.RECHARGE)
                        binding.tvTodayIncome.text = "₹ " + session.getData(com.example.weagri.helper.Constant.TODAY_INCOME)
                        binding.tvTotalAssests.text = "₹ " + session.getData(com.example.weagri.helper.Constant.TOTAL_ASSETS)
                        binding.tvTotalWithdrawal.text = "₹ " + session.getData(com.example.weagri.helper.Constant.TOTAL_WITHDRAWAL)
                        binding.tvTeamIncome.text = "₹ " + session.getData(com.example.weagri.helper.Constant.TEAM_INCOME)



                    } else {
                        DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, Constant.USER_DETAILS, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }



}