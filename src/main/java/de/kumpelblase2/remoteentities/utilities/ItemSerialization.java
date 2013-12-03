package de.kumpelblase2.remoteentities.utilities;

import java.io.*;
import java.math.BigInteger;
import net.minecraft.server.v1_7_R1.*;
import org.bukkit.craftbukkit.v1_7_R1.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_7_R1.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Made by Comprehenix.
 * Code can be found at: https://gist.github.com/aadnk/4102407
 * Slightly modified to work with newest bukkit version.
 */
public class ItemSerialization {

	/**
	 * Serializes an inventory to a encoded string.
	 *
	 * @param inventory The inventory to serialize
	 * @return          serialized string
	 */
	public static String toString(Inventory inventory) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutput = new DataOutputStream(outputStream);
		NBTTagList itemList = new NBTTagList();

		// Save every element in the list
		for (int i = 0; i < inventory.getSize(); i++) {
			NBTTagCompound outputObject = new NBTTagCompound();
			CraftItemStack craft = getCraftVersion(inventory.getItem(i));

			// Convert the item stack to a NBT compound
			if (craft != null)
				CraftItemStack.asNMSCopy(craft).save(outputObject);
			itemList.add(outputObject);
		}

		// Now save the list
		NBTCompressedStreamTools.a(itemList, dataOutput);

		// Serialize that array
		return new BigInteger(1, outputStream.toByteArray()).toString(32);
	}

	/**
	 * Deserializes an inventory from a string
	 *
	 * @param data  String to deserialize
	 * @return      Deserialized inventory
	 */
	public static Inventory fromString(String data) {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new BigInteger(data, 32).toByteArray());
		NBTTagList itemList = (NBTTagList) NBTCompressedStreamTools.a(new DataInputStream(inputStream));
		Inventory inventory = new CraftInventoryCustom(null, itemList.size());

		for (int i = 0; i < itemList.size(); i++) {
			NBTTagCompound inputObject = itemList.get(i);

			// IsEmpty
			if (!inputObject.isEmpty()) {
				inventory.setItem(i, CraftItemStack.asBukkitCopy(net.minecraft.server.v1_7_R1.ItemStack.createStack(inputObject)));
			}
		}

		// Serialize that array
		return inventory;
	}

	private static CraftItemStack getCraftVersion(ItemStack stack) {
		if (stack instanceof CraftItemStack)
			return (CraftItemStack) stack;
		else if (stack != null)
			return CraftItemStack.asCraftCopy(stack);
		else
			return null;
	}
}
