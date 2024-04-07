package com.app.pocketfarm.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
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
import com.airbnb.lottie.LottieAnimationView

import com.bumptech.glide.Glide
import com.app.pocketfarm.model.MyPlan
import com.app.pocketfarm.R
import com.app.pocketfarm.activity.PaymentActivity
import com.app.pocketfarm.activity.RechargeActivity
import com.app.pocketfarm.helper.ApiConfig
import com.app.pocketfarm.helper.Constant
import com.app.pocketfarm.helper.Session
import org.json.JSONException
import org.json.JSONObject

class MyplansAdapter(
    val activity: Activity,
    myplan: java.util.ArrayList<MyPlan>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val myplan: ArrayList<MyPlan>
    val activitys: Activity
    val session = Session(activity)

    init {
        this.myplan = myplan
        this.activitys = activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.layout_myplans, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder: ItemHolder = holderParent as ItemHolder
        val report: MyPlan = myplan[position]

        holder.cardView.startAnimation(
            AnimationUtils.loadAnimation(
                holder.itemView.context,
                R.anim.recycler_anim1
            )
        )


        holder.btnActivate.setOnClickListener {
            activatePlan(report.id)

        }

        holder.tvplanName.text = report.products

        holder.tvplan.text = "₹ " + report.price
        holder.tvDailyIncome.text = "₹ " + report.daily_income
        holder.tvTotalIncome.text =  report.num_times
        holder.tvInvitebonus.text = "₹ " + report.invite_bonus
        holder.tvQuantity.text = report.daily_quantity + " " + report.unit
        // holder.tvValidity.text = report.validity + " days"
        Glide.with(activity).load(report.image).placeholder(R.drawable.sample_agri)
            .into(holder.ivImage)


    }

    private fun activatePlan(id: String?) {
        val session = Session(activity)
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session.getData(Constant.USER_ID)!!
        params["plan_id"] = id!!
        ApiConfig.RequestToVolley({ result, response ->

            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    val msg = jsonObject.getString(Constant.MESSAGE).toString()
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        showCustomDialog(msg)
                    } else {

                        showCustomDialog(msg)

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
        val tvDailyIncome: TextView
        val tvTotalIncome: TextView
        val tvInvitebonus: TextView

        //  val tvValidity : TextView
        val ivImage: ImageView
        val btnActivate: Button
        val cardView: CardView
        val tvplanName: TextView
        val tvQuantity: TextView


        init {
            tvplan = itemView.findViewById(R.id.tvplan)
            tvDailyIncome = itemView.findViewById(R.id.tvDailyIncome)
            tvTotalIncome = itemView.findViewById(R.id.tvTotalIncome)
            tvInvitebonus = itemView.findViewById(R.id.tvInvitebonus)
            //tvValidity = itemView.findViewById(R.id.tvValidity)
            ivImage = itemView.findViewById(R.id.ivImage)
            btnActivate = itemView.findViewById(R.id.btnActivate)
            cardView = itemView.findViewById(R.id.cardView)
            tvplanName = itemView.findViewById(R.id.tvplanName)
            tvQuantity = itemView.findViewById(R.id.tvQuantity)
        }
    }

    private fun showCustomDialog(status: String) {
        val builder = AlertDialog.Builder(activity)
        val dialogView: View = activity.layoutInflater.inflate(R.layout.plan_dialog, null)

        builder.setView(dialogView)
        val dialog = builder.create()
        val tvMessage = dialogView.findViewById<TextView>(R.id.tvMessage)



        if (status.equals("Production started successfully")) {
            tvMessage.text = "Start sell from tomorrow."


        } else if (status.equals("You have already started this production")) {
            tvMessage.text = status



        } else if (status.equals("Insufficient balance to start this production")) {
            tvMessage.text = "Insufficient recharge to start this production"



        } else {
            tvMessage.text = status

        }
            dialog.show()

    }

}

