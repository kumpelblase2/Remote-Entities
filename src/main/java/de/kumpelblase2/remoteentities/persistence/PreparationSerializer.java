package de.kumpelblase2.remoteentities.persistence;

import org.bukkit.plugin.Plugin;
import de.kumpelblase2.remoteentities.CreateEntityContext;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.RemoteEntities;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

public abstract class PreparationSerializer implements IEntitySerializer
{
	protected final Plugin m_plugin;
	
	public PreparationSerializer(Plugin inPlugin)
	{
		this.m_plugin = inPlugin;
	}
	
	@Override
	public EntityData prepare(RemoteEntity inEntity)
	{
		return new EntityData(inEntity);
	}

	@Override
	public RemoteEntity create(EntityData inData)
	{
		EntityManager manager = RemoteEntities.getManagerOfPlugin(this.m_plugin.getName());
		CreateEntityContext contex = manager.prepareEntity(inData.type);
		contex.withName(inData.name).atLocation(inData.location.toBukkitLocation()).asPushable(inData.pushable).asStationary(inData.stationary).withID(inData.id);
		contex.withSpeed(inData.speed);
		return contex.create();
	}
}
