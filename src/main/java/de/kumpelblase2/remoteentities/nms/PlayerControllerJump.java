package de.kumpelblase2.remoteentities.nms;

import net.minecraft.server.v1_7_R3.ControllerJump;
import net.minecraft.server.v1_7_R3.EntityLiving;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;

public class PlayerControllerJump extends ControllerJump
{
	// --- Taken from minecraft/ControllerJump.java
	// --- Modified to work with an entity living
	private EntityLiving a;
	private boolean b;

	public PlayerControllerJump(EntityLiving inEntity) {
		super(NMSUtil.getTempInsentientEntity());
		this.a = inEntity;
	}

	public void a() {
		this.b = true;
	}

	public void b() {
		this.a.f(this.b);
		this.b = false;
	}
}