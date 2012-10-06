package de.kumpelblase2.removeentities.entities;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.minecraft.server.*;
import de.kumpelblase2.removeentities.api.*;
import de.kumpelblase2.removeentities.api.thinking.*;
import de.kumpelblase2.removeentities.nms.*;

public class RemotePlayerEntity extends EntityPlayer implements RemoteEntityHandle
{
	protected RemoteEntity m_remoteEntity;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;
	protected int m_maxHealth = 20;
	protected final PathfinderGoalSelectorHelper goalSelectorHelper;
	protected final PathfinderGoalSelectorHelper targetSelectorHelper;
	
	public RemotePlayerEntity(MinecraftServer minecraftserver, World world, String s, ItemInWorldManager iteminworldmanager)
	{
		super(minecraftserver, world, s, iteminworldmanager);
		this.goalSelectorHelper = new PathfinderGoalSelectorHelper(this.goalSelector);
		this.targetSelectorHelper = new PathfinderGoalSelectorHelper(this.targetSelector);
		iteminworldmanager.setGameMode(EnumGamemode.SURVIVAL);
		this.noDamageTicks = 1;
		this.W = 1;
		this.getNavigation().e(true);
	}
	
	public RemotePlayerEntity(MinecraftServer minecraftserver, World world, String s, ItemInWorldManager iteminworldmanager, RemoteEntity inEntity)
	{
		this(minecraftserver, world, s, iteminworldmanager);
		this.m_remoteEntity = inEntity;
		try
		{
			NetworkManager manager = new RemoteEntityNetworkManager(minecraftserver);
			this.netServerHandler = new NullNetServerHandler(minecraftserver, manager, this);
			manager.a(netServerHandler);
		}
		catch(Exception e){}
	}

	@Override
	public RemoteEntity getRemoteEntity()
	{
		return this.m_remoteEntity;
	}

	@Override
	public Inventory getInventory()
	{
		return this.getBukkitEntity().getInventory();
	}
	
	@Override
	public void b_(EntityHuman entity)
	{
		if(entity instanceof EntityPlayer)
		{
			if (this.getRemoteEntity().getMind().canFeel() && (this.m_lastBouncedId != entity.id || System.currentTimeMillis() - this.m_lastBouncedTime > 1000) && this.getRemoteEntity().getMind().hasBehaviour("Touch")) {
				if(entity.getBukkitEntity().getLocation().distanceSquared(getBukkitEntity().getLocation()) <= 1)
				{
					((TouchBehaviour)this.getRemoteEntity().getMind().getBehaviour("Touch")).onTouch((Player)entity.getBukkitEntity());
					this.m_lastBouncedTime = System.currentTimeMillis();
					this.m_lastBouncedId = entity.id;
				}
			}
		}
		super.b_(entity);
	}
	
	@Override
	public boolean c(EntityHuman entity)
	{
		if(entity instanceof EntityPlayer && this.getRemoteEntity().getMind().canFeel() && this.getRemoteEntity().getMind().hasBehaviour("Interact"))
		{
			((InteractBehaviour)this.getRemoteEntity().getMind().getBehaviour("Interact")).onInteract((Player)entity.getBukkitEntity());
		}
		
		return super.c(entity);
	}
	
	@Override
	public void h_()
	{
		super.h_();
		super.g();
		
		if(this.noDamageTicks > 0)
			this.noDamageTicks--;
		
		Navigation navigation = getNavigation();
        if(!navigation.f())
        {
            navigation.e();
            this.applyMovement();
        }
        else if(motX != 0 || motZ != 0 || motY != 0)
        {
            if(Math.abs(motX) < 0.001F && Math.abs(motY) < 0.001F && Math.abs(motZ) < 0.001F)
                motX = motY = motZ = 0;
        }
        
        this.getRemoteEntity().getMind().tick();
	}
	
	public void applyMovement()
	{
		getControllerMove().c();
		getControllerLook().a();
		getControllerJump().b();
		e(this.getRemoteEntity().getSpeed());
		
		if (bu)
		{
            boolean inLiquid = H() || J();
            if (inLiquid)
            {
                motY += 0.04;
            }
            else if (onGround && bE == 0)
            {
                motY = 0.6;
                bE = 10;
            }
        }
		else
		{
            bE = 0;
        }
        br *= 0.98F;
        bs *= 0.98F;
        bt *= 0.9F;

        float prev = aG;
        aG *= bs() * this.getRemoteEntity().getSpeed();
        e(br, bs); 
        aG = prev;
        as = yaw;
	}
	
	@Override
	public void g(double x, double y, double z)
	{
		if(this.m_remoteEntity.isPushable())
			super.g(x, y, z);
	}
	
	@Override
	public int getMaxHealth()
	{
		if(this.m_maxHealth == 0) // otherwise the entity will die instantly
			return 20;
		return this.m_maxHealth;
	}
	
	public void setMaxHealth(int inMax)
	{
		this.m_maxHealth = inMax;
	}

	@Override
	public void setupStandardGoals()
	{
	}

	@Override
	public PathfinderGoalSelectorHelper getGoalSelector()
	{
		return this.goalSelectorHelper;
	}

	@Override
	public PathfinderGoalSelectorHelper getTargetSelector()
	{
		return this.targetSelectorHelper;
	}
	
	@Override
	public boolean aV()
	{
		return true;
	}
}