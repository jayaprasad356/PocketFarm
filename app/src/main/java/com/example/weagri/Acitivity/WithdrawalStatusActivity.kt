package com.example.weagri.Acitivity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.weagri.Adapter.WithdrawalAdapter
import com.example.weagri.Model.Withdrawal
import com.example.weagri.R
import com.example.weagri.databinding.ActivityWithdrawalBinding
import com.example.weagri.databinding.ActivityWithdrawalStatusBinding
import com.example.weagri.helper.Constant
import com.example.weagri.utils.DialogUtils
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

class WithdrawalStatusActivity : AppCompatActivity() {

    lateinit var binding: ActivityWithdrawalStatusBinding
    private lateinit var activity: Activity
    private lateinit var session: com.example.weagri.helper.Session
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWithdrawalStatusBinding.inflate(layoutInflater)
        activity = this
        session = com.example.weagri.helper.Session(activity)

        val swipeRefreshLayout: SwipeRefreshLayout = binding.swipeRefreshLayout

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }


        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvHistory.layoutManager = linearLayoutManager


         swipeRefreshLayout.setOnRefreshListener { history(swipeRefreshLayout) }

          history(swipeRefreshLayout)


        setContentView(binding.root)
    }

    private fun history(swipeRefreshLayout:SwipeRefreshLayout) {
        val params: MutableMap<String, String> = HashMap()
        params["user_id"] = session.getData(Constant.USER_ID)
        com.example.weagri.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.example.weagri.helper.Constant.SUCCESS)) {
                        val jsonArray = jsonObject.getJSONArray(com.example.weagri.helper.Constant.DATA)

                        val g = Gson()
                        val withdrawal: java.util.ArrayList<Withdrawal> =
                            java.util.ArrayList<Withdrawal>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: Withdrawal = g.fromJson(jsonObject1.toString(), Withdrawal::class.java)
                                withdrawal.add(group)
                            } else {
                                break
                            }
                        }

                        val adapter = WithdrawalAdapter(activity, withdrawal)
                        binding.rvHistory.adapter = adapter
                        swipeRefreshLayout.isRefreshing = false


                    } else {
                        swipeRefreshLayout.isRefreshing = false
                        DialogUtils.showCustomDialog(this, ""+jsonObject.getString(Constant.MESSAGE))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(activity, java.lang.String.valueOf(response) + java.lang.String.valueOf(result), Toast.LENGTH_SHORT).show()
            }
        }, activity, com.example.weagri.helper.Constant.WITHDRAWAL_LIST, params, true)
    }


}