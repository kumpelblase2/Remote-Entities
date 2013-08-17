package de.kumpelblase2.remoteentities.nms;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.v1_6_R2.*;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;

public class PlayerSenses extends EntitySenses
{
	// --- Taken from minecraft/EntitySenses.java
	// --- Modified to work with an entity living
	EntityLiving entity;
	List seenEntities = new ArrayList();
	List unseenEntities = new ArrayList();

	public PlayerSenses(EntityLiving inEntity) {
		super(NMSUtil.getTempInsentientEntity());
		this.entity = inEntity;
	}

	@Override
	public void a() {
		this.seenEntities.clear();
		this.unseenEntities.clear();
	}

	@Override
	public boolean canSee(Entity entity) {
		if (this.seenEntities.contains(entity)) {
			return true;
		} else if (this.unseenEntities.contains(entity)) {
			return false;
		} else {
			this.entity.world.methodProfiler.a("canSee");
			boolean flag = this.entity.o(entity);

			this.entity.world.methodProfiler.b();
			if (flag) {
				this.seenEntities.add(entity);
			} else {
				this.unseenEntities.add(entity);
			}

			return flag;
		}
	}
}