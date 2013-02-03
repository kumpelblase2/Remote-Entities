package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_4_R1.EntityHuman;
import net.minecraft.server.v1_4_R1.EntityWolf;
import org.bukkit.Material;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;

public class DesireBegForItem extends DesireBase
{
	@SerializeAs(pos = 2)
	protected final Material[] m_toBeg;
	protected EntityHuman m_nearestPlayer;
	@SerializeAs(pos = 1)
	protected float m_minDistance;
	protected float m_minDistanceSquared;
	private int m_ticks;
	
	public DesireBegForItem(RemoteEntity inEntity, Material... inMaterial)
	{
		this(inEntity, 5f, inMaterial);
	}
	
	public DesireBegForItem(RemoteEntity inEntity, float inMinDistance, Material... inMaterial)
	{
		super(inEntity);
		this.m_toBeg = inMaterial;
		this.m_type = 2;
		this.m_minDistance = inMinDistance;
		this.m_minDistanceSquared = this.m_minDistance * this.m_minDistance;
	}
	
	@Override
	public void startExecuting()
	{
		if(this.getEntityHandle() instanceof EntityWolf)
			((EntityWolf)this.getEntityHandle()).j(true);
		
		this.m_ticks = 40 + this.getEntityHandle().aB().nextInt(40);
	}

	@Override
	public void stopExecuting()
	{
		this.m_nearestPlayer = null;
		if(this.getEntityHandle() instanceof EntityWolf)
			((EntityWolf)this.getEntityHandle()).j(false);
	}
	
	@Override
	public boolean update()
	{
		this.getEntityHandle().getControllerLook().a(this.m_nearestPlayer.locX, this.m_nearestPlayer.locY + this.m_nearestPlayer.getHeadHeight(), this.m_nearestPlayer.locZ, 10F, this.getEntityHandle().bp());
		this.m_ticks--;
		return true;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;
		
		this.m_nearestPlayer = this.getEntityHandle().world.findNearbyPlayer(this.getEntityHandle(), this.m_minDistance);
		return this.m_nearestPlayer == null ? false : this.update();
	}

	@Override
	public boolean canContinue()
	{
		if(!this.m_nearestPlayer.isAlive())
			return false;
		
		if(this.getEntityHandle().e(this.m_nearestPlayer) > this.m_minDistanceSquared)
			return false;
		
		return this.m_ticks > 0 && this.hasItemInHand(this.m_nearestPlayer);
	}
	
	protected boolean hasItemInHand(EntityHuman inPlayer)
	{
		if(inPlayer.getBukkitEntity().getItemInHand() == null)
			return false;
		
		Material inHand = inPlayer.getBukkitEntity().getItemInHand().getType();
		for(Material m : this.m_toBeg)
		{
			if(m == inHand)
				return true;
		}
		return false;
	}
}
