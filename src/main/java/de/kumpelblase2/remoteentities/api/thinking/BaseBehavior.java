package de.kumpelblase2.remoteentities.api.thinking;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import org.bukkit.plugin.Plugin;

public abstract class BaseBehavior implements Behavior
{
	protected final String m_name;
	protected RemoteEntity m_entity;
	
	public BaseBehavior(RemoteEntity inEntity, String inName)
	{
		this.m_name = inName;
		this.m_entity = inEntity;
	}

//    public BaseBehavior(RemoteEntity entity, Plugin plugin)
//    {
//
//    }

    @Override
	public void run()
	{
	}

	@Override
	public String getName()
	{
		return this.m_name;
	}

	@Override
	public void onAdd()
	{
	}

	@Override
	public void onRemove()
	{
	}

	@Override
	public RemoteEntity getRemoteEntity()
	{
		return this.m_entity;
	}
}
