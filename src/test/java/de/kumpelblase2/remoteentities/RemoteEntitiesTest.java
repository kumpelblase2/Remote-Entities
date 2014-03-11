package de.kumpelblase2.remoteentities;

import org.bukkit.command.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
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
		when(this.m_remoteEntities.onCommand(any(CommandSender.class), any(Command.class), anyString(), any(String[].class))).thenCallRealMethod();
		doCallRealMethod().when(this.m_remoteEntities).checkConfig();
	}

	@Test
	public void testAutoUpdate()
	{
		this.m_remoteEntities.checkConfig();
		assertFalse("Auto update should be disabled by default.", this.m_remoteEntities.isAutoUpdateEnabled());
	}

	@Test(expected = PluginNotEnabledException.class)
	public void testCreateManagerFail()
	{
		RemoteEntities.createManager(null);
	}

	@Test
	public void testCommand()
	{
		Command command = mock(Command.class);
		when(command.getName()).thenReturn("remoteentities");
		ConsoleCommandSender sender = mock(ConsoleCommandSender.class);
		String[] args = new String[] { "rebuild" };
		this.m_remoteEntities.onCommand(sender, command, "re", args);
		verify(this.m_remoteEntities).updateTo(anyString(), anyString());
		Player player = mock(Player.class);
		this.m_remoteEntities.onCommand(player, command, "re", args);
		verify(player).sendMessage(anyString());
		this.m_remoteEntities.onCommand(player, command, "re", new String[0]);
		verify(player).sendMessage("Please provide arguments.");
	}
}