package com.tlannigan.tavern.extensions

import com.tlannigan.tavern.models.Serializer
import org.bukkit.inventory.PlayerInventory

fun PlayerInventory.serialize(): Array<String> {
    return Serializer().playerInventoryToBase64(this)
}