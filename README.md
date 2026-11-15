# 현대자동차그룹 차량용 앱 개발 공모전 2026

4인 팀의 아이디어 검증, Pleos 기술 확인, 1차 제출물 준비를 위한 작업 저장소입니다. 추가 팀원 초대와 역할 확인은 진행 예정입니다.

> **현재 상태 · 2026-11-15 KST:** 사용자가 〈회전 습관 거울〉을 1차 주제로 선택했다. [자체 앱 소스](turn-mirror-app/)와 [서명된 release APK](apk/turn-mirror-prototype_release.apk)를 준비했다. Pleos AVD에서 방향지시등·조향각의 capability READ와 정적 현재값 OFF/0.0, 관찰 시작·종료·결과 화면, 명확히 표시된 예시 재생·정정을 실행 확인했다. **차량값 변화 → 실제 후보 생성은 미확인**이다. Virtual Console의 두 값 변경 요청은 503 `emulator_inactive`였다. [재현 증거와 한계](docs/TURN_MIRROR_PROTOTYPE_2026-11-15.md)를 먼저 읽는다. 공식 접수·개인정보/서명 작성은 아직 팀의 별도 작업이다.

## 〈회전 습관 거울〉 1차 개발 산출물

앱은 사용자가 화면에서 관찰을 시작하면 Pleos Vehicle SDK 2.3.2로 방향지시등과 조향각을 읽고 구독한다. 조향각이 처음 45°를 넘는 순간을 **조향 변화 후보**로 남기고, 관찰을 종료한 뒤 후보를 `회전 아님`으로 정정할 수 있다. 이 기준은 초기 구현값이며 실제 도로 회전이나 안전·법규 준수를 판정하지 않는다. `예시 화면 보기`는 고정 합성 입력을 같은 검토 로직에 통과시키며 전 화면에 실제 운행이 아님을 표시한다. 세션은 메모리에만 있고 앱 화면을 벗어나면 관찰이 끝난다.

- APK: [`apk/turn-mirror-prototype_release.apk`](apk/turn-mirror-prototype_release.apk), SHA-256 `aa8dfae270653b17fd75d41156b1b568fbed89d2b9319c4e95c0971b3e491e0c`. 작업 시점에 팀명을 전달받지 못해 공식 권장 파일명 `팀명_앱이름_release.apk`로 확정하지 않았다. 제출 전에 이름을 바꾸면 새 커밋 해시를 메일에 적는다.
- 실행: Pleos AVD가 켜진 상태에서 저장소 루트의 `adb install -r apk/turn-mirror-prototype_release.apk`, 이어 `adb shell am start -n org.hmgchallenge.turnmirror/.MainActivity`. 앱에서 `실제 관찰 시작`을 누른다.
- 빌드: Windows JDK 17, Android SDK API 35와 Pleos Maven 접근이 있는 환경에서 `cd turn-mirror-app` 후 `gradlew.bat testReleaseUnitTest assembleRelease`. Gradle 8.10.2 wrapper가 포함된다. `app/build/outputs/apk/release/app-release-unsigned.apk`는 서명 전 산출물이므로, 제출 APK는 저장소 밖 개인 키로 서명해 `apk/`에 둔 파일이다. 키와 암호는 저장소에 없다.
- 시험 환경: Pleos Connect **v2.1.0** 시스템 이미지, Android Automotive API 34 x86_64, AVD `PleosOfficialProfile`, **2560×1440 / 160 dpi**. 앱은 API 35로 컴파일하고 Vehicle SDK 2.3.2를 사용했다.
- 검증 범위: `assembleRelease`, release unit test 2건, APK v3 서명 검증, 새 설치와 실행, 정적 신호 읽기, 종료 후 0건 검토, 합성 예시 3건과 정정 1건은 **실행 확인**. 값 변화·실제 회전 후보·속도·기어·주행 제한·실차·백그라운드/자동 시작은 **미확인**. 실제 차량 데이터 정확도나 수요·효과는 주장하지 않는다.

