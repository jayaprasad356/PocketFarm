package com.example.weagri.Fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.weagri.Acitivity.HomeActivity
import com.example.weagri.Acitivity.TransactionActivity
import com.example.weagri.Acitivity.UpdateProfileActivity
import com.example.weagri.Acitivity.WithdrawalActivity
import com.example.weagri.R
import com.example.weagri.databinding.ActivityHomeBinding
import com.example.weagri.databinding.FragmentMoreBinding
import com.example.weagri.databinding.FragmentPlansBinding
import com.example.weagri.helper.Session
import com.google.android.material.bottomnavigation.BottomNavigationView


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

        (activity as HomeActivity).binding.rlToolbar.visibility = View.VISIBLE
        binding.tvRecharge.text =  "₹" + session.getData(com.example.weagri.helper.Constant.RECHARGE_BALANCE)
        binding.tvTotalEarnings.text = "₹" + session.getData(com.example.weagri.helper.Constant.TOTAL_EARNINGS)


        binding.rlwithdraw.setOnClickListener {
            startActivity(Intent(activity, WithdrawalActivity::class.java))
        }

        binding.rlhistory.setOnClickListener {
            startActivity(Intent(activity, TransactionActivity::class.java))
        }

        binding.rlUpdateprofile.setOnClickListener{
            startActivity(Intent(activity, UpdateProfileActivity::class.java))
        }


        binding.rlCutomerSupport.setOnClickListener{

        }


        binding.rlLogout.setOnClickListener{
            session.logoutUser(activity)
        }

        return binding.root
    }

}