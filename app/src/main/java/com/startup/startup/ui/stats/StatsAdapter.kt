package com.startup.startup.ui.stats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.startup.startup.R
import com.startup.startup.datamodels.VendorStats

class StatsAdapter: RecyclerView.Adapter<StatsAdapter.StatsViewHolder>() {

    private var stats: ArrayList<VendorStats> = ArrayList()
    private var size: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_stats, parent, false)
        return StatsViewHolder(v)
    }

    override fun getItemCount(): Int {
        return stats.size
    }

    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        holder.amount.text = "₹${stats[size-position-1].total_amount}"
        holder.orders.text = stats[size-position-1].total_orders
        holder.month.text = stats[size-position-1].month
    }

    fun setData(stats: ArrayList<VendorStats>){
        this.stats = stats
        size = stats.size
        notifyDataSetChanged()
    }

    inner class StatsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val amount: TextView = itemView.findViewById(R.id.list_item_stats_amount)
        val orders: TextView = itemView.findViewById(R.id.list_item_stats_orders)
        val month: TextView = itemView.findViewById(R.id.list_item_stats_month)
    }
}