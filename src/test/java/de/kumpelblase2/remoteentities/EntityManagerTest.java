package de.kumpelblase2.remoteentities;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.events.RemoteEntityCreateEvent;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Bukkit.class)
public class EntityManagerTest
{
	EntityManager m_entityManager;

	@Mock
	Plugin plugin;

	@Mock
	RemoteEntity entity;

	@Before
	public void setup()
	{
		this.m_entityManager = spy(new EntityManager(false, this.plugin));
		BukkitTestHelper.setupBukkit();
	}

	@Test
	public void testFreeIDs()
	{
		assertTrue("First free id should be 0", 0 == this.m_entityManager.getNextFreeID());
		this.m_entityManager.addRemoteEntity(0, entity);
		assertTrue("After adding the next free id should be 1", 1 == this.m_entityManager.getNextFreeID());
		assertTrue("After adding giving the starting point should yield the same result", 1 == this.m_entityManager.getNextFreeID(1));
	}

	@Test
	public void testCreateEntity()
	{
		this.m_entityManager.createEntity(RemoteEntityType.Pig, 0);
		verify(BukkitTestHelper.managerMock).callEvent(any(RemoteEntityCreateEvent.class));
	}

	@Test
	public void testSetup()
	{
		this.m_entityManager.setup(this.plugin);
		verify(BukkitTestHelper.schedulerMock).scheduleSyncRepeatingTask(eq(this.plugin), any(Runnable.class), anyLong(), anyLong());
	}

	@Test
	public void testDespawn()
	{
		this.m_entityManager.addRemoteEntity(0, this.entity);
		this.m_entityManager.despawnAll(DespawnReason.CUSTOM);
		verify(this.entity).despawn(eq(DespawnReason.CUSTOM));
	}

	@Test
	public void testDespawnSave()
	{
		this.m_entityManager.despawnAll(DespawnReason.CUSTOM, true);
		verify(this.m_entityManager).saveEntities();
	}
}