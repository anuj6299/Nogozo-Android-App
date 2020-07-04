package com.startup.startup.ui.inventory

import com.startup.startup.datamodels.Item

interface InventoryCommunicator {
    fun onItemClick(item: Item)

    fun onNewItem()

    fun onBackPressedFromEdit()
}