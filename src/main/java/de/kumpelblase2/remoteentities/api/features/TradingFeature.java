package de.kumpelblase2.remoteentities.api.features;

import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface TradingFeature extends Feature
{
	public String getTradeName();
	public void setTradeName(String inName);
	public void openFor(Player inPlayer);
	public void closeFor(Player inPlayer);
	public List<Player> getTradingPlayers();
	public List<TradeOffer> getOfferings();
	public void addOffer(ItemStack inOffering, ItemStack inCost);
	public void removeOffer(TradeOffer inOffer);
	public void removeOffers(ItemStack inForItem);
	public void useOffer(TradeOffer inOffer);
}