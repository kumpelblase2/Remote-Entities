package de.kumpelblase2.remoteentities.persistence;

import net.minecraft.server.v1_5_R3.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

/**
 * This class is used to serialize and deserialize any kind of object
 */
public class ObjectParser
{
	@SuppressWarnings("rawtypes")
	public Object deserialize(ParameterData inData)
	{
		Class typeClass = this.getClass(inData.type);
		if(typeClass.isArray())
		{
			String valueString = inData.value.toString();
			valueString = valueString.substring(1, valueString.length() - 1);
			String[] values = valueString.split(",");
			Object[] data = new Object[values.length];
			for(int i = 0; i < values.length; i++)
			{
				data[i] = this.getDeserializedObject(typeClass, values[i]);
			}
			return data;
		}
		else
		{
			return this.getDeserializedObject(typeClass, inData.value);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object getDeserializedObject(Class inType, Object inObject)
	{		
		if(inType.isAssignableFrom(Class.class))
			return this.getClass(inObject.toString());
		else if(inType.isAssignableFrom(int.class) || inType.isAssignableFrom(Integer.class))
			return this.getInt(inObject);
		else if(inType.isAssignableFrom(boolean.class) || inType.isAssignableFrom(Boolean.class))
			return this.getBoolean(inObject);
		else if(inType.isAssignableFrom(Enum.class))
			return Enum.valueOf(inType, inObject.toString());
		else if(inType.isAssignableFrom(float.class) || inType.isAssignableFrom(Float.class))
			return this.getFloat(inObject);
		else if(inType.isAssignableFrom(double.class) || inType.isAssignableFrom(Double.class))
			return this.getDouble(inObject);
		else if(inType.isAssignableFrom(EntityLiving.class))
			return this.getNMSEntity(inObject);
		else if(inType.isAssignableFrom(LivingEntity.class))
			return this.getEntity(inObject);
		else if(inType.isAssignableFrom(IEntitySerializer.class))
		{
			try
			{
				return inType.newInstance();
			}
			catch(Exception e)
			{
				return null;
			}
		}
		else
			return inObject.toString();
	}

	public Object serialize(Object inObject)
	{
		if(inObject.getClass().isArray())
		{
			Object[] data = (Object[])inObject;			
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			for(Object aData : data)
			{
				sb.append(this.getSerializedObject(aData).toString());
				sb.append(",");
			}
			sb.setCharAt(sb.length(), ']');
			return sb.toString();
		}
		else
		{
			return this.getSerializedObject(inObject);
		}
	}
	
	@SuppressWarnings("rawtypes")
	protected Object getSerializedObject(Object inObject)
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
	
	protected boolean getBoolean(Object inValue)
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
	
	protected float getFloat(Object inValue)
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
	
	protected double getDouble(Object inValue)
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
