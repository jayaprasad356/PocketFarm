package com.example.weagri.Adapter

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
import com.example.weagri.Acitivity.PaymentActivity
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

class MyplansAdapter(
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
            LayoutInflater.from(activity).inflate(R.layout.layout_myplans, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder: ItemHolder = holderParent as ItemHolder
        val report: MyPlan = myplan[position]

        holder.cardView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recycler_anim1))


        holder.btnActivate.setOnClickListener {
            activatePlan(report.id)

        }

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
        params[Constant.USER_ID] = session.getData(Constant.USER_ID)!!
        params["plan_id"] = id!!
        ApiConfig.RequestToVolley({ result, response ->

            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val `object` = JSONObject(response)

                        showCustomDialog("Plan Activated Successfully")


                    } else {
                        val msg = jsonObject.getString(Constant.MESSAGE).toString()
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

    private fun showCustomDialog(status: String) {
        val builder = AlertDialog.Builder(activity)
        val dialogView: View = activity.layoutInflater.inflate(R.layout.plan_dialog, null)

        builder.setView(dialogView)
        val dialog = builder.create()
        val ivSuccess = dialogView.findViewById<ImageView>(R.id.ivSuccess)
        val tvStatus = dialogView.findViewById<TextView>(R.id.tvStatus)
        val tvMessage = dialogView.findViewById<TextView>(R.id.tvMessage)
        val btnOk = dialogView.findViewById<Button>(R.id.btnOk)
        val avRecharge = dialogView.findViewById<LottieAnimationView>(R.id.avRecharge)


        if (status.equals("Plan Activated Successfully")) {
            ivSuccess.setImageResource(R.drawable.success)
            tvStatus.text = "Plan Activated Successfully"
            tvMessage.text = "Your plan has been activated successfully. You can now start earning from your plan."
            btnOk.text = "Done"
            avRecharge.visibility = View.GONE
            btnOk.setOnClickListener {
                dialog.dismiss()

            }
        } else if (status.equals("You have already applied this Plan")) {
            ivSuccess.setImageResource(R.drawable.warning)
            tvStatus.text = "Unable to apply this Plan"
            tvMessage.text = "You have already applied this Plan . You can apply only one plan at a time."
            btnOk.text = "Done"
            avRecharge.visibility = View.GONE
            btnOk.setOnClickListener {
                dialog.dismiss()

            }

        } else if (status.equals("Insufficient balance to apply for this plan")) {
            ivSuccess.setImageResource(R.drawable.warning)
            tvStatus.text = "Unable to apply this Plan"
            tvMessage.text = "Insufficient balance to apply for this plan"
            btnOk.text = "Recharge Now"
            avRecharge.visibility = View.VISIBLE
            ivSuccess.visibility = View.GONE
            btnOk.setOnClickListener {
                dialog.dismiss()
                val intent = Intent(activity, PaymentActivity::class.java)
                activity.startActivity(intent)
            }
        }


        dialog.show()
    }
}