package edu.bsu.cybersec.core;

import org.junit.Before;
import org.junit.Test;
import playn.core.Clock;

import static org.junit.Assert.assertEquals;

public final class TimeElapseSystemTest {

    private GameWorld world;
    private TimeElapseSystem system;
    private int gameClockEntityId;

    @Before
    public void setUp() {
        world = new GameWorld() {
            {
                gameClockEntityId = createClockEntity().id;
            }
        };
        system = new TimeElapseSystem(world);
    }


    @Test
    public void testUpdate_noElapsedTime_noChange() {
        whenMsElapses(0);
        thenElapsedMsIs(0);
    }

    private void thenElapsedMsIs(int expected) {
        assertEquals(expected, world.elapsedSimMs.get(gameClockEntityId));
    }

    private void whenMsElapses(int elapsedMS) {
        Clock playnClock = new Clock();
        playnClock.dt = elapsedMS;
        playnClock.tick += elapsedMS;
        world.update(playnClock);
    }

    @Test
    public void testUpdate_oneSimSecondPerClockSecond_oneSecondElapses() {
        final int elapsedMS = 1000;
        whenMsElapses(elapsedMS);
        thenElapsedMsIs(elapsedMS);
    }

    @Test
    public void testUpdate_twoSimSecondsPerClockSecond_twoSecondsElapse() {
        system.setSimSecondsPerClockSecond(2);
        whenMsElapses(1000);
        thenElapsedMsIs(2000);
    }

    @Test
    public void testUpdate_noElapsedTime_tickIsUnchanged() {
        thenTickIs(0);
    }

    private void thenTickIs(int i) {
        assertEquals(i, world.tickMs.get(gameClockEntityId));
    }

    @Test
    public void testUpdate_oneSecondElapsed_tickIsOneSecond() {
        whenMsElapses(1000);
        thenTickIs(1000);
    }

    @Test
    public void testUpdate_oneSecondElapsedTwice_tickIsTwoSeconds() {
        whenMsElapses(1000);
        whenMsElapses(1000);
        thenTickIs(2000);
    }

}