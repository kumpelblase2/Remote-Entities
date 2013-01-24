package de.kumpelblase2.remoteentities.persistence;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationData
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
	
	public Location toBukkitLocation()
	{
		return new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z, this.yaw, this.pitch);
	}
}
