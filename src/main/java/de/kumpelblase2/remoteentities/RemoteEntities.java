package de.kumpelblase2.remoteentities;

import java.io.*;
import java.net.URL;
import java.util.*;
import javassist.*;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.exceptions.PluginNotEnabledException;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

public class RemoteEntities extends JavaPlugin
{
	private final Map<String, EntityManager> m_managers = new HashMap<String, EntityManager>();
	private static RemoteEntities s_instance;
	private static String COMPATIBLE_VERSION = "1.6.4";
	private static String COMPATIBLE_REVISION = "v1_6_R3";
	private static final String VERSION_FILE = "http://repo.infinityblade.de/re_versions.txt";
	private static String MINECRAFT_REVISION;
	private final ClassPool m_pool = new ClassPool();
	private final Set<String> m_classesToLoad = new HashSet<String>();

	@Override
	public void onEnable()
	{
		this.checkConfig();
		this.checkClasses();
		MINECRAFT_REVISION = ReflectionUtil.getMinecraftRevision();
		String minecraftversion = this.getPresentMinecraftVersion();
		VersionCheck:
		{
			if(!minecraftversion.equals(COMPATIBLE_VERSION) && !MINECRAFT_REVISION.equals(COMPATIBLE_REVISION))
			{
				this.getLogger().severe("Invalid minecraft version for remote entities (Present: " + minecraftversion + ").");
				if(this.isAutoUpdateEnabled())
				{
					if(this.isVersionOnline(MINECRAFT_REVISION))
					{
						this.getLogger().info("Version found online for MC " + minecraftversion + ". Initializing updating process.");
						this.updateTo(MINECRAFT_REVISION, minecraftversion);
						break VersionCheck;
					}
					else
						this.getLogger().info("No new version found online.");
				}

				this.getLogger().severe("Disabling plugin to prevent issues.");
				Bukkit.getPluginManager().disablePlugin(this);
				return;
			}
			else
			{
				try
				{
					ClassPath cp = this.m_pool.insertClassPath(new File(this.getDataFolder(), "sources/").getAbsolutePath());
					for(String className : this.m_classesToLoad)
					{
						CtClass ctclass = this.m_pool.makeClass(cp.find(className.substring(className.lastIndexOf(".") + 1, className.length())).openStream());
						ctclass.toClass();
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		s_instance = this;
		RemoteEntityType.update();
		Bukkit.getPluginManager().registerEvents(new DisableListener(), this);
	}

	private void checkClasses()
	{
		if(!this.isAutoUpdateEnabled())
			return;

		File sources = new File(this.getDataFolder(), "sources/");
		if(!sources.exists())
		{
			sources.mkdirs();
			try
			{
				BufferedReader reader = new BufferedReader(new InputStreamReader(this.getResource("re_classes.txt")));
				String line;
				while((line = reader.readLine()) != null)
				{
					this.m_classesToLoad.add(line);
				}

				for(String className : this.m_classesToLoad)
				{
					this.m_pool.get(className).writeFile(sources.getAbsolutePath());
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private void checkConfig()
	{
		this.getConfig().set("autoUpdateSources", this.getConfig().get("autoUpdateSources", false));
		COMPATIBLE_REVISION = this.getConfig().getString("COMPATIBLE_REVISION", "1_6_R3");
		COMPATIBLE_VERSION = this.getConfig().getString("COMPATIBLE_VERSION", "1.6.4");
		this.saveConfig();
	}

	public boolean isAutoUpdateEnabled()
	{
		return this.getConfig().getBoolean("autoUpdateSources");
	}

	public void setAutoUpdateEnabled(boolean inEnabled)
	{
		this.getConfig().set("autoUpdateSources", inEnabled);
		this.getLogger().info("RemoteEntities auto updating has been enabled.");
	}

	@Override
	public void onDisable()
	{
		for(EntityManager manager : m_managers.values())
		{
			manager.despawnAll(DespawnReason.PLUGIN_DISABLE);
			manager.unregisterEntityLoader();
		}
		s_instance = null;
	}

	private String getPresentMinecraftVersion()
	{
		String fullVersion = Bukkit.getServer().getVersion();
		String[] split = fullVersion.split("MC: ");
		split = split[1].split("\\)");

		return split[0];
	}

	/**
	 * Creates a manager for your plugin
	 *
	 * @param inPlugin	plugin using that manager
	 * @return			instance of a manager
	 */
	public static EntityManager createManager(Plugin inPlugin) throws PluginNotEnabledException
	{
		if(getInstance() == null)
			throw new PluginNotEnabledException();

		return createManager(inPlugin, false);
	}

	/**
	 * Creates a manager for your plugin
	 * You can also specify whether despawned entities should be removed or not
	 *
	 * @param inPlugin				plugin using that manager
	 * @param inRemoveDespawned		automatically removed despawned entities
	 * @return						instance of a manager
	 */
	public static EntityManager createManager(Plugin inPlugin, boolean inRemoveDespawned)
	{
		if(getInstance() == null)
			throw new PluginNotEnabledException();

		EntityManager manager = new EntityManager(inPlugin, inRemoveDespawned);
		registerCustomManager(manager, inPlugin.getName());
		return manager;
	}

	/**
	 * Adds custom created manager to internal map
	 *
	 * @param inManager custom manager
	 * @param inName	name of the plugin using it
	 */
	public static void registerCustomManager(EntityManager inManager, String inName)
	{
		if(getInstance() == null)
			return;

		getInstance().m_managers.put(inName, inManager);
	}

	/**
	 * Gets the manager of a specific plugin
	 *
	 * @param inName	name of the plugin
	 * @return			EntityManager of that plugin
	 */
	public static EntityManager getManagerOfPlugin(String inName)
	{
		if(getInstance() == null)
			return null;

		return getInstance().m_managers.get(inName);
	}

	/**
	 * Checks if a specific plugin has registered a manager
	 *
	 * @param inName	name of the plugin
	 * @return			true if the given plugin has a manager, false if not
	 */
	public static boolean hasManagerForPlugin(String inName)
	{
		return getInstance() != null && getInstance().m_managers.containsKey(inName);
	}

	/**
	 * Gets the instance of the RemoteEntities plugin
	 *
	 * @return RemoteEntities
	 */
	public static RemoteEntities getInstance()
	{
		return s_instance;
	}

	/**
	 * Checks if the given entity is a RemoteEntity created by any manager.
	 *
	 * @param inEntity	entity to check
	 * @return			true if it is a RemoteEntity, false if not
	 */
	public static boolean isRemoteEntity(LivingEntity inEntity)
	{
		if(getInstance() == null)
			return false;

		for(EntityManager manager : getInstance().m_managers.values())
		{
			if(manager.isRemoteEntity(inEntity))
				return true;
		}
		return false;
	}

	/**
	 * Gets the RemoteEntity from a given entity which can be created by any manager.
	 *
	 * @param inEntity	entity
	 * @return			RemoteEntity
	 */
	public static RemoteEntity getRemoteEntityFromEntity(LivingEntity inEntity)
	{
		if(getInstance() == null)
			return null;

		for(EntityManager manager : getInstance().m_managers.values())
		{
			RemoteEntity entity = manager.getRemoteEntityFromEntity(inEntity);
			if(entity != null)
				return entity;
		}
		return null;
	}

	/**
	 * Returns the minecraft version this version of remote entities is compatible with.
	 *
	 * @return  A string representing the version
	 */
	public static String getCompatibleMinecraftVersion()
	{
		return COMPATIBLE_VERSION;
	}

	/**
	 * Gets the current revision of the minecraft server that is used by bukkit.
	 * These normally come in the format x_y_Rz .
	 * Whereas x and y are the first two numbers of the minecraft version and z shows how many times the internal minecraft server code has been changed.
	 *
	 * @return  Revision string
	 */
	public static String getMinecraftRevision()
	{
		return MINECRAFT_REVISION;
	}

	private Set<String> getOnlineVersions()
	{
		Scanner s = null;
		Set<String> versions = new HashSet<String>();
		try
		{
			s = new Scanner(new URL(VERSION_FILE).openStream());
			while(s.hasNextLine())
			{
				versions.add(s.nextLine());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(s != null)
				s.close();
		}

		return versions;
	}

	public boolean isVersionOnline(String inVersion)
	{
		return this.getOnlineVersions().contains(inVersion);
	}

	public void updateTo(String inRevision, String inVersion)
	{
		Scanner s = null;
		try
		{
			s = new Scanner(new URL("http://repo.infinityblade.de/re_classes.txt").openStream());
			List<String> classes = new ArrayList<String>();
			while(s.hasNextLine())
			{
				classes.add(s.nextLine());
			}

			ClassPath cp = new URLClassPath("repo.infinityblade.de", 80, "/re/" + inRevision + "/", "de.kumpelblase2.remoteentities.");
			this.m_pool.insertClassPath(cp);
			for(String className : classes)
			{
				if(className != null && className.length() > 0)
				{
					CtClass ctclass = this.m_pool.get(className);
					ctclass.writeFile(new File(this.getDataFolder(), "sources").getAbsolutePath());
				}
			}

			this.getConfig().set("COMPATIBLE_REVISION", inRevision);
			this.getConfig().set("COMPATIBLE_VERSION", inVersion);
			this.saveConfig();

			this.getLogger().info("Loaded new classes. Please restart the server to apply changes.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(s != null)
				s.close();
		}
	}

	@Override
	public boolean onCommand(CommandSender inSender, Command inCommand, String inLabel, String[] inArgs)
	{
		if(inCommand.getName().equals("remoteentities"))
		{
			if(inArgs.length == 0)
				inSender.sendMessage("Please provide arguments.");
			else
			{
				if(inArgs[0].equalsIgnoreCase("rebuild"))
				{
					if(inSender instanceof ConsoleCommandSender)
						this.updateTo(MINECRAFT_REVISION, COMPATIBLE_VERSION);
					else
						inSender.sendMessage("Only the console can do that.");
				}
			}
		}
		return true;
	}

	class DisableListener implements Listener
	{
		@EventHandler
		public void onPluginDisable(PluginDisableEvent event)
		{
			EntityManager manager = RemoteEntities.getManagerOfPlugin(event.getPlugin().getName());
			if(manager != null)
			{
				manager.despawnAll(DespawnReason.PLUGIN_DISABLE);
				manager.unregisterEntityLoader();
			}
		}
	}
}