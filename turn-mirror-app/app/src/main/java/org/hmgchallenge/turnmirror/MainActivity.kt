package org.hmgchallenge.turnmirror

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import java.util.Locale

class MainActivity : Activity(), VehicleObserver.Callback {
    private enum class Mode { READY, OBSERVING, REVIEW, EXAMPLE }

    private var mode = Mode.READY
    private var session: ObservationSession? = null
    private var observer: VehicleObserver? = null
    private var finishReason = ""
    private val statuses = linkedMapOf<String, String>()
    private var renderQueued = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = BACKGROUND
        window.navigationBarColor = BACKGROUND
        render()
    }

    override fun onStop() {
        if (mode == Mode.OBSERVING) stopObservation("화면 이탈로 관찰 종료")
        super.onStop()
    }

    override fun onDestroy() {
        observer?.stop()
        observer = null
        super.onDestroy()
    }

    override fun onStatus(key: String, value: String) {
        if (mode != Mode.OBSERVING) return
        statuses[key] = value
        scheduleRender()
    }

    override fun onSignal(value: String) {
        if (mode != Mode.OBSERVING) return
        session?.onSignal(value)
        statuses["방향지시등 마지막 수신"] = value
        scheduleRender()
    }

    override fun onAngle(value: Float) {
        if (mode != Mode.OBSERVING) return
        val candidate = session?.onAngle(value)
        statuses["조향각 마지막 수신"] = formatAngle(value)
        if (candidate != null) {
            Log.i("TurnMirrorProbe", "Steering excursion candidate #${candidate.sequence} recorded")
        }
        scheduleRender()
    }

    private fun startObservation() {
        observer?.stop()
        session = ObservationSession()
        statuses.clear()
        finishReason = ""
        mode = Mode.OBSERVING
        val bridge = VehicleObserver(this, this)
        observer = bridge
        if (!bridge.start()) {
            observer = null
            mode = Mode.REVIEW
            finishReason = "Vehicle SDK 연결 실패 · 실제 관찰 없음"
        }
        render()
    }

    private fun stopObservation(reason: String = "사용자가 관찰 종료") {
        observer?.stop()
        observer = null
        if (mode == Mode.OBSERVING) {
            mode = Mode.REVIEW
            finishReason = reason
        }
        render()
    }

    private fun showExample() {
        if (mode == Mode.OBSERVING) return
        var clock = 0L
        val sample = ObservationSession { clock }
        sample.onSignal("OFF")
        sample.onAngle(0f)
        clock = 3_000L
        sample.onSignal("RIGHT")
        sample.onAngle(58f)
        sample.onAngle(0f)
        clock = 9_000L
        sample.onSignal("OFF")
        sample.onAngle(-53f)
        sample.onAngle(0f)
        clock = 14_000L
        sample.onSignal("LEFT")
        sample.onAngle(-65f)
        session = sample
        mode = Mode.EXAMPLE
        finishReason = "고정 예시 입력으로 만든 화면 · 차량 관찰 데이터 아님"
        render()
    }

    private fun scheduleRender() {
        if (renderQueued) return
        renderQueued = true
        window.decorView.postDelayed({
            renderQueued = false
            if (!isFinishing && !isDestroyed) render()
        }, 250)
    }

    private fun render() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(BACKGROUND)
            setPadding(dp(28), dp(20), dp(28), dp(20))
        }
        root.addView(text("회전 습관 거울", 30f, WHITE, true))
        root.addView(text("Pleos Vehicle SDK 2.3.2 · 1차 개발 프로토타입", 16f, MUTED).apply {
            setPadding(0, dp(3), 0, dp(16))
        })

        val columns = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        root.addView(columns, LinearLayout.LayoutParams(-1, 0, 1f))
        val left = column(columns, 0.9f)
        val right = column(columns, 1.1f)

        val stateLabel = when (mode) {
            Mode.READY -> "시작 전"
            Mode.OBSERVING -> "실제 차량 신호 관찰 중"
            Mode.REVIEW -> "실제 관찰 결과"
            Mode.EXAMPLE -> "예시 재생 · 실제 운행 아님"
        }
        left.addView(card("현재 상태", stateLabel, if (mode == Mode.EXAMPLE) AMBER else ACCENT))
        val intro = when (mode) {
            Mode.READY -> "차를 안전하게 세운 상태에서 관찰을 시작하세요. 이 앱을 화면에 둔 동안 방향지시등과 조향각을 읽습니다."
            Mode.OBSERVING -> "방향지시등과 조향각의 현재값 및 콜백을 수신합니다. 화면에서 벗어나면 관찰을 종료합니다."
            Mode.REVIEW -> finishReason
            Mode.EXAMPLE -> finishReason
        }
        left.addView(card("이 세션", intro))

        when (mode) {
            Mode.READY -> {
                left.addView(action("실제 관찰 시작", true, ::startObservation))
                left.addView(action("예시 화면 보기", false, ::showExample))
            }
            Mode.OBSERVING -> left.addView(action("관찰 종료 · 결과 보기", true, { stopObservation() }))
            Mode.REVIEW, Mode.EXAMPLE -> {
                left.addView(action("새 실제 관찰 시작", true, ::startObservation))
                left.addView(action("예시 화면 보기", false, ::showExample))
            }
        }
        left.addView(card(
            "판정 범위",
            "조향각 절댓값 45° 이상을 '조향 변화 후보'로 표시합니다. 18° 이하로 돌아온 후 다음 후보를 허용합니다. 실제 도로 회전·방향·차로변경·위반·과실은 판정하지 않습니다."
        ))

        when (mode) {
            Mode.READY -> {
                right.addView(card("관찰 입력", "방향지시등과 조향각의 읽기·구독을 시작하면 이곳에 SDK 상태가 나타납니다."))
                right.addView(card("검토 흐름", "관찰 시작 → 실제 차량 신호 수신 → 관찰 종료 → 후보 확인 → '회전 아님'으로 정정"))
            }
            Mode.OBSERVING -> {
                val current = session
                right.addView(card("실시간 수신값", "방향지시등: ${current?.lastSignal ?: "대기 중"}\n조향각: ${current?.lastAngle?.let(::formatAngle) ?: "대기 중"}"))
                right.addView(card("관찰 상태", "방향지시등 값 변화 ${current?.signalChanges ?: 0}회 · 조향각 수신 ${current?.angleSamples ?: 0}건 · 조향 변화 후보 ${current?.candidates?.size ?: 0}건"))
                right.addView(card("SDK 상태", statuses.entries.joinToString("\n") { "${it.key}: ${it.value}" }.ifEmpty { "SDK 응답 대기 중" }))
            }
            Mode.REVIEW, Mode.EXAMPLE -> review(right)
        }
        setContentView(root)
    }

    private fun review(parent: LinearLayout) {
        val current = session
        val candidates = current?.candidates.orEmpty()
        val included = candidates.count { !it.excluded }
        val kind = if (mode == Mode.EXAMPLE) "예시 입력" else "실제 SDK 수신"
        parent.addView(card("$kind · 검토", "조향 변화 후보 ${candidates.size}건 · 회전 아님 정정 ${candidates.size - included}건\n방향지시등 값 변화 ${current?.signalChanges ?: 0}회 · 조향각 수신 ${current?.angleSamples ?: 0}건"))
        if (mode == Mode.REVIEW && candidates.isEmpty()) {
            parent.addView(card("후보 없음", "현재 AVD에서는 정적값 OFF / 0.0만 확인될 수 있습니다. 후보 0건은 좋은 운전이나 실제 회전 0회를 뜻하지 않습니다."))
        }
        if (mode == Mode.EXAMPLE) {
            parent.addView(card("예시 데이터 표시", "아래 카드 3건은 앱에 고정한 합성 입력입니다. 실제 차량·운전자·주행에서 얻은 기록이 아닙니다.", AMBER))
        }
        candidates.forEach { candidate ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                background = rounded(CARD)
                setPadding(dp(18), dp(14), dp(18), dp(14))
            }
            val label = if (mode == Mode.EXAMPLE) "예시 후보" else "관측 후보"
            card.addView(text("$label ${candidate.sequence} · 시작 후 ${candidate.elapsedSeconds}초", 19f, WHITE, true))
            card.addView(text("기준 통과 시 조향각 ${formatAngle(candidate.angleDegrees)}\n그 순간 방향지시등 마지막 수신값 ${candidate.signalAtThreshold}", 17f, MUTED))
            card.addView(text(if (candidate.excluded) "정정됨: 회전 아님" else "미확정: 실제 회전 여부 확인 필요", 16f, if (candidate.excluded) AMBER else ACCENT))
            card.addView(action(if (candidate.excluded) "정정 취소" else "회전 아님", false) {
                candidate.excluded = !candidate.excluded
                render()
            })
            parent.addView(card, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(12) })
        }
        if (mode == Mode.REVIEW) {
            parent.addView(card("해석 한계", "표시된 방향지시등 값은 후보 생성 순간의 마지막 수신값입니다. 회전 전체 구간의 사용 여부를 보증하지 않습니다. 속도·정지·도로 상황은 측정하지 않습니다."))
        }
    }

    private fun column(row: LinearLayout, weight: Float): LinearLayout {
        val content = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        val scroll = ScrollView(this).apply {
            isFillViewport = false
            addView(content)
        }
        row.addView(scroll, LinearLayout.LayoutParams(0, -1, weight).apply { marginEnd = dp(16) })
        return content
    }

    private fun card(title: String, body: String, bodyColor: Int = WHITE): View {
        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = rounded(CARD)
            setPadding(dp(18), dp(16), dp(18), dp(16))
            addView(text(title, 15f, MUTED, true).apply { setPadding(0, 0, 0, dp(8)) })
            addView(text(body, 19f, bodyColor))
            layoutParams = LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(12) }
        }
    }

    private fun action(label: String, primary: Boolean, onClick: () -> Unit): Button {
        return Button(this).apply {
            text = label
            isAllCaps = false
            textSize = 17f
            setTypeface(null, Typeface.BOLD)
            setTextColor(if (primary) BACKGROUND else WHITE)
            background = rounded(if (primary) ACCENT else SECONDARY)
            minHeight = dp(54)
            gravity = Gravity.CENTER
            setOnClickListener { onClick() }
            layoutParams = LinearLayout.LayoutParams(-1, dp(54)).apply { bottomMargin = dp(12) }
        }
    }

    private fun text(value: String, size: Float, color: Int, bold: Boolean = false): TextView {
        return TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            if (bold) setTypeface(null, Typeface.BOLD)
            gravity = Gravity.START
            setLineSpacing(dp(3).toFloat(), 1f)
        }
    }

    private fun rounded(color: Int) = GradientDrawable().apply {
        setColor(color)
        cornerRadius = dp(14).toFloat()
    }

    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()
    private fun formatAngle(value: Float) = String.format(Locale.US, "%+.1f°", value)

    companion object {
        private val BACKGROUND = Color.rgb(15, 20, 29)
        private val CARD = Color.rgb(28, 37, 52)
        private val SECONDARY = Color.rgb(49, 62, 80)
        private val WHITE = Color.rgb(239, 245, 248)
        private val MUTED = Color.rgb(177, 193, 206)
        private val ACCENT = Color.rgb(87, 218, 181)
        private val AMBER = Color.rgb(248, 194, 103)
    }
}
