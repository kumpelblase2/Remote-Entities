package de.kumpelblase2.remoteentities.persistence;

import de.kumpelblase2.remoteentities.api.thinking.Behavior;
import de.kumpelblase2.remoteentities.api.thinking.Desire;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireLookAtNearest;
import net.minecraft.server.v1_4_R1.EntityHuman;
import net.minecraft.server.v1_4_R1.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import de.kumpelblase2.remoteentities.CreateEntityContext;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.RemoteEntities;
import de.kumpelblase2.remoteentities.api.RemoteEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

//        Behavior[] behaviors = new Behavior[inData.behaviors.length];
//
//        int index = 0;
//        for (BehaviorData behaviorData : inData.behaviors) {
//            behaviors[index] = this.createBehaviorForEntity(behaviorData, entity);
//
//            index++;
//        }
//
//        contex.withBehaviors(behaviors);

        RemoteEntity entity = contex.create();
        for (BehaviorData behaviorData : inData.behaviors) {
            entity.getMind().addBehaviour(this.createBehaviorForEntity(behaviorData, entity));
        }


		return entity;
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

        ParameterData parameterData[] = ConstructorSerializer.constructionalsFromArrayForEntity(inData.constructionData, entity);
        Class[] classes = new Class[1];
        classes[0] = ParameterData[].class;

        try {
            Constructor constructor = desireClass.getConstructor(classes);
            return (Desire)constructor.newInstance((Object)parameterData);
        } catch (Exception e) {
            System.out.println("Failed for: " + desireClass.getCanonicalName());
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public Behavior createBehaviorForEntity(BehaviorData inData, RemoteEntity entity)
    {
        try {
            Class behaviorClass = Class.forName(inData.canonicallyWrittenClass);

            ParameterData parameterData[] = ConstructorSerializer.constructionalsFromArrayForEntity(inData.parameterData, entity);
            System.out.println("Parameter info" + parameterData[0].value.getClass());
            Constructor constructor = behaviorClass.getConstructor(ParameterData[].class);
            return (Behavior)constructor.newInstance((Object)parameterData);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchMethodException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;
    }
}
