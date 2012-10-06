package de.kumpelblase2.removeentities.api.features;

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
	
	public void setItemInHand(ItemStack inNewItem)
	{
		this.m_inHand = inNewItem;
	}
	
	public ItemStack getItemInHand()
	{
		return this.m_inHand;
	}
	
	public void setHelmet(ItemStack inHelmet)
	{
		this.m_armor[0] = inHelmet;
	}
	
	public ItemStack getHelmet()
	{
		return this.m_armor[0];
	}
	
	public void setChestplate(ItemStack inChestplate)
	{
		this.m_armor[1] = inChestplate;
	}
	
	public ItemStack getChestplate()
	{
		return this.m_armor[1];
	}
	
	public void setLeggins(ItemStack inLeggins)
	{
		this.m_armor[2] = inLeggins;
	}
	
	public ItemStack getLeggins()
	{
		return this.m_armor[2];
	}
	
	public void setBoots(ItemStack inBoots)
	{
		this.m_armor[3] = inBoots;
	}
	
	public ItemStack getBoots()
	{
		return this.m_armor[3];
	}
}
