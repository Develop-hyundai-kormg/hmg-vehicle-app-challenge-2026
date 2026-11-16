package org.hmgchallenge.turnmirror

import android.os.SystemClock
import kotlin.math.abs

/** Volatile observations only. No route, driver identifier, or driving log is stored. */
class ObservationSession(private val nowMs: () -> Long = SystemClock::elapsedRealtime) {
    data class Candidate(
        val sequence: Int,
        val elapsedSeconds: Long,
        val angleDegrees: Float,
        val signalAtThreshold: String,
        var excluded: Boolean = false
    )

    private val startedMs = nowMs()
    private var armed = false
    private var haveAngle = false
    var lastSignal: String? = null
        private set
    var lastAngle: Float? = null
        private set
    var signalChanges: Int = 0
        private set
    var angleSamples: Int = 0
        private set
    val candidates = mutableListOf<Candidate>()

    fun onSignal(value: String) {
        if (lastSignal != null && lastSignal != value) signalChanges++
        lastSignal = value
    }

    /**
     * A threshold crossing is only a steering excursion candidate. It cannot establish a road
     * turn, lane change, driver fault, or whether a signal was used throughout the maneuver.
     */
    fun onAngle(value: Float): Candidate? {
        if (!value.isFinite()) return null
        angleSamples++
        lastAngle = value
        val magnitude = abs(value)
        if (!haveAngle) {
            haveAngle = true
            armed = magnitude <= REARM_ANGLE
            return null
        }
        if (magnitude <= REARM_ANGLE) {
            armed = true
            return null
        }
        if (!armed || magnitude < CANDIDATE_ANGLE) return null
        armed = false
        return Candidate(
            sequence = candidates.size + 1,
            elapsedSeconds = ((nowMs() - startedMs) / 1000).coerceAtLeast(0),
            angleDegrees = value,
            signalAtThreshold = lastSignal ?: "미확인"
        ).also(candidates::add)
    }

    companion object {
        const val CANDIDATE_ANGLE = 45f
        const val REARM_ANGLE = 18f
    }
}
