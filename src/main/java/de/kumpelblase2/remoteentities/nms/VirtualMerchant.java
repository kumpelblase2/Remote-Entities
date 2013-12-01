package de.kumpelblase2.remoteentities.nms;

import net.minecraft.server.v1_7_R1.*;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import de.kumpelblase2.remoteentities.api.features.RemoteTradingFeature;

public class VirtualMerchant implements IMerchant
{
	protected final RemoteTradingFeature m_feature;

	public VirtualMerchant(RemoteTradingFeature inFeature)
	{
		this.m_feature = inFeature;
	}

	@Override
	public void a_(EntityHuman inEntityHuman)
	{
	}

	@Override
	public EntityHuman b()
	{
		return ((CraftPlayer)this.m_feature.getTradingPlayers().get(0)).getHandle();
	}

	@Override
	public MerchantRecipeList getOffers(EntityHuman inEntityHuman)
	{
		return this.m_feature.getRecipeList();
	}

	@Override
	public void a(MerchantRecipe inMerchantRecipe)
	{
		this.m_feature.useOffer(this.m_feature.getOfferFromRecipe(inMerchantRecipe));
	}

	@Override
	public void a_(ItemStack inItemStack)
	{

	}
}