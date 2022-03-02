# AndroidSimpleAPI
이 소스코드는 온전히 단독 빌드되는  형태가 아니어서 AOSP 수정에 익숙하신 분들에게만 도움이 되리라 생각합니다.<br>
전체 개념 설명을 위주로 문서를 작성하였으며, Android.mk, Android.bp 를 포함시키면 얼마든지 동작 가능한 소스이지만 참고용 코드 의미임을 밝힙니다.

## Need
Android 서비스/매니저 구조로 Custom API를 작성했을 경우 시나리오가 변경되면 API가 추가되거나 변경이 빈번히 발생 할 수 있습니다.<br>
이는 기존에 배포된 API를 사용하는 3rd party 앱에서는 호환성 유지를 위해서는 신규 배포된 API를 기반으로 재빌드해야 하는 문제점이 있습니다.<br>
이는 AOSP SDK 버전이 올라감에 따라서 각 앱들도 구현을 달리해야하는 것과 같이 Custom API를 사용하는 앱도 마찬가지의 작업이 필요합니다.<br>
또, AIDL로 인하여 메소드의 순서가 바뀌었을 경우에는 Index로 인해 기존 API를 Call 할 경우에 Crash가 발생하는 등의 치명적인 문제를 내포합니다.<br>
따라서 사용 앱과 단말기 안의 서비스간의 Dependency를 줄이기 위한 요구사항이 발생합니다.<br>

## Study
이를 해결하기 위해 AOSP의 기존 구현을 활용하여 구현 자유도가 높은 API 형태를 만들고자 합니다.<br>
Custom API의 구현을 최소화 하고 Protocal 인터페이스를 만들어 호환성 문제를 해결하며, API 관리를 간결화 시키고자 합니다.<br>
더 나아가 Intent 방식으로 API를 호출하여 Return 결과를 Async/Sync 방식으로 값을 가져갈 수 있는 방법도 고안해 봅니다.<br>
이를 위해서 다음과 같은 3가지 개념을 생성합니다.<br>
1. **`Index API`**
   - 기능을 제공하는 API들을 실제 Method로 구현하지 않고 Index기반의 문자열로만 정의함
   - Bundle/Intent API에서 실제 기능 구현과 연관시켜 주어 호환성을 유지함
   - 문자열 기반이기 때문에 실제 구현을 사용자로부터 숨겨줄 수 있고, 구현 여부와 관계없이 호출 가능함
3. **`Bundle API`**
   - AOSP에 이미 구현된 기능을 활용해 API 호출 시 넘기는 Parameter 와 Return 값을 Bundle 객체를 사용하여 타입을 단일화 함
   - Protocal 인터페이스 역할을 하는 Get/Set/Do 3개의 API로 이를 활용하여 여러 Wrapper 클래스 매니저 구현 가능함
5. **`Intent API`**
   - AOSP에 이미 구현된 기능을 활용해 Message를 전달 하는 방식에서 벗어나 처리 결과를 기다릴 수 있는 기능을 추가함
   - Async/Sync 두가지 동기화 방식을 모두 지원하며, Get/Set API 를 Intent 로 처리할 수 있게하는 핵심 기능임

Index API
<그림>
