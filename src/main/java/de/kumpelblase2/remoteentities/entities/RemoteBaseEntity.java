package de.kumpelblase2.remoteentities.entities;

import java.lang.reflect.Field;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.MathHelper;
import net.minecraft.server.PathEntity;
import net.minecraft.server.World;
import net.minecraft.server.WorldServer;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.DefaultEntitySpeed;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import de.kumpelblase2.remoteentities.api.features.FeatureSet;
import de.kumpelblase2.remoteentities.api.thinking.Behaviour;
import de.kumpelblase2.remoteentities.api.thinking.Mind;

public abstract class RemoteBaseEntity implements RemoteEntity
{
	private final int m_id;
	protected Mind m_mind;
	protected FeatureSet m_features;
	protected boolean m_isStationary = false;
	protected RemoteEntityType m_type;
	protected EntityLiving m_entity;
	protected boolean m_isPushable = true;
	protected float m_speed;
	protected final EntityManager m_manager;
	
	public RemoteBaseEntity(int inID, RemoteEntityType inType, EntityManager inManager)
	{
		this.m_id = inID;
		this.m_mind = new Mind(this);
		this.m_features = new FeatureSet();
		this.m_type = inType;
		try
		{
			Field speed = DefaultEntitySpeed.class.getDeclaredField(this.m_type.name().toUpperCase() + "_SPEED");
			this.m_speed = speed.getFloat(null);
		}
		catch(Exception e)
		{
			this.m_speed = 0;
		}
		this.m_manager = inManager;
	}

	@Override
	public int getID()
	{
		return this.m_id;
	}

	@Override
	public EntityManager getManager()
	{
		return this.m_manager;
	}
	
	@Override
	public Mind getMind()
	{
		return this.m_mind;
	}

	@Override
	public FeatureSet getFeatures()
	{
		return this.m_features;
	}

	@Override
	public void setStationary(boolean inState)
	{
		this.m_isStationary = inState;
	}

	@Override
	public boolean isStationary()
	{
		return this.m_isStationary;
	}
	
	protected void clearPathfindingStrategies()
	{
	}
	
	protected void restorePathfindingStrategies()
	{
	}

	@Override
	public RemoteEntityType getType()
	{
		return this.m_type;
	}

	@Override
	public EntityLiving getHandle()
	{
		return this.m_entity;
	}
	
	@Override
	public LivingEntity getBukkitEntity()
	{
		if(this.m_entity != null)
			return (LivingEntity)this.m_entity.getBukkitEntity();
		
		return null;
	}
	
	@Override
	public boolean move(Location inLocation)
	{
		if(!this.m_entity.getNavigation().a(inLocation.getX(), inLocation.getY(), inLocation.getZ(), this.getSpeed()))
		{
			PathEntity path = this.m_entity.world.a(this.getHandle(), MathHelper.floor(inLocation.getX()), (int) inLocation.getY(), MathHelper.floor(inLocation.getZ()), 20, true, false, false, true);
			if(this.m_entity instanceof EntityCreature)
				((EntityCreature)this.m_entity).setPathEntity(path);
			return this.m_entity.getNavigation().a(path, this.getSpeed());
		}
		return true;
	}

	@Override
	public void teleport(Location inLocation)
	{
		this.getBukkitEntity().teleport(inLocation);
	}
	
	@Override
	public void spawn(Location inLocation)
	{
		if(this.isSpawned())
			return;
		
		try
		{
			WorldServer worldServer = ((CraftWorld)inLocation.getWorld()).getHandle();
			this.m_entity = (EntityLiving)this.m_type.getEntityClass().getConstructor(World.class, RemoteEntity.class).newInstance(worldServer, this);
			this.m_entity.setPositionRotation(inLocation.getX(), inLocation.getY(), inLocation.getZ(), inLocation.getYaw(), inLocation.getPitch());
			worldServer.addEntity(this.m_entity, SpawnReason.CUSTOM);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void despawn()
	{
		for(Behaviour behaviour : this.getMind().getBehaviours())
		{
			behaviour.onRemove();
		}
		this.getMind().clearBehaviours();
		if(this.getBukkitEntity() != null)
			this.getBukkitEntity().remove();
		this.m_entity = null;
	}
	
	@Override
	public boolean isSpawned()
	{
		return this.m_entity != null;
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
}