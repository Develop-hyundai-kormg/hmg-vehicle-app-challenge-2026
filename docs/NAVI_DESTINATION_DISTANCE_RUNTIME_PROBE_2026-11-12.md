# NaviHelper 현재 경로거리 실행 재검증

검증일: **2026-11-12 KST** · 범위: **Pleos Connect v2.1.0 Linux x86_64 AVD와 공식 NaviHelper 샘플**. 〈할인 경계〉 제품 앱이나 실차 검증이 아니다.

## 결과

| 항목 | 결과 | 수준 |
|---|---|---|
| AVD 부팅과 샘플 설치 | Android API 34 Pleos AVD 부팅, [공식 샘플](https://github.com/Pleos-Playground/pleos-sample-navi-app) `76ed592487a1eab7ba102b9d4b321705426886fe`를 NaviHelper 3.0.5로 빌드·설치 | 실행 확인 |
| 순정 내비 경로 생성 | POI 식별자를 비운 좌표 요청 `(37.48544722, 127.03636666)`이 내비 화면에 **12 km / 20분**으로 표시되고 `onRouteStarted(distance=12000, duration=1200)` 전달 | 실행 확인 · [경로 화면](evidence/pleos_navi_coordinate_route_12km_2026-11-12.png) |
| 현재 경로 조회 | 안내 중 `getDestinationInfo()` 호출 뒤 `onDestinationInfo`가 **요청 좌표 그대로** `remainDistance=12000`, `remainTime=1200`을 반환 | 실행 확인 · [콜백 화면](evidence/pleos_navi_destination_callback_12km_2026-11-12.png) |
| 단위 | 같은 화면의 12 km/20분과 콜백의 12000/1200이 대응한다. 이 환경에서 거리 **m**, 시간 **초**로 해석된다 | 실행값 대조 |
| 원본 샘플의 POI 요청 | 원본의 `poiId=36khp37hh8gqcm5nqhe5`, 이름 `42dot`, 요청 좌표 `(37.48544722, 127.03636666)`에서는 50m/1분 경로와 `remainDistance=50`, `remainTime=60`이 나왔다. 콜백 목적지 `(37.41289678, 127.09383464)`는 요청 좌표에서 직선 약 **9.53km** 떨어졌다 | 실행 확인 · 목적지 불일치 |

**해석:** NaviHelper가 현재 활성 경로의 거리와 시간을 자체 앱에 전달하는 경로는 AVD에서 확인됐다. 원본 POI 사례는 이름·POI ID·하위 ID를 함께 바꿔 재시험했으므로, 불일치의 원인을 **POI ID 하나로 확정하지 않는다**. 제품 앱은 콜백의 목적지 좌표·이름이 사용자가 선택한 목적지와 맞는지 검사하고, 불일치하면 보험 계산을 표시하지 않아야 한다.

## 재현 방법

1. [Pleos 공식 설치 안내](https://document.pleos.ai/en/docs/connect/guide/getting-started/application-development/setup-connect-sdk-emulator)의 Linux 저장소로 `system-images;android-34;pleos_car_emulator;x86_64`를 설치한다. 42dot `Pleos Connect v2.1.0` 기기 프로필로 AVD를 만들고 RAM 4GB, cold boot로 실행했다. 호스트는 KVM과 SwiftShader 소프트웨어 GPU를 사용했다. Android Emulator는 `37.2.13.0`이다.
2. 공식 샘플 커밋 `76ed592487a1eab7ba102b9d4b321705426886fe`의 `NaviTestData.routeInfo` 좌표는 그대로 두고 `poiId=""`, `poiName="좌표검증"`, `poiSubId=""`로 바꿨다. `assembleDebug`로 빌드했다. 빌드 APK SHA-256: `4b9e09bc7d47ca6e32839b0e1da4a9198d39ce2af6e008ce7a6e8e0143de9bbe`.
3. 샘플을 실행해 `requestRoute`를 호출했다. 내비가 시작된 뒤 샘플로 돌아와 `getDestinationInfo`를 호출했다. 내비와 샘플 화면을 캡처하고 `NaviHelperViewModel` 태그의 해당 콜백만 확인했다.

관찰 로그 발췌:

```text
onRouteStarted !!! RouteStartInfo(duration=1200, distance=12000, tollFare=0, name=좌표검증, ...)
executeApi getDestinationInfo
onDestinationInfo !!! DestinationInfo(latitude=37.48544722, longitude=127.03636666, name=좌표검증, remainDistance=12000, remainTime=1200, ...)
```

## 남은 검증

- Windows AVD에서 Vehicle SDK Odometer READ/정적값 `0.0`을 과거에 확인했지만, **동일 제품 앱에서 누적거리와 경로거리를 결합한 실행**은 아직 없다. 차량 누적거리 변화, 실차 권한, 차종별 지원도 미확인이다.
- `changeRouteOption(SHORT_DISTANCE)`의 변경 전후 값과 비활성 경로·다른 목적지·앱 재시작 상황은 시험하지 않았다.
- 보험증권별 정산 산식, 실제 운전자의 입력 부담·이동 선택 변화·재사용성은 기술 실행과 별개로 미확인이다.

이 결과는 [보험 후보 검증 감사](INSURANCE_MILEAGE_VALIDATION_AUDIT_2026-11-12.md)의 경로 콜백 항목을 **문서 확인 → AVD 실행 확인**으로 올린다. 제품 가치나 제출 적합성이 함께 검증된 것은 아니다.
