package com.startup.startup.ui.main.customer.itemsInShop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.recyclerview.widget.RecyclerView
import com.startup.startup.R
import com.startup.startup.datamodels.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemsInShopAdapter: RecyclerView.Adapter<ItemsInShopAdapter.ItemsViewHolder>() {

    private var dataList: ArrayList<Item> = ArrayList()
    private var selected: HashMap<Int, Int> = HashMap() // position, quantity TODO SWITCH position -> itemId PRIORITY HIGH
    private var priceLiveData: MediatorLiveData<Int> = MediatorLiveData()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_iteminshop, parent, false)
        return ItemsViewHolder(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.itemName.text = dataList[position].itemName
        holder.itemPrice.text = "₹${dataList[position].itemPrice}"
        holder.itemDesc.text = dataList[position].itemQuantity
        if(selected.containsKey(position)){
            holder.itemQuantity.text = "${selected[position]}"
        }else{
            holder.itemQuantity.text = "0"
        }

        println("$position ${dataList[position].isAvailable}")
        if(!dataList[position].isAvailable!!){
            holder.itemName.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.grey))
            holder.itemPrice.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.grey))
            holder.itemDesc.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.grey))
            holder.itemQuantity.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.grey))
            holder.wrapper.visibility = View.INVISIBLE
        }else{
            holder.itemName.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            holder.itemPrice.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            holder.itemDesc.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            holder.itemQuantity.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            holder.wrapper.visibility = View.VISIBLE
        }
    }

    fun setData(dataList: ArrayList<Item>){
        this.dataList = dataList
        notifyDataSetChanged()
    }

    fun getPriceLiveData(): LiveData<Int>{
        return priceLiveData
    }

    fun calculateTotal(){
        CoroutineScope(Dispatchers.Default).launch{
            var price = 0
            for((key, value) in selected){
                price += dataList[key].itemPrice.toInt()*value
            }
            priceLiveData.postValue(price)
        }
    }

    fun getSelectedItem(): HashMap<String, Any>{
        val map: HashMap<String, Any> = HashMap()
        for((key, value) in selected){
            val itemMap: HashMap<String, String> = HashMap()
            itemMap["itemname"] = dataList[key].itemName!!
            itemMap["quantity"] = dataList[key].itemQuantity!!
            itemMap["times"] = value.toString()
            map[dataList[key].itemId!!] = itemMap
        }
        return map
    }

    inner class ItemsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var itemName: TextView = itemView.findViewById(R.id.list_item_iteminshop_name)
        var itemPrice: TextView = itemView.findViewById(R.id.list_item_iteminshop_price)
        var itemDesc: TextView = itemView.findViewById(R.id.list_item_iteminshop_desc)
        var itemQuantity: TextView = itemView.findViewById(R.id.list_item_iteminshop_quantity)
        var wrapper: View = itemView.findViewById(R.id.action_wrapper)
        private val minus: ImageButton = itemView.findViewById(R.id.list_item_iteminshop_minus)
        private val add: ImageButton = itemView.findViewById(R.id.list_item_iteminshop_add)

        init {
            minus.setOnClickListener(this)
            add.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v!!.id) {
                R.id.list_item_iteminshop_add -> {
                    if(selected.containsKey(adapterPosition)){
                        selected[adapterPosition] = selected[adapterPosition]!! + 1
                    }else{
                        selected[adapterPosition] = 1
                    }
                    notifyDataSetChanged()
                    calculateTotal()
                }
                R.id.list_item_iteminshop_minus -> {
                    if(selected.containsKey(adapterPosition)){
                        val a = selected[adapterPosition]!! - 1
                        if(a == 0){
                            selected.remove(adapterPosition)
                        }else{
                            selected[adapterPosition] = a
                        }
                        notifyDataSetChanged()
                        calculateTotal()
                    }
                }
            }
        }
    }
}