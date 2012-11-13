package de.kumpelblase2.remoteentities.examples;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.RemoteEntities;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import de.kumpelblase2.remoteentities.api.features.RemoteTamingFeature;
import de.kumpelblase2.remoteentities.api.features.TamingFeature;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireFollowTamer;

public class ExampleMain extends JavaPlugin implements Listener
{
	private EntityManager npcManager;
	
	public void onEnable()
	{
		Bukkit.getPluginManager().registerEvents(this, this);
		this.npcManager = RemoteEntities.createManager(this);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent inEvent) throws Exception
	{
		RemoteEntity entity = npcManager.createNamedEntity(RemoteEntityType.Human, inEvent.getPlayer().getLocation(), "test");
		TamingFeature feature = new RemoteTamingFeature(entity);
		feature.tame(inEvent.getPlayer());
		entity.getFeatures().addFeature(feature);
		entity.getMind().addMovementDesire(new DesireFollowTamer(entity, 5, 15), entity.getMind().getHighestMovementPriority() + 1);
	}
}