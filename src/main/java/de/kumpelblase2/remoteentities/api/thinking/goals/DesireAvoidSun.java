package de.kumpelblase2.remoteentities.api.thinking.goals;

import java.util.Random;
import org.bukkit.Location;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.MathHelper;
import net.minecraft.server.Vec3D;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

public class DesireAvoidSun extends DesireBase
{
	protected double m_x;
	protected double m_y;
	protected double m_z;
	
	public DesireAvoidSun(RemoteEntity inEntity)
	{
		super(inEntity);
	}

	@Override
	public void startExecuting()
	{
		this.getRemoteEntity().move(new Location(this.getRemoteEntity().getBukkitEntity().getWorld(), this.m_x, this.m_y, this.m_z));
	}

	@Override
	public boolean shouldExecute()
	{
		EntityLiving entity = this.getRemoteEntity().getHandle();
		if(!entity.world.t())
			return false;
		else if(!entity.isBurning())
			return false;
		else if(!entity.world.j(MathHelper.floor(entity.locX), (int)entity.boundingBox.b, MathHelper.floor(entity.locZ)))
			return false;
		else
		{
			Vec3D vec = this.getShadowPlace();
			
			if(vec == null)
				return false;
			else
			{
				this.m_x = vec.c;
				this.m_y = vec.d;
				this.m_z = vec.e;
				return true;
			}
		}
	}

	@Override
	public boolean canContinue()
	{
		return !this.getRemoteEntity().getHandle().getNavigation().f();
	}
	
	protected Vec3D getShadowPlace()
	{
		EntityLiving entity = this.getRemoteEntity().getHandle();
		Random r = entity.aA();
		
		for(int i = 0; i < 10; i++)
		{
			int x = MathHelper.floor(entity.locX + r.nextInt(20) - 10);
			int y = MathHelper.floor(entity.boundingBox.b + r.nextInt(6) - 3);
			int z = MathHelper.floor(entity.locZ + r.nextInt(20) - 10);
			
			
			if(entity instanceof EntityCreature)
			{
				if(!entity.world.j(x, y, z) && ((EntityCreature)entity).a(x, y,z) < 0.0F)
					return Vec3D.a.create(x, y, z);
			}
			else
			{
				if(!entity.world.j(x, y, z) && (0.5F - entity.world.o(x, y, z)) < 0.0F)
					return Vec3D.a.create(x, y, z);
			}
		}
		return null;
	}
}
