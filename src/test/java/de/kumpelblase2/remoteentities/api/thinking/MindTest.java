package de.kumpelblase2.remoteentities.api.thinking;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MindTest
{
	Mind m_mind;

	@Mock
	InteractBehavior m_behavior;
	@Mock
	InteractBehavior m_behavior2;

	@Before
	public void setup()
	{
		RemoteEntity entity = mock(RemoteEntity.class);
		when(entity.getHandle()).thenReturn(NMSUtil.getTempInsentientEntity());
		this.m_mind = spy(new Mind(entity));
		when(this.m_behavior.getName()).thenReturn("Interact");
		when(this.m_behavior2.getName()).thenReturn("Interact");
	}

	@Test
	public void testBehaviorsAdd()
	{
		this.m_mind.addBehaviour(this.m_behavior);

		assertTrue("Mind should contain behavior after adding.", this.m_mind.hasBehavior(InteractBehavior.class));
		assertTrue("Mind should contain behavior after adding with name.", this.m_mind.hasBehaviour("Interact"));
		assertTrue("There should only be one behavior in mind", this.m_mind.getBehaviours().size() == 1);

		this.m_mind.clearBehaviours();
	}

	@Test
	public void testBehaviorsStore()
	{
		this.m_mind.addBehaviour(this.m_behavior);
		assertEquals("Behavior should be the same as store one", this.m_behavior, this.m_mind.getBehavior(InteractBehavior.class));
		assertEquals("Behavior should be the same as store one with name", this.m_behavior, this.m_mind.getBehaviour("Interact"));
		this.m_mind.clearBehaviours();
	}

	@Test
	public void testBehaviorsAddCalled()
	{
		this.m_mind.addBehaviour(this.m_behavior2);
		verify(this.m_behavior2, times(1)).onAdd();
		this.m_mind.clearBehaviours();
	}

	@Test
	public void testBehaviorsRemove()
	{
		this.m_mind.addBehaviour(this.m_behavior);
		this.m_mind.removeBehavior(InteractBehavior.class);
		assertFalse("Behavior should be gone after removing", this.m_mind.hasBehavior(InteractBehavior.class));

		this.m_mind.addBehaviour(this.m_behavior);
		this.m_mind.removeBehaviour("Interact");
		assertFalse("Behavior should be gone after removing with name", this.m_mind.hasBehaviour("Interact"));
	}

	@Test
	public void testTickBehaviors()
	{
		this.m_mind.addBehaviour(this.m_behavior);
		this.m_mind.tick();
		this.m_mind.blockFeelings(true);
		this.m_mind.tick();
		verify(this.m_behavior, times(1)).run();
		this.m_mind.clearBehaviours();
		this.m_mind.blockFeelings(false);
	}

	@Test
	public void testResets()
	{
		this.m_mind.fixHeadYawAt(1);
		this.m_mind.fixPitchAt(1);
		this.m_mind.fixYawAt(1);
		this.m_mind.tick();

		verify(this.m_mind.getEntity(), times(1)).setYaw(1);
		verify(this.m_mind.getEntity(), times(1)).setHeadYaw(1);
		verify(this.m_mind.getEntity(), times(1)).setPitch(1);

		this.m_mind.resetFixedHeadYaw();
		this.m_mind.resetFixedPitch();
		this.m_mind.resetFixedYaw();
		this.m_mind.tick();

		verify(this.m_mind.getEntity(), times(1)).setYaw(1);
		verify(this.m_mind.getEntity(), times(1)).setHeadYaw(1);
		verify(this.m_mind.getEntity(), times(1)).setPitch(1);
	}
}