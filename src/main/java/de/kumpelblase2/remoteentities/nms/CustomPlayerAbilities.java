package de.kumpelblase2.remoteentities.nms;

import net.minecraft.server.v1_6_R3.NBTTagCompound;
import net.minecraft.server.v1_6_R3.PlayerAbilities;

public class CustomPlayerAbilities extends PlayerAbilities
{
	protected float flySpeed = 0.05F;
	protected float walkSpeed = 0.1F;

	public CustomPlayerAbilities() {}

	public void a(NBTTagCompound nbttagcompound) {
		NBTTagCompound nbttagcompound1 = new NBTTagCompound();

		nbttagcompound1.setBoolean("invulnerable", this.isInvulnerable);
		nbttagcompound1.setBoolean("flying", this.isFlying);
		nbttagcompound1.setBoolean("mayfly", this.canFly);
		nbttagcompound1.setBoolean("instabuild", this.canInstantlyBuild);
		nbttagcompound1.setBoolean("mayBuild", this.mayBuild);
		nbttagcompound1.setFloat("flySpeed", this.flySpeed);
		nbttagcompound1.setFloat("walkSpeed", this.walkSpeed);
		nbttagcompound.set("abilities", nbttagcompound1);
	}

	public void b(NBTTagCompound nbttagcompound) {
		if (nbttagcompound.hasKey("abilities")) {
			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("abilities");

			this.isInvulnerable = nbttagcompound1.getBoolean("invulnerable");
			this.isFlying = nbttagcompound1.getBoolean("flying");
			this.canFly = nbttagcompound1.getBoolean("mayfly");
			this.canInstantlyBuild = nbttagcompound1.getBoolean("instabuild");
			if (nbttagcompound1.hasKey("flySpeed")) {
				this.flySpeed = nbttagcompound1.getFloat("flySpeed");
				this.walkSpeed = nbttagcompound1.getFloat("walkSpeed");
			}

			if (nbttagcompound1.hasKey("mayBuild")) {
				this.mayBuild = nbttagcompound1.getBoolean("mayBuild");
			}
		}
	}

	public float a() {
		return this.flySpeed;
	}

	public float b() {
		return this.walkSpeed;
	}

	public void setFlySpeed(float inSpeed)
	{
		this.flySpeed = inSpeed;
	}

	public void setWalkSpeed(float inSpeed)
	{
		this.walkSpeed = inSpeed;
	}
}