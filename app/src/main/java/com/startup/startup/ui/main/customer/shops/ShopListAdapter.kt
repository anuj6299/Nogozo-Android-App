package com.startup.startup.ui.main.customer.shops

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.startup.startup.R
import com.startup.startup.datamodels.Shop
import com.startup.startup.network.Database

class ShopListAdapter(private val onShopClickInterface: OnShopClickInterface): RecyclerView.Adapter<ShopListAdapter.ShopsViewHolder>() {

    private var dataList: ArrayList<Shop> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopsViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_shop, parent, false)
        return ShopsViewHolder(v, onShopClickInterface)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ShopsViewHolder, position: Int) {
        holder.name.text = dataList[position].shopName
        //GET SHOP ADDRESS
        if(dataList[position].shopAddress == null){
            Database().getShopAddress(dataList[position].shopId).addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    dataList[position].shopAddress = snapshot.value as String
                    notifyDataSetChanged()
                }
            })
        }else{
            holder.address.text = dataList[position].shopAddress
        }
        //GET SHOP STATUS
        if(dataList[position].shopAddress == null){
            Database().getShopStatus(dataList[position].shopId).addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    dataList[position].shopCurrentStatus = snapshot.value as String
                    notifyDataSetChanged()
                }
            })
        }else{
            if(dataList[position].shopCurrentStatus.equals("open", true))
                holder.available.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
            else
                holder.available.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
            holder.available.text = dataList[position].shopCurrentStatus
        }
    }

    fun setItemList(dataList: ArrayList<Shop>){
        this.dataList = dataList
        notifyDataSetChanged()
    }

    fun getItemAt(position: Int): Shop{
        return dataList[position]
    }

    inner class ShopsViewHolder(itemView: View, private val onShopClickInterface: OnShopClickInterface): RecyclerView.ViewHolder(itemView), View.OnClickListener{

        init {
            itemView.setOnClickListener(this)
        }

        var name: TextView = itemView.findViewById(R.id.list_item_shop_name)
        var address: TextView = itemView.findViewById(R.id.list_item_shop_address)
        var available: TextView = itemView.findViewById(R.id.list_item_shop_available)

        override fun onClick(v: View?) {
            if(dataList[adapterPosition].shopAddress != null)
                onShopClickInterface.onShopClick(adapterPosition)
            else
                Toast.makeText(itemView.context, "Please Wait For Data To Load...", Toast.LENGTH_SHORT).show()
        }
    }

    interface OnShopClickInterface{
        fun onShopClick(position: Int)
    }

}