package de.kumpelblase2.remoteentities.entities;

import org.bukkit.inventory.Inventory;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityWitch;
import net.minecraft.server.World;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityHandle;
import de.kumpelblase2.remoteentities.api.RemoteProjectileType;
import de.kumpelblase2.remoteentities.api.features.InventoryFeature;
import de.kumpelblase2.remoteentities.api.thinking.Mind;
import de.kumpelblase2.remoteentities.api.thinking.PathfinderGoalSelectorHelper;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireRangedAttack;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireAttackNearest;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireAttackTarget;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireLookAtNearest;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireLookRandomly;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireSwell;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireWanderAround;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public class RemoteWitchEntity extends EntityWitch implements RemoteEntityHandle
{
	private RemoteEntity m_remoteEntity;
	protected final PathfinderGoalSelectorHelper goalSelectorHelper;
	protected final PathfinderGoalSelectorHelper targetSelectorHelper;
	protected int m_maxHealth;
	public static int defaultMaxHealth = 23;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;
	
	static
	{
		ReflectionUtil.registerEntityType(RemoteWitchEntity.class, "Witch", 66);
	}
	
	public RemoteWitchEntity(World world)
	{
		this(world, null);
	}
	
	public RemoteWitchEntity(World inWorld, RemoteEntity inEntity)
	{
		super(inWorld);
		this.m_remoteEntity = inEntity;
		this.goalSelectorHelper = new PathfinderGoalSelectorHelper(this.goalSelector);
		this.targetSelectorHelper = new PathfinderGoalSelectorHelper(this.targetSelector);
		this.m_maxHealth = defaultMaxHealth;
		this.goalSelectorHelper.clearGoals();
		this.targetSelectorHelper.clearGoals();
	}

	@Override
	public Inventory getInventory()
	{
		if(!this.m_remoteEntity.getFeatures().hasFeature(InventoryFeature.class))
			return null;
		
		return ((InventoryFeature)this.m_remoteEntity.getFeatures().getFeature(InventoryFeature.class)).getInventory();
	}

	@Override
	public RemoteEntity getRemoteEntity()
	{
		return this.m_remoteEntity;
	}

	@Override
	public void setupStandardGoals()
	{
		try
		{
			Mind mind = this.m_remoteEntity.getMind();
			mind.addMovementDesire(new DesireSwell(this.m_remoteEntity), 1);
			mind.addMovementDesire(new DesireRangedAttack(this.m_remoteEntity, RemoteProjectileType.POTION), 2);
			mind.addMovementDesire(new DesireWanderAround(this.m_remoteEntity), 3);
			mind.addMovementDesire(new DesireLookAtNearest(this.m_remoteEntity, EntityHuman.class, 8F), 4);
			mind.addMovementDesire(new DesireLookRandomly(this.m_remoteEntity), 5);
			mind.addActionDesire(new DesireAttackTarget(this.m_remoteEntity, 16, true, false), 1);
			mind.addActionDesire(new DesireAttackNearest(this.m_remoteEntity, EntityHuman.class, 16, true, 0), 2);
		}
		catch(Exception e)
		{
		}
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
}