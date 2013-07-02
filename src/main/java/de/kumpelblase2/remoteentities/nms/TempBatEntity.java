package de.kumpelblase2.remoteentities.nms;

import java.util.*;
import net.minecraft.server.v1_6_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R1.entity.CraftEntity;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class TempBatEntity extends EntityBat
{
	private static final String EMPTY = "";

	public TempBatEntity(World inWorld)
	{
		super(inWorld);
		this.die();
		this.cleanAll();
	}

	private void cleanAll()
	{
		this.world = null;
		this.at = null;
		//this.boundingbox
		this.bukkitEntity = null;
		this.combatTracker = null;
		this.datawatcher = null;
		this.dropChances = new float[0];
		this.random = null;
		this.uniqueID = null;
	}

	@Override
	protected void a()
	{
	}

	@Override
	protected float aW()
	{
		return 0;
	}

	@Override
	protected float aX()
	{
		return 0;
	}

	@Override
	protected String r()
	{
		return EMPTY;
	}

	@Override
	protected String aK()
	{
		return EMPTY;
	}

	@Override
	protected String aL()
	{
		return EMPTY;
	}

	@Override
	public boolean L()
	{
		return false;
	}

	@Override
	protected void n(Entity inEntity)
	{
	}

	@Override
	protected void bf()
	{
	}

	@Override
	protected void ax()
	{
	}

	@Override
	public boolean bF()
	{
		return false;
	}

	@Override
	public void a(boolean b)
	{
	}

	@Override
	protected boolean bb()
	{
		return false;
	}

	@Override
	public void l_()
	{
	}

	@Override
	protected void be()
	{
	}

	@Override
	protected boolean e_()
	{
		return false;
	}

	@Override
	protected void b(float v)
	{
	}

	@Override
	protected void a(double v, boolean b)
	{
	}

	@Override
	public boolean as()
	{
		return false;
	}

	@Override
	public boolean damageEntity(DamageSource inDamageSource, float v)
	{
		return false;
	}

	@Override
	public void a(NBTTagCompound inNBTTagCompound)
	{
	}

	@Override
	public void b(NBTTagCompound inNBTTagCompound)
	{
	}

	@Override
	public boolean canSpawn()
	{
		return false;
	}

	@Override
	public boolean bC()
	{
		return false;
	}

	@Override
	protected boolean a(EntityHuman inEntityHuman)
	{
		return false;
	}

	@Override
	public EntityLiving getGoalTarget()
	{
		return null;
	}

	@Override
	public void setGoalTarget(EntityLiving entityliving)
	{
	}

	@Override
	public boolean a(Class oclass)
	{
		return false;
	}

	@Override
	public void n()
	{
	}

	@Override
	public int o()
	{
		return 0;
	}

	@Override
	public void p()
	{
	}

	@Override
	public void x()
	{
	}

	@Override
	protected int getExpValue(EntityHuman entityhuman)
	{
		return 0;
	}

	@Override
	public void q()
	{
	}

	@Override
	protected float f(float f, float f1)
	{
		return 0;
	}

	@Override
	protected void dropDeathLoot(boolean flag, int i)
	{
	}

	@Override
	public void n(float f)
	{
	}

	@Override
	public void i(float f)
	{
	}

	@Override
	public void c()
	{
	}

	@Override
	protected boolean isTypeNotPersistent()
	{
		return false;
	}

	@Override
	protected void bk()
	{
	}

	@Override
	protected void bh()
	{
	}

	@Override
	public int bl()
	{
		return 0;
	}

	@Override
	public void a(Entity entity, float f, float f1)
	{
	}

	@Override
	public int br()
	{
		return 0;
	}

	@Override
	public int aq()
	{
		return 0;
	}

	@Override
	public ItemStack aV()
	{
		return null;
	}

	@Override
	public ItemStack getEquipment(int i)
	{
		return null;
	}

	@Override
	public ItemStack o(int i)
	{
		return null;
	}

	@Override
	public void setEquipment(int i, ItemStack itemstack)
	{
	}

	@Override
	public ItemStack[] getEquipment()
	{
		return null;
	}

	@Override
	protected void dropEquipment(boolean flag, int i)
	{
	}

	@Override
	protected void bs()
	{
	}

	@Override
	protected void bt()
	{
	}

	@Override
	public GroupDataEntity a(GroupDataEntity groupdataentity)
	{
		return null;
	}

	@Override
	public boolean bu()
	{
		return false;
	}

	@Override
	public String getLocalizedName()
	{
		return EMPTY;
	}

	@Override
	public void bv()
	{
	}

	@Override
	public void setCustomName(String s)
	{
	}

	@Override
	public String getCustomName()
	{
		return EMPTY;
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public void setCustomNameVisible(boolean flag)
	{
	}

	@Override
	public boolean getCustomNameVisible()
	{
		return false;
	}

	@Override
	public void a(int i, float f)
	{
	}

	@Override
	public boolean bz()
	{
		return false;
	}

	@Override
	public void h(boolean flag)
	{
	}

	@Override
	public boolean bA()
	{
		return false;
	}

	@Override
	protected void bB()
	{
	}

	@Override
	public void i(boolean flag)
	{
	}

	@Override
	public boolean bD()
	{
		return false;
	}

	@Override
	public Entity bE()
	{
		return null;
	}

	@Override
	public void b(Entity entity, boolean flag)
	{
	}

	@Override
	public boolean ay()
	{
		return false;
	}

	@Override
	public int getExpReward()
	{
		return 0;
	}

	@Override
	public float getScaledHealth()
	{
		return 0;
	}

	@Override
	public boolean isBaby()
	{
		return false;
	}

	@Override
	protected void az()
	{
	}

	@Override
	protected int h(int i)
	{
		return 0;
	}

	@Override
	protected boolean alwaysGivesExp()
	{
		return false;
	}

	@Override
	public EntityLiving getLastDamager()
	{
		return null;
	}

	@Override
	public void b(EntityLiving entityliving)
	{
	}

	@Override
	public EntityLiving aD()
	{
		return null;
	}

	@Override
	public void k(Entity entity)
	{
	}

	@Override
	public int aE()
	{
		return 0;
	}

	@Override
	protected void aF()
	{
	}

	@Override
	public void aG()
	{
	}

	@Override
	public Collection getEffects()
	{
		return null;
	}

	@Override
	public boolean hasEffect(int i)
	{
		return false;
	}

	@Override
	public boolean hasEffect(MobEffectList mobeffectlist)
	{
		return false;
	}

	@Override
	public MobEffect getEffect(MobEffectList mobeffectlist)
	{
		return null;
	}

	@Override
	public void addEffect(MobEffect mobeffect)
	{
	}

	@Override
	public boolean e(MobEffect mobeffect)
	{
		return false;
	}

	@Override
	public boolean aI()
	{
		return false;
	}

	@Override
	public void k(int i)
	{
	}

	@Override
	protected void a(MobEffect mobeffect)
	{
	}

	@Override
	protected void b(MobEffect mobeffect)
	{
	}

	@Override
	protected void c(MobEffect mobeffect)
	{
	}

	@Override
	public void heal(float f)
	{
	}

	@Override
	public void heal(float f, EntityRegainHealthEvent.RegainReason regainReason)
	{
	}

	@Override
	public void setHealth(float f)
	{
	}

	@Override
	public void a(ItemStack itemstack)
	{
	}

	@Override
	public void a(Entity entity, float f, double d0, double d1)
	{
	}

	@Override
	protected ItemStack l(int i)
	{
		return null;
	}

	@Override
	public boolean e()
	{
		return false;
	}

	@Override
	public boolean isAlive()
	{
		return false;
	}

	@Override
	public int aM()
	{
		return 0;
	}

	@Override
	protected void h(float f)
	{
	}

	@Override
	protected float b(DamageSource damagesource, float f)
	{
		return 0;
	}

	@Override
	protected float c(DamageSource damagesource, float f)
	{
		return 0;
	}

	@Override
	protected void d(DamageSource damagesource, float f)
	{
	}

	@Override
	public CombatTracker aN()
	{
		return null;
	}

	@Override
	public EntityLiving aO()
	{
		return null;
	}

	@Override
	public void aR()
	{
	}

	@Override
	protected void B()
	{
	}

	@Override
	protected void aS()
	{
	}

	@Override
	public AttributeInstance a(IAttribute iattribute)
	{
		return null;
	}

	@Override
	public AttributeMapBase aT()
	{
		return null;
	}

	@Override
	public EnumMonsterType getMonsterType()
	{
		return null;
	}

	@Override
	public void setSprinting(boolean flag)
	{
	}

	@Override
	protected boolean aY()
	{
		return false;
	}

	@Override
	public void enderTeleportTo(double d0, double d1, double d2)
	{
	}

	@Override
	public void l(Entity entity)
	{
	}

	@Override
	protected void ba()
	{
	}

	@Override
	public void e(float f, float f1)
	{
	}

	@Override
	public float bc()
	{
		return 0;
	}

	@Override
	public boolean m(Entity entity)
	{
		return false;
	}

	@Override
	public boolean isSleeping()
	{
		return false;
	}

	@Override
	public void T()
	{
	}

	@Override
	protected void bg()
	{
	}

	@Override
	public void f(boolean flag)
	{
	}

	@Override
	public void receive(Entity entity, int i)
	{
	}

	@Override
	public boolean o(Entity entity)
	{
		return false;
	}

	@Override
	public Vec3D Y()
	{
		return null;
	}

	@Override
	public Vec3D j(float f)
	{
		return null;
	}

	@Override
	public boolean bi()
	{
		return false;
	}

	@Override
	public boolean K()
	{
		return false;
	}

	@Override
	public float getHeadHeight()
	{
		return 0;
	}

	@Override
	protected void J()
	{
	}

	@Override
	public float getHeadRotation()
	{
		return 0;
	}

	@Override
	public float bj()
	{
		return 0;
	}

	@Override
	public void m(float f)
	{
	}

	@Override
	public DataWatcher getDataWatcher()
	{
		return null;
	}

	@Override
	public boolean equals(Object object)
	{
		return false;
	}

	@Override
	public int hashCode()
	{
		return 0;
	}

	@Override
	protected void a(float f, float f1)
	{
	}

	@Override
	protected void b(float f, float f1)
	{
	}

	@Override
	public void setPosition(double d0, double d1, double d2)
	{
	}

	@Override
	public int y()
	{
		return 0;
	}

	@Override
	protected void z()
	{
	}

	@Override
	public void setOnFire(int i)
	{
	}

	@Override
	public void extinguish()
	{
	}

	@Override
	public boolean c(double d0, double d1, double d2)
	{
		return false;
	}

	@Override
	public void move(double d0, double d1, double d2)
	{
	}

	@Override
	protected void C()
	{
	}

	@Override
	protected void a(int i, int j, int k, int l)
	{
	}

	@Override
	public void makeSound(String s, float f, float f1)
	{
	}

	@Override
	public AxisAlignedBB D()
	{
		return null;
	}

	@Override
	protected void burn(float i)
	{
	}

	@Override
	public boolean F()
	{
		return false;
	}

	@Override
	public boolean G()
	{
		return false;
	}

	@Override
	public boolean H()
	{
		return false;
	}

	@Override
	public boolean a(Material material)
	{
		return false;
	}

	@Override
	public boolean I()
	{
		return false;
	}

	@Override
	public void a(float f, float f1, float f2)
	{
	}

	@Override
	public float d(float f)
	{
		return 0;
	}

	@Override
	public void spawnIn(World world)
	{
	}

	@Override
	public void setLocation(double d0, double d1, double d2, float f, float f1)
	{
	}

	@Override
	public void setPositionRotation(double d0, double d1, double d2, float f, float f1)
	{
	}

	@Override
	public float d(Entity entity)
	{
		return 0;
	}

	@Override
	public double e(double d0, double d1, double d2)
	{
		return 0;
	}

	@Override
	public double f(double d0, double d1, double d2)
	{
		return 0;
	}

	@Override
	public double e(Entity entity)
	{
		return 0;
	}

	@Override
	public void b_(EntityHuman entityhuman)
	{
	}

	@Override
	public void collide(Entity entity)
	{
	}

	@Override
	public void g(double d0, double d1, double d2)
	{
	}

	@Override
	public void b(Entity entity, int i)
	{
	}

	@Override
	public boolean c(NBTTagCompound nbttagcompound)
	{
		return false;
	}

	@Override
	public boolean d(NBTTagCompound nbttagcompound)
	{
		return false;
	}

	@Override
	public void e(NBTTagCompound nbttagcompound)
	{
	}

	@Override
	public void f(NBTTagCompound nbttagcompound)
	{
	}

	@Override
	public void P()
	{
	}

	@Override
	protected NBTTagList a(double... adouble)
	{
		return null;
	}

	@Override
	protected NBTTagList a(float... afloat)
	{
		return null;
	}

	@Override
	public EntityItem b(int i, int j)
	{
		return null;
	}

	@Override
	public EntityItem a(int i, int j, float f)
	{
		return null;
	}

	@Override
	public EntityItem a(ItemStack itemstack, float f)
	{
		return null;
	}

	@Override
	public boolean inBlock()
	{
		return false;
	}

	@Override
	public AxisAlignedBB g(Entity entity)
	{
		return null;
	}

	@Override
	public void U()
	{
	}

	@Override
	public double V()
	{
		return 0;
	}

	@Override
	public double W()
	{
		return 0;
	}

	@Override
	public void mount(Entity entity)
	{
	}

	@Override
	public CraftEntity getBukkitEntity()
	{
		return null;
	}

	@Override
	public void setPassengerOf(Entity entity)
	{
	}

	@Override
	public float X()
	{
		return 0;
	}

	@Override
	public void Z()
	{
	}

	@Override
	public int aa()
	{
		return 0;
	}

	@Override
	public boolean isBurning()
	{
		return false;
	}

	@Override
	public boolean ae()
	{
		return false;
	}

	@Override
	public boolean isSneaking()
	{
		return false;
	}

	@Override
	public void setSneaking(boolean flag)
	{
	}

	@Override
	public boolean isSprinting()
	{
		return false;
	}

	@Override
	public boolean isInvisible()
	{
		return false;
	}

	@Override
	public void setInvisible(boolean flag)
	{
	}

	@Override
	public void e(boolean flag)
	{
	}

	@Override
	protected boolean f(int i)
	{
		return false;
	}

	@Override
	protected void a(int i, boolean flag)
	{
	}

	@Override
	public int getAirTicks()
	{
		return 0;
	}

	@Override
	public void setAirTicks(int i)
	{
	}

	@Override
	public void a(EntityLightning entitylightning)
	{
	}

	@Override
	public void a(EntityLiving entityliving)
	{
	}

	@Override
	protected boolean i(double d0, double d1, double d2)
	{
		return false;
	}

	@Override
	public void ak()
	{
	}

	@Override
	public Entity[] am()
	{
		return null;
	}

	@Override
	public boolean h(Entity entity)
	{
		return false;
	}

	@Override
	public boolean ao()
	{
		return false;
	}

	@Override
	public boolean i(Entity entity)
	{
		return false;
	}

	@Override
	public String toString()
	{
		return EMPTY;
	}

	@Override
	public boolean isInvulnerable()
	{
		return false;
	}

	@Override
	public void j(Entity entity)
	{
	}

	@Override
	public void a(Entity entity, boolean flag)
	{
	}

	@Override
	public void b(int i)
	{
	}

	@Override
	public void teleportTo(Location exit, boolean portal)
	{
	}

	@Override
	public float a(Explosion explosion, World world, int i, int j, int k, Block block)
	{
		return 0;
	}

	@Override
	public boolean a(Explosion explosion, World world, int i, int j, int k, int l, float f)
	{
		return false;
	}

	@Override
	public int ar()
	{
		return 0;
	}

	@Override
	public void a(CrashReportSystemDetails crashreportsystemdetails)
	{
	}

	@Override
	public UUID getUniqueID()
	{
		return null;
	}

	@Override
	public boolean av()
	{
		return false;
	}

	@Override
	public String getScoreboardDisplayName()
	{
		return EMPTY;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		return null;
	}

	@Override
	protected void finalize() throws Throwable
	{
	}
}