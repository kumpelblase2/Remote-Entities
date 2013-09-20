package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_6_R3.Vec3D;
import org.bukkit.Location;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.nms.RandomPositionGenerator;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

/**
 * Using this desire the entity will move around in panic when it gets damaged.
 */
public class DesirePanic extends DesireBase
{
	protected double m_x;
	protected double m_y;
	protected double m_z;
	@SerializeAs(pos = 1)
	protected double m_speed;

	@Deprecated
	public DesirePanic(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}

	public DesirePanic()
	{
		this(-1);
	}

	public DesirePanic(double inSpeed)
	{
		super();
		this.m_speed = inSpeed;
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null || (this.getEntityHandle().getLastDamager() == null && !this.getEntityHandle().isBurning()))
			return false;
		else
		{
			Vec3D vec = RandomPositionGenerator.a(this.getEntityHandle(), 5, 4);
			if(vec == null)
				return false;
			else
			{
				this.m_x = vec.c;
				this.m_y = vec.d;
				this.m_z = vec.e;
				Vec3D.a.release(vec);
				return true;
			}
		}
	}

	@Override
	public void startExecuting()
	{
		this.getRemoteEntity().move(new Location(this.getRemoteEntity().getBukkitEntity().getWorld(), this.m_x, this.m_y, this.m_z), (this.m_speed == -1 ? this.getRemoteEntity().getSpeed() : this.m_speed));
	}

	@Override
	public boolean canContinue()
	{
		return !this.getNavigation().g();
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}