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
}