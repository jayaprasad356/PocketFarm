package com.example.weagri.Acitivity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.weagri.Adapter.ActivateplansAdapter
import com.example.weagri.Model.MyPlan
import com.example.weagri.R
import com.example.weagri.databinding.ActivityHomeBinding
import com.example.weagri.databinding.ActivityMyProductionBinding
import com.example.weagri.databinding.FragmentPlanBinding
import com.example.weagri.helper.Session
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MyProductionActivity : AppCompatActivity() {

    lateinit var binding: ActivityMyProductionBinding
    lateinit var activity: Activity
    lateinit var session: Session

    private var adapter: com.example.weagri.Adapter.SliderAdapterExample? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProductionBinding.inflate(layoutInflater)

        activity = this
        session = Session(activity)

        activateplan()

        binding.ibBack.setOnClickListener{
            onBackPressed()
        }


        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvplan.layoutManager = linearLayoutManager
        setContentView(binding.root)


    }


    private fun activateplan() {
        val params: MutableMap<String, String> = HashMap()
        params[com.example.weagri.helper.Constant.USER_ID]= session.getData(com.example.weagri.helper.Constant.USER_ID)
        com.example.weagri.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.example.weagri.helper.Constant.SUCCESS)) {
                        val `object` = JSONObject(response)
                        val jsonArray: JSONArray = `object`.getJSONArray(com.example.weagri.helper.Constant.DATA)
                        val g = Gson()
                        val myPlans: java.util.ArrayList<MyPlan> =
                            java.util.ArrayList<MyPlan>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: MyPlan = g.fromJson(jsonObject1.toString(), MyPlan::class.java)
                                myPlans.add(group)
                            } else {
                                break
                            }
                        }
                        //    Toast.makeText(getActivity(), "1" + jsonObject.getString(Constant.MESSAGE).toString(), Toast.LENGTH_SHORT).show()
                        //important
                        val adapter = ActivateplansAdapter(activity, myPlans)
                        binding.rvplan.adapter = adapter
                        binding.rvplan.visibility = View.VISIBLE
                        binding.animationView.visibility = View.GONE


                    } else {
                        binding.rvplan.visibility = View.GONE
                        binding.animationView.visibility = View.VISIBLE
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, com.example.weagri.helper.Constant.USER_PLAN_LIST, params, true)



    }

}