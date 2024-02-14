package com.example.weagri.Fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weagri.Acitivity.PaymentActivity
import com.example.weagri.Adapter.TransactionAdapter
import com.example.weagri.Model.Transaction
import com.example.weagri.R
import com.example.weagri.databinding.FragmentHomeBinding
import com.example.weagri.databinding.FragmentMoreBinding
import com.example.weagri.helper.ApiConfig
import com.example.weagri.helper.Constant
import com.example.weagri.helper.Session
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var activity: Activity
    lateinit var session: com.example.weagri.helper.Session


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container, false)


        activity = getActivity() as Activity
        session = com.example.weagri.helper.Session(activity)


        binding.tvRecharge.text =  "Recharge Rs." + session.getData(Constant.RECHARGE_BALANCE)
        binding.tvTotalEarning.text = "₹" + session.getData(Constant.TOTAL_EARNINGS)
        binding.tvLast7DaysEarning.text = "₹" + session.getData(Constant.SEVEN_DAYS_EARN)
        binding.tvTotalIncome.text = "₹" + session.getData(Constant.TOTAL_INCOME)
        binding.tvRemainingBalance.text = "₹" + session.getData(Constant.BALANCE)


        binding.tvName.text = "Hi " + session.getData(com.example.weagri.helper.Constant.NAME)
        binding.tvReferralCode.text =  session.getData(com.example.weagri.helper.Constant.REFER_CODE)

        binding.rlRecharge.setOnClickListener {
            startActivity(activity.intent.setClassName(activity, PaymentActivity::class.java.name))
        }


        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvtransactionitem.layoutManager = linearLayoutManager

        transaction()


        return binding.root

    }

    private fun transaction() {
        val params: MutableMap<String, String> = HashMap()
        params[com.example.weagri.helper.Constant.USER_ID] = session.getData(com.example.weagri.helper.Constant.USER_ID)
        com.example.weagri.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.example.weagri.helper.Constant.SUCCESS)) {
                        val `object` = JSONObject(response)
                        val jsonArray: JSONArray = `object`.getJSONArray(com.example.weagri.helper.Constant.DATA)
                        val g = Gson()
                        val transaction: java.util.ArrayList<Transaction> =
                            java.util.ArrayList<Transaction>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: Transaction = g.fromJson(jsonObject1.toString(), Transaction::class.java)
                                transaction.add(group)
                            } else {
                                break
                            }
                        }
                        binding.animationView.visibility = View.GONE

                        val adapter = TransactionAdapter(activity, transaction)
                        binding.rvtransactionitem.adapter = adapter

                    } else {
                        binding.animationView.visibility = View.VISIBLE
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, com.example.weagri.helper.Constant.TRANSACTIONS_LIST, params, true)


    }



}