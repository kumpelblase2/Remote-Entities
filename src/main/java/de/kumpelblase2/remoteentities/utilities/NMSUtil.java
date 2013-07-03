package de.kumpelblase2.remoteentities.utilities;

import net.minecraft.server.v1_6_R1.*;
import de.kumpelblase2.remoteentities.entities.RemotePlayerEntity;

public class NMSUtil
{
	public static Navigation getNavigation(EntityLiving inEntity)
	{
		if(inEntity instanceof EntityInsentient)
			return ((EntityInsentient)inEntity).getNavigation();
		else if(inEntity instanceof RemotePlayerEntity)
			return ((RemotePlayerEntity)inEntity).getNavigation();
		else
			return null;
	}

	public static ControllerJump getControllerJump(EntityLiving inEntity)
	{
		if(inEntity instanceof EntityInsentient)
			return ((EntityInsentient)inEntity).getControllerJump();
		else if(inEntity instanceof RemotePlayerEntity)
			return ((RemotePlayerEntity)inEntity).getControllerJump();
		else
			return null;
	}

	public static ControllerMove getControllerMove(EntityLiving inEntity)
	{
		if(inEntity instanceof EntityInsentient)
			return ((EntityInsentient)inEntity).getControllerMove();
		else if(inEntity instanceof RemotePlayerEntity)
			return ((RemotePlayerEntity)inEntity).getControllerMove();
		else
			return null;
	}

	public static ControllerLook getControllerLook(EntityLiving inEntity)
	{
		if(inEntity instanceof EntityInsentient)
			return ((EntityInsentient)inEntity).getControllerLook();
		else if(inEntity instanceof RemotePlayerEntity)
			return ((RemotePlayerEntity)inEntity).getControllerLook();
		else
			return null;
	}

	public static EntitySenses getEntitySenses(EntityLiving inEntity)
	{
		if(inEntity instanceof EntityInsentient)
			return ((EntityInsentient)inEntity).getEntitySenses();
		else if(inEntity instanceof RemotePlayerEntity)
			return ((RemotePlayerEntity)inEntity).getEntitySenses();
		else
			return null;
	}

	public static void setGoalTarget(EntityLiving inFrom, EntityLiving inTarget)
	{
		if(inFrom instanceof EntityInsentient)
			((EntityInsentient)inFrom).setGoalTarget(inTarget);
		else if(inFrom instanceof RemotePlayerEntity)
			((RemotePlayerEntity)inFrom).setGoalTarget(inTarget);
	}

	public static EntityLiving getGoalTarget(EntityLiving inFrom)
	{
		if(inFrom instanceof EntityInsentient)
			return ((EntityInsentient)inFrom).getGoalTarget();
		else if(inFrom instanceof RemotePlayerEntity)
			return ((RemotePlayerEntity)inFrom).getGoalTarget();
		else
			return null;
	}

	public static int getMaxHeadRotation(EntityLiving inEntity)
	{
		if(inEntity instanceof EntityInsentient)
			return ((EntityInsentient)inEntity).bl();
		else
			return 40;
	}

	public static ChunkCoordinates getChunkCoordinates(EntityLiving inEntity)
	{
		if(inEntity instanceof EntityCreature)
			return ((EntityCreature)inEntity).bL();
		else if(inEntity instanceof EntityPlayer)
			return ((EntityPlayer)inEntity).b();
		else
			return new ChunkCoordinates(MathHelper.floor(inEntity.locX), MathHelper.floor(inEntity.locY), MathHelper.floor(inEntity.locZ));
	}

	public static boolean hasHomeArea(EntityLiving inEntity)
	{
		return inEntity instanceof EntityCreature && ((EntityCreature)inEntity).bO();
	}

	public static boolean isInHomeArea(EntityLiving inEntity)
	{
		return !NMSUtil.hasHomeArea(inEntity) || !(inEntity instanceof EntityCreature) || ((EntityCreature)inEntity).bK();
	}

	public static boolean isInHomeArea(EntityLiving inEntity, int x, int y, int z)
	{
		return !NMSUtil.hasHomeArea(inEntity) || !(inEntity instanceof EntityCreature) || ((EntityCreature)inEntity).b(x, y, z);
	}

	public static float getHomeRange(EntityLiving inEntity)
	{
		if(inEntity instanceof EntityCreature)
			return ((EntityCreature)inEntity).bM();
		else
			return 5; //TODO 5 seems weird.
	}

	public static boolean canBeSteered(EntityLiving inEntity)
	{
		return inEntity instanceof EntityInsentient && ((EntityInsentient)inEntity).bu();
	}
}