package de.kumpelblase2.remoteentities.nms;

import java.util.Random;
import net.minecraft.server.v1_6_R2.*;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;

public class RandomPositionGenerator
{
	private static final Vec3D a = Vec3D.a(0.0D, 0.0D, 0.0D);

    public static Vec3D a(EntityLiving entitycreature, int i, int j)
    {
        return c(entitycreature, i, j, null);
    }

    public static Vec3D a(EntityLiving entitycreature, int i, int j, Vec3D vec3d)
    {
        a.c = vec3d.c - entitycreature.locX;
        a.d = vec3d.d - entitycreature.locY;
        a.e = vec3d.e - entitycreature.locZ;
        return c(entitycreature, i, j, a);
    }

    public static Vec3D b(EntityLiving entitycreature, int i, int j, Vec3D vec3d)
    {
        a.c = entitycreature.locX - vec3d.c;
        a.d = entitycreature.locY - vec3d.d;
        a.e = entitycreature.locZ - vec3d.e;
        return c(entitycreature, i, j, a);
    }

    private static Vec3D c(EntityLiving entitycreature, int i, int j, Vec3D vec3d)
    {
        Random random = entitycreature.aC();
        boolean flag = false;
        int k = 0;
        int l = 0;
        int i1 = 0;
        float f = -99999.0F;
        boolean flag1;

        if (NMSUtil.hasHomeArea(entitycreature))
        {
            double d0 = (double) (NMSUtil.getChunkCoordinates(entitycreature).e(MathHelper.floor(entitycreature.locX), MathHelper.floor(entitycreature.locY), MathHelper.floor(entitycreature.locZ)) + 4.0F);
            double d1 = (double) (NMSUtil.getHomeRange(entitycreature) + (float) i);

            flag1 = d0 < d1 * d1;
        }
        else
            flag1 = false;

        for (int j1 = 0; j1 < 10; ++j1)
        {
            int k2 = random.nextInt(2 * i) - i;
            int l2 = random.nextInt(2 * j) - j;
            int i2 = random.nextInt(2 * i) - i;

            if (vec3d == null || (double) k2 * vec3d.c + (double) i2 * vec3d.c >= 0.0D)
            {
                k2 += MathHelper.floor(entitycreature.locX);
                l2 += MathHelper.floor(entitycreature.locY);
                i2 += MathHelper.floor(entitycreature.locZ);
                if (!flag1 || NMSUtil.isInHomeArea(entitycreature, k2, l2, i2))
                {
	                float f1;
	                if(entitycreature instanceof EntityCreature)
		                f1 = ((EntityCreature)entitycreature).a(k2, l2, i2);
	                else
                        f1 = 0.5F - entitycreature.world.q(k2, l2, i2);

                    if (f1 > f)
                    {
                        f = f1;
                        k = k2;
                        l = l2;
                        i1 = i2;
                        flag = true;
                    }
                }
            }
        }

        if (flag)
            return entitycreature.world.getVec3DPool().create((double) k, (double) l, (double) i1);
        else
            return null;
    }
}