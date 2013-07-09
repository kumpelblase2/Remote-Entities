package de.kumpelblase2.remoteentities.nms;

import java.util.UUID;
import net.minecraft.server.v1_6_R2.AttributeModifier;

public class RemoteSpeedModifier extends AttributeModifier
{
	private static final UUID s_uid = UUID.fromString("fcf71a34-f856-46e1-a94d-8b22b7746046");

	public RemoteSpeedModifier(double inAmount, boolean inIsAdditive)
	{
		super(s_uid, "RemoteEntities modifier", inAmount, (inIsAdditive ? 0 : 1));
	}
}