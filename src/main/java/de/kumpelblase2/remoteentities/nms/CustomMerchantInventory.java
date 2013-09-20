package de.kumpelblase2.remoteentities.nms;

import net.minecraft.server.v1_6_R3.InventoryMerchant;
import de.kumpelblase2.remoteentities.api.features.RemoteTradingFeature;

public class CustomMerchantInventory extends InventoryMerchant
{
	private final RemoteTradingFeature m_feature;

	public CustomMerchantInventory(RemoteTradingFeature inFeature)
	{
		super(null, new VirtualMerchant(inFeature));
		this.m_feature = inFeature;
	}

	@Override
	public String getName()
	{
		return this.m_feature.getTradeName();
	}
}