package de.kumpelblase2.remoteentities.api.thinking;

import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.BaseBehavior;
import de.kumpelblase2.remoteentities.api.thinking.InteractBehavior;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

public class BaseBehaviorTest
{
	BaseBehavior m_firstBehavior;
	BaseBehavior m_secondBehavior;
	BaseBehavior m_thirdBehavior;
	BaseBehavior m_fourthBehavior;

	@Before
	public void setup()
	{
		this.m_firstBehavior = new FirstInteract(null);
		this.m_secondBehavior = new SecondInteract(null);
		this.m_thirdBehavior = new FirstInteract(mock(RemoteEntity.class));
		this.m_fourthBehavior = new FirstInteract(null);
	}

	@Test
	public void testEquals()
	{
		assertEquals("Two behaviors of the same class should be equal", this.m_firstBehavior, this.m_fourthBehavior);
		assertNotEquals("Two behaviors of different class should not be equal", this.m_firstBehavior, this.m_secondBehavior);
		assertNotEquals("Two behaviors with different entities should not be equal ", this.m_firstBehavior, this.m_thirdBehavior);
	}

	private class FirstInteract extends InteractBehavior
	{
		public FirstInteract(RemoteEntity inEntity)
		{
			super(inEntity);
		}

		@Override
		public void onInteract(Player inPlayer)
		{
		}
	}

	private class SecondInteract extends InteractBehavior
	{
		public SecondInteract(RemoteEntity inEntity)
		{
			super(inEntity);
		}

		@Override
		public void onInteract(Player inPlayer)
		{
		}
	}
}
