package de.kumpelblase2.removeentities.entities;

import org.bukkit.inventory.Inventory;
import net.minecraft.server.EntityCreeper;
import net.minecraft.server.World;
import de.kumpelblase2.removeentities.api.RemoteEntity;
import de.kumpelblase2.removeentities.api.RemoteEntityHandle;
import de.kumpelblase2.removeentities.api.features.InventoryFeature;
import de.kumpelblase2.removeentities.api.thinking.PathfinderGoalSelectorHelper;

public class RemoteCreeperEntity extends EntityCreeper implements RemoteEntityHandle
{
	private RemoteEntity m_remoteEntity;
	protected final PathfinderGoalSelectorHelper goalSelectorHelper;
	protected final PathfinderGoalSelectorHelper targetSelectorHelper;
	protected float m_speed;
	protected int m_maxHealth = 20;
	
	public RemoteCreeperEntity(World world, RemoteEntity inRemoteEntity)
	{
		super(world);
		this.m_remoteEntity = inRemoteEntity;
		this.goalSelectorHelper = new PathfinderGoalSelectorHelper(this.goalSelector);
		this.targetSelectorHelper = new PathfinderGoalSelectorHelper(this.targetSelector);
	}

	@Override
	public Inventory getInventory()
	{
		if(!this.m_remoteEntity.getFeatures().hasFeature("Inventory"))
			return null;
		
		return ((InventoryFeature)this.m_remoteEntity.getFeatures().getFeature("Inventory")).getInventory();
	}

	@Override
	public RemoteEntity getRemoteEntity()
	{
		return this.m_remoteEntity;
	}

	@Override
	public void setupStandardGoals()
	{
	}

	@Override
	public PathfinderGoalSelectorHelper getGoalSelector()
	{
		return this.goalSelectorHelper;
	}

	@Override
	public PathfinderGoalSelectorHelper getTargetSelector()
	{
		return this.targetSelectorHelper;
	}

	@Override
	public void setMaxHealth(int inHealth)
	{
		this.m_maxHealth = inHealth;
	}
	
	@Override
	public int getMaxHealth()
	{
		return this.m_maxHealth;
	}
	
	public float getSpeed()
	{
		return this.m_speed;
	}
	
	public void setSpeed(float inSpeed)
	{
		this.m_speed = inSpeed;
	}
}
