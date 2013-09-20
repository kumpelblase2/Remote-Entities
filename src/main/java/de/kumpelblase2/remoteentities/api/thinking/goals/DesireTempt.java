package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_6_R3.EntityHuman;
import net.minecraft.server.v1_6_R3.ItemStack;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

/**
 * Using this desire the entity will be tempted by a given item in a players hand.
 * The entity might also run away when the player moves.
 */
public class DesireTempt extends DesireBase
{
	@SerializeAs(pos = 1)
	protected int m_itemID;
	@SerializeAs(pos = 2)
	protected boolean m_scaredByMovement;
	protected double m_x;
	protected double m_y;
	protected double m_z;
	protected double m_pitch;
	protected double m_yaw;
	protected EntityHuman m_nearPlayer;
	protected int m_delayTicks;
	protected boolean m_isTempted;
	protected boolean m_avoidWaterState;
	@SerializeAs(pos = 3)
	protected double m_speed;

	@Deprecated
	public DesireTempt(RemoteEntity inEntity, int inItemID, boolean inScaredByMovement)
	{
		super(inEntity);
		this.m_itemID = inItemID;
		this.m_scaredByMovement = inScaredByMovement;
		this.m_type = DesireType.FULL_CONCENTRATION;
	}

	public DesireTempt(int inItemID, boolean inScaredByMovement)
	{
		this(inItemID, inScaredByMovement, -1);
	}

	public DesireTempt(int inItemID, boolean inScaredByMovement, double inSpeed)
	{
		super();
		this.m_itemID = inItemID;
		this.m_scaredByMovement = inScaredByMovement;
		this.m_type = DesireType.FULL_CONCENTRATION;
		this.m_speed = inSpeed;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.m_delayTicks > 0)
		{
			this.m_delayTicks--;
			return false;
		}
		else
		{
			if(this.getEntityHandle() == null)
				return false;

			this.m_nearPlayer = this.getEntityHandle().world.findNearbyPlayer(this.getEntityHandle(), 10);
			if(this.m_nearPlayer == null)
				return false;
			else
			{
				ItemStack item = this.m_nearPlayer.by();
				return item != null && item.id == this.m_itemID;
			}
		}
	}

	@Override
	public boolean canContinue()
	{
		if(this.m_scaredByMovement)
		{
			if(this.getEntityHandle().e(this.m_nearPlayer) < 36)
			{
				if(this.m_nearPlayer.e(this.m_x, this.m_y, this.m_z) > 0.010000000000000002D)
					return false;

				if(Math.abs(this.m_nearPlayer.pitch - this.m_pitch) > 5 || Math.abs(this.m_nearPlayer.yaw - this.m_yaw) > 5)
					return false;
			}
			else
			{
				this.m_x = this.m_nearPlayer.locX;
				this.m_y = this.m_nearPlayer.locY;
				this.m_z = this.m_nearPlayer.locZ;
			}

			this.m_yaw = this.m_nearPlayer.yaw;
			this.m_pitch = this.m_nearPlayer.pitch;
		}

		return this.shouldExecute();
	}

	@Override
	public void startExecuting()
	{
		this.m_x = this.m_nearPlayer.locX;
		this.m_y = this.m_nearPlayer.locY;
		this.m_z = this.m_nearPlayer.locZ;
		this.m_isTempted = true;
		this.m_avoidWaterState = this.getNavigation().a();
		this.getNavigation().a(false);
	}

	@Override
	public void stopExecuting()
	{
		this.m_nearPlayer = null;
		this.getNavigation().h();
		this.m_delayTicks = 100;
		this.m_isTempted = false;
		this.getNavigation().a(this.m_avoidWaterState);
	}

	@Override
	public boolean update()
	{
		NMSUtil.getControllerLook(this.getEntityHandle()).a(this.m_nearPlayer, 30, NMSUtil.getMaxHeadRotation(this.getEntityHandle()));
		if(this.getEntityHandle().e(this.m_nearPlayer) < 6.25)
			this.getNavigation().h();
		else
			this.getRemoteEntity().move(this.m_nearPlayer.getBukkitEntity(), (this.m_speed == -1 ? this.getRemoteEntity().getSpeed() : this.m_speed));

		return false;
	}

	public boolean isTempted()
	{
		return this.m_isTempted;
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}