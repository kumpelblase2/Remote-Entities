package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_5_R3.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityHandle;
import de.kumpelblase2.remoteentities.api.thinking.DesireItem;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireSwim;
import de.kumpelblase2.remoteentities.nms.*;
import de.kumpelblase2.remoteentities.utilities.WorldUtilities;

public class RemotePlayerEntity extends EntityPlayer implements RemoteEntityHandle
{
	protected RemoteEntity m_remoteEntity;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;
	protected EntityLiving m_target;

	public RemotePlayerEntity(MinecraftServer minecraftserver, World world, String s, PlayerInteractManager iteminworldmanager)
	{
		super(minecraftserver, world, s, iteminworldmanager);
		try
		{
			NetworkManager manager = new RemoteEntityNetworkManager(minecraftserver);
			this.playerConnection = new NullNetServerHandler(minecraftserver, manager, this);
			manager.a(playerConnection);
		}
		catch(Exception e)
		{
		}

		new PathfinderGoalSelectorHelper(this.goalSelector).clearGoals();
		new PathfinderGoalSelectorHelper(this.targetSelector).clearGoals();
		iteminworldmanager.setGameMode(EnumGamemode.SURVIVAL);
		this.noDamageTicks = 1;
		this.Y = 1;
		this.getNavigation().e(true);
		this.fauxSleeping = true;
	}

	public RemotePlayerEntity(MinecraftServer minecraftserver, World world, String s, PlayerInteractManager iteminworldmanager, RemoteEntity inEntity)
	{
		this(minecraftserver, world, s, iteminworldmanager);
		this.m_remoteEntity = inEntity;
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
	public void l_()
	{
		this.yaw = this.az;
		super.l_();
		super.g();

		if(this.noDamageTicks > 0)
			this.noDamageTicks--;

		//Taken from Citizens2#EntityHumanNPC.java#129 - #138
        if(Math.abs(motX) < 0.001F && Math.abs(motY) < 0.001F && Math.abs(motZ) < 0.001F)
            motX = motY = motZ = 0;

		Navigation navigation = getNavigation();
        if(!navigation.f())
        {
            navigation.e();
            this.applyMovement();
        }
        //End Citizens

        if(this.getRemoteEntity() != null)
        	this.getRemoteEntity().getMind().tick();
	}

	public void applyMovement()
	{
		if(this.m_remoteEntity.isStationary())
			return;

		//Taken from Citizens2#NMS.java#259 - #262
		getControllerMove().c();
		getControllerLook().a();
		getControllerJump().b();
		e(this.getRemoteEntity().getSpeed());
		//End Citizens

		if(this.bG)
		{
            boolean inLiquid = G() || I();
            if (inLiquid)
                this.motY += 0.04;
            else if (this.onGround && this.bC == 0)
            {
                this.motY = 0.6;
                this.bD = 10;
            }
        }
		else
            this.bD = 0;

		this.bC *= 0.98F;
		this.bD *= 0.98F;
		this.bE *= 0.9F;

        float prev = this.aN;
		this.aN *= this.bE() * this.getRemoteEntity().getSpeed();
		this.e(this.bC, this.bD);
		this.aN = prev;
		this.az = this.yaw;
	}

	@Override
	public void setupStandardGoals()
	{
		this.getRemoteEntity().getMind().addMovementDesires(getDefaultMovementDesires(this.getRemoteEntity()));
	}

	@Override
	public boolean bh()
	{
		return true;
	}

	@Override
	public void g(double x, double y, double z)
	{
		if(this.m_remoteEntity == null)
		{
			super.g(x, y, z);
			return;
		}

		Vector vector = ((RemoteBaseEntity)this.m_remoteEntity).onPush(x, y, z);
		if(vector != null)
			super.g(vector.getX(), vector.getY(), vector.getZ());
	}

	@Override
	public void move(double d0, double d1, double d2)
	{
		if(this.m_remoteEntity != null && this.m_remoteEntity.isStationary())
			return;

		super.move(d0, d1, d2);
	}

	@Override
	public void collide(Entity inEntity)
	{
		if(this.getRemoteEntity() == null)
		{
			super.collide(inEntity);
			return;
		}

		if(((RemoteBaseEntity)this.m_remoteEntity).onCollide(inEntity.getBukkitEntity()))
			super.collide(inEntity);
	}

	@Override
	public boolean a_(EntityHuman entity)
	{
		if(this.getRemoteEntity() == null)
			return super.a_(entity);

		if(!(entity.getBukkitEntity() instanceof Player))
			return super.a_(entity);

		if(((RemoteBaseEntity)this.m_remoteEntity).onInteract((Player)entity.getBukkitEntity()))
			return super.a_(entity);
		else
			return false;
	}

	@Override
	public void die(DamageSource damagesource)
	{
		((RemoteBaseEntity)this.m_remoteEntity).onDeath();
		super.die(damagesource);
	}

	@Override
	public EntityLiving getGoalTarget()
	{
		return this.m_target;
	}

	@Override
	public void setGoalTarget(EntityLiving inEntity)
	{
		this.m_target = inEntity;
	}

	@Override
	public boolean m(Entity inEntity)
	{
		this.attack(inEntity);
		return super.m(inEntity);
	}

	void updateSpawn()
	{
		Packet20NamedEntitySpawn packet = new Packet20NamedEntitySpawn(this);
		for(Player player : WorldUtilities.getNearbyPlayers(this.getBukkitEntity(), 64))
		{
			WorldUtilities.sendPacketToPlayer(player, packet);
		}
	}

	public static DesireItem[] getDefaultMovementDesires(RemoteEntity inEntityFor)
	{
		return new DesireItem[] {
				new DesireItem(new DesireSwim(inEntityFor), 0)
		};
	}

	public static DesireItem[] getDefaultTargetingDesires(RemoteEntity inEntityFor)
	{
		return new DesireItem[0];
	}
}