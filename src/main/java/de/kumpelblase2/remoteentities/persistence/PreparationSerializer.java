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
        System.out.println("Logging 0");

        EntityManager manager = RemoteEntities.getManagerOfPlugin(this.m_plugin.getName());
        Object[] constructors = new Object[inData.constructionData.length];
        int index = 0;

        System.out.println("Logging 1");
        ParameterData constructionals[] = ConstructorSerializer.constructionalsFromArrayForEntity(inData.constructionData, entity);

        Class[] classCorrespondence = new Class[constructionals.length];

        System.out.println("Logging 2");

        for (ParameterData parameterData : constructionals) {
             classCorrespondence[index] = (Class)constructionals[index].type;

            index++;
        }

        System.out.println("Logging 3");

        Constructor[] constructors1 = DesireLookAtNearest.class.getConstructors();

        System.out.println("Logging");
        System.out.println(constructors1);
        for (Constructor construct : constructors1) {
            System.out.println(construct);
            System.out.println(construct.getGenericParameterTypes());
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
