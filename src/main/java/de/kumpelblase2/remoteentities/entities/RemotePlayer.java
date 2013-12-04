package de.kumpelblase2.remoteentities.entities;

import java.util.UUID;
import net.minecraft.server.v1_7_R1.*;
import net.minecraft.util.com.google.common.base.Charsets;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.DespawnReason;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import de.kumpelblase2.remoteentities.api.events.RemoteEntitySpawnEvent;

public class RemotePlayer extends RemoteAttackingBaseEntity<Player>
{
	protected String m_name;

	public RemotePlayer(int inID, String inName, EntityManager inManager)
	{
		this(inID, inName, null, inManager);
	}

	public RemotePlayer(int inID, String inName, RemotePlayerEntity inEntity, EntityManager inManager)
	{
		super(inID, RemoteEntityType.Human, inManager);
		this.m_name = inName;
		this.m_entity = inEntity;
	}

	@Override
	public String getName()
	{
		return this.m_name;
	}

	@Override
	public void setName(String inName)
	{
		this.m_name = inName;
		Location loc = this.getBukkitEntity().getLocation();
		this.despawn(DespawnReason.NAME_CHANGE);
		this.spawn(loc);
	}

	@Override
	public void spawn(Location inLocation)
	{
		if(this.isSpawned())
			return;

		RemoteEntitySpawnEvent event = new RemoteEntitySpawnEvent(this, inLocation);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;

		inLocation = event.getSpawnLocation();
		WorldServer worldServer = ((CraftWorld)inLocation.getWorld()).getHandle();
		UUID uuid = UUID.nameUUIDFromBytes(("NPC:" + this.getID() + this.getName()).getBytes(Charsets.UTF_8));
		GameProfile profile = new GameProfile(uuid.toString().replaceAll("-", ""), this.getName());
		this.m_entity = new RemotePlayerEntity(worldServer.getMinecraftServer(), worldServer, profile, new PlayerInteractManager(worldServer), this);
		worldServer.addEntity(m_entity);
		this.m_entity.getBukkitEntity().teleport(inLocation);
		this.m_entity.world.players.remove(this.m_entity);
		this.getBukkitEntity().setMetadata("remoteentity", new FixedMetadataValue(this.m_manager.getPlugin(), this));
		((RemotePlayerEntity)this.m_entity).updateSpawn();
		if(!inLocation.getBlock().getRelative(BlockFace.DOWN).isEmpty())
			this.m_entity.onGround = true;

		if(this.m_speed != -1)
			this.setSpeed(this.m_speed);
		else
			this.setSpeed(1d);

		if(this.m_speedModifier != null)
		{
			this.addSpeedModifier(this.m_speedModifier.d(), (this.m_speedModifier.c() == 0));
			this.m_speedModifier = null;
		}
	}

	@Override
	public void setSpeed(double inSpeed)
	{
		super.setSpeed(inSpeed);
		((EntityPlayer)this.m_entity).abilities.walkSpeed = (float)inSpeed;
	}

	@Override
	public String getNativeEntityName()
	{
		return "Player";
	}

	/**
	 * Tries to place the npc in a bed at given location.
	 *
	 * @param inLocation	Location the bed is present.
	 * @return				True if it was possible, false if not
	 */
	public boolean enterBed(Location inLocation)
	{
		this.teleport(inLocation);
		return ((EntityHuman)this.getHandle()).a((int)inLocation.getX(), (int)inLocation.getY(), (int)inLocation.getZ()) == EnumBedResult.OK;
	}

	/**
	 * Leaves the bed the npc currently is in.
	 */
	public void leaveBed()
	{
		((EntityHuman)this.getHandle()).a(true, true, false);
	}

	/**
	 * Checks if the npc is currently in a bed.
	 *
	 * @return	true if he is, false if not
	 */
	public boolean isSleeping()
	{
		return this.getHandle().isSleeping();
	}

	/**
	 * Send the arm swing animation to nearby players.
	 */
	public void doArmSwing()
	{
		((WorldServer)this.getHandle().world).getTracker().a(this.getHandle(), new PacketPlayOutAnimation(this.getHandle(), 0));
	}

	/**
     * Send the hurt animation to nearby players.
     */
	/*public void fakeDamage() TODO do we still need this?
	{
		((WorldServer)this.getHandle().world).getTracker().a(this.getHandle(), new Packet18ArmAnimation(this.getHandle(), 2));
	}*/

	@Override
	protected void setupSounds()
	{
	}
}