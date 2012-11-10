package de.kumpelblase2.remoteentities.api.features;

import org.bukkit.inventory.ItemStack;

public class Equipment
{
	private ItemStack m_inHand;
	private ItemStack[] m_armor = new ItemStack[4];
	
	public Equipment(ItemStack inInHand, ItemStack... inArmor)
	{
		this.m_inHand = inInHand;
		System.arraycopy(inArmor, 0, this.m_armor, 0, (inArmor.length > 4 ? 4 : inArmor.length));
	}
	
	/**
	 * Sets the item that's hold in the hand
	 * 
	 * @param inNewItem item in hand
	 */
	public void setItemInHand(ItemStack inNewItem)
	{
		this.m_inHand = inNewItem;
	}
	
	/**
	 * Gets the item which is holded in the hand
	 * 
	 * @return item in hand
	 */
	public ItemStack getItemInHand()
	{
		return this.m_inHand;
	}
	
	/**
	 * Sets the helmet item
	 * 
	 * @param inHelmet helmet
	 */
	public void setHelmet(ItemStack inHelmet)
	{
		this.m_armor[0] = inHelmet;
	}
	
	/**
	 * Gets the helmet item
	 * 
	 * @return helmet
	 */
	public ItemStack getHelmet()
	{
		return this.m_armor[0];
	}
	
	/**
	 * Sets the chestplate
	 * 
	 * @param inChestplate chestplate
	 */
	public void setChestplate(ItemStack inChestplate)
	{
		this.m_armor[1] = inChestplate;
	}
	
	/**
	 * Gets the chestplate
	 * 
	 * @return chestplate
	 */
	public ItemStack getChestplate()
	{
		return this.m_armor[1];
	}
	
	/**
	 * Sets the leggings
	 * 
	 * @param inLeggins leggings
	 */
	public void setLeggings(ItemStack inLeggings)
	{
		this.m_armor[2] = inLeggings;
	}
	
	/**
	 * Gets the leggings
	 * 
	 * @return leggings
	 */
	public ItemStack getLeggings()
	{
		return this.m_armor[2];
	}
	
	/**
	 * Sets the boots
	 * 
	 * @param inBoots boots
	 */
	public void setBoots(ItemStack inBoots)
	{
		this.m_armor[3] = inBoots;
	}
	
	/**
	 * Gets the boots
	 * 
	 * @return boots
	 */
	public ItemStack getBoots()
	{
		return this.m_armor[3];
	}
	
	/**
	 * Gets all armor parts. Where boots have index 3 and helmet 0
	 * 
	 * @return armor
	 */
	public ItemStack[] getArmorParts()
	{
		return this.m_armor;
	}
	
	/**
	 * Sets the armor parts
	 * 
	 * @param inArmor armor
	 */
	public void setArmorParts(ItemStack[] inArmor)
	{
		this.m_armor = inArmor;
	}
}
