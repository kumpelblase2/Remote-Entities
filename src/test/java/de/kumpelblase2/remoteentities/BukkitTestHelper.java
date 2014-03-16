package de.kumpelblase2.remoteentities;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.powermock.api.mockito.PowerMockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public final class BukkitTestHelper
{
	static
	{
		managerMock = mock(PluginManager.class);
		schedulerMock = mock(BukkitScheduler.class);
		serverMock = mock(Server.class);
	}

	public static PluginManager managerMock;
	public static BukkitScheduler schedulerMock;
	public static Server serverMock;

	public static void setupBukkit()
	{
		PowerMockito.mockStatic(Bukkit.class);
		when(Bukkit.getPluginManager()).thenReturn(managerMock);
		when(Bukkit.getScheduler()).thenReturn(schedulerMock);
		when(Bukkit.getServer()).thenReturn(serverMock);
	}
}