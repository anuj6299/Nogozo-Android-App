package com.startup.startup.ui.payment.customer.confirm

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.startup.startup.R
import com.startup.startup.network.Database
import com.startup.startup.ui.BaseFragment
import com.startup.startup.ui.ViewModelFactory
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
import java.util.*
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

    private lateinit var orderData: HashMap<String, Any>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        confirmButton = view.findViewById(R.id.customer_confirm_confirm_button)
        confirmButton.setOnClickListener(this)
        itemsText = view.findViewById(R.id.customer_confirm_items)
        grandTotal = view.findViewById(R.id.customer_confirm_total_price)
        charges = view.findViewById(R.id.customer_confirm_charges_price)
        itemPrice = view.findViewById(R.id.customer_confirm_base_price)
        instruction = view.findViewById(R.id.customer_confirm_instruction)

        viewModel = ViewModelProvider(this, factory)[CustomerConfirmFragmentViewModel::class.java]

        orderData = arguments!!.getSerializable("order") as HashMap<String, Any>

        setItemsToText()

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
        itemPrice.text = "₹ ${orderData["price"] as String}"
        charges.text = "₹ 10"
        grandTotal.text = "₹ ${orderData["price"] as String}"
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.customer_confirm_confirm_button -> {
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
}