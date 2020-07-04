package com.startup.startup.ui.inventory.editinventory

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.startup.startup.R
import com.startup.startup.datamodels.Item
import com.startup.startup.ui.BaseFragment
import com.startup.startup.ui.ViewModelFactory
import com.startup.startup.ui.inventory.InventoryCommunicator
import javax.inject.Inject

class EditInventoryFragment(private val communicator: InventoryCommunicator): BaseFragment(R.layout.fragment_edit_inventory), View.OnClickListener {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: EditInventoryFragmentViewModel

    private lateinit var itemName: TextInputEditText
    private lateinit var itemPrice: TextInputEditText
    private lateinit var itemQuantity: TextInputEditText
    private lateinit var imageButton: Button
    private lateinit var done: MaterialButton

    private var oldItem: Item? = null
    private var newItem: Item? = null
    private var isNew: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this, factory)[EditInventoryFragmentViewModel::class.java]

        itemName = view.findViewById(R.id.editinventory_name_field)
        itemPrice = view.findViewById(R.id.editinventory_price_field)
        itemQuantity = view.findViewById(R.id.editinventory_quantity_field)
        imageButton = view.findViewById(R.id.editinventory_imagebutton)
        imageButton.setOnClickListener(this)
        done = view.findViewById(R.id.editinventory_done)
        done.setOnClickListener(this)

        getItem()
    }

    private fun getItem(): Boolean{
        if(arguments != null){
            if(arguments!!.containsKey("item")){
                oldItem = arguments!!.getSerializable("item") as Item
                newItem = arguments!!.getSerializable("item") as Item
                setDataToViews(oldItem!!)
                isNew = false
                return true
            }
        }
        newItem = Item()
        isNew = true
        return false
    }

    private fun setDataToViews(item: Item){
        itemName.setText(item.itemName)
        itemPrice.setText(item.itemPrice)
        itemQuantity.setText(item.itemQuantity)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.editinventory_imagebutton -> {}
            R.id.editinventory_done -> {
                val name = itemName.text.toString()
                val price = itemPrice.text.toString()
                val quantity = itemQuantity.text.toString()
                if(name.isEmpty() || price.isEmpty() || quantity.isEmpty()){
                    Toast.makeText(context, "Please Enter all details", Toast.LENGTH_SHORT).show()
                    return
                }
                val map: HashMap<String, Any> = HashMap()
                map["itemname"] = name
                map["itemprice"] = price
                map["quantity"] = quantity
                if(isNew){
                    map["isAvailable"] = "true"
                    viewModel.createItem(map).addOnCompleteListener{
                        if(it.isSuccessful){
                            Toast.makeText(context, "Update Successful", Toast.LENGTH_SHORT).show()
                            communicator.onBackPressedFromEdit()
                        }
                    }
                }else{
                    if(oldItem!!.equalsTo(newItem))
                        return

                    viewModel.updateItem(oldItem!!.itemId!!, map).addOnCompleteListener{
                        if(it.isSuccessful){
                            Toast.makeText(context, "Update Successful", Toast.LENGTH_SHORT).show()
                            communicator.onBackPressedFromEdit()
                        }
                    }
                }
            }
        }
    }
}