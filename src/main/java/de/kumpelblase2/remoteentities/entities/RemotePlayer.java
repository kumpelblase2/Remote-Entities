package de.kumpelblase2.remoteentities.entities;

import de.kumpelblase2.remoteentities.*;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.events.*;
import net.minecraft.server.v1_5_R3.*;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_5_R3.*;
import org.bukkit.craftbukkit.v1_5_R3.entity.*;
import org.bukkit.entity.*;
import org.bukkit.metadata.*;

public class RemotePlayer extends RemoteBaseEntity implements Nameable, Fightable
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
		
		this.m_entity.setGoalTarget(((CraftLivingEntity)inTarget).getHandle());
	}

	@Override
	public void loseTarget()
	{
		if(this.m_entity == null)
			return;
		
		this.m_entity.setGoalTarget(null);
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
		this.getBukkitEntity().setMetadata("remoteentity", new FixedMetadataValue(this.m_manager.getPlugin(), this));
	}

	@Override
	public LivingEntity getTarget()
	{
		if(this.m_entity == null)
			return null;
		
		EntityLiving target = this.m_entity.getGoalTarget();
		if(target != null)
			return (LivingEntity)target.getBukkitEntity();
		
		return null;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Player";
	}

	/**
	 * Tries to place the npc in a bed at given location.
	 * 
	 * @param inLocation	Location the bed is present.
	 * @return				True if it was possible, false if not
	 */
	public boolean enterBed(Location inLocation)
	{
		this.teleport(inLocation);
		return ((EntityHuman)this.getHandle()).a((int)inLocation.getX(), (int)inLocation.getY(), (int)inLocation.getZ()) == EnumBedResult.OK;
	}

	/**
	 * Leaves the bed the npc currently is in. 
	 */
	public void leaveBed()
	{
		((EntityHuman)this.getHandle()).a(true, true, false);
	}

	/**
	 * Checks if the npc is currently in a bed.
	 * 
	 * @return	true if he is, false if not
	 */
	public boolean isSleeping()
	{
		return this.getHandle().isSleeping();
	}
	
	/**
	 * Send the arm swing animation to nearby players. 
	 */
	public void doArmSwing()
	{
		((WorldServer)this.getHandle().world).getTracker().a(this.getHandle(), new Packet18ArmAnimation(this.getHandle(), 1));
	}
}
