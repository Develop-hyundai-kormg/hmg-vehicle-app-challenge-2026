package org.hmgchallenge.turnmirror

import ai.pleos.playground.vehicle.Vehicle
import ai.pleos.playground.vehicle.constant.area.TurnSignalValue
import ai.pleos.playground.vehicle.listener.SteeringWheelAngleListener
import ai.pleos.playground.vehicle.listener.TurnSignalStateListener
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log

/** Thin bridge to the two public Vehicle SDK 2.3.2 read domains used by this prototype. */
class VehicleObserver(context: Context, private val callback: Callback) {
    interface Callback {
        fun onStatus(key: String, value: String)
        fun onSignal(value: String)
        fun onAngle(value: Float)
    }

    private val vehicle = Vehicle(context.applicationContext)
    private val main = Handler(Looper.getMainLooper())
    private var active = false
    private var turnRegistered = false
    private var angleRegistered = false

    private val turnListener = object : TurnSignalStateListener {
        override fun onTurnSignalStateUpdated(value: TurnSignalValue?) {
            if (value != null) signal(value)
        }
        override fun onFailed(error: Exception) = failure("방향지시등 구독", error)
    }
    private val angleListener = object : SteeringWheelAngleListener {
        override fun onSteeringWheelAngleUpdated(value: Float?) {
            if (value != null) angle(value)
        }
        override fun onFailed(error: Exception) = failure("조향각 구독", error)
    }

    fun start(): Boolean {
        try {
            vehicle.initialize()
            active = true
            status("연결", "Vehicle SDK 초기화 완료")
        } catch (error: Exception) {
            failure("연결", error)
            return false
        }

        val turn = vehicle.getTurnSignal()
        val steering = vehicle.getSteeringWheel()
        call("방향지시등 capability") {
            turn.checkTurnSignalStateCapabilities(
                { status("방향지시등 capability", it.accessType.name) },
                { failure("방향지시등 capability", it) }
            )
        }
        call("조향각 capability") {
            steering.checkSteeringWheelAngleCapabilities(
                { config, _ -> status("조향각 capability", config.accessType.name) },
                { failure("조향각 capability", it) }
            )
        }
        call("방향지시등 현재값") {
            turn.getCurrentTurnSignalState(
                { if (it != null) { status("방향지시등 현재값", "수신 성공"); signal(it) } },
                { failure("방향지시등 현재값", it) }
            )
        }
        call("조향각 현재값") {
            steering.getCurrentSteeringWheelAngle(
                { if (it != null) { status("조향각 현재값", "수신 성공"); angle(it) } },
                { failure("조향각 현재값", it) }
            )
        }
        call("방향지시등 구독") {
            turn.registerTurnSignalState(turnListener)
            turnRegistered = true
            status("방향지시등 구독", "요청됨 · 변화 수신 미검증")
        }
        call("조향각 구독") {
            steering.registerSteeringWheelAngle(angleListener)
            angleRegistered = true
            status("조향각 구독", "요청됨 · 변화 수신 미검증")
        }
        return true
    }

    fun stop() {
        if (!active) return
        active = false
        if (turnRegistered) runCatching { vehicle.getTurnSignal().unregisterTurnSignalState(turnListener) }
        if (angleRegistered) runCatching { vehicle.getSteeringWheel().unregisterSteeringWheelAngle(angleListener) }
        runCatching { vehicle.release() }
    }

    private fun call(key: String, action: () -> Unit) {
        try { action() } catch (error: Exception) { failure(key, error) }
    }

    private fun signal(value: TurnSignalValue) {
        main.post { if (active) callback.onSignal(value.name) }
    }

    private fun angle(value: Float) {
        if (!value.isFinite()) return
        main.post { if (active) callback.onAngle(value) }
    }

    private fun status(key: String, value: String) {
        Log.i(TAG, "$key: $value")
        main.post { if (active) callback.onStatus(key, value) }
    }

    private fun failure(key: String, error: Exception) {
        val detail = error.javaClass.simpleName
        Log.w(TAG, "$key failed: $detail")
        main.post { callback.onStatus(key, "실패: $detail") }
    }

    companion object { private const val TAG = "TurnMirrorProbe" }
}
