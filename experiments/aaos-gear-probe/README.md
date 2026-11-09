# AAOS 기어·주행 UI 제한 진단 앱

2026-11-09 데스크탑 Pleos Connect v2.1.0 에뮬레이터 시험에 쓴 **진단용 소스**다. 공모전 제품 앱, 제출용 APK, Pleos SDK 전체 동작의 증거가 아니다. 앱은 기어 선택, AAOS 직접 누적거리 권한, 현재 `CarUxRestrictions`를 한 번 조회하고 화면·logcat에 표시한다.

## 재현

Android SDK API 34, JDK 17, Android SDK Platform Tools와 AAOS 에뮬레이터가 필요하다. 포함한 Gradle wrapper는 8.10.2, Android Gradle Plugin은 8.8.2다. Android Studio에서 이 디렉터리를 프로젝트로 열거나, 터미널에서 실행한다.

```bash
./gradlew :app:assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.hmg.validation.aaosprobe/com.hmg.validation.MainActivity
adb logcat -d -s HMG_AAOS_PROBE:I
```

Windows PowerShell에서는 `./gradlew` 대신 `.\gradlew.bat`을 사용한다. 생성되는 APK는 **debug 진단 앱**이므로 공식 제출물의 release APK가 아니다.

## 관측 결과와 한계

데스크탑의 Pleos 전용 AVD에서 `lifecycle_ready=true`, `gear_selection=4`(P), `ux_restrictions=active=0,requiresDO=false`를 관측했다. `PERF_ODOMETER` 직접 조회는 `CAR_MILEAGE`를 선언해도 권한이 부여되지 않아 `SecurityException`이었다. 반면 공식 Pleos Vehicle 샘플에서는 Vehicle SDK의 누적거리 읽기가 성공했다. 따라서 두 API 경로를 섞어 설명하면 안 된다. [Android Car 권한](https://developer.android.com/reference/android/car/Car), [VehiclePropertyIds](https://developer.android.com/reference/android/car/VehiclePropertyIds), [Pleos Vehicle SDK](https://document.pleos.ai/en/docs/pleos-only/connect-sdk-pleos/Vehicle/intro)

차량값 변경을 위한 Virtual Console은 503 `emulator_inactive`였으므로 D 전환, 기어 변경 콜백, 주행 중 제한 활성화는 이 앱으로 확인하지 못했다. 에뮬레이터에서 P와 제한 해제 상태를 읽은 결과를 실차·모든 차종의 권한으로 일반화하지 않는다. [전체 실험 기록](../../docs/INDEPENDENT_REVIEW_2026-11-09.md)

추가로 AAOS 공식 문서의 ADB 차량값 주입을 시도했지만, 이 Pleos 이미지(`ro.build.type=user`)의 `cmd car_service inject-vhal-event`는 `requires non-user build` 보안 오류로 거절됐다. 구형 `dumpsys activity service` 경로로도 기어 조회값이 바뀌지 않았다. [최신 시험 기록](../../docs/DIRECTION_REASSESSMENT_2026-11-09.md)
