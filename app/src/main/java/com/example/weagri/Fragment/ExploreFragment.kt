package com.example.weagri.Fragment

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weagri.Adapter.SliderAdapterExample
import com.example.weagri.Model.Slide
import com.example.weagri.databinding.FragmentExploreBinding
import com.example.weagri.helper.ApiConfig
import com.example.weagri.helper.Constant
import com.example.weagri.helper.Session
import com.google.gson.Gson
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class ExploreFragment : Fragment() {

    lateinit var binding: FragmentExploreBinding
    lateinit var activity: Activity
    lateinit var session: com.example.weagri.helper.Session

    private var adapter: com.example.weagri.Adapter.SliderAdapterExample? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        activity = requireActivity()
        session = com.example.weagri.helper.Session(activity)
        adapter = com.example.weagri.Adapter.SliderAdapterExample(getActivity())


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


        binding.webview.setVerticalScrollBarEnabled(true)


        explore()

        return binding.root

    }


    private fun slideslist() {
        val slides: ArrayList<com.example.weagri.Model.Slide> = ArrayList<com.example.weagri.Model.Slide>()
        val params: Map<String, String> = HashMap()
        com.example.weagri.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.example.weagri.helper.Constant.SUCCESS)) {
                        val `object` = JSONObject(response)
                        val jsonArray: JSONArray = `object`.getJSONArray(com.example.weagri.helper.Constant.DATA)
                        val g = Gson()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: com.example.weagri.Model.Slide =
                                    g.fromJson(jsonObject1.toString(), com.example.weagri.Model.Slide::class.java)
                                slides.add(group)
                            } else {
                                break
                            }
                        }
                        adapter!!.renewItems(slides)
                    } else {
                        Toast.makeText(
                            activity,
                            "" + jsonObject.getString(com.example.weagri.helper.Constant.MESSAGE).toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, com.example.weagri.helper.Constant.PLAN_SLIDE_LIST, params, true)
    }


    private fun explore() {
        val params: MutableMap<String, String> = HashMap()
        val FileParams: MutableMap<String, String> = HashMap()
        com.example.weagri.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.example.weagri.helper.Constant.SUCCESS)) {
                        val jsonArray = jsonObject.getJSONArray(com.example.weagri.helper.Constant.DATA)

                        val fontName = "poppins_regular.ttf" // Replace 'YourCustomFont.ttf' with the actual name of your font file
                        val htmlContent =
                            "<html><head><style>@font-face { font-family: 'CustomFont'; src: url('file:///android_res/font/$fontName'); } body { background-color: #EFEFEF; color: black; font-family: 'CustomFont'; }</style></head><body>" +
                                    jsonArray.getJSONObject(0).getString("main_content") +
                                    "</body></html>"
                        binding.webview.loadDataWithBaseURL(
                            "",
                            htmlContent,
                            "text/html",
                            "UTF-8",
                            ""
                        )




                    } else {
                        Toast.makeText(activity, "" + jsonObject.getString(com.example.weagri.helper.Constant.MESSAGE), Toast.LENGTH_SHORT).show()

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, activity, com.example.weagri.helper.Constant.EXPLORE_LIST, params, FileParams)
    }



}