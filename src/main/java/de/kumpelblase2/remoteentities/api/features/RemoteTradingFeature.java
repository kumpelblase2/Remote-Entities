package de.kumpelblase2.remoteentities.api.features;

import java.util.*;
import net.minecraft.server.v1_6_R2.MerchantRecipe;
import net.minecraft.server.v1_6_R2.MerchantRecipeList;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_6_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.nms.VirtualMerchant;
import de.kumpelblase2.remoteentities.persistence.*;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;
import de.kumpelblase2.remoteentities.utilities.ReflectionUtil;

@IgnoreSerialization
public class RemoteTradingFeature extends RemoteFeature implements TradingFeature, Listener
{
	protected List<Player> m_tradingPlayers;
	@SerializeAs(pos = 2)
	protected List<TradeOffer> m_offerings;
	protected final VirtualMerchant m_merchant;
	@SerializeAs(pos = 1)
	protected String m_name;
	protected MerchantRecipeList m_recipeList;

	public RemoteTradingFeature(RemoteEntity inEntity)
	{
		this(inEntity, new ArrayList<TradeOffer>());
	}

	public RemoteTradingFeature(RemoteEntity inEntity, String inName)
	{
		this(inEntity, inName, new ArrayList<TradeOffer>());
	}

	public RemoteTradingFeature(RemoteEntity inEntity, List<TradeOffer> inOfferings)
	{
		this(inEntity, "Trade", inOfferings);
	}

	public RemoteTradingFeature(RemoteEntity inEntity, String inName, List<TradeOffer> inOfferings)
	{
		super("TRADING", inEntity);
		this.m_offerings = inOfferings;
		this.m_tradingPlayers = new ArrayList<Player>();
		this.m_merchant = new VirtualMerchant(this);
		this.m_name = inName;
	}

	@Override
	public void onAdd()
	{
		Bukkit.getPluginManager().registerEvents(this, this.m_entity.getManager().getPlugin());
		this.populateRecipeList();
	}

	@Override
	public void onRemove()
	{
		InventoryCloseEvent.getHandlerList().unregister(this);
	}

	@Override
	public String getTradeName()
	{
		return this.m_name;
	}

	@Override
	public void setTradeName(String inName)
	{
		this.m_name = inName;
	}

	@Override
	public void openFor(Player inPlayer)
	{
		if(this.m_offerings.size() == 0)
			return;

		this.m_tradingPlayers.add(inPlayer);
		((CraftPlayer)inPlayer).getHandle().openTrade(this.m_merchant, this.getTradeName());
	}

	@Override
	public void closeFor(Player inPlayer)
	{
		this.m_tradingPlayers.remove(inPlayer);
	}

	@Override
	public List<Player> getTradingPlayers()
	{
		return this.m_tradingPlayers;
	}

	@Override
	public List<TradeOffer> getOfferings()
	{
		return this.m_offerings;
	}

	@Override
	public void addOffer(ItemStack inOffering, ItemStack inCost)
	{
		this.addOffer(new TradeOffer(inOffering, inCost));
	}

	@Override
	public void addOffer(TradeOffer inOffer)
	{
		this.m_offerings.add(inOffer);
		if(this.m_recipeList != null)
			this.populateRecipeList();
	}

	@Override
	public void removeOffer(TradeOffer inOffer)
	{
		Iterator it = this.m_recipeList.iterator();
		while(it.hasNext())
		{
			MerchantRecipe recipe = (MerchantRecipe)it.next();
			if(isSameOffer(inOffer, recipe))
				it.remove();
		}
		this.m_offerings.remove(inOffer);
	}

	@Override
	public void removeOffers(ItemStack inForItem)
	{
		Iterator<TradeOffer> it = this.m_offerings.iterator();
		while(it.hasNext())
		{
			TradeOffer next = it.next();
			if(next.getCost().equals(inForItem))
			{
				MerchantRecipe recipe = this.getRecipeFromOffer(next);
				this.m_recipeList.remove(recipe);
				it.remove();
			}
		}
	}

	@Override
	public void useOffer(TradeOffer inOffer)
	{
		inOffer.takeout();
		if(!inOffer.isInStore())
			this.removeOffer(inOffer);
	}

	public MerchantRecipeList getRecipeList()
	{
		return this.m_recipeList;
	}

	public TradeOffer getOfferFromRecipe(MerchantRecipe inRecipe)
	{
		for(TradeOffer offer : this.m_offerings)
		{
			if(isSameOffer(offer, inRecipe))
				return offer;
		}

		return null;
	}

	public MerchantRecipe getRecipeFromOffer(TradeOffer inOffer)
	{
		for(Object o : this.m_recipeList)
		{
			MerchantRecipe recipe = (MerchantRecipe)o;
			if(isSameOffer(inOffer, recipe))
				return recipe;
		}

		return null;
	}

	@Override
	public ParameterData[] getSerializableData()
	{
		return ReflectionUtil.getParameterDataForClass(this).toArray(new ParameterData[0]);
	}

	private void populateRecipeList()
	{
		this.m_recipeList = new MerchantRecipeList();
		for(TradeOffer offer : this.m_offerings)
		{
			MerchantRecipe recipe = new MerchantRecipe(CraftItemStack.asNMSCopy(offer.getCost()), (offer.getSecondCost() != null ? CraftItemStack.asNMSCopy(offer.getSecondCost()) : null), CraftItemStack.asNMSCopy(offer.getResult()));
			recipe.a(-7); //reset to 0
			if(offer.getRemainingUses() == -1)
				recipe.a(2);
			else
				recipe.a(offer.getRemainingUses());

			this.m_recipeList.a(recipe);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClose(InventoryCloseEvent event)
	{
		if(!this.m_tradingPlayers.contains(event.getPlayer()))
			return;

		this.closeFor((Player)event.getPlayer());
	}

	public static boolean isSameOffer(TradeOffer inOffer, MerchantRecipe inRecipe)
	{
		if(!NMSUtil.isAboutEqual(inRecipe.getBuyItem1(), CraftItemStack.asNMSCopy(inOffer.getCost())))
			return false;

		if((inOffer.getSecondCost() == null && inRecipe.getBuyItem2() != null) || (inOffer.getSecondCost() != null && inRecipe.getBuyItem2() == null))
			return false;

		if(inRecipe.getBuyItem2() != null && inOffer.getSecondCost() != null && !NMSUtil.isAboutEqual(inRecipe.getBuyItem2(), CraftItemStack.asNMSCopy(inOffer.getSecondCost())))
			return false;

		if(!NMSUtil.isAboutEqual(inRecipe.getBuyItem3(), CraftItemStack.asNMSCopy(inOffer.getResult())))
			return false;

		return true;
	}
}