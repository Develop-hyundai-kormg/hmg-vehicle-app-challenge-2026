# 일반 운전자 후보용 차량 신호 진단 (제품 앱 아님)

2026-11-09 KST, **Windows 데스크탑**의 공식 Pleos Connect v2.1 AVD(Android 14)에서 Vehicle SDK 2.3.2 / Vehicle Service 2.3.22를 사용했다. [공식 Vehicle SDK 샘플](https://document.pleos.ai/en/docs/pleos-only/tutorial/vehicle-sdk-sample-app)의 검증용 복제본에서 `MainActivity.kt`를 이 폴더의 진단 소스로 잠시 바꾸고 빌드·설치·실행했다. 실차 시험은 아니다. 원본 샘플 소스는 복구했다.

## 실행 결과

| 신호/동작 | 결과 | 해석 범위 |
|---|---|---|
| 앞좌석 왼쪽 창문 위치 capability/조회 | `READ_WRITE`, 범위 `0..10`; 현재값 `0` | AVD의 현재값 읽기 성공 |
| 앞유리 와이퍼 상태 capability/조회 | `READ`, `OFF/ON/SERVICE`; 현재값 `OFF` | AVD의 현재값 읽기 성공 |
| 와이퍼 스위치 capability | `READ_WRITE`, `OFF/CONTINUOUS_LEVEL_1/CONTINUOUS_LEVEL_3/AUTO` | **지원 enum 확인 후** 쓰기해야 함. 최초 `INTERMITTENT_LEVEL_1` 요청은 미지원 enum으로 실패 |
| 창문 위치 `5` 쓰기 | 성공 콜백 `5`, 즉시·5초 후 재조회는 모두 `0` | **상태 변화 확인 실패.** 콜백을 실제 창문 변경으로 해석하지 않음 |
| 와이퍼 `CONTINUOUS_LEVEL_1` 쓰기 | 성공 콜백, 즉시·5초 후 상태 재조회는 모두 `OFF` | **상태 변화 확인 실패.** 실제 와이퍼 작동의 증거 아님 |
| 타이어 압력 capability/조회 | 둘 다 `SecurityException: ... need Permission` | 샘플 Manifest에 `CAR_TIRES`를 요청했으나 `dumpsys package`의 granted install permissions에는 없었음. 이 AVD의 제3자 앱에서는 현재 접근 실패 |
| Safety telltales 조회 | `SecurityException: ... need Permission` | 현재 접근 실패 |
| 워셔액 부족 경고 조회 | `property ID ... not supported: 0x2120071e` | 이 AVD에서 미지원 |
| 연료 백분율 조회 | `property ID ... not supported: 0x21400be5` | 이 AVD에서 미지원. 이전 독립 시험 결과와 일치 |
| 남은 거리 조회 | `500000.0` | 비현실적 AVD 기본값으로 판단. 실제 주행 가능 거리의 증거 아님. 이 SDK 메서드는 deprecated 경고도 발생 |
| 누적거리, EV SOC, 충전 스위치, 운전석 문 위치 | 각 `0.0`, `100`, `false`, `0` | 정적 현재값 읽기 성공. 변화 감지나 실제 차량 정확도는 미확인 |

핵심 결론: **문서상 기능 → AVD capability → 현재값 읽기 → 상태 변경 → 실차 동작**은 서로 다른 증거다. 이번 AVD는 창문·와이퍼 읽기가 되지만, 앱의 `set` 성공 응답 후 값 변화는 확인되지 않았다. 따라서 이 두 신호의 변화에 의존하는 데모를 실제 동작으로 소개할 수 없다. 다른 차종이나 테스트 환경 전체가 불가능하다는 뜻은 아니다. [차종별 API 호환성 원칙](https://document.pleos.ai/en/docs/pleos-only/vehicle-app-planning-guide/api-compatibility-availability)

## 재현

1. 데스크탑의 **Pleos 전용 AVD**와 공식 Vehicle SDK 샘플 v1.0.0-sdk35를 준비한다. 일반 AAOS 에뮬레이터에서는 Vehicle Service가 없다.
2. 검증용 샘플 복제본의 `app/src/main/java/ai/pleos/playground/sample/vehicle/MainActivity.kt`를 이 폴더의 파일로 잠시 바꾼다. 진단 소스는 에뮬레이터라고 판단될 때에만 창문/와이퍼 쓰기를 시도하도록 보호한다. 실차에 설치하지 않는다.
3. 샘플이 실제로 빌드되도록 Vehicle SDK를 `2.3.2`로 맞춘 뒤 `:app:assembleDebug`를 실행한다. 빌드 산출물을 AVD에 설치하고 Activity를 실행한다.
4. `adb logcat -d -s HMG_PROBE`에서 요청별 콜백과 5초 뒤 재조회 결과를 확인한다.
5. 진단이 끝나면 샘플 원본 Activity를 복원하고 AVD를 종료한다.

저장소에 남긴 에뮬레이터 쓰기 보호 버전도 2026-11-09 Windows 데스크탑의 검증용 샘플 복제본에서 `:app:assembleDebug --offline`로 **빌드 성공**했다. 이 버전의 AVD 실행 로그는 앞의 보호 추가 전 진단 실행과 별도로 다시 수집하지 않았다. 빌드 직후 샘플 원본 Activity를 복원했다.

이 진단 APK는 공모전 1차 가산점용 **팀 자체 제품 앱**이 아니다. 공식 샘플을 기반으로 신호 접근만 시험한 것이다.
