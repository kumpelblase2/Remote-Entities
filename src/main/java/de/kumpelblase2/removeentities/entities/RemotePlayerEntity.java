package de.kumpelblase2.removeentities.entities;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EnumGamemode;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.World;
import de.kumpelblase2.removeentities.nms.NullNetServerHandler;
import de.kumpelblase2.removeentities.nms.RemoteEntityNetworkManager;
import de.kumpelblase2.removeentities.thinking.*;

public class RemotePlayerEntity extends EntityPlayer implements RemoteEntityHandle
{
	private RemoteEntity m_remoteEntity;
	private int m_lastBouncedId;
	private long m_lastBouncedTime;
	
	public RemotePlayerEntity(MinecraftServer minecraftserver, World world, String s, ItemInWorldManager iteminworldmanager)
	{
		super(minecraftserver, world, s, iteminworldmanager);
		iteminworldmanager.setGameMode(EnumGamemode.SURVIVAL);
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
		if (this.getRemoteEntity().getMind().canFeel() && (this.m_lastBouncedId != entity.id || System.currentTimeMillis() - this.m_lastBouncedTime > 1000) && this.getRemoteEntity().getMind().hasBehaviour("Touch")) {
			if(entity.getBukkitEntity().getLocation().distanceSquared(getBukkitEntity().getLocation()) <= 1)
			{
				((TouchBehaviour)this.getRemoteEntity().getMind().getBehaviour("Touch")).onTouch(entity.getBukkitEntity());
				this.m_lastBouncedTime = System.currentTimeMillis();
				this.m_lastBouncedId = entity.id;
			}
		}
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
}
