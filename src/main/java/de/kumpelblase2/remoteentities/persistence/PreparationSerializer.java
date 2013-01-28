package de.kumpelblase2.remoteentities.persistence;

import de.kumpelblase2.remoteentities.api.thinking.Desire;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireLookAtNearest;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import de.kumpelblase2.remoteentities.CreateEntityContext;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.RemoteEntities;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

import java.lang.reflect.Constructor;
import java.util.HashMap;

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
    public Desire createDesireForEntity(DesireData inData, RemoteEntity entity)
    {
        Class desireClass = null;

        try {
            desireClass = Class.forName(inData.name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        EntityManager manager = RemoteEntities.getManagerOfPlugin(this.m_plugin.getName());
        Object[] parameters = new Object[inData.constructionData.length];
        int index = 0;

        ParameterData parameterData[] = ConstructorSerializer.constructionalsFromArrayForEntity(inData.constructionData, entity);

        Class[] classCorrespondence = new Class[parameterData.length];

        for (ParameterData pData : parameterData) {
            classCorrespondence[index] = (Class)pData.type;
            parameters[index] = pData.value;
            index++;
        }

        System.out.println(DesireLookAtNearest.class.getConstructors()[1].getGenericParameterTypes());

        Constructor desireConstructor = null;
        try {
            desireConstructor = desireClass.getConstructor(classCorrespondence);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            return (Desire)desireConstructor.newInstance(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
