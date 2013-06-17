package de.kumpelblase2.remoteentities.persistence;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class LocationData implements ConfigurationSerializable
{
	public String world;
	public int x;
	public int y;
	public int z;
	public float yaw;
	public float pitch;

	public LocationData()
	{
	}

	public LocationData(Location inLocation)
	{
		this.world = inLocation.getWorld().getName();
		this.x = inLocation.getBlockX();
		this.y = inLocation.getBlockY();
		this.z = inLocation.getBlockZ();
		this.yaw = inLocation.getYaw();
		this.pitch = inLocation.getPitch();
	}

	public LocationData(Map<String, Object> inData)
	{
		this.world = (String)inData.get("world");
		this.x = (Integer)inData.get("x");
		this.y = (Integer)inData.get("y");
		this.z = (Integer)inData.get("z");
		this.yaw = ((Double)inData.get("yaw")).floatValue();
		this.pitch = ((Double)inData.get("pitch")).floatValue();
	}

	public Location toBukkitLocation()
	{
		if(Bukkit.getWorld(world) != null)
			return new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z, this.yaw, this.pitch);

		return null;
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("world", this.world);
		data.put("x", this.x);
		data.put("y", this.y);
		data.put("z", this.z);
		data.put("yaw", this.yaw);
		data.put("pitch", this.pitch);
		return data;
	}
}
