package de.kumpelblase2.remoteentities.api;

import net.minecraft.server.EntityLiving;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.api.features.FeatureSet;
import de.kumpelblase2.remoteentities.api.thinking.Mind;

public interface RemoteEntity
{
	public int getID();
	public RemoteEntityType getType();
	public Mind getMind();
	public LivingEntity getBukkitEntity();
	public EntityLiving getHandle();
	public FeatureSet getFeatures();
	public boolean move(Location inLocation);
	public boolean move(LivingEntity inEntity);
	public void stopMoving();
	public void teleport(Location inLocation);
	public void spawn(Location inLocation);
	public void despawn(DespawnReason inReason);
	public boolean isSpawned();
	public void setMaxHealth(int inMax);
	public int getMaxHealth();
	public void setStationary(boolean inState);
	public boolean isStationary();
	public float getSpeed();
	public void setSpeed(float inSpeed);
	public boolean isPushable();
	public void setPushable(boolean inState);
	public EntityManager getManager();
}
