package de.kumpelblase2.removeentities.entities;

import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.removeentities.thinking.Behaviour;

public class RemotePlayer extends RemoteBaseEntity implements RemoteEntity, Nameable, Fightable
{
	protected RemotePlayerEntity m_entity;
	protected String m_name;
	protected boolean m_isPushable = false;
	
	public RemotePlayer(int inID, String inName)
	{
		super(inID, RemoteEntityType.Human);
		this.m_name = inName;
	}
	
	public RemotePlayer(int inID, String inName, RemotePlayerEntity inEntity)
	{
		this(inID, inName);
		this.m_entity = inEntity;
	}
	
	public void setEntity(RemotePlayerEntity inEntity)
	{
		this.m_entity = inEntity;
	}
	
	@Override
	public LivingEntity getBukkitEntity()
	{
		return (LivingEntity)this.m_entity.getBukkitEntity();
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
	public boolean isSpawned()
	{
		return this.m_entity != null;
	}
	
	@Override
	public void despawn()
	{
		for(Behaviour behaviour : this.getMind().getBehaviours())
		{
			behaviour.onRemove();
		}
		this.getMind().clearBehaviours();
		this.getBukkitEntity().remove();
		this.m_entity = null;
	}

	@Override
	public void move(Location inLocation)
	{
		
	}

	@Override
	public void teleport(Location inLocation)
	{
		this.m_entity.getBukkitEntity().teleport(inLocation);
	}

	@Override
	public void setMaxHealth(int inMax)
	{
		this.m_entity.setMaxHealth(inMax);
	}

	@Override
	public int getMaxHealth()
	{
		return this.m_entity.getMaxHealth();
	}

	@Override
	public float getSpeed()
	{
		return this.m_entity.getSpeed();
	}

	@Override
	public void setSpeed(float inSpeed)
	{
		this.m_entity.setSpeed(inSpeed);
	}

	@Override
	public boolean isPushable()
	{
		return this.m_isPushable;
	}

	@Override
	public void setPushable(boolean inState)
	{
		this.m_isPushable = inState;
	}
}
