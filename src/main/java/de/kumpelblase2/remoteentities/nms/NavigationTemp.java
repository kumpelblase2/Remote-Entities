package de.kumpelblase2.remoteentities.nms;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import net.minecraft.server.v1_5_R3.Navigation;

public class NavigationTemp extends Navigation
{
	private final RemoteEntity m_entity;
	
	public NavigationTemp(RemoteEntity inEntity, float arg2)
	{
		super(inEntity.getHandle(), inEntity.getHandle().world, arg2);
		this.m_entity = inEntity;
	}
	
	public void g()
	{
		this.m_entity.stopMoving();
	}
	
	public boolean f()
	{
		return this.m_entity.getPathfinder().hasPath();
	}
	
	public void e()
	{
		this.m_entity.getPathfinder().update();
	}
}
