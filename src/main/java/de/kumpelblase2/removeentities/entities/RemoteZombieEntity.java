package de.kumpelblase2.removeentities.entities;

import org.bukkit.inventory.Inventory;
import de.kumpelblase2.removeentities.features.InventoryFeature;
import de.kumpelblase2.removeentities.thinking.PathfinderGoalSelectorHelper;
import de.kumpelblase2.removeentities.thinking.goals.DesireLookAtNearest;
import de.kumpelblase2.removeentities.utilities.ReflectionUtil;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EntityZombie;
import net.minecraft.server.World;

public class RemoteZombieEntity extends EntityZombie implements RemoteEntityHandle
{
	protected RemoteEntity m_remoteEntity;
	protected PathfinderGoalSelectorHelper goalSelectorHelper;
	protected PathfinderGoalSelectorHelper targetSelectorHelper;
	
	static
	{
		ReflectionUtil.registerEntityType(RemoteZombieEntity.class, "Zombie");
	}
	
	public RemoteZombieEntity(World world)
	{
		this(world, null);
	}
	
	public RemoteZombieEntity(World world, RemoteEntity inEntity)
	{
		super(world);
		this.m_remoteEntity = inEntity;
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
		this.goalSelectorHelper.clearGoals();
		this.targetSelectorHelper.clearGoals();
		this.goalSelector.a(0, new DesireLookAtNearest(this.m_remoteEntity, EntityPlayer.class, 8));
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
}
