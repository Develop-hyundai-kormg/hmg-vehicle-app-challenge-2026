# 〈회전 습관 거울〉 1차 개발 프로토타입 검증 — 2026-11-15 KST

## 현재 판단

**실행 가능한 초기 개발 산출물**이다. 자체 소스와 서명된 release APK를 같은 저장소에 두었고 Pleos AVD에서 새 설치·실행·정적 차량 신호 읽기와 예시/검토 UI를 확인했다. 하지만 핵심인 **실제 차량값 변화 → 회전 후보 생성**은 현재 AVD의 Virtual Console 503 때문에 **미확인**이다. 이 APK만으로 회전 습관 관찰 제품이 완성됐다고 주장할 수 없다. 팀이 선택 제출한다면 이 범위를 메일과 PR에서 그대로 밝혀야 한다. 공식 양식과 명확히 표시된 목업 영상만 제출하는 경로도 유지된다.

## 환경과 산출물

| 항목 | 확인값 |
|---|---|
| 앱 소스 | [`../turn-mirror-app/`](../turn-mirror-app/) · 샘플 앱 소스 복사 없음 |
| Pleos Connect AVD | v2.1.0 (`ro.build.display.id`), `PleosOfficialProfile`, Android Automotive API 34 x86_64, 2560×1440 / 160 dpi |
| 앱 빌드 | Windows JDK 17, Android Gradle Plugin 8.8.2, Gradle 8.10.2, compileSdk 35, Vehicle SDK 2.3.2 |
| APK | [`../apk/상명F4_회전습관거울_release.apk`](../apk/상명F4_회전습관거울_release.apk) · SHA-256 `aa8dfae270653b17fd75d41156b1b568fbed89d2b9319c4e95c0971b3e491e0c` |
| 서명 | 저장소 밖 임시 PKCS12 키. `apksigner verify --verbose`: `Verifies`, v3=true, signer=1. 키·암호는 커밋하지 않음 |

Windows 임시 빌드 디렉터리의 Gradle/매니페스트/앱 코드/단위 테스트 9개 파일을 저장소 원본과 SHA-256으로 비교해 모두 일치함을 확인했다. 임시 디렉터리의 `local.properties`와 빌드 캐시는 저장소에 넣지 않았다.

## 재현과 판정

| 절차·관찰 | 판정 | 증거와 한계 |
|---|---|---|
| `gradlew.bat testReleaseUnitTest assembleRelease` | **실행 확인** | BUILD SUCCESSFUL. 경계 조건 단위 테스트 2건 통과(2/2). |
| APK 서명 검증, `adb uninstall org.hmgchallenge.turnmirror` → `adb install -r ...` → `adb shell am start -n org.hmgchallenge.turnmirror/.MainActivity` | **실행 확인** | 서명 v3 검증, 설치 `Success`, Activity 시작. 앱이 실제 화면에 표시됨. |
| 주차된 AVD에서 `실제 관찰 시작` | **실행 확인** | Vehicle SDK 초기화, 방향지시등·조향각 capability **READ**, 각 현재값 **OFF / 0.0°** 수신. 리스너 등록 요청과 정적 초기 콜백 수신. [화면](evidence/turn-mirror-20261115/observing-static.png) · [앱 로그](evidence/turn-mirror-20261115/runtime-log.txt). READ는 값 변화나 실차 권한을 보증하지 않음. |
| `관찰 종료 · 결과 보기` | **실행 확인** | 실제 SDK 세션은 후보 0건으로 표시하고 0건의 해석 한계를 안내. [화면](evidence/turn-mirror-20261115/review-zero.png). |
| `예시 화면 보기` → 첫 후보 `회전 아님` | **실행 확인** | 고정 합성 입력 3건을 검토 화면에 표시하고 정정 1건으로 갱신. 전 화면에 **예시/실제 운행 아님** 표시. [화면](evidence/turn-mirror-20261115/example-corrected.png). 실제 차량 사건 시험이 아님. |
| Virtual Console `BlinkRight`, 조향각 60° 요청 | **실패** | 각각 HTTP **503** `emulator_inactive`. `GET /vehicle/state`도 DDS bridge_ready=false, emulator_active=false. [요약 응답](evidence/turn-mirror-20261115/virtual-console-result.txt). 따라서 값 변화·실제 후보 생성은 **미확인**. |

재현 명령의 `adb`는 Windows Android SDK `platform-tools/adb.exe`를 사용했다. Virtual Console은 공식 지침대로 `adb forward tcp:8088 tcp:8088` 후 `127.0.0.1:8088`에 연결했다. [공식 Virtual Console 설정](https://document.pleos.ai/en/docs/connect/guide/getting-started/application-development/use-vehicle-emulator), [지원 카드](https://document.pleos.ai/en/docs/pleos-only/app-test-guide/virtual-console-guide). 확인일: 2026-11-15 KST.

첫 빌드에서는 조향각 호출이 `SecurityException`이었다. SDK 2.3.2 AAR의 `SteeringWheel` 메서드 `@RequiresPermission`이 `pleos.car.permission.CAR_DRIVE`를 요구함을 확인해 매니페스트를 수정했다. 수정 뒤 두 신호의 capability/현재값이 모두 읽혔다. 앱은 `CAR_LIGHTS`와 `CAR_DRIVE`만 선언한다. [공식 권한 원칙](https://document.pleos.ai/en/docs/pleos-only/connect-sdk-pleos/Vehicle/vehicle-permission)은 메서드 주석을 확인하라고 안내한다.

## 구현 경계와 다음 검증

- **실행 확인:** 관찰 시작/종료, 현재값 표시, 조향각 절댓값 45° 이상으로 넘어갈 때만 후보를 생성하는 코드, 18° 이하에서 재무장하는 코드, 합성 입력으로 후보·정정 UI 실행. 방향지시등은 후보가 생긴 순간의 **마지막 수신값**만 연결한다.
- **추론:** 이 초기 기준이 실제 회전을 어느 정도 포착할지는 모른다. 핸들 부호를 좌/우 도로 방향으로 해석하지 않고, 신호가 회전 전체에서 켜졌는지도 판단하지 않는다.
- **미확인:** 실제 방향지시등/조향각 변화 수신, 실제 후보 생성과 정확도, 시동 후 자동 실행, 백그라운드 유지, 기어 P/주행 제한 대응, 속도·정지 판정, 실차 동작, 사용자의 반복 가치.
- **미구현:** 우회전 정지 비율, 법규/안전 점수, 주간 추이, 가족 공유, 위치 저장. 실제 데이터는 세션 메모리에만 보관하고 앱 화면을 벗어나면 관찰을 종료한다. 정정 결과도 앱을 닫으면 사라진다.

다음 관문은 Virtual Console이 실제로 온라인인 Pleos 환경 또는 허가된 실차에서 **방향지시등 변화 → 조향각 변화 → 앱 후보 → 종료 후 정정**을 한 흐름으로 재현하고, 주행 제한·기어·백그라운드 범위를 따로 확인하는 것이다. 실제 주행에서 위험한 행동을 일부러 만들지 않는다.

공식 1차 안내는 [소스+실행 가능한 release APK](https://appdevchallenge.auto-hmg.io/guide/submission)를 선택 개발 산출물로 허용하지만 가산점 크기는 공개하지 않는다. 비공개 저장소의 공개 전환/대체 압축본 조건은 [추가 공지](https://appdevchallenge.auto-hmg.io/notice/c0a1a000-2026-4923-8000-000000000007)를 따른다. 확인일: 2026-11-15 KST.
