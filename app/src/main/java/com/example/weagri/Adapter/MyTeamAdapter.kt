package com.example.weagri.Adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.example.weagri.Model.MyPlan
import com.example.weagri.Model.MyTeam
import com.example.weagri.R

class MyTeamAdapter(
    val activity: Activity,
    myTeam : ArrayList<MyTeam>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val myTeam: ArrayList<MyTeam>
    val activitys: Activity

    init {
        this.myTeam = myTeam
        this.activitys = activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.layout_myteam, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder: ItemHolder = holderParent as ItemHolder
        val report: MyTeam = myTeam[position]


        holder.rlMain.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recycler_anim1))


//        holder.tvName.text = report.name
        holder.tvMobile.text = report.mobile


    }


    override fun getItemCount(): Int {
        return myTeam.size
    }

    internal class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tvName: TextView
        val tvMobile: TextView
        val rlMain: RelativeLayout



        init {
//            tvName = itemView.findViewById(R.id.tvName)
            tvMobile = itemView.findViewById(R.id.tvMobile)
            rlMain = itemView.findViewById(R.id.rlMain)


        }
    }
}