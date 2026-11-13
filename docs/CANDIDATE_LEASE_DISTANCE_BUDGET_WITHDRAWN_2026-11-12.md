# 철회한 후보: 리스·장기렌트 거리예산

판단일: **2026-11-12 KST** · 상태: **초기 선정 후 같은 날 주력 추천 철회**. 아래는 초기 기획과 당시 반례 검토의 이력이며 현재 제출 문안이 아니다.

**추가 반증:** [MileMind](https://milemind.ggrydev.co.uk/)가 리스 약정거리·초과액 전망과 경로 계획의 약정 영향까지 이미 제공한다. [NissanConnect Services](https://www.nissanusa.com/owners/connect.html)는 차량 화면에서 리스 잔여거리·추가 거리 구매를 제공한다. [현대캐피탈 법인오토케어](https://www.hyundaicapital.com/corp/atcr/CPCPAC0101.hc)는 실시간 운행거리 모니터링을 제공하고 [기아 프라이버시 고지](https://privacy.kia.com/kr/ko/notice/49089)에는 현대캐피탈 앱에 차량 누적거리 정보를 제공하는 항목이 있다. 한국 Pleos 순정 내비 경로와 계기판을 한 번에 결합하는 편의는 남지만, 일반적인 경로 간 몇 km 차이의 초과 부담은 작고 장거리 여행의 교통수단 결정은 차에 앉기 전에 끝나는 경우가 많다. 따라서 차량 안 앱을 반복 사용하게 할 차이가 충분하다고 보기 어렵다. 초기 경쟁 조사에서 가장 가까운 경로 결합 반례와 자동차 금융사 데이터를 놓쳤다.

후속 탐색은 [보험 마일리지 할인 경계](FIRST_ROUND_TOPIC_INSURANCE_MILEAGE_2026-11-12.md)로 옮겼다. 본문의 '선정' 표현은 당시 가설이며 현재 판단에 적용하지 않는다.

## 한 문장

**거리예산**은 주행거리 제한이 있는 리스·장기렌트 운전자가 **출발 전**, 차량의 실제 누적거리와 계약상 남은 거리를 현재 내비 경로에 대조해 이번 이동의 예상 초과 부담을 보고 경로를 선택하는 Pleos 차량용 앱이다.

계약의 기준거리·만기일·초과 단가는 운전자가 **자신의 계약서에서 한 번 입력**한다. 앱은 계약 조건을 추측하거나 초과요금이 지금 확정됐다고 말하지 않는다. 핵심 결과는 “현재 17,800km, 약정 30,000km 중 12,200km 남음 → 현재 속도라면 만기 약 5,535km 초과 예상 → 오늘 왕복 500km는 다른 주행량이 같다는 가정에서 예상 부담을 약 6만 원 늘림”처럼 **결정 가능한 금액과 전제**를 함께 보여주는 것이다. 아래 수치는 예시 계약(2025-05-12~2028-05-12, 시작 0km, 30,000km, 120원/km)을 계산한 값이며 실제 사용자 데이터가 아니다.

## 왜 이 문제인가

- [현대캐피탈 신차 리스](https://www.hyundaicapital.com/nwcafn/carleas/CPNCAL0101.hc)는 연간 약정 1만~5만km와 무제한 상품을 구분하고, 반납 시 초과 운행 부담금을 **초과 km × 계약별 km당 80~200원**으로 제시한다. [장기렌터카](https://www.hyundaicapital.com/nwcafn/ltmrtlc/CPNCLC0101.hc)에도 거리 약정과 초과 부담금이 있다. 따라서 손실은 실제 계약 구조에 있다. 개별 계약의 면제·인수·반납 조건은 다를 수 있다.
- 기존 [LeasePace](https://apps.apple.com/us/app/leasepace-car-lease-mileage/id6785332761), [LEASE Tracker](https://leasetracker.ales-urbanek.cz/en/), [Leastimator](https://apps.apple.com/us/app/leastimator/id1228501014)는 남은 거리·예상 초과액·여행 전 계산을 이미 다룬다. Leastimator의 Tesla 연동은 자동 누적거리 조회도 제공하고, [MileGuard](https://mileguard.co/help/how-tracking-works/)는 휴대폰의 자동 주행 감지를 쓴다. [Ford Credit 앱](https://www.ford.com/finance/mobile-app/)도 리스 고객에게 거리 정보를 보여준다. **리스 거리 계산이나 자동 추적 자체를 새 발명이라고 주장하지 않는다.**
- 남는 가설은 **Pleos 차 안에서 지금 계기판의 누적거리를 직접 읽고, 순정 내비의 현재 경로 거리와 계약상 한도를 같은 출발 화면에서 연결**하면 수동 숫자 입력과 앱 전환 없이 여행·장거리 출장 전에 선택이 바뀐다는 것이다. 해외 Ford Credit 등에도 유사 관리 도구가 있으므로 제품 차별성은 해당 결합과 한국 Pleos 환경에서 검증해야 한다.
- 공개 사례 중 [리스 운전자의 거리 초과 우려](https://www.reddit.com/r/CarLeasingHelp/comments/1f5ivni)와 [수동 계기판 입력의 반복 부담](https://www.reddit.com/r/promoteMyApp/comments/1wfhne6/i_built_a_lease_mileage_tracker_because_i_kept/)은 문제를 찾는 단서일 뿐 국내 Pleos 운전자의 수요 규모를 증명하지 않는다. **반납 예정 운전자 인터뷰와 같은 과제 비교는 아직 하지 않았다.**

## 제품 흐름

1. **최초 1회·주차 중:** 계약 시작일·만기일, 계약 시작 계기판 km, 총 허용 km, 계약서의 초과 km 단가를 입력한다. 반납/인수 여부를 기록하고, 인수 예정이면 초과 부담 계산을 기본으로 꺼 둔다.
2. **출발 전·주차 중:** Vehicle SDK `getOdometer().getCurrentDistanceDriven()`로 누적거리를 읽는다. 값·단위·capability가 유효할 때만 `남은 약정거리 = 총 허용거리 - (현재 누적거리 - 계약 시작 누적거리)`를 계산한다. 이전 읽기 기록이 있으면 최근 운행 추세로 만기 예상치를 보여주고, 기록이 부족하면 예측을 숨긴다.
3. **내비 목적지가 있을 때:** NaviHelper `getDestinationInfo()`의 `remainDistance`와 `remainTime`을 받아 현재 경로가 거리예산에서 차지하는 양을 보여준다. 왕복은 귀로가 확인되지 않으면 편도 거리의 단순 2배를 기본값으로 쓰지 않고 사용자 확인을 받는다. 운전자가 원하면 `changeRouteOption(SHORT_DISTANCE)`를 요청한다. 변경 전후의 거리·시간을 **각각 콜백으로 확인했을 때만** 절약 거리와 소요 시간 차이를 표시한다. 이 단계는 공식 API에 근거한 **구현 가설**이며 에뮬레이터의 실제 거리 콜백·경로 변경 성공은 아직 미검증이다.
4. **운행 후·주차 중:** 다음 앱 실행 시 누적거리를 다시 읽어 예상치와 실제 증가분을 대조한다. 주행 중 앱 화면을 열게 하거나 경로를 자동으로 바꾸지 않는다.

경로 정보가 없거나 내비가 미탑재인 차량은 **거리예산 기본 화면**만 제공한다. Vehicle SDK 누적거리 조회가 실패하면 계기판을 보고 직접 입력하는 명시적 대체 흐름을 제공하되, 이를 SDK 기반 자동 조회의 성공으로 보고하지 않는다. 단위가 불명확하거나 현재값이 시작값보다 작으면 금액을 계산하지 않는다.

## 선정·피드백·재검증 세 번

| 단계 | 후보와 반례 | 판단 수정 |
|---|---|---|
| 문제 선정 | 공영주차 감면 계산은 [서울시 공식 감면 규칙](https://news.seoul.go.kr/traffic/archives/26877)처럼 지역·자격 조건을 계속 관리해야 하고 기존 계산기·자동 감면과 겹친다. 오디오 길이 맞추기는 [Tunelapse](https://tunelapse.com/)가 이미 한다. 목적지 폐점 경고·주차 후 도보는 Google Maps에도 있다. | 외부 시설 데이터·콘텐츠 공급 없이 실제 차량 누적거리로 금전 결정을 바꾸는 계약 거리 문제를 선택. |
| 첫 피드백 | 단순 리스 거리 추적은 LeasePace·LEASE Tracker와 중복된다. Tesla 연동 제품에는 자동 읽기도 있다. | 결과를 단순 계기판 화면이 아니라 **현재 경로를 선택하기 전의 거리예산 판단**으로 옮기고 계약별 초과 단가를 함께 표시. |
| 기술 재검증 | 기존 데스크탑 Pleos AVD에서는 Vehicle SDK 누적거리 **현재값 0.0과 READ capability**를 실제로 읽었다. NaviHelper 목적지 요청은 순정 내비 **미리보기**까지 실행했다. 반면 Virtual Console의 차량값 변경은 503이었고 Navi의 `remainDistance` 전달은 아직 실행 확인하지 않았다. | 1차 기획서에는 누적거리 읽기만 실행 확인으로 쓰고, 경로 거리·전후 비교는 문서 확인/2차 구현 과제로 쓴다. 주행거리 변화 시연을 약속하지 않는다. |

## 공식 심사 기준에 맞춘 1차 문안

**문제·타깃.** 주행거리 제한이 있는 리스·장기렌트 이용자는 계약상 남은 거리와 지금 계획한 이동의 비용을 출발 전에 따로 계산한다. 누적거리는 차에, 허용량과 초과 단가는 계약서에, 경로 거리는 내비에 흩어져 있다. 만기에 한꺼번에 드러나는 초과 부담금을 미리 인지하고 장거리 이동·교통수단·경로를 선택할 수 있게 한다.

**차량 특화·창의성.** Pleos Vehicle SDK가 읽은 **실제 계기판 누적거리**를 계약상 한도와 결합하고, NaviHelper가 제공하는 **현재 순정 경로의 거리**를 같은 화면에서 비용으로 환산한다. 휴대폰 거리관리 앱의 수동 계기판 입력 및 별도 내비 조회를 줄이는 것이 목표다. 단순 잔여거리 표시나 기존 주행기록 앱을 재현하는 것에 그치지 않는다.

**기술.** Vehicle SDK 정적 누적거리 조회는 Pleos AVD에서 확인했다. [Vehicle SDK 개요](https://document.pleos.ai/en/docs/pleos-only/connect-sdk-pleos/Vehicle/intro)와 [NaviHelper 목적지 조회](https://document.pleos.ai/en/docs/pleos-only/connect-sdk-pleos/NaviHelper/destinations), [경로 옵션](https://document.pleos.ai/en/docs/pleos-only/connect-sdk-pleos/NaviHelper/routes)은 공식 문서로 확인했다. `getDestinationInfo()`에서 실제 거리·시간 값과 경로 변경 전후 값이 나오는지는 별도 검증 대상이다. Vehicle State와 NaviHelper의 [프로젝트 접근 상태](https://document.pleos.ai/en/docs/pleos-only/create-my-project)는 Ready to Use지만 목표 차종·권한은 별도 확인한다.

**사업·재사용 가설.** 초기는 한국의 Pleos 탑재 리스·장기렌트 반납 예정 이용자다. 월 1회 거리 추세 확인과 장거리 이동 전 사용이 재방문 장면이다. 무료 기본 기능으로 시작하고 다수 계약·만기 시나리오 비교 또는 리스사 연계는 실제 고객 의사와 제휴 가능성 검증 후 확장한다. 예상 MAU나 유지율은 **지원 차량 수 × 거리 제한 계약 비율 × 설치율 × 월 재방문율**로 산정하며, 조사 없이 숫자를 만들지 않는다.

## 12월 실동작 시연 계획과 탈락 조건

- **최소 실동작:** 정차/P 상태에서 계약 샘플 입력 → Pleos Vehicle SDK로 누적거리 현재값 조회 → 계약 잔여 km·초과 예상액 계산 → 활성 내비 경로가 있으면 NaviHelper 목적지 조회 결과 표시. 차량값이 0.0으로 고정된 AVD라면 실제 읽은 값과 별도의 **예시 계약/계산 시나리오**를 명확히 구분한다. 화면 전환·계산이 되는 자체 앱 APK가 필요하며 공식 샘플 영상을 제품 영상으로 쓰지 않는다.
- **11월 14일 전 우선 시험:** AVD에서 목적지 설정 후 `getDestinationInfo()`의 `remainDistance`·`remainTime` 수신, `SHORT_DISTANCE` 변경 결과, 거리 단위 대조를 기록한다. 도착 전 콜백이 불안정하면 ‘경로 거리 자동 반영’을 1차 **구현 완료** 표현에서 제외하고 서류의 검증 과제로 둔다.
- **사용자 확인:** 거리 제한 계약 운전자 3~5명에게 최근에 실제로 약정거리를 확인한 장면, 현재 방법, 여행 전 거리 때문에 선택을 바꾼 적이 있는지 묻는다. 같은 “다음 주 500km 여행을 갈지 판단” 과제를 기존 앱/계기판과 거리예산 시안으로 수행해 시간·오류·선호를 비교한다. 응답을 받기 전에는 수요가 검증됐다고 쓰지 않는다.
- **핵심 반증:** 대상 차종에서 누적거리 조회 권한이 거절되거나, 내비 경로 거리 콜백이 사용 가능한 상황에서 계속 나오지 않거나, 운전자가 현재 해결법 대비 의사결정 이득을 느끼지 못하면 ‘경로 연동 거리예산’의 차별성을 재평가한다. 차종별 지원과 배포 권한은 [공식 호환성·심사 정책](https://document.pleos.ai/en/docs/pleos-only/vehicle-app-planning-guide/api-compatibility-availability)을 따로 확인한다.

## 확인한 사실의 경계

- **실행 확인:** Windows 데스크탑 Pleos Connect v2.1.0 AVD의 공식 샘플에서 Vehicle SDK 2.3.2 누적거리 READ capability 및 현재값 0.0, NaviHelper 3.0.5 일반 목적지·경유지 경로 미리보기. [재현 기록](TECHNICAL_VALIDATION.md)
- **공식 문서 확인:** 현대캐피탈의 거리 약정·초과 부담 구조, Pleos Odometer/NaviHelper API, 주행 중 일반 앱 제한. [공식 심사 기준](https://appdevchallenge.auto-hmg.io/guide/judging-awards), [주행 제한](https://document.pleos.ai/en/docs/pleos-only/vehicle-app-planning-guide/driving-restiction-policy)
- **추론:** 차 안에서 계기판·내비·계약 정보를 합치면 기존 앱보다 빠르게 결정할 수 있고 월 재방문이 생긴다는 제품 가설.
- **미확인:** 실제 리스 이용자의 인터뷰·사용 의사, 대상 실차의 권한·거리 단위·누적거리 변화, 목적지 거리 콜백과 경로 변경 후 수치, 유료화·제휴 가능성.

1차 접수는 [공식 기획서](https://appdevchallenge.auto-hmg.io/guide/submission)가 필수이고 영상·GitHub/APK는 선택이다. **기획서 주제 선정**과 **앱 구현 완료**를 구분한다.
