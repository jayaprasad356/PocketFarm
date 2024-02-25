package com.app.pocketfarm.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.pocketfarm.adapter.TransactionAdapter
import com.app.pocketfarm.model.Transaction
import com.app.pocketfarm.databinding.ActivityTransactionBinding
import com.app.pocketfarm.helper.Constant
import com.app.pocketfarm.utils.DialogUtils
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class TransactionActivity : AppCompatActivity() {

    lateinit var binding: ActivityTransactionBinding
    lateinit var activity: Activity
    lateinit var session: com.app.pocketfarm.helper.Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        activity = this
        session = com.app.pocketfarm.helper.Session(activity)
        val swipeRefreshLayout: SwipeRefreshLayout = binding.swipeRefreshLayout
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvTransactions.layoutManager = linearLayoutManager

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }

        swipeRefreshLayout.setOnRefreshListener { transaction(swipeRefreshLayout) }
        transaction(swipeRefreshLayout)


        setContentView(binding.root)
    }

    private fun transaction(swipeRefreshLayout:SwipeRefreshLayout) {
        val params: MutableMap<String, String> = HashMap()
        params[com.app.pocketfarm.helper.Constant.USER_ID] = session.getData(com.app.pocketfarm.helper.Constant.USER_ID)
        com.app.pocketfarm.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.app.pocketfarm.helper.Constant.SUCCESS)) {
                        val `object` = JSONObject(response)
                        val jsonArray: JSONArray = `object`.getJSONArray(com.app.pocketfarm.helper.Constant.DATA)
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

                        val adapter = TransactionAdapter(activity, transaction)
                        binding.rvTransactions.adapter = adapter
                        swipeRefreshLayout.isRefreshing = false

                    } else {
                        swipeRefreshLayout.isRefreshing = false
                        DialogUtils.showCustomDialog(this, ""+jsonObject.getString(Constant.MESSAGE))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, com.app.pocketfarm.helper.Constant.TRANSACTIONS_LIST, params, true)


    }
}