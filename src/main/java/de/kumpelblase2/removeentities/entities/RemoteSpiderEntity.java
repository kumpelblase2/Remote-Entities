package de.kumpelblase2.removeentities.entities;

import org.bukkit.inventory.Inventory;
import net.minecraft.server.EntitySpider;
import net.minecraft.server.World;
import de.kumpelblase2.removeentities.api.RemoteEntity;
import de.kumpelblase2.removeentities.api.RemoteEntityHandle;
import de.kumpelblase2.removeentities.api.features.InventoryFeature;
import de.kumpelblase2.removeentities.api.thinking.PathfinderGoalSelectorHelper;
import de.kumpelblase2.removeentities.utilities.ReflectionUtil;

public class RemoteSpiderEntity extends EntitySpider implements RemoteEntityHandle
{
	private RemoteEntity m_remoteEntity;
	protected final PathfinderGoalSelectorHelper goalSelectorHelper;
	protected final PathfinderGoalSelectorHelper targetSelectorHelper;
	protected int m_maxHealth;
	public static int defaultMaxHealth = 16;
	
	static
	{
		ReflectionUtil.registerEntityType(RemoteSpiderEntity.class, "Spider", 52);
	}
	
	public RemoteSpiderEntity(World world)
	{
		super(world);
		this.goalSelectorHelper = new PathfinderGoalSelectorHelper(this.goalSelector);
		this.targetSelectorHelper = new PathfinderGoalSelectorHelper(this.targetSelector);
		this.m_maxHealth = defaultMaxHealth;
	}
	
	public RemoteSpiderEntity(World world, RemoteEntity inEntity)
	{
		this(world);
		this.m_remoteEntity = inEntity;
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
		if(this.m_maxHealth == 0)
			return defaultMaxHealth;
		return this.m_maxHealth;
	}
	
	@Override
	public boolean aV()
	{
		return true;
	}
	
	@Override
	public void h_()
	{
		super.h_();
		this.getRemoteEntity().getMind().tick();
	}
}
