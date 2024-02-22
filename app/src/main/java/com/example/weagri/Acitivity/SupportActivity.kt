package com.example.weagri.Acitivity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weagri.R
import com.example.weagri.databinding.ActivitySupportBinding
import com.example.weagri.databinding.FragmentMyteamBinding

class SupportActivity : AppCompatActivity() {

    lateinit var binding: ActivitySupportBinding
    lateinit var activity: Activity
    lateinit var session: com.example.weagri.helper.Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySupportBinding.inflate(layoutInflater)
        activity = this
        session = com.example.weagri.helper.Session(activity)

        binding.ibBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        return setContentView(binding!!.root)
    }
}