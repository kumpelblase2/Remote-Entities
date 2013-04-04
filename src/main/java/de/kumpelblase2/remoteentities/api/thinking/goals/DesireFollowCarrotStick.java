package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_5_R2.Block;
import net.minecraft.server.v1_5_R2.EntityHuman;
import net.minecraft.server.v1_5_R2.EntityLiving;
import net.minecraft.server.v1_5_R2.Item;
import net.minecraft.server.v1_5_R2.ItemStack;
import net.minecraft.server.v1_5_R2.MathHelper;
import net.minecraft.server.v1_5_R2.PathPoint;
import net.minecraft.server.v1_5_R2.Pathfinder;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.persistence.ParameterData;
import de.kumpelblase2.remoteentities.persistence.SerializeAs;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public class DesireFollowCarrotStick extends DesireBase
{
	@SerializeAs(pos = 1)
	protected float m_maxSpeed;
	protected float m_currentSpeed = 0;
	protected boolean m_speedBoosted = false;
	protected int m_speedBoostTime = 0;
	protected int m_maxSpeedBoostTime = 0;
	
	public DesireFollowCarrotStick(RemoteEntity inEntity)
	{
		this(inEntity, 10);
	}
	
	public DesireFollowCarrotStick(RemoteEntity inEntity, float inMaxSpeed)
	{
		super(inEntity);
		this.m_maxSpeed = inMaxSpeed;
		this.m_type = 7;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;
		
		return this.getEntityHandle().isAlive() && this.getEntityHandle().passenger != null && this.getEntityHandle().passenger instanceof EntityHuman && (this.m_speedBoosted || this.getEntityHandle().bL());
	}
	
	@Override
	public void stopExecuting()
	{
		this.m_speedBoosted = false;
		this.m_currentSpeed = 0;
	}
	
	@Override
	public void startExecuting()
	{
		this.m_currentSpeed = 0;
	}
	
	@Override
	public boolean update()
	{
		EntityHuman passenger = (EntityHuman)this.getEntityHandle().passenger;
		EntityLiving entity = this.getEntityHandle();
		float f = MathHelper.g(passenger.yaw - entity.yaw) * 0.5f;
		
		if(f > 5)
			f = 5;
		
		if(f < -5)
			f = -5;
		
		entity.yaw = MathHelper.g(entity.yaw + f);
		if(this.m_currentSpeed < this.m_maxSpeed)
			this.m_currentSpeed += (this.m_maxSpeed - this.m_currentSpeed) * 0.01;
		
		if(this.m_currentSpeed > this.m_maxSpeed)
			this.m_currentSpeed = this.m_maxSpeed;
		
		int x = MathHelper.floor(entity.locX);
		int y = MathHelper.floor(entity.locY);
		int z = MathHelper.floor(entity.locZ);
		float speed = this.m_currentSpeed;
		
		if(this.m_speedBoosted)
		{
			if(this.m_speedBoostTime++ > this.m_maxSpeedBoostTime)
				this.m_speedBoosted = false;
			
			speed += speed * 1.15 * MathHelper.sin((float)(this.m_speedBoostTime / this.m_maxSpeedBoostTime * Math.PI));
		}
		
		float f2 = 0.91f;
		if(entity.onGround)
		{
			f2 = 0.54600006F;
			int id = entity.world.getTypeId(MathHelper.d(x), MathHelper.d(y) - 1, MathHelper.d(z));
			if(id > 0)
				f2 = Block.byId[id].frictionFactor * 0.91f;
		}
		
		
		float f3 = 0.16277136F / (f2 * f2 * f2);
        float f4 = MathHelper.sin(entity.yaw * 3.1415927F / 180.0F);
        float f5 = MathHelper.cos(entity.yaw * 3.1415927F / 180.0F);
        float f6 = entity.aI() * f3;
        float f7 = Math.max(speed, 1.0F);

        f7 = f6 / f7;
        float f8 = speed * f7;
        float f9 = -(f8 * f4);
        float f10 = f8 * f5;

        if (MathHelper.abs(f9) > MathHelper.abs(f10)) {
            if (f9 < 0.0F) {
                f9 -= entity.width / 2.0F;
            }

            if (f9 > 0.0F) {
                f9 += entity.width / 2.0F;
            }

            f10 = 0.0F;
        } else {
            f9 = 0.0F;
            if (f10 < 0.0F) {
                f10 -= entity.width / 2.0F;
            }

            if (f10 > 0.0F) {
                f10 += entity.width / 2.0F;
            }
        }
		
        int nextX = MathHelper.floor(entity.locX + f9);
        int nextZ = MathHelper.floor(entity.locZ + f10);
        PathPoint point = new PathPoint(MathHelper.d(entity.width + 1), MathHelper.d(entity.length + passenger.length + 1), MathHelper.d(entity.width + 1));
        if((x != nextX || z != nextZ) && Pathfinder.a(entity, nextX, y, nextZ, point, false, false, true) == 0 && Pathfinder.a(entity, x, y + 1, z, point, false, false, true) == 1 && Pathfinder.a(entity, nextX, y + 1, nextZ, point, false, false, true) == 1)
        	entity.getControllerJump().a();
        
        if(!passenger.abilities.canInstantlyBuild && this.m_currentSpeed >= this.m_maxSpeed * 0.5 && entity.aE().nextFloat() < 0.006f && !this.m_speedBoosted)
        {
        	ItemStack item = passenger.bG();
        	
        	if(item != null && item.id == Item.CARROT_STICK.id)
        	{
        		item.damage(1, passenger);
        		if(item.count == 0)
        		{
        			ItemStack newItem = new ItemStack(Item.FISHING_ROD);
        			newItem.setTag(item.tag);
        			passenger.inventory.items[passenger.inventory.itemInHandIndex] = newItem;
        		}
        	}
        }
        
        entity.e(0, speed);
		return true;
	}
	
	public boolean isSpeedBoosted()
	{
		return this.m_speedBoosted;
	}
	
	public void boostSpeed()
	{
		this.m_speedBoosted = true;
		this.m_speedBoostTime = 0;
		this.m_maxSpeedBoostTime = this.getEntityHandle().aE().nextInt(841) + 140;
	}
	
	public boolean isControlledByPlayer()
	{
		return !this.isSpeedBoosted() && this.m_currentSpeed > this.m_maxSpeed * 0.3;
	}
	
	@Override
	public ParameterData[] getSerializeableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}
}
