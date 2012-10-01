package de.kumpelblase2.removeentities.entities;

import net.minecraft.server.EntityLiving;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.removeentities.features.FeatureSet;
import de.kumpelblase2.removeentities.thinking.Mind;

public class RemoteBaseEntity implements RemoteEntity
{
	private final int m_id;
	protected Mind m_mind;
	protected FeatureSet m_features;
	protected boolean m_isStationary = false;
	protected RemoteEntityType m_type;
	
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
	public LivingEntity getBukkitEntity()
	{
		return null;
	}

	@Override
	public FeatureSet getFeatures()
	{
		return this.m_features;
	}

	@Override
	public void move(Location inLocation)
	{
	}

	@Override
	public void teleport(Location inLocation)
	{
	}

	@Override
	public void spawn(Location inLocation)
	{
	}

	@Override
	public void depsawn()
	{
	}

	@Override
	public void setMaxHealth(int inMax)
	{
	}

	@Override
	public int getMaxHealth()
	{
		return 0;
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
		return ((CraftLivingEntity)this.getBukkitEntity()).getHandle();
	}
}