package com.tlannigan.tavern.models

import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException

class Serializer {

    @Throws(IllegalStateException::class)
    fun playerInventoryToBase64(inventory: PlayerInventory): List<String> {
        val content = toBase64(inventory)
        val armor = itemStackArrayToBase64(inventory.armorContents)
        return listOf(content, armor)
    }

    @Throws(IllegalStateException::class)
    fun itemStackArrayToBase64(items: Array<ItemStack?>): String {
        return try {
            val outputStream = ByteArrayOutputStream()
            val dataOutput = BukkitObjectOutputStream(outputStream)

            dataOutput.writeInt(items.size)

            for (i in items.indices) {
                dataOutput.writeObject(items[i])
            }

            dataOutput.close()
            Base64Coder.encodeLines(outputStream.toByteArray())
        } catch (e: Exception) {
            throw IllegalStateException("Unable to save item stacks.", e)
        }
    }

    @Throws(IllegalStateException::class)
    fun toBase64(inventory: Inventory): String {
        return try {
            val outputStream = ByteArrayOutputStream()
            val dataOutput = BukkitObjectOutputStream(outputStream)

            dataOutput.writeInt(inventory.size)

            for (i in 0 until inventory.size) {
                dataOutput.writeObject(inventory.getItem(i))
            }

            dataOutput.close()
            Base64Coder.encodeLines(outputStream.toByteArray())
        } catch (e: Exception) {
            throw IllegalStateException("Unable to save item stacks.", e)
        }
    }

    @Throws(IOException::class)
    fun fromBase64(string: String?): Inventory {
        return try {
            val inputStream = ByteArrayInputStream(Base64Coder.decodeLines(string))
            val dataInput = BukkitObjectInputStream(inputStream)
            val inventory = Bukkit.getServer().createInventory(null, dataInput.readInt())

            for (i in 0 until inventory.size) {
                inventory.setItem(i, dataInput.readObject() as ItemStack)
            }

            dataInput.close()
            inventory
        } catch (e: ClassNotFoundException) {
            throw IOException("Unable to decode class type.", e)
        }
    }

    @Throws(IOException::class)
    fun itemStackArrayFromBase64(string: String?): Array<ItemStack?> {
        return try {
            val inputStream = ByteArrayInputStream(Base64Coder.decodeLines(string))
            val dataInput = BukkitObjectInputStream(inputStream)
            val items = arrayOfNulls<ItemStack>(dataInput.readInt())

            for (i in items.indices) {
                items[i] = dataInput.readObject() as? ItemStack
            }

            dataInput.close()
            items
        } catch (e: ClassNotFoundException) {
            throw IOException("Unable to decode class type.", e)
        }
    }

    companion object {
        val emptyInventoryBase64 = listOf(
            "rO0ABXcEAAAAKXBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBw",
            "rO0ABXcEAAAABHBwcHA="
        )
    }

}