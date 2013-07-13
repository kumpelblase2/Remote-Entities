package de.kumpelblase2.remoteentities.api.thinking.goals;

import java.util.Random;
import net.minecraft.server.v1_6_R2.*;
import org.bukkit.Location;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.api.thinking.DesireType;

/**
 * Using this desire the entity will try to find a dark place to be instead of remaining at a sunny spot.
 * This is primarily seen and used by zombies and skeletons.
 */
public class DesireAvoidSun extends DesireBase
{
	protected double m_x;
	protected double m_y;
	protected double m_z;

	@Deprecated
	public DesireAvoidSun(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}

	public DesireAvoidSun()
	{
		super();
		this.m_type = DesireType.PRIMAL_INSTINCT;
	}

	@Override
	public void startExecuting()
	{
		this.getRemoteEntity().move(new Location(this.getRemoteEntity().getBukkitEntity().getWorld(), this.m_x, this.m_y, this.m_z));
	}

	@Override
	public boolean shouldExecute()
	{
		EntityLiving entity = this.getEntityHandle();
		if(entity == null)
			return false;

		if(!entity.world.v())
			return false;
		else if(!entity.isBurning())
			return false;
		else if(!entity.world.l(MathHelper.floor(entity.locX), (int)entity.boundingBox.b, MathHelper.floor(entity.locZ)))
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
				Vec3D.a.release(vec);
				return true;
			}
		}
	}

	@Override
	public boolean canContinue()
	{
		return !this.getNavigation().g();
	}

	protected Vec3D getShadowPlace()
	{
		EntityLiving entity = this.getEntityHandle();
		Random r = entity.aC();

		for(int i = 0; i < 10; i++)
		{
			int x = MathHelper.floor(entity.locX + r.nextInt(20) - 10);
			int y = MathHelper.floor(entity.boundingBox.b + r.nextInt(6) - 3);
			int z = MathHelper.floor(entity.locZ + r.nextInt(20) - 10);

			if(entity instanceof EntityCreature)
			{
				if(!entity.world.l(x, y, z) && ((EntityCreature)entity).a(x, y, z) < 0.0F)
					return entity.world.getVec3DPool().create(x, y, z);
			}
			else
			{
				if(!entity.world.l(x, y, z) && (0.5F - entity.world.q(x, y, z)) < 0.0F)
					return entity.world.getVec3DPool().create(x, y, z);
			}
		}
		return null;
	}
}
