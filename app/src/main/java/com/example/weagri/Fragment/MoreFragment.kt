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

        (activity as HomeActivity).binding.rlToolbar.visibility = View.GONE
//        binding.tvRecharge.text =  "₹" + session.getData(com.example.weagri.helper.Constant.RECHARGE_BALANCE)
//        binding.tvTotalEarnings.text = "₹" + session.getData(com.example.weagri.helper.Constant.TOTAL_EARNINGS)


        binding.llInvite.setOnClickListener {
            val shareBody = "Hey, I am using WeAgri App. It's a great app to earn money. Use my refer code " + session.getData(
                com.example.weagri.helper.Constant.REFER_CODE) + " to get 100 coins on signup. Download the app from the link below.\n" + ""
            val sharingIntent = android.content.Intent(android.content.Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "WeAgri")
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
            startActivity(android.content.Intent.createChooser(sharingIntent, "Share using"))
        }


        binding.tvMobile.text = session.getData(com.example.weagri.helper.Constant.MOBILE)
        binding.tvReferralCode.text = session.getData(com.example.weagri.helper.Constant.REFER_CODE)

        binding.cvWithdraw.setOnClickListener {
            startActivity(Intent(activity, WithdrawalActivity::class.java))
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

        return binding.root
    }

}