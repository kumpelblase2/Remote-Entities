package de.kumpelblase2.remoteentities.persistence.serializers;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import de.kumpelblase2.remoteentities.RemoteEntities;
import de.kumpelblase2.remoteentities.persistence.EntityData;
import de.kumpelblase2.remoteentities.persistence.PreparationSerializer;

public class YMLSerializer extends PreparationSerializer
{
	public YMLSerializer(Plugin inPlugin)
	{
		super(inPlugin);
		ConfigurationSerialization.registerClass(EntityData.class);
	}

	@Override
	public boolean save(EntityData[] inData)
	{
		try
		{
			File fileFolder = new File(RemoteEntities.getInstance().getDataFolder(), this.m_plugin.getName());
			if(!fileFolder.exists())
			{
				if(!fileFolder.mkdirs())
					return false;
			}
			
			File ymlFile = new File(fileFolder, "entities.yml");
			if(!ymlFile.exists())
			{
				if(!ymlFile.createNewFile())
					return false;
			}
		
			FileConfiguration conf = YamlConfiguration.loadConfiguration(ymlFile);
			conf.set("entities", inData);
			conf.save(ymlFile);
			
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	@Override
	public EntityData[] loadData()
	{
		File ymlFile = new File(RemoteEntities.getInstance().getDataFolder(), this.m_plugin.getName() + File.separator + "entities.yml");
		if(!ymlFile.exists())
			return new EntityData[0];

		FileConfiguration conf = YamlConfiguration.loadConfiguration(ymlFile);
		
		return conf.getList("entities").toArray(new EntityData[0]);
	}
}
