package de.kumpelblase2.remoteentities;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import de.kumpelblase2.remoteentities.exceptions.PluginNotEnabledException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RemoteEntitiesTest
{
	@Mock
	RemoteEntities m_remoteEntities;

	@Before
	public void setup()
	{
		when(this.m_remoteEntities.getConfig()).thenReturn(new YamlConfiguration());
		doCallRealMethod().when(this.m_remoteEntities).checkConfig();
		this.m_remoteEntities.checkConfig();
	}

	@Test
	public void testAutoUpdate()
	{
		assertFalse("Auto update should be disabled by default.", this.m_remoteEntities.isAutoUpdateEnabled());
	}

	@Test(expected = PluginNotEnabledException.class)
	public void testCreateManagerFail()
	{
		RemoteEntities.createManager(null);
	}
}
