package de.kumpelblase2.remoteentities.api.features;

import org.bukkit.craftbukkit.v1_6_R2.inventory.CraftInventoryCustom;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.persistence.*;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

@IgnoreSerialization
public class RemoteInventoryFeature extends RemoteFeature implements InventoryFeature
{
	@SerializeAs(pos = 1)
	private CraftInventoryCustom m_inventory;
	protected int m_size;

	public RemoteInventoryFeature()
	{
		this(null);
	}

	public RemoteInventoryFeature(RemoteEntity inEntity)
	{
		this(inEntity, 36);
	}

	public RemoteInventoryFeature(RemoteEntity inEntity, int inSize)
	{
		this(inEntity, null);
		this.m_size = inSize;
	}

	public RemoteInventoryFeature(RemoteEntity inEntity, CraftInventoryCustom inInventory)
	{
		super("INVENTORY", inEntity);
		this.m_inventory = inInventory;
	}

	@Override
	public Inventory getInventory()
	{
		return this.m_inventory;
	}

	public void onAdd(RemoteEntity inEntity)
	{
		super.onAdd(inEntity);
		this.m_inventory = new CraftInventoryCustom((InventoryHolder)this.m_entity.getHandle(), this.m_size);
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}