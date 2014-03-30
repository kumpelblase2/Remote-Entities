package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_7_R2.EntityTameableAnimal;
import net.minecraft.server.v1_7_R2.Vec3D;
import org.bukkit.Location;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.nms.RandomPositionGenerator;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;

/**
 * Using this desire the entity will move around randomly.
 */
public class DesireWanderAround extends DesireBase
{
	protected double m_xPos;
	protected double m_yPos;
	protected double m_zPos;
	@SerializeAs(pos = 0)
	protected float m_speed = -1;

	@Deprecated
	public DesireWanderAround(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}

	public DesireWanderAround()
	{
		super();
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}

	public DesireWanderAround(float inSpeed)
	{
		this();
		this.m_speed = inSpeed;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;

		if(this.getEntityHandle().aM() >= 100)
			return false;
		else if(this.getEntityHandle().aH().nextInt(120) != 0)
			return false;
		else if(this.getEntityHandle() instanceof EntityTameableAnimal && ((EntityTameableAnimal)this.getEntityHandle()).isSitting())
			return false;
		else
		{
			Vec3D vector = RandomPositionGenerator.a(this.getEntityHandle(), 10, 7);
			if(vector == null)
				return false;
			else
			{
				this.m_xPos = vector.a;
				this.m_yPos = vector.b;
				this.m_zPos = vector.c;
				return true;
			}
		}
	}

	@Override
	public boolean canContinue()
	{
		return !this.getNavigation().g();
	}

	@Override
	public void startExecuting()
	{
		this.getRemoteEntity().move(new Location(this.getRemoteEntity().getBukkitEntity().getWorld(), this.m_xPos, this.m_yPos, this.m_zPos), (this.m_speed == -1 ? this.getRemoteEntity().getSpeed() : this.m_speed));
	}
}