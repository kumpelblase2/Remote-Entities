package de.kumpelblase2.remoteentities.persistence.serializers;

import org.bukkit.plugin.Plugin;
import de.kumpelblase2.remoteentities.CreateEntityContext;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.RemoteEntities;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireItem;
import de.kumpelblase2.remoteentities.persistence.BehaviorData;
import de.kumpelblase2.remoteentities.persistence.DesireData;
import de.kumpelblase2.remoteentities.persistence.EntityData;
import de.kumpelblase2.remoteentities.persistence.IEntitySerializer;

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
		CreateEntityContext context = manager.prepareEntity(inData.type);
		context.withName(inData.name).asPushable(inData.pushable).asStationary(inData.stationary).withID(inData.id);
		context.withSpeed(inData.speed);
		if(inData.location != null)
			context.atLocation(inData.location.toBukkitLocation());
		
		RemoteEntity entity = context.create();
		for(DesireData data : inData.movementDesires)
		{
			DesireItem item = data.create(entity);
			entity.getMind().addMovementDesire(item.getDesire(), item.getPriority());
		}
		
		for(DesireData data : inData.actionDesires)
		{
			DesireItem item = data.create(entity);
			entity.getMind().addTargetingDesire(item.getDesire(), item.getPriority());
		}
		
		for(BehaviorData data : inData.behaviors)
		{
			entity.getMind().addBehaviour(data.create(entity));
		}
		
		return entity;
	}
}