package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.entity.LivingEntity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

public class DesireTempt extends DesireBase
{
	protected int m_itemId;
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
	
	public DesireTempt(RemoteEntity inEntity, int inItemId, boolean inScaredByMovement)
	{
		super(inEntity);
		this.m_itemId = inItemId;
		this.m_scaredByMovement = inScaredByMovement;
		this.m_type = 3;
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
			this.m_nearPlayer = this.getRemoteEntity().getHandle().world.findNearbyPlayer(this.getRemoteEntity().getHandle(), 10);
			if(this.m_nearPlayer == null)
				return false;
			else
			{
				ItemStack item = this.m_nearPlayer.bP();
				return item == null ? false : item.id == this.m_itemId;
			}
		}
	}
	
	@Override
	public boolean canContinue()
	{
		if(this.m_scaredByMovement)
		{
			if(this.getRemoteEntity().getHandle().e(this.m_nearPlayer) < 36)
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
		this.m_avoidWaterState = this.getRemoteEntity().getHandle().getNavigation().a();
		this.getRemoteEntity().getHandle().getNavigation().a(false);
	}
	
	@Override
	public void stopExecuting()
	{
		this.m_nearPlayer = null;
		this.getRemoteEntity().getHandle().getNavigation().g();
		this.m_delayTicks = 100;
		this.m_isTempted = false;
		this.getRemoteEntity().getHandle().getNavigation().a(this.m_avoidWaterState);
	}
	
	@Override
	public boolean update()
	{
		this.getRemoteEntity().getHandle().getControllerLook().a(this.m_nearPlayer, 30, this.getRemoteEntity().getHandle().bm());
		if(this.getRemoteEntity().getHandle().e(this.m_nearPlayer) < 6.25)
			this.getRemoteEntity().getHandle().getNavigation().g();
		else
			this.getRemoteEntity().move((LivingEntity)this.m_nearPlayer.getBukkitEntity());
		
		return false;
	}
	
	public boolean isTempted()
	{
		return this.m_isTempted;
	}
}
