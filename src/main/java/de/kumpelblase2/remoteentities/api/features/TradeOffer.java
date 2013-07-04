package de.kumpelblase2.remoteentities.api.features;

import org.bukkit.inventory.ItemStack;

public class TradeOffer
{
	protected final ItemStack m_result;
	protected final ItemStack m_cost;
	protected final ItemStack m_cost2;

	protected int m_inStore;

	public TradeOffer(ItemStack inResult, ItemStack inCost)
	{
		this(inResult, inCost, null);
	}

	public TradeOffer(ItemStack inResult, ItemStack inCost, ItemStack inCost2)
	{
		this(inResult, inCost, inCost2, -1);
	}

	public TradeOffer(ItemStack inResult, ItemStack inCost, int inInStore)
	{
		this(inResult, inCost, null, inInStore);
	}

	public TradeOffer(ItemStack inResult, ItemStack inCost, ItemStack inCost2, int inInStore)
	{
		this.m_cost = inCost;
		this.m_result = inResult;
		this.m_cost2 = inCost2;
		this.m_inStore = inInStore;
	}

	public ItemStack getResult()
	{
		return this.m_result;
	}

	public ItemStack getCost()
	{
		return this.m_cost;
	}

	public ItemStack getSecondCost()
	{
		return this.m_cost2;
	}

	public boolean isInStore()
	{
		return this.m_inStore == -1 || this.m_inStore > 0;
	}

	public void takeout()
	{
		this.m_inStore = (this.m_inStore <= 0 ? this.m_inStore : this.m_inStore - 1);
	}

	public int getRemainingUses()
	{
		return this.m_inStore;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;

		if(!(o instanceof TradeOffer))
			return false;

		TradeOffer tradeOffer = (TradeOffer)o;

		if(!m_cost.equals(tradeOffer.m_cost))
			return false;

		if(!m_result.equals(tradeOffer.m_result))
			return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = m_result.hashCode();
		result = 31 * result + m_cost.hashCode();
		return result;
	}
}