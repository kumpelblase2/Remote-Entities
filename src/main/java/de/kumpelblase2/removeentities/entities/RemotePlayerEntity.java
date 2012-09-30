package de.kumpelblase2.removeentities.entities;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.World;

public class RemotePlayerEntity extends EntityPlayer implements RemoteEntityHandle
{
	private RemoteEntity m_remoteEntity;
	
	public RemotePlayerEntity(MinecraftServer minecraftserver, World world, String s, ItemInWorldManager iteminworldmanager)
	{
		super(minecraftserver, world, s, iteminworldmanager);
	}
	
	public RemotePlayerEntity(MinecraftServer minecraftserver, World world, String s, ItemInWorldManager iteminworldmanager, RemoteEntity inEntity)
	{
		this(minecraftserver, world, s, iteminworldmanager);
		this.m_remoteEntity = inEntity;
	}

	@Override
	public RemoteEntity getRemoteEntity()
	{
		return this.m_remoteEntity;
	}
}
