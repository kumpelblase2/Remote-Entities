package de.kumpelblase2.remoteentities.entities;

import net.minecraft.server.v1_7_R3.*;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityHandle;
import de.kumpelblase2.remoteentities.api.thinking.DesireItem;
import de.kumpelblase2.remoteentities.api.thinking.RideBehavior;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireSwim;
import de.kumpelblase2.remoteentities.nms.*;
import de.kumpelblase2.remoteentities.utilities.WorldUtilities;

public class RemotePlayerEntity extends EntityPlayer implements RemoteEntityHandle
{
	protected RemoteEntity m_remoteEntity;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;
	protected EntityLiving m_target;
	protected Navigation m_navigation;
	protected EntitySenses m_senses;
	protected ControllerJump m_controllerJump;
	protected ControllerLook m_controllerLook;
	protected ControllerMove m_controllerMove;
	protected int m_jumpTicks = 0;

	public RemotePlayerEntity(MinecraftServer minecraftserver, WorldServer world, GameProfile profile, PlayerInteractManager iteminworldmanager)
	{
		super(minecraftserver, world, profile, iteminworldmanager);
		try
		{
			NetworkManager manager = new RemoteEntityNetworkManager(minecraftserver);
			this.playerConnection = new NullNetServerHandler(minecraftserver, manager, this);
			manager.a(this.playerConnection);
		}
		catch(Exception e)
		{
		}

		this.bb().b(GenericAttributes.b).setValue(16);
		iteminworldmanager.setGameMode(EnumGamemode.SURVIVAL);
		this.noDamageTicks = 1;
		this.W = 1;
		this.fauxSleeping = true;
		this.m_navigation = new PlayerNavigation(this, this.world);
		this.m_senses = new PlayerSenses(this);
		this.m_controllerJump = new PlayerControllerJump(this);
		this.m_controllerMove = new PlayerControllerMove(this);
		this.m_controllerLook = new PlayerControllerLook(this);
	}

	public RemotePlayerEntity(MinecraftServer minecraftserver, WorldServer world, GameProfile profile, PlayerInteractManager iteminworldmanager, RemoteEntity inEntity)
	{
		this(minecraftserver, world, profile, iteminworldmanager);
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
	public void h()
	{
		this.i();

		if(this.noDamageTicks > 0)
			this.noDamageTicks--;

		//Taken from Citizens2#EntityHumanNPC.java#129 - #138 - slightly modified.
		if(Math.abs(motX) < 0.001F && Math.abs(motY) < 0.001F && Math.abs(motZ) < 0.001F)
			motX = motY = motZ = 0;

		this.updateControllers();
		this.applyMovement();
		//End Citizens

		if(this.getRemoteEntity() != null)
			this.getRemoteEntity().getMind().tick();
	}

	private void updateControllers()
	{
		//Taken from Citizens2#NMS.java#259 - #262
		getControllerMove().c();
		getControllerLook().a();
		getControllerJump().b();
		getNavigation().f();
		//End Citizens
	}

	private void applyMovement()
	{
		if(this.m_remoteEntity.isStationary())
			return;

		if(this.bc)
		{
			boolean inLiquid = K() || M();
			if(inLiquid)
				this.motY += 0.04;
			else if(this.onGround && this.m_jumpTicks == 0)
			{
				this.be();
				this.m_jumpTicks = 10;
			}
		}
		else
			this.m_jumpTicks = 0;

		this.bd *= 0.98F;
		this.be *= 0.98F;
		this.bf *= 0.9F;

		this.e(this.bd, this.be);
		this.getRemoteEntity().setYaw(this.yaw);
		this.getRemoteEntity().setHeadYaw(this.yaw);
	}

	@Override
	public void setupStandardGoals()
	{
		this.getRemoteEntity().getMind().addMovementDesires(getDefaultMovementDesires());
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
	public void e(float inXMotion, float inZMotion)
	{
		float[] motion = new float[] { inXMotion, inZMotion, (float)this.motY };
		if(this.m_remoteEntity.getMind().hasBehavior(RideBehavior.class))
			this.m_remoteEntity.getMind().getBehavior(RideBehavior.class).ride(motion);

		this.motY = (double)motion[2];
		super.e(motion[0], motion[1]);
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
	public boolean a(EntityHuman entity)
	{
		if(this.getRemoteEntity() == null)
			return super.a(entity);

		if(!(entity.getBukkitEntity() instanceof Player))
			return super.a(entity);

		return ((RemoteBaseEntity)this.m_remoteEntity).onInteract((Player)entity.getBukkitEntity()) && super.a(entity);
	}

	@Override
	public void die(DamageSource damagesource)
	{
		((RemoteBaseEntity)this.m_remoteEntity).onDeath();
		super.die(damagesource);
	}

	public EntityLiving getGoalTarget()
	{
		return this.m_target;
	}

	public void setGoalTarget(EntityLiving inEntity)
	{
		this.m_target = inEntity;
	}

	@Override
	public boolean n(Entity inEntity)
	{
		this.attack(inEntity);
		return super.n(inEntity);
	}

	void updateSpawn()
	{
		PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn(this);
		for(Player player : this.getBukkitEntity().getLocation().getWorld().getPlayers())
		{
			WorldUtilities.sendPacketToPlayer(player, packet);
		}
	}

	public static DesireItem[] getDefaultMovementDesires()
	{
		return new DesireItem[] { new DesireItem(new DesireSwim(), 0) };
	}

	public static DesireItem[] getDefaultTargetingDesires()
	{
		return new DesireItem[0];
	}

	public Navigation getNavigation()
	{
		return this.m_navigation;
	}

	public ControllerLook getControllerLook()
	{
		return this.m_controllerLook;
	}

	public ControllerMove getControllerMove()
	{
		return this.m_controllerMove;
	}

	public ControllerJump getControllerJump()
	{
		return this.m_controllerJump;
	}

	public EntitySenses getEntitySenses()
	{
		return this.m_senses;
	}
}