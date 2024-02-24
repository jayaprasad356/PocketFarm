package com.example.weagri.Fragment

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weagri.Acitivity.HomeActivity
import com.example.weagri.Adapter.SliderAdapterExample
import com.example.weagri.Model.Slide
import com.example.weagri.databinding.FragmentExploreBinding
import com.example.weagri.helper.ApiConfig
import com.example.weagri.helper.Constant
import com.example.weagri.helper.Session
import com.example.weagri.utils.DialogUtils
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

//        (activity as HomeActivity).binding.coordinatorLayout.setBackgroundColor(Color.WHITE)
//        (activity as HomeActivity).binding.rlToolbar.setBackgroundColor(Color.WHITE)


        return binding.root

    }


}