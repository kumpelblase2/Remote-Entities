package de.kumpelblase2.removeentities.thinking.goals;

import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import net.minecraft.server.EntityLiving;
import de.kumpelblase2.removeentities.entities.RemoteEntity;
import de.kumpelblase2.removeentities.thinking.Desire;

public class DesireLookAtSpecific extends DesireLookAtNearest implements Desire
{
	private final EntityLiving m_target;
	
	public DesireLookAtSpecific(RemoteEntity inEntity, EntityLiving inTarget, float inDelay)
	{
		super(inEntity, inTarget.getClass(), inDelay);
		this.m_target = inTarget;
	}
	
	public DesireLookAtSpecific(RemoteEntity inEntity, LivingEntity inTarget, float inDelay)
	{
		this(inEntity, ((CraftLivingEntity)inTarget).getHandle(), inDelay);
	}

	@Override
	public boolean a()
	{
		if(!super.a())
			return false;
		
		if(this.a != this.m_target)
			this.a = this.m_target;
		
		return true;
	}
}
