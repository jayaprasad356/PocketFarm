package com.example.weagri.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.example.weagri.Model.MyPlan
import com.example.weagri.Model.MyTeam
import com.example.weagri.Model.Transaction
import com.example.weagri.R
import com.example.weagri.helper.ApiConfig
import com.example.weagri.helper.Constant
import com.example.weagri.helper.Session
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ActivateplansAdapter(
    val activity: Activity,
    myplan: java.util.ArrayList<MyPlan>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val myplan: ArrayList<MyPlan>
    val activitys: Activity

    init {
        this.myplan = myplan
        this.activitys = activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.layout_activateplan, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder: ItemHolder = holderParent as ItemHolder
        val report: MyPlan = myplan[position]
        holder.cardView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recycler_anim1))



        holder.btnActivate.visibility = View.GONE

//        holder.btnActivate.setOnClickListener {
//            activatePlan(report.id)
//
//        }

        holder.tvplan.text = report.crop
        holder.tvDailyIncome.text = "Rs." + report.daily_income
        holder.tvTotalIncome.text = "Rs." + report.total_income
        holder.tvInvitebonus.text = "Rs." + report.invite_bonus
        holder.tvValidity.text = report.validity + " days"
        Glide.with(activity).load(report.image).placeholder(R.drawable.sample_agri).into(holder.ivImage)



    }

    private fun activatePlan(id: String?) {
        val session = Session(activity)

        val params: MutableMap<String, String> = HashMap()
        params["user_id"] = "1"
        params["plan_id"] = id!!
        ApiConfig.RequestToVolley({ result, response ->

            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(com.example.weagri.helper.Constant.SUCCESS)) {
                        val `object` = JSONObject(response)
                        val jsonArray: JSONArray = `object`.getJSONArray(com.example.weagri.helper.Constant.DATA)
                        val g = Gson()

                        Toast.makeText(activity, "" + jsonObject.getString(com.example.weagri.helper.Constant.MESSAGE).toString(), Toast.LENGTH_SHORT).show()


                    } else {
                        Toast.makeText(activity, "" + jsonObject.getString(com.example.weagri.helper.Constant.MESSAGE).toString(), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }

        }, activity, Constant.ACTIVATE_PLAN, params, true)


    }


    override fun getItemCount(): Int {
        return myplan.size
    }

    internal class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvplan: TextView
        val tvDailyIncome : TextView
        val tvTotalIncome : TextView
        val tvInvitebonus : TextView
        val tvValidity : TextView
        val  ivImage : ImageView
        val  btnActivate : Button
        val cardView : CardView


        init {
            tvplan = itemView.findViewById(R.id.tvplan)
            tvDailyIncome = itemView.findViewById(R.id.tvDailyIncome)
            tvTotalIncome = itemView.findViewById(R.id.tvTotalIncome)
            tvInvitebonus = itemView.findViewById(R.id.tvInvitebonus)
            tvValidity = itemView.findViewById(R.id.tvValidity)
            ivImage = itemView.findViewById(R.id.ivImage)
            btnActivate = itemView.findViewById(R.id.btnActivate)
            cardView = itemView.findViewById(R.id.cardView)

        }
    }
}