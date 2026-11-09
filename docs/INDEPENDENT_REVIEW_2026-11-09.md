# 2026-11-09 독립 재검증과 1차 제출 판단

> 상태: **주제 미확정, 조건부 1순위는 병원 환자 하차 → 보호자 주차 → 픽업(C)**. 합격 가능성을 수치로 말할 근거는 없다. 이 문서는 팀원들이 보낸 독립 보고서 3건을 반례와 실제 Pleos 실행 결과로 다시 판단한 기록이다. 팀이 앱을 만들거나 출품했다는 뜻이 아니다.

## 판단 기준과 증거 등급

공식 1차 심사는 **차량특화성·창의성·Pleos 기술 활용성·사업성**을 평가하며 세부 배점은 공개하지 않는다. 시연 영상과 소스·APK를 담은 GitHub 저장소는 선택 가산점 대상이다. [공식 심사 기준](https://appdevchallenge.auto-hmg.io/guide/judging-awards), [제출 방법](https://appdevchallenge.auto-hmg.io/guide/submission)

아래에서 **실행 확인**은 2026-11-09 데스크탑의 Pleos 에뮬레이터에서 재현한 결과, **문서 확인**은 공식 공개 자료, **추론**은 설계 판단, **미확인**은 사용자·현장·실차 증거가 없는 항목, **실패**는 해당 시험에서 오류가 난 항목을 뜻한다. 에뮬레이터 성공은 실차 또는 앱 심사 승인을 뜻하지 않는다.

검토한 팀원 자료:

1. HMG_2026_Independent_Review (3).md — 병원 동선 C를 조건부 추천.
2. 독립검토_보고서_2026-11-08.md — 누적거리 기반 업무용 운행기록을 새 후보로 추천.
3. HMG-2026-Pleos-독립기획안-4종.md — 공용차 연료 인수인계 〈다음 차례〉를 추천하고 EV 충전·적재 높이·휴식 후보를 제시.

보고서 3건 모두 실제 사용자 인터뷰, 병원 현장 검증, Pleos 런타임 시험은 하지 않았다. 따라서 추천 문장을 증거로 취급하지 않았다.

## 데스크탑 실험 결과

환경: Windows 데스크탑, NVIDIA RTX 4060 Ti 8GB, Android Studio 2026.2.2.8, JDK 17, Android API 34, 공식 Pleos Connect v2.1.0 x86_64 이미지, Pleos 전용 AVD 프로필 2560×1440. 공식 이미지 ZIP SHA-1은 게시된 값과 일치했다. 공식 [에뮬레이터 설치 안내](https://document.pleos.ai/en/docs/connect/guide/getting-started/application-development/setup-connect-sdk-emulator)의 GPU·콜드 부팅 조건으로 실행했다. 공개 업데이트 사이트가 CLI의 패키지 목록에 등록되지 않아, 받은 공식 이미지 압축을 SDK 이미지 디렉터리에 수동 배치했다. **이 설치 경로는 공식 Android Studio GUI 설치와 동일하다고 검증하지 않았다.**

| 항목 | 실제 결과 | 사용할 수 있는 주장 |
|---|---|---|
| 공식 Vehicle 샘플 빌드 | 처음 실패. 샘플이 Vehicle 2.3.1을 고정했지만 공개 Maven 메타데이터에는 2.0.3·2.3.2만 확인. 검증용 복제본만 2.3.2로 바꿔 빌드 성공 | 의존성 버전 조정 후 빌드 가능. 공식 저장소 자체가 그대로 빌드된 것은 아님 |
| Vehicle 샘플 설치·초기화 | 설치 성공, SDK 2.3.2 / Vehicle Service 2.3.22 표시 | 에뮬레이터에서 Vehicle 서비스 연결 확인 |
| 누적거리 | checkCurrentDistanceDrivenCapabilities = READ 지원. getCurrentDistanceDriven = 0.0 성공 | Pleos Vehicle SDK를 통한 읽기 경로 확인. 운행 자동 분할·거리 변화 콜백은 미확인 |
| 내연기관 연료 잔량 | Fuel Percent capability가 ERROR_NOT_SUPPORTED_PROPERTY. getFuelPercent도 속성 0x21400be5 미지원 오류 | 현재 확보한 에뮬레이터로 〈다음 차례〉의 연료 핵심 장면을 시연할 수 없음. 실차 전체 미지원이라는 뜻은 아님 |
| EV 배터리 SOC | capability READ 지원, getEvBatteryStateOfCharge = 100 성공 | 현재값 읽기 확인. 충전이 시작됐다는 뜻은 아님 |
| EV 충전 스위치 | capability READ_WRITE, 초기 읽기 false. setEvChargeSwitch(true)는 onComplete(true), 재조회는 false | 명령 수락과 실제 상태 변화를 구별해야 함. 충전 시작 감지는 미확인 |
| 도어 위치 | capability READ_WRITE, 앞좌석 왼쪽 도어 현재값 0 읽기 성공 | 도어 상태 조회 경로 확인. 환자 하차 완료를 도어만으로 판단할 수 없음 |
| AAOS 기어·UI 제한 | 별도 검증 APK에서 GEAR_SELECTION = 4(P), CarUxRestrictions active = 0 읽기 성공 | 일반 앱의 P 상태·현재 UI 제한 조회 확인. D 전환 콜백과 주행 제한 반응은 미확인 |
| AAOS 직접 누적거리 | android.car.permission.CAR_MILEAGE 선언했으나 granted=false, 직접 PERF_ODOMETER 조회 SecurityException | 일반 AAOS API로 거리 읽는다고 주장하면 안 됨. Pleos SDK 경로와 구분 |
| NaviHelper 경로 | 공식 Navi 샘플 빌드·설치·초기화 성공. requestRoute로 순정 내비 경로 미리보기 표시. requestRouteWithWaypoints로 **‘경유지 1개 포함’** 미리보기 표시 | Pleos NaviHelper → 순정 내비 경로 요청의 에뮬레이터 실행 확인. 특정 병원 동선·현장 안전·도착 완료는 미확인 |
| Virtual Console 값 변경 | HTTP 200으로 콘솔 접속되나 Odometer/Gear PUT은 503 emulator_inactive, DDS OFFLINE. 범용/전용 AVD, 4/8GB RAM, 창 표시/무창 모드에서 동일 | 차량 신호 변화 → 앱 콜백 데모는 현재 실패. 원인 미확정. 값 변화 시연으로 포장하지 않음 |

공식 [Vehicle SDK 소개](https://document.pleos.ai/en/docs/pleos-only/connect-sdk-pleos/Vehicle/intro), [Vehicle 권한](https://document.pleos.ai/en/docs/pleos-only/connect-sdk-pleos/Vehicle/vehicle-permission), [Virtual Console](https://document.pleos.ai/en/docs/pleos-only/app-test-guide/virtual-console-guide), [NaviHelper 경유지](https://document.pleos.ai/en/docs/pleos-only/connect-sdk-pleos/NaviHelper/route-with-waypoints), [AAOS 차량 속성·권한](https://developer.android.com/reference/android/car/VehiclePropertyIds).

## 후보를 다시 비교한 결과

| 후보 | 1차 판단 | 가장 강한 근거와 반례 | 판단을 바꿀 증거 |
|---|---|---|---|
| A. 목적지·내비 화면 없이 자동 250m 카메라 알림·순정 중복 억제 | **주력안 보류** | 원래 사용자 요구. 그러나 자동 시작, 무목적지 이벤트, 250m 거리·방향, 순정 경고 상태, 주행 중 백그라운드 음성이 각각 미확인. 순정 카메라 경고와 Pleos 내비 앱이 이미 있음. [Pleos 내비 정책](https://document.pleos.ai/en/docs/pleos-only/vehicle-app-developer-guide/navigation-app-development-guide), [HMG App Market 발표](https://www.hyundaimotorgroup.com/ko/news/42dot-pleos-connect-app-market-new-apps) | **목적지 없음·화면 비활성·재시동 후** 실제 경고와 중복 억제, 정책 허용을 따로 증명 |
| B. 주행 후 체크리스트·맥락 정정 | **주력안 보류** | 현대·기아·지도 앱의 운전 분석, 기존 이벤트 검토 패턴과 겹침. 차량 신호만으로 앞차 거리·실제 차로 변경·과실을 확정할 수 없음 | 반복 운전자 사용 이유와 관찰 가능한 사건만으로 바뀌는 행동을 사용자 시험으로 확인 |
| C. 병원 환자 하차 → 보호자 주차 → 픽업 | **조건부 1순위** | NaviHelper 경유지 경로 요청이 실행됨. 환자와 운전자의 분리 동선은 차량 안에서 흐름을 이어갈 가능성이 있음. 다만 기존 병원 길찾기·주차 안내와 겹치며 특정 병원의 허용 승하차점·무계단 출입구·픽업점을 확인하지 못함. 병원 길찾기와 주차 위치 저장은 해외 병원 앱에도 이미 있다. [서울대병원 주차 안내](https://www.snuh.org/content/E003007.do), [삼성서울병원 위치도](https://www.samsunghospital.com/home/info/map.do), [Johns Hopkins Find Your Way](https://www.hopkinsmedicine.org/patient-care/locations/find-your-way), [CHOC ChocoTracks](https://choc.org/chocotracks/) | 병원 **1곳의 공식 승하차 정책·현장 동선·표지·경로 POI**, 기존 앱과의 직접 비교, 목표 사용자 인터뷰, Gear/CarUxRestrictions 기반 최소 앱 시연 |
| 누적거리 기반 업무용 운행기록 | **현재 주력안 아님** | Pleos 누적거리 읽기는 실행 확인. 그러나 [Pleos Fleet](https://pleos.ai/service-solution/pleos-fleet/?locale=en-US)와 [카택스](https://cartax.biz/)가 자동 운행기록을 이미 제공. 자동 운행 분할·세무 양식 충족도 미확인 | 기존 솔루션이 못 푸는 좁은 반복 업무와 데이터 수집·분할의 실제 결과 |
| 〈다음 차례〉 공용차 연료 인수인계 | **현재 2차 시연 경로에서 제외** | 연료 잔량 핵심 API가 에뮬레이터에서 미지원. Fleet·카택스와 운영 기능도 일부 중복 | 지원 실차 또는 동등한 공식 테스트 환경에서 Fuel Percent 변화, 운전자 간 기록 공유, 실제 교대 문제 확인 |
| EV 충전 확인·적재 높이·반복 휴식 | **예비 아이디어** | EV SOC는 읽히지만 충전 시작 확정 신호는 아직 없음. 높이·시설 입구 데이터는 수동 갱신 필요. 휴식은 기존 경유지·즐겨찾기와 겹침 | 각각 실제 사건, 차별성, 핵심 신호 또는 데이터 경로 |

**C도 합격이 예상된다는 뜻이 아니다.** 차량 앱이어야 하는 이유와 병원 지도·일반 내비 대비 남는 이점을 현장 사용자에게 확인해야 한다. 이 문서의 상대 순위는 공식 심사 점수나 합격 확률이 아니다.

## C를 택할 경우의 최소 제품 범위

대상은 **거동이 불편한 외래 환자를 차로 모시고 가는 보호자 운전자**다. 첫 버전은 병원 1곳의 한 방문 흐름만 다룬다. 병원이나 환자에게 안전·접근성·주차 가능을 보증하는 앱으로 표현하지 않는다.

1. **출발 전:** 방문 건물과 병원 공식 안내·현장 방문으로 확인한 하차 지점·주차 입구·픽업 지점을 보여준다. 실제 승하차 허용 여부를 별도로 확인하고, 자료의 확인 날짜와 현장 표지를 우선한다는 안내를 둔다.
2. **하차까지:** NaviHelper 경유지 경로 요청으로 하차 지점을 거쳐 주차장으로 가는 경로 미리보기를 순정 내비에 전달한다. 주행 중에는 경로 안내 외 긴 입력·설명을 띄우지 않는다.
3. **하차 후:** 기어 P와 CarUxRestrictions 상태를 확인한 화면에서 운전자가 **직접 ‘환자 하차 완료’**를 누른다. 도어·좌석 신호만으로 하차나 안전한 보호 인계를 추정하지 않는다.
4. **주차·픽업:** 운전자가 주차 위치를 수동 기록하고, 환자의 픽업 요청을 **직접 확인**하면 검증된 픽업 지점으로 경로를 요청한다. 환자의 전화·문자 자동 연동, 실내 길찾기, 주차 가능 실시간 정보는 초기 범위에 넣지 않는다.
5. **실패 처리:** 위치/경로 실패, 목적지 POI 불일치, 기어/제한 상태 불명, 병원 공사·표지 변경 시 자동 결론 대신 현장 확인을 요구한다.

일반 지도에서도 두 번 목적지를 넣을 수 있다. 차별성 가설은 **한 방문 안에서 환자 하차·운전자 주차·재픽업의 세 장소와 단계를 차량 맥락에 맞춰 연결하는 것**이다. 실제 시간 절약·실수 감소는 아직 측정하지 않았다.

## 1차 제출 패키지와 문구 경계

마감은 **2026-11-15 23:59 KST**이며, 공식 접수양식 DOCX가 필수다. 대표자 포함 4명 모두 동의·서명·날짜, 전원 CC가 필요하다. 영상은 목업도 인정하지만 화면마다 실행/목업을 분명히 표시한다. 비공개 GitHub를 제출하려면 공지된 심사용 계정의 접근을 확인하고 **소스와 구동 가능한 release APK를 함께** 둔다. 현재 팀 저장소에는 제출용 앱과 APK가 없다. [공식 일정](https://appdevchallenge.auto-hmg.io/guide/schedule), [공식 제출 방법](https://appdevchallenge.auto-hmg.io/guide/submission).

**우선순위:** (1) 현장·사용자 관문, (2) 실제 범위만 적은 DOCX, (3) 팀 자체 앱의 한 흐름 실행 영상, (4) 자체 앱의 재설치 확인된 release APK+소스. 공식 샘플 APK나 검증용 기어 앱을 제품 APK로 제출하지 않는다. 가산점의 수치가 없으므로 부실한 APK를 위해 기획서의 근거를 희생하지 않는다.

DOCX에는 문제와 기존 대안의 빈틈, 한 방문의 세 단계, NaviHelper 경유지 실행 사실, AAOS 기어/UX 제한 확인 사실, 병원 자료·사용자 검증 결과, 실패 시 수동 경로, 2차까지의 구현 계획을 각각 근거 수준대로 쓴다. 실측 MAU가 없으면 지원 차량 운전자 수 × 대상 방문율 × 설치율 × 월 사용률의 **가정식**으로만 제시한다. 재방문은 월 1회 이상 실제 방문 시 이 흐름을 다시 쓴 고유 운전자 비율로 정의한다.

## 팀 검증 요청과 결정 관문

앱 구현과 1차 제출안을 확정하기 전에 4인 팀이 서로 다른 증거를 모은다. 모두가 현장에 갈 필요는 없지만, 현장 기록과 사용자 응답은 원문을 확인할 수 있어야 한다.

| 맡을 검증 | 반드시 남길 결과 |
|---|---|
| 병원 1곳 현장 확인 | 병원 공식 승하차·픽업 정책과 현장 표지, 출입구의 계단/경사로, 하차→주차 입구의 실제 차로, 픽업 대기 가능 지점, 내비 POI 일치 여부, 확인 일시. 얼굴·차량번호가 보이는 사진은 공유하지 않음 |
| 실제 보호자 운전자 확인 | 최근 병원 방문을 어떻게 처리했는지, 어디서 길을 잃었는지, 일반 내비/병원 앱을 어떻게 썼는지, 같은 방문 흐름을 다시 쓸 이유가 있는지. 유도 질문 대신 과거 행동을 묻기 |
| 기존 대안 반례 확인 | 대상 병원 앱·지도 앱이 하차→주차→픽업을 이미 제공하는지 직접 화면과 절차로 비교 |
| Pleos 앱 제작 가능성 | NaviHelper 경유지와 P 상태만으로 세 단계가 끝까지 이어지는 자체 앱의 짧은 실행 흐름. Virtual Console 오류와 주행 제한 상태는 미확인으로 분리 |
| 운영 문의 | 대학생·일반인 혼합 4인 팀의 참가 부문과 심사용 GitHub 계정은 운영사무국의 서면 답변/공지를 확인 |

**11월 11일까지 내부 결정 목표(팀 일정 제안):** 현장 지점이 정확하고 합법적이며, 최근 경험이 있는 보호자 운전자가 기존 방식의 구체적 불편을 확인하고, 자체 앱의 최소 경로 호출이 가능하면 C를 주력으로 확정한다. 어느 하나라도 실패하면 범위를 줄이거나 다른 주제를 다시 검토한다. 사용자 반응 몇 건을 전체 수요나 합격 확률로 일반화하지 않는다.

## 남은 위험

- 데스크탑 Virtual Console은 연결되지만 DDS OFFLINE이라 기어·도어·거리 변화 콜백, 주행 제한 전환을 검증하지 못했다.
- 경유지 미리보기만 확인했다. 실제 안내 시작, 하차 후 다음 목적지 전환, 병원 POI, 재픽업 전체 흐름은 미검증이다.
- 특정 병원의 현장 접근성·합법 승하차·운전 동선, 실사용 가치, 지원 실차의 API 권한은 미확인이다.
- 특정 병원에 묶인 낮은 이용 빈도와 장소 데이터 유지비가 사업성을 약하게 만들 수 있다. 세 장소를 일반 지도에 저장하는 방식보다 분명히 낫다는 비교 결과가 없으면 C 역시 주력에서 내린다.
- 혼합팀 참가 부문, 팀원 네 번째 인적 사항·역할, 심사용 GitHub 계정은 아직 확정되지 않았다.

다음 시작점은 **병원 1곳의 현장/사용자 반례와 자체 앱의 짧은 경로 흐름**이다. 그 전에 완성·자동화·안전 보장을 주장하지 않는다.
