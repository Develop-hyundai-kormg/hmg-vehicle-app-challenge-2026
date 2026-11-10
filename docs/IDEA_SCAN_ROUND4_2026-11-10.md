# 4차 재탐색: 원안의 무목적지 카메라 안내를 다시 검증할 이유

> 2026-11-10 확인. **출품 주제 확정이나 구현 성공 보고가 아니다.** 사용자가 탈락시킨 〈다시 큰길로〉를 다시 추천하지 않는다. 제품 앱 코드·APK·실차 시험·운전자 인터뷰는 없다.

## 우선 검증할 한 문제

운전자는 매일 익숙한 길에서는 목적지를 입력하지 않을 수 있다. 이때 별도 내비 앱을 열지 않아도 필요한 카메라 안내를 약 250m 전에 한 번만 듣고 싶다는 것이 **사용자가 직접 제시한 요구**다. 순정 내비가 안내 중이면 우리 앱의 음성은 억제해야 한다. 경고가 없어도 안전하다는 보증, 법규 위반 방지 보증, 카메라 데이터의 완전성을 약속하지 않는다.

**왜 다시 봤나:** [2026-10-19 Pleos SDK 릴리스 노트](https://document.pleos.ai/en/docs/pleos-only/release-note/sdk-release-note)는 NaviHelper v3.0.5의 `CameraType`과 `onCautionPointInfo`를 명시한다. [NaviHelper 이벤트 가이드](https://document.pleos.ai/en/docs/pleos-only/connect-sdk-pleos/NaviHelper/event-listeners)는 `onCameraAlert()`, `onCautionPointInfo()`, `onNavigationStatus()` 콜백을 열거한다. 따라서 '카메라 관련 공개 콜백 자체가 없다'는 판단은 틀리다. **콜백 선언의 존재는 무목적지 전달·거리값·자동 실행·중복 억제·실차 작동의 증거가 아니다.** 같은 가이드는 내비 앱의 구현 상황에 따라 이벤트 전달이 달라질 수 있다고 경고한다.

| 판단 항목 | 현재 증거 | 1차 문안에 쓸 수 있는 표현 |
|---|---|---|
| 차량용 SDK 연결 | NaviHelper는 [Ready to Use](https://document.pleos.ai/en/docs/pleos-only/create-my-project), 경유지 경로 미리보기는 데스크탑 AVD에서 실행 | 사용 가능한 SDK와 기존 실행 범위만 기재 |
| 카메라 이벤트 | 공식 문서에 콜백 이름과 유형 있음 | **문서상 API 존재** |
| 목적지·내비 화면 없는 이벤트 | 직접 실험 없음 | **미확인** |
| 시동 뒤 자동 대기, 백그라운드 수신 | 직접 실험·정책 확인 없음 | **미확인** |
| 약 250m 시점 | 이벤트의 거리값·발행 시점 실험 없음 | **미확인** |
| 순정 내비 안내 중 자체 음성 억제 | `onNavigationStatus()`는 문서상 있으나 상태와 실제 안내 음성의 관계 미검증 | 로직 가설만 가능 |
| 실제 차종 | AVD 정적 조회와 NaviHelper 경유지 미리보기만 확인 | 실차 작동 주장 불가 |

## 선행 서비스와 창의성 반례

[Waze 도움말](https://support.google.com/waze/answer/14717826?hl=en)은 목적지 안내가 없어도 앱을 열어 둔 동안 구간단속 경고가 나온다고 설명한다. [RadarNav](https://radarnav.me/)도 목적지 없는 주행 알림과 Android Auto/CarPlay 표시를 홍보한다. 일부 순정 내비 역시 카메라 알림을 제공한다. 따라서 '목적지 없이 카메라를 알려주는 최초 앱'은 허위다. 비교해야 할 차이는 **차량 탑재 상태에서 운전자 조작 없이 시작하고, 다른 순정 안내와 중복되지 않는 경험**인데, 현재 이 핵심은 검증되지 않았다. [공식 1차 심사](https://appdevchallenge.auto-hmg.io/guide/judging-awards)의 창의성·차량특화성에 높은 점수를 받을 것이라고 말할 근거가 없다.

## 11월 15일 전 결정 관문

1. **데스크탑 Pleos AVD에서만** 진단용 리스너로 `onCameraAlert`, `onCautionPointInfo`, `onNavigationStatus`의 실제 이벤트를 기록한다. 목적지 없음/있음, 순정 내비 전면/후면을 분리한다. Virtual Console 차량 상태 PUT이 503이었던 점과 카메라 사건 생성 경로를 구분한다. AVD에 실제 카메라 위치 재현이 불가능하면 `미확인`으로 남긴다.
2. `CameraAlert`의 실제 거리·종류·반복 발행을 기록한다. 250m 전 안내를 앱이 선택할 수 있는지 확인한다. 시동/재부팅 뒤 자동 대기와 오디오 포커스·주행 UI 정책도 별도 확인한다.
3. 일반 자가용 운전자 5명에게 최근 한 달 목적지 없는 주행 빈도, 현재 카메라 안내 방법, 실제 놓침/중복 경험, 자동 안내를 켤 의향을 유도 없이 묻는다. Waze/티맵/순정 설정으로 충분하다는 답을 반례로 기록한다.
4. **무목적지 이벤트, 자동 대기, 음성 중복 억제 중 하나라도 재현 경로가 없으면 이번 1차의 핵심 기능을 구현 가능으로 쓰지 않는다.** 제출안 선정은 사용자 브리핑과 팀 검증 뒤 한다.

## 이번에 함께 내린 후보

| 후보 | 확인한 수요·선행 기능 | 주력으로 올리지 않은 이유 |
|---|---|---|
| 동승자가 남긴 초보 운전 코스 음성 메모 | [2026년 한국 초보 운전자가 남편과 출퇴근길을 미리 달린 사례](https://theqoo.net/review/4325873750), [가족에게 연수받고 코스를 반복한 사례](https://theqoo.net/review/4209227531) | [Mekavo](https://mekavo.com/lb/my)는 강사 음성 메모를 실제 위치에 연결한다. [Waze](https://support.google.com/waze/answer/7388769?co=GENIE.Platform%3DAndroid&hl=en-GB)는 지인 음성 안내 공유, [위치 음성 알림 앱](https://play.google.com/store/apps/details?id=dev.swaha.spotvoicereminder)은 지점 진입 재생을 제공한다. 국내 차내 사용 차이는 가능성일 뿐, 주행 중 위치·백그라운드 오디오도 미검증. |
| 주차 시간을 더한 실제 약속 도착시각 | 운전자 지각을 줄일 수 있다는 가설 | [Google Maps](https://blog.google/products-and-platforms/products/maps/put-it-park-new-features-google-maps/)는 주차 난이도와 주차 후 도보 안내를 제공했고, [Google 연구](https://research.google/blog/using-machine-learning-to-predict-parking-difficulty/)는 주행 종료부터 실제 도착까지 차이를 모델에 사용했다. 우리 앱은 별도 휴대폰 동반·도보 완료 기록이 필요하고 NaviHelper ETA 제공 여부가 확인되지 않았다. |
| 혼유·주유 카드 분실 예방 | [실제 카드 분실 글](https://theqoo.net/car/4385132573), 혼유 시 수리비 위험 | 차 안 앱이 주유기·카드 상태를 알 수 없고 [현대 인카페이먼트](https://ownersmanual.hyundai.com/ivi/ccNC/AVNT/KOR/English/Carpay.html)는 제휴 주유소에서 실물 카드 없이 결제한다. 단순 주유 전 확인표는 손실 예방을 입증하기 어렵다. |
| 이중주차 중립 기어 확인·주차 할인 확인 | 실제 불편은 존재 | 전자는 기존에 사용자가 탈락시킨 세차 준비표와 유사하고 기어/주차브레이크 제어·변화가 미확인, 후자는 사업자 앱과 겹치고 시설 규칙 데이터가 없다. |

## 현재 판단

**새 안을 억지로 선정하지 않는다.** 사용자 원안의 무목적지 카메라 안내는 다른 신규 후보보다 목표가 분명하고 NaviHelper에 관련 콜백이 문서상 있어 **우선 검증 후보**로 되돌릴 가치가 있다. 현재 기준에서 합격 가능성이 높다거나 12월 시연이 가능하다고 말할 수 없다. 팀이 위 관문을 통과한 뒤에만 1차 제출 주제로 확정한다.
