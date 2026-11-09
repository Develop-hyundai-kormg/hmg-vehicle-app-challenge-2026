package ai.pleos.playground.sample.vehicle

import ai.pleos.playground.vehicle.Vehicle
import ai.pleos.playground.vehicle.api.model.area.DoorArea
import ai.pleos.playground.vehicle.api.model.area.WheelArea
import ai.pleos.playground.vehicle.api.model.area.WindowArea
import ai.pleos.playground.vehicle.constant.area.DoorValue
import ai.pleos.playground.vehicle.constant.area.WheelValue
import ai.pleos.playground.vehicle.constant.area.WindowValue
import ai.pleos.playground.vehicle.constant.control.WiperSwitch
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    private var vehicle: Vehicle? = null
    private val tag = "HMG_PROBE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TextView(this).apply { text = "HMG capability probe: see logcat" })
        val v = Vehicle(applicationContext)
        vehicle = v
        try {
            v.initialize()
            Log.i(tag, "initialized")
        } catch (e: Exception) {
            Log.e(tag, "initialize failed: $e")
            return
        }
        fun attempt(name: String, action: () -> Unit) {
            try { action() } catch (e: Exception) { Log.e(tag, "$name throw: $e") }
        }
        val frontWindow = WindowArea(WindowValue.ROW_1_LEFT)
        val frontWiper = WindowArea(WindowValue.FRONT_WINDSHIELD)
        val frontTire = WheelArea(WheelValue.LEFT_FRONT)
        val frontDoor = DoorArea(DoorValue.ROW_1_LEFT)
        attempt("window capability") { v.getWindow().checkWindowPositionCapabilities(
            onComplete = { config, areas -> Log.i(tag, "window capability: $config $areas") },
            onFailed = { Log.e(tag, "window capability failed: $it") }
        ) }
        attempt("window read") { v.getWindow().getCurrentWindowPosition(frontWindow,
            onComplete = { Log.i(tag, "window position: $it") },
            onFailed = { Log.e(tag, "window read failed: $it") }
        ) }
        attempt("wiper capability") { v.getWiper().checkWiperStateCapabilities(
            onComplete = { config, areas -> Log.i(tag, "wiper capability: $config $areas") },
            onFailed = { Log.e(tag, "wiper capability failed: $it") }
        ) }
        attempt("wiper read") { v.getWiper().getCurrentWiperState(frontWiper,
            onComplete = { Log.i(tag, "wiper state: $it") },
            onFailed = { Log.e(tag, "wiper read failed: $it") }
        ) }
        attempt("tire capability") { v.getTire().checkTirePressureCapabilities(
            onComplete = { config, areas -> Log.i(tag, "tire capability: $config $areas") },
            onFailed = { Log.e(tag, "tire capability failed: $it") }
        ) }
        attempt("tire read") { v.getTire().getTirePressure(frontTire,
            onComplete = { Log.i(tag, "tire pressure: $it") },
            onFailed = { Log.e(tag, "tire read failed: $it") }
        ) }
        attempt("washer read") { v.getSafety().hasLowWasherFluidWarning(
            onComplete = { Log.i(tag, "washer warning: $it") },
            onFailed = { Log.e(tag, "washer read failed: $it") }
        ) }
        attempt("telltales read") { v.getSafety().getTelltalesInfo(
            onComplete = { Log.i(tag, "telltales: $it") },
            onFailed = { Log.e(tag, "telltales read failed: $it") }
        ) }
        attempt("range read") { v.getOdometer().getRangeRemainingDistance(
            onComplete = { Log.i(tag, "range remaining: $it") },
            onFailed = { Log.e(tag, "range read failed: $it") }
        ) }
        attempt("odometer read") { v.getOdometer().getCurrentDistanceDriven(
            onComplete = { Log.i(tag, "odometer: $it") },
            onFailed = { Log.e(tag, "odometer read failed: $it") }
        ) }
        attempt("fuel read") { v.getFuel().getFuelPercent(
            onComplete = { Log.i(tag, "fuel percent: $it") },
            onFailed = { Log.e(tag, "fuel read failed: $it") }
        ) }
        attempt("ev soc read") { v.getEvBattery().getEvBatteryStateOfCharge(
            onComplete = { Log.i(tag, "ev soc: $it") },
            onFailed = { Log.e(tag, "ev soc read failed: $it") }
        ) }
        attempt("ev charge switch read") { v.getEvBattery().getEvChargeSwitchState(
            onComplete = { Log.i(tag, "ev charge switch: $it") },
            onFailed = { Log.e(tag, "ev charge switch read failed: $it") }
        ) }
        attempt("door read") { v.getDoor().getCurrentDoorPosition(frontDoor,
            onComplete = { Log.i(tag, "door position: $it") },
            onFailed = { Log.e(tag, "door read failed: $it") }
        ) }
        val isEmulator = Build.HARDWARE.contains("ranchu", ignoreCase = true) ||
            Build.FINGERPRINT.contains("generic", ignoreCase = true)
        attempt("wiper switch capability") { v.getWiper().checkWiperSwitchCapabilities(
            onComplete = { config, areas -> Log.i(tag, "wiper switch capability: $config $areas") },
            onFailed = { Log.e(tag, "wiper switch capability failed: $it") }
        ) }
        if (isEmulator) {
            attempt("window write and reread") { v.getWindow().setWindowPosition(frontWindow, 5,
                onComplete = { _, value ->
                    Log.i(tag, "window set callback: $value")
                    v.getWindow().getCurrentWindowPosition(frontWindow,
                        onComplete = { Log.i(tag, "window after set: $it") },
                        onFailed = { Log.e(tag, "window reread failed: $it") }
                    )
                },
                onFailed = { Log.e(tag, "window set failed: $it") }
            ) }
            attempt("wiper switch write and reread") { v.getWiper().setWiperSwitch(frontWiper, WiperSwitch.CONTINUOUS_LEVEL_1,
                onComplete = { _, value ->
                    Log.i(tag, "wiper set callback: $value")
                    v.getWiper().getCurrentWiperState(frontWiper,
                        onComplete = { Log.i(tag, "wiper after set: $it") },
                        onFailed = { Log.e(tag, "wiper reread failed: $it") }
                    )
                },
                onFailed = { Log.e(tag, "wiper set failed: $it") }
            ) }
            Handler(Looper.getMainLooper()).postDelayed({
                attempt("window delayed reread") { v.getWindow().getCurrentWindowPosition(frontWindow,
                    onComplete = { Log.i(tag, "window after 5s: $it") },
                    onFailed = { Log.e(tag, "window delayed read failed: $it") }
                ) }
                attempt("wiper delayed reread") { v.getWiper().getCurrentWiperState(frontWiper,
                    onComplete = { Log.i(tag, "wiper after 5s: $it") },
                    onFailed = { Log.e(tag, "wiper delayed read failed: $it") }
                ) }
            }, 5000L)
        } else {
            Log.i(tag, "write tests skipped outside emulator")
        }
    }

    override fun onDestroy() {
        vehicle?.release()
        super.onDestroy()
    }
}
