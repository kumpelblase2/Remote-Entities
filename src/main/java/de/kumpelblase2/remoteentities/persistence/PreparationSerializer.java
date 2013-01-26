package de.kumpelblase2.remoteentities.persistence;

import de.kumpelblase2.remoteentities.api.thinking.Desire;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import de.kumpelblase2.remoteentities.CreateEntityContext;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.RemoteEntities;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

import java.lang.reflect.Constructor;

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

//        for (DesireData)

		return contex.create();
	}

    @Override
    public Desire create(DesireData inData)
    {
        Class desireClass = null;
        try {
            desireClass = Class.forName(inData.name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        EntityManager manager = RemoteEntities.getManagerOfPlugin(this.m_plugin.getName());
        Object[] constructors = new Object[inData.constructionData.length];
        int index = 0;

        for (Object object : inData.constructionData) {
            Object result = object;

            if (object instanceof String) {
                String string = (String)object;
                String entityKeyword = "EntityID = ";
                if (string.startsWith(entityKeyword)) {
                    int id = Integer.parseInt(string.substring(entityKeyword.length()));
                    RemoteEntity entity = manager.getRemoteEntityByID(id);
                    System.out.println(entity);
                    result = entity;
                }

            }

            constructors[index] = result;
            index++;
        }

        Class[] classCorrespondence = new Class[constructors.length];
        index = 0;

        for (Object object : constructors) {
            if (object instanceof RemoteEntity)
                classCorrespondence[index] = RemoteEntity.class;
            else
                classCorrespondence[index] = object.getClass();

            index++;
        }

        Constructor desireConstructor = null;
        try {
            desireConstructor = desireClass.getConstructor(classCorrespondence);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            return (Desire)desireConstructor.newInstance(constructors);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
