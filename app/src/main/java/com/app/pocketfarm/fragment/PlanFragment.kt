package com.app.pocketfarm.fragment

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.pocketfarm.activity.HomeActivity
import com.app.pocketfarm.adapter.ActivateplansAdapter
import com.app.pocketfarm.adapter.MyplansAdapter
import com.app.pocketfarm.model.MyPlan
import com.app.pocketfarm.databinding.FragmentPlanBinding
import com.app.pocketfarm.helper.Constant
import com.app.pocketfarm.utils.DialogUtils
import com.google.gson.Gson
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class PlanFragment : Fragment() {

    lateinit var binding: FragmentPlanBinding
    lateinit var activity: Activity
    lateinit var session: com.app.pocketfarm.helper.Session

    private var adapter: com.app.pocketfarm.adapter.SliderAdapterExample? = null





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlanBinding.inflate(inflater, container, false)


        activity = requireActivity()
        session = com.app.pocketfarm.helper.Session(activity)
        adapter = com.app.pocketfarm.adapter.SliderAdapterExample(getActivity())

        (activity as HomeActivity).binding.rlToolbar.visibility = View.VISIBLE

        val swipeRefreshLayout: SwipeRefreshLayout = binding.swipeRefreshLayout

        slideslist()
        binding.imageSlider.setSliderAdapter(adapter!!)
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!

        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH)
        binding.imageSlider.setIndicatorSelectedColor(Color.WHITE)
        binding.imageSlider.setIndicatorUnselectedColor(Color.GRAY)
        binding.imageSlider.setScrollTimeInSec(3)
        binding.imageSlider.setAutoCycle(true)
        binding.imageSlider.startAutoCycle()






        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvplan.layoutManager = linearLayoutManager



        binding.tvRecharge.text = "Balance Recharge"+" : ₹"+session.getData(com.app.pocketfarm.helper.Constant.RECHARGE)

        binding.rlPlan.setOnClickListener(View.OnClickListener {
            binding.rlPlan.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F8B328"))
            binding.rlActivate.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.tvPlan.setTextColor(Color.parseColor("#FFFFFF"))
            binding.tvActivate.setTextColor(Color.parseColor("#000000"))
            myplans(swipeRefreshLayout)

        })

        binding.rlActivate.setOnClickListener(View.OnClickListener {
            binding.rlActivate.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F8B328"))
            binding.rlPlan.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.tvActivate.setTextColor(Color.parseColor("#FFFFFF"))
            binding.tvPlan.setTextColor(Color.parseColor("#000000"))
            activateplan(swipeRefreshLayout)

        })


        myplans(swipeRefreshLayout)





        return binding.root

    }






    private fun slideslist() {
        val slides: ArrayList<com.app.pocketfarm.model.Slide> = ArrayList<com.app.pocketfarm.model.Slide>()
        val params: Map<String, String> = HashMap()
        com.app.pocketfarm.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.app.pocketfarm.helper.Constant.SUCCESS)) {
                        val `object` = JSONObject(response)
                        val jsonArray: JSONArray = `object`.getJSONArray(com.app.pocketfarm.helper.Constant.DATA)
                        val g = Gson()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: com.app.pocketfarm.model.Slide =
                                    g.fromJson(jsonObject1.toString(), com.app.pocketfarm.model.Slide::class.java)
                                slides.add(group)
                            } else {
                                break
                            }
                        }
                        adapter!!.renewItems(slides)
                    } else {
                        DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, com.app.pocketfarm.helper.Constant.PLAN_SLIDE_LIST, params, true)
    }


    private fun myplans(swipeRefreshLayout:SwipeRefreshLayout) {
        swipeRefreshLayout.setOnRefreshListener { myplans(swipeRefreshLayout) }
        val params: MutableMap<String, String> = HashMap()
        params[com.app.pocketfarm.helper.Constant.USER_ID]= session.getData(com.app.pocketfarm.helper.Constant.USER_ID)
        params[com.app.pocketfarm.helper.Constant.CATEGORY]= "fruits"

        com.app.pocketfarm.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.app.pocketfarm.helper.Constant.SUCCESS)) {
                        val `object` = JSONObject(response)
                        val jsonArray: JSONArray = `object`.getJSONArray(com.app.pocketfarm.helper.Constant.DATA)
                        val g = Gson()
                        val myPlans: java.util.ArrayList<MyPlan> = java.util.ArrayList<MyPlan>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: MyPlan = g.fromJson(jsonObject1.toString(), MyPlan::class.java)
                                myPlans.add(group)
                            } else {
                                break
                            }
                        }
                        //  Toast.makeText(getActivity(), "1" + jsonObject.getString(Constant.MESSAGE).toString(), Toast.LENGTH_SHORT).show()
                        //important
                        val adapter = MyplansAdapter(activity, myPlans)
                        binding.rvplan.adapter = adapter
                        binding.rvplan.visibility = View.VISIBLE
                        binding.animationView.visibility = View.GONE
                        swipeRefreshLayout.isRefreshing = false

                    } else {
                        DialogUtils.showCustomDialog(activity, ""+jsonObject.getString(Constant.MESSAGE))

                        binding.rvplan.visibility = View.GONE
                        binding.animationView.visibility = View.VISIBLE
                        swipeRefreshLayout.isRefreshing = false
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, com.app.pocketfarm.helper.Constant.PLAN_LIST, params, true)



    }

    private fun activateplan(swipeRefreshLayout:SwipeRefreshLayout) {
        swipeRefreshLayout.setOnRefreshListener { activateplan(swipeRefreshLayout) }
        val params: MutableMap<String, String> = HashMap()
        params[com.app.pocketfarm.helper.Constant.USER_ID]= session.getData(com.app.pocketfarm.helper.Constant.USER_ID)
        params[com.app.pocketfarm.helper.Constant.CATEGORY]= "vegetables"

        com.app.pocketfarm.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.app.pocketfarm.helper.Constant.SUCCESS)) {
                        val `object` = JSONObject(response)
                        val jsonArray: JSONArray = `object`.getJSONArray(com.app.pocketfarm.helper.Constant.DATA)
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
                        val adapter = MyplansAdapter(activity, myPlans)
                        binding.rvplan.adapter = adapter
                        binding.rvplan.visibility = View.VISIBLE
                        binding.animationView.visibility = View.GONE
                        swipeRefreshLayout.isRefreshing = false


                    } else {
                        binding.rvplan.visibility = View.GONE
                        binding.animationView.visibility = View.VISIBLE
                        swipeRefreshLayout.isRefreshing = false
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, com.app.pocketfarm.helper.Constant.PLAN_LIST, params, true)



    }




}


