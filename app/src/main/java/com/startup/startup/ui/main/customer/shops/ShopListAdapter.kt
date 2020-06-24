package com.startup.startup.ui.main.customer.shops

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.startup.startup.R
import com.startup.startup.datamodels.Shop
import de.hdodenhof.circleimageview.CircleImageView

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
        holder.distance.text = ""//dataList[position].distance
    }

    fun setItemList(dataList: ArrayList<Shop>){
        this.dataList = dataList
        notifyDataSetChanged()
    }

    fun getItemAt(position: Int): Shop{
        return dataList[position]
    }

    class ShopsViewHolder(itemView: View, private val onShopClickInterface: OnShopClickInterface): RecyclerView.ViewHolder(itemView), View.OnClickListener{

        init {
            itemView.setOnClickListener(this)
        }

        var name: TextView = itemView.findViewById(R.id.list_item_shop_name)
        var distance: TextView = itemView.findViewById(R.id.list_item_shop_distance)

        override fun onClick(v: View?) {
            onShopClickInterface.onShopClick(adapterPosition)
        }
    }

    interface OnShopClickInterface{
        fun onShopClick(position: Int)
    }

}