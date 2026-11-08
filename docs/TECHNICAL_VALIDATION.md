# Pleos 기술 검증표

기준일: **2026-11-08**. 이 표의 `문서 확인`은 팀 계정·에뮬레이터·차량에서의 성공을 뜻하지 않는다. 현재 이 저장소에 앱 코드나 실행 결과는 없다.

## 개발 환경과 공개 SDK 상태

- [공식 시작 가이드](https://document.pleos.ai/en/docs/pleos-only/getting-started)는 Android Studio, Android SDK API 34, `minSdk` API 28 이상, Pleos Connect 에뮬레이터 시스템 이미지 사용을 안내한다. [공모전 FAQ](https://appdevchallenge.auto-hmg.io/faq)는 Gradle 8 이상을 요구한다. 팀 컴퓨터별 설치 상태는 확인해야 한다.
- [Pleos 프로젝트·SDK 접근 문서](https://document.pleos.ai/en/docs/pleos-only/create-my-project)는 Vehicle State와 NaviHelper를 **Ready to Use**, FusedLocation·TextToSpeech·SpeechToText·Vehicle Control을 **Not Available Yet**로 표시한다. 한편 [FusedLocation 사용 문서](https://document.pleos.ai/en/docs/pleos-only/connect-sdk-pleos/FusedLocation/intro)는 API를 설명한다. 실제 프로젝트/계정 상태를 확인하기 전에는 사용 가능하다고 쓰지 않는다.
- [차종별 API 호환성](https://document.pleos.ai/en/docs/pleos-only/vehicle-app-planning-guide/api-compatibility-availability)은 Vehicle SDK capability와 AAOS 속성의 지원이 차종마다 다를 수 있다고 안내한다. 내비 탑재 여부도 NaviHelper 사용 범위에 영향을 준다.

## 기능별 검증 관문

| 기능·주장 | 현재 근거 | 아직 필요한 실행 증거 | 실패하면 |
|---|---|---|---|
| 차량 상태 콜백 | [Vehicle SDK](https://document.pleos.ai/en/docs/pleos-only/connect-sdk-pleos/Vehicle/intro)에 브레이크·방향지시등 등 도메인이 있고 [가상 콘솔](https://document.pleos.ai/en/docs/pleos-only/app-test-guide/virtual-console-guide)에 입력 카드가 있다 | 앱에서 권한·capability 확인 후 콜백 수신, 화면 반응·로그·녹화 | 수신되지 않는 신호를 핵심 데이터로 쓰지 않음 |
| 차량 속도와 급가감속 후보 | [Pleos FAQ](https://document.pleos.ai/en/docs/pleos-only/vehicle-app-developer-guide/vehicle-app-faq)는 AAOS `PERF_VEHICLE_SPEED`를 안내한다 | `CarPropertyManager` 속성 지원·권한·샘플링 주기 확인, 변화량 계산의 오탐 점검 | 브레이크 페달 상태를 감속량·잘못된 운전으로 표시하지 않음 |
| 무목적지 카메라 정보 | [NaviHelper 이벤트 문서](https://document.pleos.ai/en/docs/pleos-only/connect-sdk-pleos/NaviHelper/event-listeners)에 `onCameraAlert()`가 있으나 이벤트 전달은 내비 구현에 따라 달라진다 | **목적지 없음+내비 화면 비활성** 상태에서 실제 콜백·거리·방향 확인 | 독립 위치·카메라 데이터 경로를 별도 평가하거나 A를 1차 구현 기능에서 제외 |
| 순정 내비 중복 억제 | 같은 문서에 `onNavigationStatus()` 등 상태 콜백이 있다 | 순정 내비가 켜지고 꺼질 때의 신호가 실제 카메라 안내 상태를 충분히 구분하는지 | 중복 억제를 구현된 기능이라고 쓰지 않음 |
| 위치·약 250m 경고 | [FusedLocation 문서](https://document.pleos.ai/en/docs/pleos-only/connect-sdk-pleos/FusedLocation/intro)에 위치 API가 있으나 접근 문서와 상태가 엇갈린다 | 계정 접근, 위치 권한, 주행 중 위치 갱신, 방향·도로 매칭, 카메라 DB의 최신성·허가 범위 | 위치 기반 경고를 핵심 시연에서 제외 |
| 자동 시작·백그라운드 | [앱 구조 문서](https://document.pleos.ai/en/docs/pleos-only/vehicle-app-planning-guide/app-structure-screen-model)는 화면 이탈 후 앱이 백그라운드에 남을 *수 있다*고만 한다 | 차량 재시동/에뮬레이터 재부팅, 다른 앱 전환, 장시간 유지 뒤 이벤트·오디오 확인 | 사용자의 원래 무수동 실행 경험이 성립하지 않으므로 범위와 주제 재결정 |
| 주행 중 음성·앱 분류 | [내비 앱 정책](https://document.pleos.ai/en/docs/pleos-only/vehicle-app-developer-guide/navigation-app-development-guide)은 비활성/백그라운드 경로 안내와 동시 경로 안내를 제한한다. 카메라 전용 경고에 이 정책이 적용되는지는 미확인 | 앱 카테고리와 오디오 포커스·안내 허용 범위에 대한 공식 확인, 주행 제한 상태 테스트 | 기능을 앱 카테고리 우회 방식으로 구현하지 않음 |
| 주차 후 체크리스트 | [주행 안전 설계](https://document.pleos.ai/en/docs/pleos-only/vehicle-app-planning-guide/driver-distraction-guideline)는 `CarUxRestrictions` 적용을 요구한다 | 기어/정차 신호, 주행 제한 반영, 이벤트 기록·주차 후 첫 화면·오탐 정정 재현 | 주행 중 긴 설명·입력 화면을 내지 않음 |
| 앞차 거리·실제 차로변경 | 일반 공개 Vehicle/AAOS 자료만으로 모든 대상 차의 전방 실측거리·차로 인식 접근은 확인되지 않았다 | 해당 차종과 권한에 대한 직접 증거 | 자동 측정·안전 판정 항목에서 제외 |

가상 콘솔의 브레이크 페달·핸들·방향지시등 값을 바꾸는 영상은 **신호 배선 시험**이다. 실제 급제동, 차로 변경, 외부 상황, 운전자 잘못이 발생했음을 입증하지 않는다.

## 가장 작은 실험 순서

1. **환경:** 팀 컴퓨터의 Android Studio·Pleos 이미지, 프로젝트 계정/SDK 접근, 앱 설치를 확인한다. 첫 Vehicle SDK 조회가 성공하는지 기록한다.
2. **이벤트:** 가상 콘솔 상태 변화 → 앱 콜백 → 화면 반응을 한 번에 녹화한다. API·권한·capability·에뮬레이터 버전을 함께 기록한다.
3. **원래 목표의 관문:** 무목적지 카메라 콜백, 순정 내비 활성 감지, 다른 앱 전환·재시동 후 알림을 각각 분리 시험한다. 하나의 성공으로 나머지를 성공 처리하지 않는다.
4. **주차 후 흐름:** 주행 중 `CarUxRestrictions`로 입력을 제한하고, 주차 후 관측 사실만 1~3개 표시하여 확인·정정·상세를 시험한다.
5. **실차가 필요하면:** 먼저 Pleos Connect 탑재 차종/트림과 시험 APK 설치·디버깅 허용을 확인한다. 자연 주행만 관찰하고 급제동·급차로변경을 일부러 만들지 않는다.

## 실험 기록 형식

각 Issue 또는 PR에 다음을 남긴다.

```text
검증할 주장:
환경: 기기/에뮬레이터, Pleos·Android·SDK 버전
권한·capability:
재현 절차:
예상 결과:
실제 결과와 로그/녹화 링크:
판정: 실행 확인 / 문서 확인 / 실패 / 미확인
앱 제출 문구에 미치는 영향:
```

운행 데이터에는 동의·최소 수집·보관/삭제·공유 차량의 운전자 분리 정책이 필요하다. 원본 주행 경로와 식별자를 Issue나 공개 시연 영상에 올리지 않는다. [Pleos 개인정보 설계](https://document.pleos.ai/en/docs/pleos-only/vehicle-app-planning-guide/security-privacy)

