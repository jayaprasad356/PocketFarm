package com.app.pocketfarm.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.app.pocketfarm.R
import com.app.pocketfarm.model.Recharge
import com.app.pocketfarm.model.Review
import com.app.pocketfarm.model.Transaction

class ReviewlistAdapter(
    val activity: Activity,
    review: ArrayList<Review>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val review: ArrayList<Review>
    val activitys: Activity

    init {
        this.review = review
        this.activitys = activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.recharge_history_layout, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder: ItemHolder = holderParent as ItemHolder
        val report: Review = review[position]


        holder.tvRechargeType.text = "Review"

        holder.tvDate.text = report.datetime
        if (report.status.equals("0")) {
            holder.tvStatus.text = "Pending"
            holder.tvStatus.setTextColor(activity.resources.getColor(R.color.blue_color))
        } else if (report.status.equals("1")) {
            holder.tvStatus.text = "Verified"
            holder.tvStatus.setTextColor(activity.resources.getColor(R.color.green))
        } else if (report.status.equals("2")) {
            holder.tvStatus.text = "Cancelled"
            holder.tvStatus.setTextColor(activity.resources.getColor(R.color.red))
        }


    }


    override fun getItemCount(): Int {
        return review.size
    }

    internal class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView
        val tvAmount: TextView
        val tvStatus: TextView
        val tvRechargeType: TextView


        init {
            tvDate = itemView.findViewById(R.id.tvDate)
            tvAmount = itemView.findViewById(R.id.tvAmount)
            tvStatus = itemView.findViewById(R.id.tvStatus)
            tvRechargeType = itemView.findViewById(R.id.tvRechargeType)
        }
    }
}