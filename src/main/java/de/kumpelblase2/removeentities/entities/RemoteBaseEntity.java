package de.kumpelblase2.removeentities.entities;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.MathHelper;
import net.minecraft.server.PathEntity;
import de.kumpelblase2.removeentities.api.RemoteEntity;
import de.kumpelblase2.removeentities.api.RemoteEntityType;
import de.kumpelblase2.removeentities.api.features.FeatureSet;
import de.kumpelblase2.removeentities.api.thinking.Behaviour;
import de.kumpelblase2.removeentities.api.thinking.Mind;

public abstract class RemoteBaseEntity implements RemoteEntity
{
	private final int m_id;
	protected Mind m_mind;
	protected FeatureSet m_features;
	protected boolean m_isStationary = false;
	protected RemoteEntityType m_type;
	protected EntityLiving m_entity;
	protected boolean m_isPushable = true;
	
	public RemoteBaseEntity(int inID, RemoteEntityType inType)
	{
		this.m_id = inID;
		this.m_mind = new Mind(this);
		this.m_features = new FeatureSet();
		this.m_type = inType;
	}
	
	@Override
	public int getID()
	{
		return this.m_id;
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
		PathEntity path = this.m_entity.world.a(this.getHandle(), MathHelper.floor(inLocation.getX()), (int) inLocation.getY(), MathHelper.floor(inLocation.getZ()), 20, true, false, false, true);
		return this.m_entity.getNavigation().a(path, this.getSpeed());
	}

	@Override
	public void teleport(Location inLocation)
	{
		this.getBukkitEntity().teleport(inLocation);
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
}