package com.startup.startup.ui.stats.vendor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.startup.startup.SessionManager
import com.startup.startup.datamodels.VendorStats
import com.startup.startup.network.Database
import com.startup.startup.ui.main.DataResource
import com.startup.startup.util.Helper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import javax.inject.Inject

class VendorStatsFragmentViewModel
@Inject constructor(
    private val database: Database,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val statsLiveData: MediatorLiveData<DataResource<ArrayList<VendorStats>>> =
        MediatorLiveData()

    fun getStatsLiveData(): LiveData<DataResource<ArrayList<VendorStats>>> {
        return statsLiveData
    }

    fun getStats() {
        database.getShopStats(sessionManager.getUserId())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    statsLiveData.value = DataResource.error(error.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val stats: ArrayList<VendorStats> = ArrayList()
                        CoroutineScope(Default).launch{
                            for(month in snapshot.children){
                                val stat = VendorStats(
                                    month.key!!,
                                    month.child("total_amount").value.toString(),
                                    month.child("total_orders").value.toString(),
                                    Helper.getMonthString(month.key!!.substring(4))
                                )
                                stats.add(stat)
                            }
                            statsLiveData.postValue(DataResource.success(stats))
                        }
                    }else{
                        statsLiveData.value = DataResource.error("No Orders Completed")
                    }
                }
            })
    }
}