현재 메일에 GitHub를 선택 첨부하려면 이 한계를 그대로 설명하고 저장소 주소와 **최종 커밋 해시**를 적어야 한다. [공식 제출 안내](https://appdevchallenge.auto-hmg.io/guide/submission)와 [추가 공지](https://appdevchallenge.auto-hmg.io/notice/c0a1a000-2026-4923-8000-000000000007)는 비공개 저장소를 11월 16일까지 공개해 11월 23일까지 유지하거나, 마감 전 소스+APK 압축본 다운로드 주소를 보내도록 안내한다. 저장소 공개와 이메일 발송은 팀이 결정한다. 확인일: **2026-11-15 KST**.

> **아래 2026-11-10 상태와 후보 표는 당시 탐색 이력이다.** 현재 주제·앱 상태는 위 11월 15일 결과를 따른다.

> **과거 상태 · 2026-11-10:** [지금까지의 주제와 미선정 이유](docs/IDEA_HISTORY.md)를 모았다. 사용자가 〈다음 운전자〉, 픽업·주차 후보, 자동세차 준비안을 내렸고 간헐 고장 기록·말 콘텐츠 이어듣기는 재판단 후 주력 추천에서 철회했다. [3차 탐색](docs/IDEA_SCAN_ROUND3_2026-11-10.md)의 〈다시 큰길로〉는 주력 추천하지 않는다. [4차 재탐색](docs/IDEA_SCAN_ROUND4_2026-11-10.md)에서 사용자의 **목적지 없는 카메라 알림** 원안에 관련된 SDK 콜백을 확인했으나, [독립 재검증](docs/CAMERA_ALERT_INDEPENDENT_REVIEW_2026-11-10.md) 결과 **1차 단독 주력 추천은 철회**했다. 거리 자료형·자유 주행 상태는 SDK에 있으나 무목적지 이벤트·자동 대기·실제 중복 억제는 **실행 미확인**이고, RadarNav·순정 내비와의 남는 차별성도 입증하지 못했다. **당시 출품 주제는 미확정**이었다. 11월 9일의 [인계안](docs/FIRST_ROUND_DIRECTION_2026-11-09.md)과 [제출 문안](docs/SUBMISSION_DRAFT_NEXT_DRIVER.md)은 당시 이력이다.

## 처음 합류했다면

1. [공모전 규칙과 제출물](docs/CONTEST.md)을 읽고 **11월 15일 23:59 KST** 1차 마감과 **12월 3~4일** 실제 앱 시연 조건을 확인합니다.
2. [주제 탐색 이력과 미선정 이유](docs/IDEA_HISTORY.md)에서 지금까지의 후보, 사용자 탈락과 추천 철회를 먼저 구분합니다. 상세 조사인 [간헐 고장 기록](docs/COMMUNITY_IDEA_SCAN_ROUND2_2026-11-10.md)과 [픽업·주차](docs/COMMUNITY_IDEA_SCAN_2026-11-10.md)는 당시 이력이며 현재 제출 문안이 아닙니다.
3. [제품 목표와 후보](docs/PRODUCT_VISION.md), [이전 방향 재판단](docs/DIRECTION_REASSESSMENT_2026-11-09.md)과 [독립 재검증](docs/INDEPENDENT_REVIEW_2026-11-09.md)으로 선정 이전 판단과 SDK 실행 결과를 확인합니다.
4. [운전자 인터뷰 질문지](docs/DRIVER_INTERVIEW_2026-11-09.md)의 중립 질문으로 최근 실제 문제와 기존 해결법을 기록하고, SDK나 사용자 문제를 확인하면 [기술 검증표](docs/TECHNICAL_VALIDATION.md)의 기준으로 Issue에 증거를 기록합니다.

로컬에서 문서나 코드를 작업할 때는 조직 권한이 있는 계정으로 저장소를 복제합니다. 현재 앱 소스는 [`turn-mirror-app/`](turn-mirror-app/)에 있습니다. `experiments/`는 과거 차량 신호·기어·주행 UI 제한 진단용입니다.

```bash
git clone https://github.com/Develop-hyundai-kormg/hmg-vehicle-app-challenge-2026.git
cd hmg-vehicle-app-challenge-2026
```

## 11월 10일 후보 검토 이력

| 후보 | 현재 상태 | 먼저 확인할 것 |
|---|---|---|
| 간헐 고장 발생 조건 기록 〈증상 순간〉 | **주력 추천 해제** | 드문 사용 순간, OBD 앱과 차별성·Pleos 지속 기록 미입증 |
| 말 콘텐츠 〈말 이어듣기〉 | **주력 추천 해제** | 기존 오디오 앱의 자동 되감기와 겹치고 방향지시등 연동 효용 미입증 |
| 자동세차 진입 전 확인 | **사용자가 탈락 결정** | 순정 세차 모드와 겹치고 핵심 차량 상태·변화 시연 미확인 |
| 학원 픽업 대기/재방문 판단 | **사용자 피드백으로 주력에서 내림** | 과거 탐색 이력만 보존 |
| 큰 차의 주차장 진입 사전 확인 | **사용자 피드백으로 주력에서 내림** | 과거 탐색 이력만 보존 |
| 가족 자가용 인계 〈다음 운전자〉 | **사용자가 탈락 결정** | 과거 이력만 보존 |
| 목적지 없는 약 250m 카메라 알림·순정 내비 중복 억제 | 원래 제품 목표, **1차 주력 추천 철회** | RadarNav·순정 기능 대비 남는 사용자 이득과 무경로 이벤트·자동 시작·중복 억제의 실행 증거 |
| 주행 후 관찰 체크리스트·맥락 정정 | **1차 주력 보류** | 기존 분석 앱보다 반복 사용 이유와 실제 사건 관측 |
| 병원 환자 하차→보호자 주차→픽업 | **주력 추천 해제.** 경유지 미리보기 기술 실험·예비 기획 | 반복 사용자 가치, 일반 지도 대비 차이, 병원별 데이터 확대 비용 |
| 누적거리 기반 업무용 운행기록 | **검증 트랙.** 읽기 실행 확인, 기존 서비스와 겹침 | Pleos Fleet·카택스 사용자의 실제 미해결 비용과 자동 구간 분할 증거 |
| 공용차 연료 인수인계 〈다음 차례〉 | **조건부 탐색.** 현재 에뮬레이터에서 Fuel Percent 미지원 | 지원 실차·공유 프로필·교대 현장의 직접 증거와 기존 관리 앱 대비 이점 |
| EV 충전 확인 등 기타 후보 | 예비 아이디어 | 실제 충전 상태 신호와 기존 대안 대비 사용자 가치 |
| 정차 중 초보 운전자 실차 조작 연습 | **주력 보류.** 국내 경험담 1건, SDK 정적 조회만 확인. Pleos 기본 차량 제어 화면과 겹침 | 최근 실제 사례 3건 이상, 순정 설명서·음성·기본 제어 대비 이득, 차량값 변화 감지 |
| 비·김서림 준비 / 주차장 진입 기억 | 낮은 우선순위 | 반복 문제, 기존 기능의 빈틈, SDK 핵심 신호 |

공식 샘플에서 Vehicle SDK 정적 조회와 NaviHelper 경유지 경로 미리보기가 실행됐지만, Virtual Console의 차량값 변경은 503 오류로 실패했습니다. ADB 차량값 주입도 이 이미지의 `user` 빌드 정책으로 거절됐습니다. 창문·와이퍼 쓰기 성공 콜백 뒤 현재값은 바뀌지 않았고, 타이어·경고 정보는 권한 오류가 났습니다. **새 후보의 ETA 비교와 차량 치수 조회는 아직 실행 확인되지 않았습니다.** [진단 결과](experiments/consumer-signal-probe/README.md)

## 1차 전 핵심 Issue

| Issue | 확인할 결과 |
|---|---|
| [#1 팀원 AI 독립 검토](https://github.com/Develop-hyundai-kormg/hmg-vehicle-app-challenge-2026/issues/1) | 세 보고서 수신·재검증 완료. 네 번째 팀원 의견과 실제 사용자·현장 반례는 대기 |
| [#2 Pleos 기술 검증](https://github.com/Develop-hyundai-kormg/hmg-vehicle-app-challenge-2026/issues/2) | Vehicle/Navi 공식 샘플의 일부 기능 실행. Virtual Console 값 변경 실패, 자동 카메라·차량 콜백은 미검증 |
| [#3 혼합팀 참가 부문](https://github.com/Develop-hyundai-kormg/hmg-vehicle-app-challenge-2026/issues/3) | 운영사무국의 서면 답변 |
| [#4 기존 서비스·사용자 가치](https://github.com/Develop-hyundai-kormg/hmg-vehicle-app-challenge-2026/issues/4) | 대상 운전자의 반복 문제와 기존 앱의 남은 빈틈 |
| [#5 1차 제출물](https://github.com/Develop-hyundai-kormg/hmg-vehicle-app-challenge-2026/issues/5) | 공식 기획서, 선택 영상·소스/APK의 검증된 범위 |

## 핵심 일정

| 날짜(KST) | 단계 | 팀이 준비할 것 |
|---|---|---|
| 2026-11-15 23:59 | 1차 접수 마감 | 서명된 공식 DOCX 기획서 필수. 영상·소스와 release APK는 선택 가산점 대상 |
| 2026-11-23 | 1차 결과 | 합격 시 2차 시연 준비 상태 재점검 |
| 2026-12-03~04 | 2차 오프라인 PT | 에뮬레이터 또는 기기에서 **실제로 동작하는 앱 시연 필수** |
| 2026-12-18 | 최종 선정 결과 | 공식 발표 확인 |

일정은 [공식 일정](https://appdevchallenge.auto-hmg.io/guide/schedule), 제출 시각은 [공식 접수양식](https://appdevchallenge.auto-hmg.io/downloads/contest-application-consent.docx) 기준입니다. 변경 공지는 [공모전 공지사항](https://appdevchallenge.auto-hmg.io/notice)을 확인합니다.

## 문서 길잡이

| 문서 | 용도 |
|---|---|
| [CONTEST.md](docs/CONTEST.md) | 참가 자격, 평가 기준, 제출 형식, 혼합팀 부문 확인 |
| [IDEA_HISTORY.md](docs/IDEA_HISTORY.md) | 지금까지 찾은 주제, 사용자 판단·추천 철회·보류와 미선정 이유 |
| [PRODUCT_VISION.md](docs/PRODUCT_VISION.md) | 원래 사용자 요구, 후보 상태, 선행 서비스 반례 |
| [TECHNICAL_VALIDATION.md](docs/TECHNICAL_VALIDATION.md) | SDK/API 접근, 실험 순서, 미확인 조건 |
| [AAOS 진단 앱 소스](experiments/aaos-gear-probe/README.md) | 데스크탑에서 실행한 기어·UI 제한·직접 누적거리 권한 시험의 재현 코드 |
| [차량 신호 진단 소스](experiments/consumer-signal-probe/README.md) | 데스크탑 Pleos AVD에서 일반 운전자 후보 관련 조회·쓰기 재조회 결과 |
| [CONSUMER_DISCOVERY_2026-11-09.md](docs/CONSUMER_DISCOVERY_2026-11-09.md) | 국내 운전자 경험담, 기존 기능 반례, 새 후보와 15일까지의 결정 관문 |
| [COMMUNITY_IDEA_SCAN_ROUND2_2026-11-10.md](docs/COMMUNITY_IDEA_SCAN_ROUND2_2026-11-10.md) | 주력 추천에서 내린 간헐 고장 기록의 조사 이력 |
| [COMMUNITY_IDEA_SCAN_2026-11-10.md](docs/COMMUNITY_IDEA_SCAN_2026-11-10.md) | 내린 픽업·주차 후보의 당시 탐색 이력 |
| [IDEA_SCAN_ROUND3_2026-11-10.md](docs/IDEA_SCAN_ROUND3_2026-11-10.md) | 초행길 재탐색 문제와 3차 제외안의 근거·남은 관문 |
| [IDEA_SCAN_ROUND4_2026-11-10.md](docs/IDEA_SCAN_ROUND4_2026-11-10.md) | NaviHelper 카메라 이벤트 재발견, 사용자의 무목적지 알림 원안 재검증 조건, 4차 제외안 |
| [CAMERA_ALERT_INDEPENDENT_REVIEW_2026-11-10.md](docs/CAMERA_ALERT_INDEPENDENT_REVIEW_2026-11-10.md) | 카메라안의 선행 서비스·순정 기능·SDK 자료형·미검증 조건을 재평가하고 1차 주력 추천을 철회한 최신 판단 |
| [FIRST_ROUND_DIRECTION_2026-11-09.md](docs/FIRST_ROUND_DIRECTION_2026-11-09.md) | **탈락한 과거 작업안** 〈다음 운전자〉의 당시 판단 |
| [SUBMISSION_DRAFT_NEXT_DRIVER.md](docs/SUBMISSION_DRAFT_NEXT_DRIVER.md) | **사용 금지:** 탈락한 과거 작업안의 접수양식 초안 |
| [TEAM_REVIEW_NEXT_DRIVER.md](docs/TEAM_REVIEW_NEXT_DRIVER.md) | 탈락한 과거 작업안의 당시 팀 검토 프롬프트 |
| [DRIVER_INTERVIEW_2026-11-09.md](docs/DRIVER_INTERVIEW_2026-11-09.md) | 일반 자가용 운전자 10~15분 인터뷰·익명 기록 양식 |
| [INDEPENDENT_REVIEW_2026-11-09.md](docs/INDEPENDENT_REVIEW_2026-11-09.md) | 팀원 보고서 3건 재평가, 데스크탑 실행 증거, 후보 결정 관문 |
| [DIRECTION_REASSESSMENT_2026-11-09.md](docs/DIRECTION_REASSESSMENT_2026-11-09.md) | 병원안 주력 추천 해제와 후보별 결정 관문의 당시 판단 |
| [SUBMISSION_PLAN.md](docs/SUBMISSION_PLAN.md) | 공식 기획서 작성·영상·GitHub/APK 체크리스트 |
| [SUBMISSION_DRAFT_C.md](docs/SUBMISSION_DRAFT_C.md) | 주력 추천에서 내린 병원안의 예비 문안. 검증으로 복귀할 때만 사용 |
| [REVIEW_REQUEST.md](docs/REVIEW_REQUEST.md) | 팀원 AI 독립 검토 프롬프트와 답변 형식 |
| [REVIEW_ROUND_2_2026-11-09.md](docs/REVIEW_ROUND_2_2026-11-09.md) | 새 소비자 방향과 실제 SDK 반례를 반영한 2차 독립 검토 요청 |
| [DECISIONS.md](docs/DECISIONS.md) | 판단 변경과 이유의 기록 |
| [CONTRIBUTING.md](CONTRIBUTING.md) | Issue, 브랜치, PR, 자료 취급 규칙 |
| [apk/README.md](apk/README.md) | 선택 제출용 APK의 위치와 준비 조건 |

## 바로 확인할 공식 자료

[공모전 홈페이지](https://appdevchallenge.auto-hmg.io/) · [심사 기준](https://appdevchallenge.auto-hmg.io/guide/judging-awards) · [지원·제출 방법](https://appdevchallenge.auto-hmg.io/guide/submission) · [참가 규정](https://appdevchallenge.auto-hmg.io/guide/rules) · [FAQ](https://appdevchallenge.auto-hmg.io/faq) · [Pleos 개발 시작](https://document.pleos.ai/en/docs/pleos-only/getting-started)

## 저장소에서 지킬 경계

- 팀원 서명, 연락처, 개인정보 동의서 작성본, 인증 키와 비밀값, 운전자의 원본 위치·운행 기록을 커밋하지 않습니다.
- 문서·시안·영상에서 **실제 실행**, **문서상 지원**, **목업**, **미확인**을 구분합니다. SDK 인터페이스의 존재는 에뮬레이터나 실차 실행 성공의 증거가 아닙니다.
- 차량의 브레이크·조향 신호만으로 차로 변경, 앞차 거리, 법규 위반, 운전자의 잘못을 확정하지 않습니다. 주행 중 화면과 음성은 [Pleos 주행 안전 지침](https://document.pleos.ai/en/docs/pleos-only/vehicle-app-planning-guide/driver-distraction-guideline)을 따릅니다.

