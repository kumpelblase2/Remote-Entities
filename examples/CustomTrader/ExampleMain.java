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
		RemoteEntity entity = npcManager.createNamedEntity(RemoteEntityType.Human, inEvent.getPlayer().getLocation(), "Lonely Trader");

		//First of all, we create the feature with a store title. In this case, I named the store 'custom store'.
		RemoteTradingFeature feature = new RemoteTradingFeature(entity, "Custom store");

		//Then I add an offer. This time, the trader will sell 1 paper and it costs 1 stone.
		feature.addOffer(new ItemStack(Material.PAPER), new ItemStack(Material.STONE));

		//We can also specify how often this offer can be used. In order to do that, we create a TraderOffer, where we can specify the usable amount.
		//This means that this offer can only be used twice. If player 1 buys it once and player 2 buys it also one time, it will be gone for player 3.
		feature.addOffer(new TradeOffer(new ItemStack(Material.FEATHER), new ItemStack(Material.STONE), 2));

		//We can also add lore to the item if we want to. Here, I add some text to make it a bit more interesting.
		ItemStack customItem = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta meta = customItem.getItemMeta();
		meta.setDisplayName("BACON MAKER");
		meta.setLore(new ArrayList<String>(Arrays.asList("GET THAT BACON!")));
		customItem.setItemMeta(meta);

		//And just like we did earlier, I add it to the feature.
		feature.addOffer(new TradeOffer(customItem, new ItemStack(Material.FEATHER), 1));

		//Lastly, don't forget to add the feature to the entity.
		entity.getFeatures().addFeature(feature);
	}
}