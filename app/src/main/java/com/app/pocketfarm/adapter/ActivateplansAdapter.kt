package com.app.pocketfarm.adapter

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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.app.pocketfarm.model.MyPlan
import com.app.pocketfarm.R
import com.app.pocketfarm.helper.ApiConfig
import com.app.pocketfarm.helper.Constant
import com.app.pocketfarm.helper.Session
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


        val context = holder.itemView.context
        if (report.claim.equals("1")) {
            // Set background color to primary_color
            holder.btnActivate.setBackgroundColor(ContextCompat.getColor(context, R.color.primary_color))
        } else {
            // Set background color to grey_extra_light
            holder.btnActivate.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_extra_light))
        }




        holder.tvplanName.text = report.products

        holder.tvplan.text = "₹ " + report.income

        holder.tvstartedate.text =report.joined_date
        holder.tvprice.text = "₹ " + report.price

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
                    if (jsonObject.getBoolean(com.app.pocketfarm.helper.Constant.SUCCESS)) {
                        val `object` = JSONObject(response)
                        val jsonArray: JSONArray = `object`.getJSONArray(com.app.pocketfarm.helper.Constant.DATA)
                        val g = Gson()

                        Toast.makeText(activity, "" + jsonObject.getString(com.app.pocketfarm.helper.Constant.MESSAGE).toString(), Toast.LENGTH_SHORT).show()


                    } else {
                        Toast.makeText(activity, "" + jsonObject.getString(com.app.pocketfarm.helper.Constant.MESSAGE).toString(), Toast.LENGTH_SHORT).show()
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
    //    val tvValidity : TextView
        val  ivImage : ImageView
        val  btnActivate : Button
        val cardView : CardView
        var tvplanName : TextView
        var tvstartedate : TextView
        var tvprice : TextView


        init {
            tvplan = itemView.findViewById(R.id.tvplan)
            ivImage = itemView.findViewById(R.id.ivImage)
            btnActivate = itemView.findViewById(R.id.btnActivate)
            cardView = itemView.findViewById(R.id.cardView)
            tvplanName = itemView.findViewById(R.id.tvplanName)
            tvstartedate = itemView.findViewById(R.id.tvstartedate)
            tvprice = itemView.findViewById(R.id.tvprice)

        }
    }
}