package de.kumpelblase2.remoteentities.persistence;

import net.minecraft.server.v1_4_R1.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class ObjectParser
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object deserialize(ParameterData inData)
	{
		Class typeClass = this.getClass(inData.type);
		if(typeClass.isAssignableFrom(Class.class))
			return this.getClass(inData.value.toString());
		else if(typeClass.isAssignableFrom(int.class) || typeClass.isAssignableFrom(Integer.class))
			return this.getInt(inData.value);
		else if(typeClass.isAssignableFrom(boolean.class) || typeClass.isAssignableFrom(Boolean.class))
			return this.getBoolean(inData.value);
		else if(typeClass.isAssignableFrom(Enum.class))
			return Enum.valueOf(typeClass, inData.value.toString());
		else if(typeClass.isAssignableFrom(float.class) || typeClass.isAssignableFrom(Float.class))
			return this.getFloat(inData.value);
		else if(typeClass.isAssignableFrom(double.class) || typeClass.isAssignableFrom(Double.class))
			return this.getDouble(inData.value);
		else if(typeClass.isAssignableFrom(EntityLiving.class))
			return this.getNMSEntity(inData.value);
		else if(typeClass.isAssignableFrom(LivingEntity.class))
			return this.getEntity(inData.value);
		else
			return inData.value.toString();
	}

	@SuppressWarnings("rawtypes")
	public Object serialize(Object inObject)
	{
		if(inObject instanceof Location)
			return new LocationData((Location)inObject).serialize();
		else if(inObject instanceof EntityLiving)
			return ((EntityLiving)inObject).getBukkitEntity().getUniqueId().toString();
		else if(inObject instanceof LivingEntity)
			return ((LivingEntity)inObject).getUniqueId().toString();
		else if(inObject instanceof Class)
			return ((Class)inObject).getName();
		else
			return inObject.toString();
	}
	
	@SuppressWarnings("rawtypes")
	protected Class getClass(String inName)
	{
		try
		{
			return Class.forName(inName);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	protected Integer getInt(Object inValue)
	{
		try
		{
			return Integer.parseInt(inValue.toString());
		}
		catch(Exception e)
		{
			return 0;
		}
	}
	
	protected Boolean getBoolean(Object inValue)
	{
		try
		{
			return Boolean.parseBoolean(inValue.toString());
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	protected Float getFloat(Object inValue)
	{
		try
		{
			return Float.parseFloat(inValue.toString());
		}
		catch(Exception e)
		{
			return 0f;
		}
	}
	
	protected Double getDouble(Object inValue)
	{
		try
		{
			return Double.parseDouble(inValue.toString());
		}
		catch(Exception e)
		{
			return 0d;
		}
	}
	
	protected EntityLiving getNMSEntity(Object inValue)
	{
		Entity e = this.getEntity(inValue);
		if(e == null)
			return null;
		
		return ((CraftLivingEntity)e).getHandle();
	}
	
	protected Entity getEntity(Object inValue)
	{
		for(World w : Bukkit.getWorlds())
		{
			for(Entity e : w.getEntities())
			{
				if(e.getUniqueId().toString().equals(inValue.toString()))
					return e;
			}
		}
		return null;
	}
}
