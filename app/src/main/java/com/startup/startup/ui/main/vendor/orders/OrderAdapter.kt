package com.startup.startup.ui.main.vendor.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.startup.startup.R
import com.startup.startup.datamodels.Order

class OrderAdapter(private val showPackedButton: Boolean = false): RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private var dataList: ArrayList<Order> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_order, parent, false)
        return OrderViewHolder(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.dateTime.text = "${dataList[position].date}\n${dataList[position].time}"
        holder.price.text = "₹${dataList[position].price}"

        //items stored in map under each order
        var items = ""
        for((key, values) in dataList[position].items){
            val item = values as HashMap<String, String>
            items += "${item["times"]} x ${item["itemname"]}(${item["quantity"]})\n"
        }
        holder.items.text = items
        if(!showPackedButton)
            holder.markedPacked.visibility = View.GONE
    }

    fun setList(dataList: ArrayList<Order>){
        this.dataList = dataList
        notifyDataSetChanged()
    }

    inner class OrderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val dateTime: TextView = itemView.findViewById(R.id.list_item_current_order_date_time)
        val price: TextView = itemView.findViewById(R.id.list_item_current_order_price)
        val items: TextView = itemView.findViewById(R.id.list_item_current_order_items)
        val markedPacked: TextView = itemView.findViewById(R.id.list_item_current_order_markpacked_button)
        init {
            markedPacked.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if(v!!.id == R.id.list_item_current_order_markpacked_button){
                //When Marker Packed
                Toast.makeText(itemView.context, "Coming Soon..", Toast.LENGTH_SHORT).show()
            }
        }
    }
}