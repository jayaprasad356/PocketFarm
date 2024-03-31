package com.app.pocketfarm.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView

import com.bumptech.glide.Glide
import com.app.pocketfarm.model.MyPlan
import com.app.pocketfarm.R
import com.app.pocketfarm.activity.PaymentActivity
import com.app.pocketfarm.helper.ApiConfig
import com.app.pocketfarm.helper.Constant
import com.app.pocketfarm.helper.Session
import com.app.pocketfarm.model.Selltomarket
import org.json.JSONException
import org.json.JSONObject

class SelltomarketAdapter(
    val activity: Activity,
    myplan: java.util.ArrayList<Selltomarket>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val myplan: ArrayList<Selltomarket>
    val activitys: Activity
    val session: Session = Session(activity)

    private var selectedPosition: Int = -1
    init {
        this.myplan = myplan
        this.activitys = activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.layout_selltomarket, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holderParent: RecyclerView.ViewHolder, position: Int) {
        val holder: ItemHolder = holderParent as ItemHolder
        val report: Selltomarket = myplan[position]



        holder.tvMarketName.text = report.name


        val price = "<font color='#FF000000'>Current Selling Price:</font> "+"<font color='#00B251'>Rs.${report.price}</font>"

        holder.tvMarketPrice.text = Html.fromHtml(price)

        // Set checked state based on position
        holder.rbMarket.isChecked = position == selectedPosition

        // default selected market set position 0
        if (position == 0 && session.getData(Constant.MARKET_ID) == "market_id") {
            holder.rbMarket.isChecked = true
            selectedPosition = 0
            val select_id = report.id
            session.setData(Constant.MARKET_ID, select_id)
        }

        // Handle click event for radio button
        holder.rbMarket.setOnClickListener {
            // Update selected position
            selectedPosition = holder.adapterPosition
            val select_id = report.id
            session.setData(Constant.MARKET_ID, select_id)
            notifyDataSetChanged()
        }

        holder.cardMarket.setOnClickListener {
            holder.rbMarket.isChecked = position == selectedPosition
            selectedPosition = holder.adapterPosition
            val select_id = report.id
            session.setData(Constant.MARKET_ID, select_id)
            notifyDataSetChanged()
        }



        // selectedPosition to Activity



    }






    override fun getItemCount(): Int {
        return myplan.size
    }

    internal class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardMarket: CardView
        val tvMarketName: TextView
        val tvMarketPrice: TextView
        val rbMarket: RadioButton


        init {
            cardMarket = itemView.findViewById(R.id.cardMarket)
            tvMarketName = itemView.findViewById(R.id.tvMarketName)
            tvMarketPrice = itemView.findViewById(R.id.tvMarketPrice)
            rbMarket = itemView.findViewById(R.id.rbMarket)

        }
    }



}

