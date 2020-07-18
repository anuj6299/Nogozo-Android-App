package com.startup.startup.ui.payment.customer.confirm

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.Window
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.startup.startup.R
import com.startup.startup.network.Database
import com.startup.startup.ui.BaseFragment
import com.startup.startup.ui.ViewModelFactory
import com.startup.startup.ui.main.DataResource
import com.startup.startup.ui.main.MainActivity
import com.startup.startup.util.Constants.USER_TYPE
import com.startup.startup.util.Constants.userType_CUSTOMER
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject
import kotlin.collections.HashMap

class CustomerConfirmFragment: BaseFragment(R.layout.fragment_payment_confirm), View.OnClickListener {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: CustomerConfirmFragmentViewModel

    private lateinit var confirmButton: MaterialButton
    private lateinit var itemsText: TextView
    private lateinit var grandTotal: TextView
    private lateinit var charges: TextView
    private lateinit var itemPrice: TextView
    private lateinit var instruction: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var orderData: HashMap<String, Any>
    private lateinit var fare: HashMap<String, String>
    private var totalFare: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        confirmButton = view.findViewById(R.id.customer_confirm_confirm_button)
        confirmButton.setOnClickListener(this)
        itemsText = view.findViewById(R.id.customer_confirm_items)
        grandTotal = view.findViewById(R.id.customer_confirm_total_price)
        charges = view.findViewById(R.id.customer_confirm_charges_price)
        itemPrice = view.findViewById(R.id.customer_confirm_base_price)
        instruction = view.findViewById(R.id.customer_confirm_instruction)
        progressBar = view.findViewById(R.id.customer_confirm_progressbar)
        view.findViewById<View>(R.id.customer_confirm_price_wrapper).setOnClickListener(this)

        viewModel = ViewModelProvider(this, factory)[CustomerConfirmFragmentViewModel::class.java]

        orderData = arguments!!.getSerializable("order") as HashMap<String, Any>

        subscribeObserver()
        viewModel.getFare(orderData["itemprice"] as String, orderData["shopareaid"] as String)

    }

    private fun subscribeObserver(){
        viewModel.getFareLiveData().removeObservers(viewLifecycleOwner)
        viewModel.getFareLiveData().observe(viewLifecycleOwner, Observer {
            when(it.status){
                DataResource.Status.SUCCESS -> {
                    fare = it.data
                    totalFare = orderData["itemprice"].toString().toInt() + fare["total"]!!.toInt()
                    orderData["price"] = totalFare.toString()
                    orderData["charges"] = fare["total"]!!
                    setItemsToText()
                    progressBar.visibility = View.GONE
                    confirmButton.visibility = View.VISIBLE
                }
                DataResource.Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    confirmButton.visibility = View.GONE
                }
                DataResource.Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    confirmButton.visibility = View.GONE
                }
            }
        })
    }

    private fun setItemsToText(){
        CoroutineScope(Default).launch{
            var itemString = ""
            for((key, value) in orderData["items"] as HashMap<String, Any>){
                val details = value as HashMap<String, String>
                itemString += "${details["times"]}x ${details["itemname"]}\n"
                //key=id, value={name, quantity, times}
            }
            itemString.removeSuffix("\n")
            withContext(Main){
                itemsText.text = itemString
            }
        }
        itemPrice.text = "₹ ${orderData["itemprice"] as String}"
        charges.text = "₹ ${fare["total"]}"
        grandTotal.text = "₹ $totalFare"
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.customer_confirm_price_wrapper -> {
                chargesDialog();
            }
            R.id.customer_confirm_confirm_button -> {
                loadingDialog()
                val instructions = instruction.text.toString()
                CoroutineScope(IO).launch{
                    orderData["customeraddress"] = viewModel.getUserAddress()
                    orderData["customername"] = viewModel.getUserName()
                    orderData["customerphone"] = viewModel.getUserPhone()
                    orderData["customerid"] = viewModel.getUserId()
                    if(instructions.isNotEmpty())
                        orderData["shopinstruction"] = instructions
                    val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
                    val timeFormatter = SimpleDateFormat("HH:mm")
                    orderData["date"] = dateFormatter.format(Date())
                    orderData["time"] = timeFormatter.format(Date())
                    val datetimeFormatter = SimpleDateFormat("yyyyMMddHHmmss")
                    orderData["datetime"] = datetimeFormatter.format(Date())
                    Database().createOrder().setValue(orderData).addOnCompleteListener{
                        if(it.isSuccessful){
                            createDialog()
                        }
                    }
                }
            }
        }
    }

    private fun createDialog(){
        val b = AlertDialog.Builder(context!!)
        b.setTitle("Order Booked")
        b.setMessage("Your Order have been Booked.\ndfsjdn")
        b.setPositiveButton("YAY!!"){ dialogInterface: DialogInterface, i: Int ->
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(USER_TYPE, userType_CUSTOMER)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
        val dialog = b.create()
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun loadingDialog(){
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.show()
    }

    private fun chargesDialog(){
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_extra_charge)

        val close: MaterialButton = dialog.findViewById(R.id.dialog_extra_charge_close)
        close.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)

    }
}