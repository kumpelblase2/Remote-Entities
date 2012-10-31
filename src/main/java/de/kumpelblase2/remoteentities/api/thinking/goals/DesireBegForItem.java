package de.kumpelblase2.remoteentities.api.thinking.goals;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityWolf;
import org.bukkit.Material;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

public class DesireBegForItem extends DesireBase
{
	protected final Set<Material> m_toBeg;
	protected EntityHuman m_nearestPlayer;
	protected float m_minDistance;
	private int m_ticks;
	
	public DesireBegForItem(RemoteEntity inEntity, Material... inMaterial)
	{
		this(inEntity, 5, inMaterial);
	}
	
	public DesireBegForItem(RemoteEntity inEntity, float inMinDistance, Material... inMaterial)
	{
		super(inEntity);
		this.m_toBeg = EnumSet.copyOf(Arrays.asList(inMaterial));
		this.m_type = 2;
		this.m_minDistance = inMinDistance;
	}
	
	@Override
	public void startExecuting()
	{
		if(this.getRemoteEntity().getHandle() instanceof EntityWolf)
			((EntityWolf)this.getRemoteEntity().getHandle()).i(true);
		
		this.m_ticks = 40 + this.getRemoteEntity().getHandle().au().nextInt(40);
	}

	@Override
	public void stopExecuting()
	{
		this.m_nearestPlayer = null;
		if(this.getRemoteEntity().getHandle() instanceof EntityWolf)
			((EntityWolf)this.getRemoteEntity().getHandle()).i(false);
	}
	
	@Override
	public boolean update()
	{
		this.getRemoteEntity().getHandle().getControllerLook().a(this.m_nearestPlayer.locX, this.m_nearestPlayer.locY + this.m_nearestPlayer.getHeadHeight(), this.m_nearestPlayer.locZ, 10F, this.getRemoteEntity().getHandle().bf());
		this.m_ticks--;
		return true;
	}

	@Override
	public boolean shouldExecute()
	{
		this.m_nearestPlayer = this.getRemoteEntity().getHandle().world.findNearbyPlayer(this.getRemoteEntity().getHandle(), this.m_minDistance);
		return this.m_nearestPlayer == null ? false : this.update();
	}

	@Override
	public boolean canContinue()
	{
		if(!this.m_nearestPlayer.isAlive())
			return false;
		
		if(this.getRemoteEntity().getHandle().e(this.m_nearestPlayer) > this.m_minDistance * this.m_minDistance)
			return false;
		
		return this.m_ticks > 0 && this.hasItemInHand(this.m_nearestPlayer);
	}
	
	protected boolean hasItemInHand(EntityHuman inPlayer)
	{
		if(inPlayer.getBukkitEntity().getItemInHand() == null)
			return false;
		
		return this.m_toBeg.contains(inPlayer.getBukkitEntity().getItemInHand().getType());
	}
}
