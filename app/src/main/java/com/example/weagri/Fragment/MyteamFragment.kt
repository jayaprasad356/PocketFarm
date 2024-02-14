package com.example.weagri.Fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weagri.Adapter.MyTeamAdapter
import com.example.weagri.Adapter.MyplansAdapter
import com.example.weagri.Model.MyTeam
import com.example.weagri.databinding.FragmentMyteamBinding
import com.example.weagri.helper.ApiConfig
import com.example.weagri.helper.Constant
import com.example.weagri.helper.Session
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class MyteamFragment : Fragment() {

    lateinit var binding: FragmentMyteamBinding
    lateinit var activity: Activity
    lateinit var session: com.example.weagri.helper.Session

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMyteamBinding.inflate(inflater, container, false)

        activity = getActivity() as Activity
        session = com.example.weagri.helper.Session(activity)

        binding.tvCopy.text = session.getData(com.example.weagri.helper.Constant.REFER_CODE)

        binding.rlCopy.setOnClickListener {
            val text = binding.tvCopy.text.toString()
            val clipboard = activity.getSystemService(Activity.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("Copied Text", text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(activity, "Copied", Toast.LENGTH_SHORT).show()
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

        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvMyTeam.layoutManager = linearLayoutManager

        referlist()


        return binding.root
    }

    private fun referlist() {

        val params: MutableMap<String, String> = HashMap()
        params[com.example.weagri.helper.Constant.USER_ID] = session.getData(com.example.weagri.helper.Constant.USER_ID)
        com.example.weagri.helper.ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.example.weagri.helper.Constant.SUCCESS)) {
                        val `object` = JSONObject(response)
                        val jsonArray: JSONArray = `object`.getJSONArray(com.example.weagri.helper.Constant.DATA)
                        val g = Gson()
                        val myteam: java.util.ArrayList<MyTeam> =
                            java.util.ArrayList<MyTeam>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            if (jsonObject1 != null) {
                                val group: MyTeam = g.fromJson(jsonObject1.toString(), MyTeam::class.java)
                                myteam.add(group)
                            } else {
                                break
                            }
                        }
                     //   Toast.makeText(getActivity(), "1" + jsonObject.getString(Constant.MESSAGE).toString(), Toast.LENGTH_SHORT).show()
                        //important
                        val adapter = MyTeamAdapter(activity, myteam)
                        binding.rvMyTeam.adapter = adapter
                        binding.animationView.visibility = View.GONE

                    } else {
                        binding.animationView.visibility = View.VISIBLE
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, com.example.weagri.helper.Constant.MY_TEAM, params, true)




    }


}