# 회전 습관 거울 앱 소스

이 디렉터리는 팀이 작성한 작은 Android 앱이다. 공식 [Pleos Vehicle 샘플](https://github.com/Pleos-Playground/pleos-sample-vehicle-app)을 **SDK 사용 확인과 Gradle wrapper/버전 설정의 참고자료**로 사용했다. 샘플의 Activity, ViewModel, 탐색기 또는 UI 소스는 복사하지 않았다.

## 빌드와 실행

Windows JDK 17, Android SDK API 35, Gradle wrapper 8.10.2, Pleos Maven 저장소 접근이 필요하다. `local.properties`에 `sdk.dir=C:/.../Android/Sdk`를 지정하거나 Android SDK 환경변수를 설정한다. 서명 키는 저장소 밖에 둔다.

```powershell
cd turn-mirror-app
.\gradlew.bat testReleaseUnitTest assembleRelease
```

Gradle이 만드는 `app/build/outputs/apk/release/app-release-unsigned.apk`는 서명 전이다. [저장소에 둔 release APK](../apk/turn-mirror-prototype_release.apk)는 별도 개인 키로 서명하고 `apksigner verify --verbose`로 검증했다. `adb install -r ../apk/turn-mirror-prototype_release.apk` 후 `adb shell am start -n org.hmgchallenge.turnmirror/.MainActivity`로 연다.

## 작동 범위

`VehicleObserver`가 `CAR_LIGHTS`와 `CAR_DRIVE` 권한으로 방향지시등·조향각의 capability/현재값을 읽고 리스너를 등록한다. `ObservationSession`은 조향각의 절댓값이 45° 이상으로 넘어가는 것을 후보로 만들고 18° 이하로 돌아와야 다음 후보를 허용한다. 이 수치는 초기 구현값이다. 방향지시등은 후보 생성 순간의 마지막 수신값만 붙인다. 세션 데이터는 메모리에만 있다. 화면 이탈 시 관찰을 끝낸다.

`예시 화면 보기`는 코드에 고정된 합성 입력으로 후보 3건을 만들고 정정 버튼을 보여준다. 화면에 실제 운행이 아님을 명시한다. 이 재생은 실제 신호 변화 검증을 대신하지 않는다.

AVD 결과, 한계와 스크린샷은 [프로토타입 검증 기록](../docs/TURN_MIRROR_PROTOTYPE_2026-11-15.md)을 따른다.
