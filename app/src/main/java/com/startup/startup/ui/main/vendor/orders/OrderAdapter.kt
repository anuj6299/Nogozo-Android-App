package com.startup.startup.ui.main.vendor.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.startup.startup.R
import com.startup.startup.datamodels.Order
import com.startup.startup.network.Database
import com.startup.startup.util.OrderByDateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderAdapter(
    private val showPackedButton: Boolean = false
): RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private var orderList: ArrayList<Order> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_order, parent, false)
        return OrderViewHolder(v)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.dateTime.text = "${orderList[position].date}\n${orderList[position].time}"
        holder.price.text = "₹${orderList[position].price}"
        holder.status.text = orderList[position].status

        //items stored in map under each order
        var items = ""
        for((key, values) in orderList[position].items){
            val item = values as HashMap<String, String>
            items += "${item["times"]} x ${item["itemname"]}(${item["quantity"]})\n"
        }
        holder.items.text = items
        if(!showPackedButton)
            holder.markedPacked.visibility = View.GONE

        if(orderList[position].status != "New Order")
            holder.markedPacked.visibility = View.GONE
    }

    fun setList(dataList: ArrayList<Order>){
        orderList = dataList
        CoroutineScope(Dispatchers.Default).launch{
            orderList.sortWith(OrderByDateTime())
            withContext(Main){
                notifyDataSetChanged()
            }
        }
    }

    inner class OrderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val dateTime: TextView = itemView.findViewById(R.id.list_item_current_order_date_time)
        val price: TextView = itemView.findViewById(R.id.list_item_current_order_price)
        val items: TextView = itemView.findViewById(R.id.list_item_current_order_items)
        val status: TextView = itemView.findViewById(R.id.list_item_current_order_status)
        val markedPacked: TextView = itemView.findViewById(R.id.list_item_current_order_markpacked_button)
        init {
            markedPacked.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if(v!!.id == R.id.list_item_current_order_markpacked_button){
                Database().markedOrderPacked(orderList[adapterPosition].orderId, "Packed").addOnCompleteListener{
                    if(it.isSuccessful){
                        orderList[adapterPosition].status = "Packed"
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }
}