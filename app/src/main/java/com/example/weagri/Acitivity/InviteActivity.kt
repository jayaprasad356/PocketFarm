package com.example.weagri.Acitivity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weagri.R
import com.example.weagri.databinding.ActivityInviteBinding
import com.example.weagri.databinding.ActivitySupportBinding

class InviteActivity : AppCompatActivity() {

    lateinit var binding: ActivityInviteBinding
    lateinit var activity: Activity
    lateinit var session: com.example.weagri.helper.Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInviteBinding.inflate(layoutInflater)
        activity = this
        session = com.example.weagri.helper.Session(activity)

        binding.ibBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        binding.tvCopy.text = session.getData(com.example.weagri.helper.Constant.REFER_CODE)

        binding.rlCopy.setOnClickListener {
            val clipboard = android.content.Context.CLIPBOARD_SERVICE
            val clip = android.content.ClipData.newPlainText("label", session.getData(com.example.weagri.helper.Constant.REFER_CODE))
            val clipboardManager = getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            clipboardManager.setPrimaryClip(clip)
            android.widget.Toast.makeText(activity, "Copied", android.widget.Toast.LENGTH_SHORT).show()
        }

        binding.btnInvite.setOnClickListener {
            val shareBody = "Hey, I am using WeAgri App. It's a great app to earn money. Use my refer code " + session.getData(
                com.example.weagri.helper.Constant.REFER_CODE) + " to get 100 coins on signup. Download the app from the link below.\n" + ""
            val sharingIntent = android.content.Intent(android.content.Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "WeAgri")
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
            startActivity(android.content.Intent.createChooser(sharingIntent, "Share using"))
        }

        return setContentView(binding!!.root)
    }
}