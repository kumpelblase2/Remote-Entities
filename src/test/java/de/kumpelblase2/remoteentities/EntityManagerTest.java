package de.kumpelblase2.remoteentities;

import org.bukkit.plugin.Plugin;
import org.junit.Before;
import org.junit.Test;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EntityManagerTest
{
	EntityManager m_entityManager;

	@Before
	public void setup()
	{
		this.m_entityManager = spy(new EntityManager(false, mock(Plugin.class)));
	}

	@Test
	public void testFreeIDs()
	{
		assertTrue("First free id should be 0", 0 == this.m_entityManager.getNextFreeID());
		this.m_entityManager.addRemoteEntity(0, mock(RemoteEntity.class));
		assertTrue("After adding the next free id should be 1", 1 == this.m_entityManager.getNextFreeID());
		assertTrue("After adding giving the starting point should yield the same result", 1 == this.m_entityManager.getNextFreeID(1));
	}
}