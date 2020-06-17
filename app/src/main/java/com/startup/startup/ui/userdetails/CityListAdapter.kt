package com.startup.startup.ui.userdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.TextView
import com.startup.startup.R
import com.startup.startup.datamodels.City
import java.util.*
import kotlin.collections.ArrayList

class CityListAdapter: BaseAdapter() {

    private var OriginalList: List<City> = List(1,init = {City("Fetching ...","-1")})
    private var filteredList: List<City> = List(1,init = {City("Locating","-1")})

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v: View
        var holder = ViewHolder()

        return if(convertView == null){
            v = LayoutInflater.from(parent!!.context).inflate(R.layout.list_item_city, parent, false)
            holder.name = v.findViewById(R.id.list_item_city_view)
            v.tag = holder
            holder.name.text = filteredList[position].cityName
            v
        }else{
            holder = convertView.tag as ViewHolder
            holder.name.text = filteredList[position].cityName
            convertView
        }
    }

    override fun getItem(position: Int): City {
        return filteredList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return filteredList.size
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults? {
                val oReturn = FilterResults()
                val results: ArrayList<City> = ArrayList()
                if (constraint != null) {
                    if (OriginalList.isNotEmpty()) {
                        for (g in OriginalList) {
                            if (g.cityName.toLowerCase(Locale.ROOT).contains(constraint.toString()))
                                results.add(g)
                        }
                    }
                    if(results.isEmpty()){
                        results.add(City("Looks Like We Are Not In That City","-1"))
                    }
                    oReturn.values = results
                }
                return oReturn
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults){
                filteredList = results.values as ArrayList<City>
                notifyDataSetChanged()
            }
        }
    }

    fun setOriginalList(data: List<City>){
        this.OriginalList = data
        this.filteredList = data
        notifyDataSetChanged()
    }

    class ViewHolder{
        lateinit var name: TextView
    }
}