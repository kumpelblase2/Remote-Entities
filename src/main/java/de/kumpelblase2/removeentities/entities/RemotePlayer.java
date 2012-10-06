package de.kumpelblase2.removeentities.entities;

import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.removeentities.api.*;

public class RemotePlayer extends RemoteBaseEntity implements RemoteEntity, Nameable, Fightable
{
	protected String m_name;
	
	public RemotePlayer(int inID, String inName)
	{
		super(inID, RemoteEntityType.Human);
		this.m_name = inName;
	}
	
	public RemotePlayer(int inID, String inName, RemotePlayerEntity inEntity)
	{
		this(inID, inName);
		this.m_entity = inEntity;
		this.m_speed = DefaultEntitySpeed.PLAYER_SPEED;
	}

	@Override
	public void attack(LivingEntity inTarget)
	{
	}

	@Override
	public void loseTarget()
	{
	}

	@Override
	public String getName()
	{
		return this.m_name;
	}

	@Override
	public void setName(String inName)
	{
		this.m_name = inName;
		Location loc = this.getBukkitEntity().getLocation();
		this.despawn();
		this.spawn(loc);
	}
	
	@Override
	public void spawn(Location inLocation)
	{
		if(this.isSpawned())
			return;
		
		WorldServer worldServer = ((CraftWorld)inLocation.getWorld()).getHandle();
		this.m_entity = new RemotePlayerEntity(worldServer.getMinecraftServer(), worldServer, this.getName(), new ItemInWorldManager(worldServer), this);
		worldServer.addEntity(m_entity);
		this.m_entity.getBukkitEntity().teleport(inLocation);
		this.m_entity.world.players.remove(this.m_entity);
	}

	@Override
	public void setMaxHealth(int inMax)
	{
		this.getHandle().setMaxHealth(inMax);
	}

	@Override
	public int getMaxHealth()
	{
		return this.m_entity.getMaxHealth();
	}

	@Override
	public float getSpeed()
	{
		return this.m_speed;
	}

	@Override
	public void setSpeed(float inSpeed)
	{
		this.m_speed = inSpeed;
	}
	
	@Override
	public RemotePlayerEntity getHandle()
	{
		return (RemotePlayerEntity)this.m_entity;
	}
}
