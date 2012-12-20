package de.kumpelblase2.remoteentities.api.thinking.goals;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import net.minecraft.server.v1_4_5.EntityHuman;
import net.minecraft.server.v1_4_5.EntityWolf;
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
		
		if(this.getEntityHandle().e(this.m_nearestPlayer) > this.m_minDistance * this.m_minDistance)
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
