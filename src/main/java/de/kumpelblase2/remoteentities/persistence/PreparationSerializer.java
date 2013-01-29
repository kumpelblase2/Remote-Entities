package de.kumpelblase2.remoteentities.persistence;

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

        ParameterData parameterData[] = ConstructorSerializer.constructionalsFromArrayForEntity(inData.constructionData, entity);
        Class[] classes = new Class[1];
        classes[0] = ParameterData[].class;

        if (desireClass.getCanonicalName().equals(DesireLookAtNearest.class.getCanonicalName())) {
            for (ParameterData pdata : parameterData) {
                System.out.println(pdata.value.getClass());
            }
        }

        try {
            Constructor constructor = desireClass.getConstructor(classes);
            return (Desire)constructor.newInstance((Object)parameterData);
        } catch (Exception e) {
            System.out.println("Failed for: " + desireClass.getCanonicalName());
            e.printStackTrace();
        }


        return null;
    }
}
