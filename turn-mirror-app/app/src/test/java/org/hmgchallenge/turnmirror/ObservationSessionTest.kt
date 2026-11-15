package org.hmgchallenge.turnmirror

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ObservationSessionTest {
    @Test
    fun initialHighAngleNeedsReturnToCenterBeforeCandidate() {
        var time = 0L
        val session = ObservationSession { time }
        session.onSignal("OFF")
        assertNull(session.onAngle(70f))
        assertNull(session.onAngle(80f))
        assertNull(session.onAngle(0f))
        time = 2_000L
        val candidate = session.onAngle(46f)
        assertEquals(1, candidate?.sequence)
        assertEquals("OFF", candidate?.signalAtThreshold)
        assertEquals(2L, candidate?.elapsedSeconds)
    }

    @Test
    fun oneCandidatePerExcursionAndSignalIsOnlyThresholdSnapshot() {
        val session = ObservationSession { 0L }
        session.onSignal("OFF")
        session.onAngle(0f)
        session.onSignal("RIGHT")
        val first = session.onAngle(52f)
        assertEquals("RIGHT", first?.signalAtThreshold)
        assertNull(session.onAngle(75f))
        session.onSignal("OFF")
        assertNull(session.onAngle(17f))
        val second = session.onAngle(-47f)
        assertEquals("OFF", second?.signalAtThreshold)
        assertEquals(2, session.candidates.size)
        assertEquals(2, session.signalChanges)
    }
}
