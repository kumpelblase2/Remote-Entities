package de.kumpelblase2.remoteentities.utilities;

import net.minecraft.server.v1_7_R2.*;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityHandle;
import de.kumpelblase2.remoteentities.entities.RemotePlayerEntity;

public class NMSUtil
{
	private static EntityInsentient s_tempEntity;

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
			return ((EntityInsentient)inEntity).bv();
		else
			return 40;
	}

	public static ChunkCoordinates getChunkCoordinates(EntityLiving inEntity)
	{
		if(inEntity instanceof EntityCreature)
			return ((EntityCreature)inEntity).bV();
		else if(inEntity instanceof EntityPlayer)
			return ((EntityPlayer)inEntity).getChunkCoordinates();
		else
			return new ChunkCoordinates(MathHelper.floor(inEntity.locX), MathHelper.floor(inEntity.locY), MathHelper.floor(inEntity.locZ));
	}

	public static boolean isOnLeash(EntityLiving inEntity)
	{
		return inEntity instanceof EntityInsentient && ((EntityInsentient)inEntity).bJ();
	}

	public static boolean hasHomeArea(EntityLiving inEntity)
	{
		return inEntity instanceof EntityCreature && ((EntityCreature)inEntity).bS();
	}

	public static boolean isInHomeArea(EntityLiving inEntity)
	{
		return !NMSUtil.hasHomeArea(inEntity) || !(inEntity instanceof EntityCreature) || ((EntityCreature)inEntity).bS();
	}

	public static boolean isInHomeArea(EntityLiving inEntity, int x, int y, int z)
	{
		return !NMSUtil.hasHomeArea(inEntity) || !(inEntity instanceof EntityCreature) || ((EntityCreature)inEntity).b(x, y, z);
	}

	public static float getHomeRange(EntityLiving inEntity)
	{
		if(inEntity instanceof EntityCreature)
			return ((EntityCreature)inEntity).bW();
		else
			return 5; //TODO 5 seems weird.
	}

	public static boolean canBeSteered(EntityLiving inEntity)
	{
		return inEntity instanceof EntityInsentient && ((EntityInsentient)inEntity).bE();
	}

	public static EntityInsentient getTempInsentientEntity()
	{
		if(s_tempEntity == null)
		{
			s_tempEntity = new EntityInsentient(null)
			{
			};
		}

		return s_tempEntity;
	}

	public static boolean isAboutEqual(ItemStack inNMSStack, ItemStack inNMSStack2)
	{
		if(!inNMSStack.doMaterialsMatch(inNMSStack2))
			return false;

		if(inNMSStack.usesData() != inNMSStack2.usesData() || inNMSStack.getData() != inNMSStack2.getData())
			return false;

		if((inNMSStack.tag != null && inNMSStack2.tag == null) || (inNMSStack.tag == null && inNMSStack2.tag != null))
			return false;

		if(inNMSStack.tag != null)
		{
			if(!inNMSStack2.tag.equals(inNMSStack2.tag))
				return false;
		}

		return true;
	}

	public static RemoteEntity getRemoteEntityFromEntity(LivingEntity inEntity)
	{
		EntityLiving handle = ((CraftLivingEntity)inEntity).getHandle();
		if(!(handle instanceof RemoteEntityHandle))
			return null;

		return ((RemoteEntityHandle)handle).getRemoteEntity();
	}

	public static Class<? extends EntityLiving> getNMSClassFromEntity(LivingEntity inEntity)
	{
		return ((CraftLivingEntity)inEntity).getHandle().getClass();
	}
}