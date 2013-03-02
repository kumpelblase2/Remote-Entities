package de.kumpelblase2.remoteentities.examples;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.RemoteEntities;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import de.kumpelblase2.remoteentities.persistence.serializers.YMLSerializer;

public class ExampleMain extends JavaPlugin
{
	private EntityManager npcManager;
	
	public void onEnable()
	{
		Bukkit.getPluginManager().registerEvents(this, this);
		this.npcManager = RemoteEntities.createManager(this);
		
		//First we register the serializer that should be used
		//In this case we use the YML serializer, but you can also use your own as well as the json serializer
		this.npcManager.setEntitySerializer(new YMLSerializer(this));
		
		//If we want to save all current entities, it's pretty easy:
		this.npcManager.saveEntities();
		
		//Some serializers allow single entities to be saved, i.e. the YML serializer
		//But first, we need to create an entitiy.
		RemoteEntity entity = this.npcManager.createEntity(RemoteEntityType.Zombie, Bukkit.getWorld("world").getSpawnLocation());
		//Now we can just call the save method. Keep in mind that it will not work when the serializer doesn't support single entity serialization
		entity.save();
		
		//To load the entities we saved, you can just do this:
		this.npcManager.loadEntities();
		//Now all the entities should be back like you never removed them
	}
}