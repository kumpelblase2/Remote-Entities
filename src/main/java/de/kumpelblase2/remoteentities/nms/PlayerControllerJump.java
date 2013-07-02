package de.kumpelblase2.remoteentities.nms;

import net.minecraft.server.v1_6_R1.ControllerJump;
import net.minecraft.server.v1_6_R1.EntityLiving;

public class PlayerControllerJump extends ControllerJump
{
	// --- Taken from minecraft/ControllerJump.java
	// --- Modified to work with an entity living
	private EntityLiving a;
	private boolean b;

	public PlayerControllerJump(EntityLiving inEntity) {
		super(null);
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