# 현대자동차그룹 차량용 앱 개발 공모전 2026

3인 팀의 아이디어 검증, Pleos 기술 확인, 1차 제출물 준비를 위한 작업 저장소입니다.

> **현재 상태 · 2026-11-08:** 주제 미확정 · 앱 코드 없음 · Pleos 에뮬레이터/실차 검증 없음 · 접수 전. 이 저장소의 아이디어는 출품 결정이나 구현 완료를 뜻하지 않습니다.

## 처음 합류했다면

1. [공모전 규칙과 제출물](docs/CONTEST.md)을 읽고 **11월 15일 23:59 KST** 1차 마감과 **12월 3~4일** 실제 앱 시연 조건을 확인합니다.
2. [제품 목표와 후보](docs/PRODUCT_VISION.md)를 읽습니다. 사용자가 원하는 카메라 알림과 주행 후 체크리스트는 구분해서 평가합니다.
3. [독립 검토 요청](docs/REVIEW_REQUEST.md)을 본인 AI에 전달하고 다른 팀원의 답을 보기 전에 의견을 남깁니다.
4. SDK나 사용자 문제를 확인하면 [기술 검증표](docs/TECHNICAL_VALIDATION.md)의 기준으로 Issue에 증거를 기록합니다. 아이디어를 확정하기 전에는 구현 가능성을 사실로 쓰지 않습니다.

## 현재 판단 한눈에 보기

| 후보 | 현재 상태 | 먼저 확인할 것 |
|---|---|---|
| 목적지 없는 약 250m 카메라 알림·순정 내비 중복 억제 | 원래 제품 목표, **구현·차별성 미검증** | 순정 기능과의 차이, 무경로 이벤트/위치, 자동 시작, 앱 분류와 음성 정책 |
| 주행 후 관찰 체크리스트·맥락 정정 | **조건부 후보** | 기존 운전 분석 서비스 대비 실사용 가치, 차량 신호와 주차 후 화면의 실제 동작 |
| 병원 환자 하차→보호자 주차→픽업 | **현장 정보 대기** | 병원 1곳의 합법 승하차점·무계단 출입구·픽업점 |
| 낯선 차 조작 리허설 | 우선 후보에서 제외 | 기존 차량 안내와의 차이·반복 사용 이유가 새로 확인될 때만 재검토 |

현재 **1차 주력안과 합격 가능성은 확정하지 않았습니다.** 기존 서비스의 반례와 Pleos 미검증 조건을 확인한 결과, 앞서 추천했던 체크리스트안을 주력안에서 조건부 후보로 내렸습니다. [판단 근거](docs/PRODUCT_VISION.md)

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
| [PRODUCT_VISION.md](docs/PRODUCT_VISION.md) | 원래 사용자 요구, 후보 상태, 선행 서비스 반례 |
| [TECHNICAL_VALIDATION.md](docs/TECHNICAL_VALIDATION.md) | SDK/API 접근, 실험 순서, 미확인 조건 |
| [SUBMISSION_PLAN.md](docs/SUBMISSION_PLAN.md) | 공식 기획서 작성·영상·GitHub/APK 체크리스트 |
| [REVIEW_REQUEST.md](docs/REVIEW_REQUEST.md) | 팀원 AI 독립 검토 프롬프트와 답변 형식 |
| [DECISIONS.md](docs/DECISIONS.md) | 판단 변경과 이유의 기록 |
| [CONTRIBUTING.md](CONTRIBUTING.md) | Issue, 브랜치, PR, 자료 취급 규칙 |
| [apk/README.md](apk/README.md) | 선택 제출용 APK의 위치와 준비 조건 |

## 바로 확인할 공식 자료

[공모전 홈페이지](https://appdevchallenge.auto-hmg.io/) · [심사 기준](https://appdevchallenge.auto-hmg.io/guide/judging-awards) · [지원·제출 방법](https://appdevchallenge.auto-hmg.io/guide/submission) · [참가 규정](https://appdevchallenge.auto-hmg.io/guide/rules) · [FAQ](https://appdevchallenge.auto-hmg.io/faq) · [Pleos 개발 시작](https://document.pleos.ai/en/docs/pleos-only/getting-started)

## 저장소에서 지킬 경계

- 팀원 서명, 연락처, 개인정보 동의서 작성본, 인증 키와 비밀값, 운전자의 원본 위치·운행 기록을 커밋하지 않습니다.
- 문서·시안·영상에서 **실제 실행**, **문서상 지원**, **목업**, **미확인**을 구분합니다. SDK 인터페이스의 존재는 에뮬레이터나 실차 실행 성공의 증거가 아닙니다.
- 차량의 브레이크·조향 신호만으로 차로 변경, 앞차 거리, 법규 위반, 운전자의 잘못을 확정하지 않습니다. 주행 중 화면과 음성은 [Pleos 주행 안전 지침](https://document.pleos.ai/en/docs/pleos-only/vehicle-app-planning-guide/driver-distraction-guideline)을 따릅니다.

