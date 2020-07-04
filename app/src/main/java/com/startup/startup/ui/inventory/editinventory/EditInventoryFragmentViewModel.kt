package com.startup.startup.ui.inventory.editinventory

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.startup.startup.SessionManager
import com.startup.startup.network.Database
import javax.inject.Inject

class EditInventoryFragmentViewModel
    @Inject
    constructor(
        private val sessionManager: SessionManager,
        private val database: Database
    ): ViewModel() {

    fun updateItem(itemId: String, map: HashMap<String, Any>): Task<Void>{
        return database.editItemInShop(sessionManager.getUserId(),itemId, map)
    }

    fun createItem(map: HashMap<String, Any>): Task<Void> {
        return database.createItemInShop(sessionManager.getUserId(), map)
    }
}