package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_4_6.EntityLiving;
import net.minecraft.server.v1_4_6.PlayerInteractManager;
import net.minecraft.server.v1_4_6.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_4_6.CraftWorld;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.events.RemoteEntitySpawnEvent;

public class RemotePlayer extends RemoteBaseEntity implements RemoteEntity, Nameable, Fightable
{
	protected String m_name;
	
	public RemotePlayer(int inID, String inName, EntityManager inManager)
	{
		this(inID, inName, null, inManager);
	}
	
	public RemotePlayer(int inID, String inName, RemotePlayerEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Human, inManager);
		this.m_name = inName;
		this.m_entity = inEntity;
	}

	@Override
	public void attack(LivingEntity inTarget)
	{
		if(this.m_entity == null)
			return;
		
		this.m_entity.b(((CraftLivingEntity)inTarget).getHandle());
	}

	@Override
	public void loseTarget()
	{
		if(this.m_entity == null)
			return;
		
		this.m_entity.b((EntityLiving)null);
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
		this.despawn(DespawnReason.NAME_CHANGE);
		this.spawn(loc);
	}
	
	@Override
	public void spawn(Location inLocation)
	{
		if(this.isSpawned())
			return;
		
		RemoteEntitySpawnEvent event = new RemoteEntitySpawnEvent(this, inLocation);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		
		inLocation = event.getSpawnLocation();
		
		WorldServer worldServer = ((CraftWorld)inLocation.getWorld()).getHandle();
		this.m_entity = new RemotePlayerEntity(worldServer.getMinecraftServer(), worldServer, this.getName(), new PlayerInteractManager(worldServer), this);
		worldServer.addEntity(m_entity);
		this.m_entity.getBukkitEntity().teleport(inLocation);
		this.m_entity.world.players.remove(this.m_entity);
	}

	@Override
	public void setMaxHealth(int inMax)
	{
		if(this.m_entity == null)
			return;
		
		((RemoteEntityHandle)this.getHandle()).setMaxHealth(inMax);
	}

	@Override
	public LivingEntity getTarget()
	{
		if(this.m_entity == null)
			return null;
		
		EntityLiving target = this.m_entity.aG();
		if(target != null)
			return (LivingEntity)target.getBukkitEntity();
		
		return null;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Player";
	}
}
