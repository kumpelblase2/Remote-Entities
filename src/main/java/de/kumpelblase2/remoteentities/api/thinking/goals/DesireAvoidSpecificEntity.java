package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_7_R1.*;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.*;
import de.kumpelblase2.remoteentities.api.thinking.selectors.EntitySelectorViewable;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

/**
 * Using this desire the entity will avoid a specific entity and will run away from it once it comes near.
 * Once the entity it should avoid is dead, this desire will be removed.
 */
public class DesireAvoidSpecificEntity extends DesireBase implements OneTimeDesire
{
	@SerializeAs(pos = 4)
	private final Entity m_entityToAvoid;
	@SerializeAs(pos = 1)
	protected float m_minDistance;
	@SerializeAs(pos = 3)
	protected float m_farSpeed;
	@SerializeAs(pos = 2)
	protected float m_closeSpeed;
	protected PathEntity m_path;
	protected EntitySelectorViewable m_selector;

	@Deprecated
	public DesireAvoidSpecificEntity(RemoteEntity inEntity, float inMinDistance, float inCloseSpeed, float inFarSpeed, org.bukkit.entity.Entity inToAvoid)
	{
		this(inEntity, inMinDistance, inCloseSpeed, inFarSpeed, ((CraftEntity)inToAvoid).getHandle());
	}

	@Deprecated
	public DesireAvoidSpecificEntity(RemoteEntity inEntity, float inMinDistance, float inCloseSpeed, float inFarSpeed, Entity inToAvoid)
	{
		this(inMinDistance, inCloseSpeed, inFarSpeed, inToAvoid);
		this.m_entity = inEntity;
	}

	public DesireAvoidSpecificEntity(float inMinDistance, float inCloseSpeed, float inFarSpeed, org.bukkit.entity.Entity inToAvoid)
	{
		this(inMinDistance, inCloseSpeed, inFarSpeed, ((CraftEntity)inToAvoid).getHandle());
	}

	public DesireAvoidSpecificEntity(float inMinDistance, float inCloseSpeed, float inFarSpeed, Entity inToAvoid)
	{
		super();
		this.m_entityToAvoid = inToAvoid;
		this.m_minDistance = inMinDistance;
		this.m_closeSpeed = inCloseSpeed;
		this.m_farSpeed = inFarSpeed;
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}

	@Override
	public void onAdd(RemoteEntity inEntity)
	{
		super.onAdd(inEntity);
		this.m_selector = new EntitySelectorViewable(this.getEntityHandle());
	}

	@Override
	public boolean shouldExecute()
	{
		if(!this.m_entityToAvoid.isAlive())
			return false;

		if(!NMSUtil.getEntitySenses(this.getEntityHandle()).canSee(this.m_entityToAvoid))
			return false;
		else
		{
			Vec3D var2 = de.kumpelblase2.remoteentities.nms.RandomPositionGenerator.b(this.getEntityHandle(), 16, 7, Vec3D.a.create(this.m_entityToAvoid.locX, this.m_entityToAvoid.locY, this.m_entityToAvoid.locZ));

			if (var2 == null)
				return false;
			else if (this.m_entityToAvoid.e(var2.c, var2.d, var2.e) < this.m_entityToAvoid.e(this.getEntityHandle()))
			{
				Vec3D.a.release(var2);
				return false;
			}
			else
			{
				this.m_path = this.getNavigation().a(var2.c, var2.d, var2.e);
				boolean returnValue = this.m_path != null && this.m_path.b(var2);
				Vec3D.a.release(var2);
				return returnValue;
			}
		}
	}

	@Override
	public void startExecuting()
	{
		this.movePath(this.m_path, this.m_farSpeed);
	}

	@Override
	public void stopExecuting()
	{
		this.getNavigation().h();
	}

	@Override
	public boolean canContinue()
	{
		return !this.getNavigation().g() && this.m_entityToAvoid.isAlive();
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}

	@Override
	public boolean update()
	{
		if(!this.m_entityToAvoid.isAlive())
			return false;

		if(this.getEntityHandle().e(this.m_entityToAvoid) > 49)
			this.getNavigation().a(this.m_farSpeed);
		else
			this.getNavigation().a(this.m_closeSpeed);

		return true;
	}

	@Override
	public boolean isFinished()
	{
		return !this.m_entityToAvoid.isAlive();
	}
}