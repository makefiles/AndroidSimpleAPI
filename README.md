## AndroidSimpleAPI
이 소스코드는 온전히 단독 빌드되는 형태가 아니어서 AOSP 수정에 익숙하신 분들에게만 도움이 되리라 생각합니다.

전체 개념 설명을 위주로 문서를 작성하였으며, Android.mk, Android.bp 를 포함시키면 얼마든지 동작 가능한 소스이지만 참고용 코드 의미임을 밝힙니다.

<br />

### Needs
`Android 서비스/매니저 구조로 Custom API를 작성했을 경우`

`시나리오가 변경되면 API가 추가되거나 변경이 빈번히 발생 할 수 있습니다.`

`이는 기존에 배포된 API를 사용하는 3rd party 앱에서는 호환성 유지를 위해서`

`신규 배포된 API를 기반으로 재빌드해야 하는 문제점이 있습니다.`

`이는 AOSP SDK 버전이 올라감에 따라서 각 앱들도 구현을 달리해야하는 것과 같이`

`Custom API를 사용하는 앱도 마찬가지의 작업이 필요합니다.`

`또, AIDL로 인하여 메소드의 순서가 바뀌었을 경우에는 Index로 인해`

`기존 API를 Call 할 경우에 Crash가 발생하는 등의 치명적인 문제를 내포합니다.`

`따라서 사용 앱과 단말기 안의 서비스간의 Dependency를 줄이기 위한 요구사항이 발생합니다.`

<br />

### Study
`이를 해결하기 위해 AOSP의 기존 구현을 활용하여 구현 자유도가 높은 API 형태를 만들고자 합니다.`

`Custom API의 구현을 최소화 하고 Protocal 인터페이스를 만들어 호환성 문제를 해결하며, API 관리를 간결화 시키고자 합니다.`

`더 나아가 Intent 방식으로 API를 호출하여 Return 결과를 Async/Sync 방식으로 값을 가져갈 수 있는 방법도 고안해 봅니다.`

`이를 위해서 다음과 같은 3가지 개념을 생성합니다.`

<br />

1. **`Index API`**
   - `기능을 제공하는 API들을 실제 Method로 구현하지 않고 Index기반의 문자열로만 정의함`
   - `Bundle/Intent API에서 실제 기능 구현과 연관시켜 주어 호환성을 유지함`
   - `문자열 기반이기 때문에 실제 구현을 사용자로부터 숨겨줄 수 있고, 구현 여부와 관계없이 호출 가능함`
3. **`Bundle API`**
   - `AOSP에 이미 구현된 기능을 활용해 API 호출 시 넘기는 Parameter 와 Return 값을 Bundle 객체를 사용하여 타입을 단일화 함`
   - `Protocal 인터페이스 역할을 하는 Get/Set/Do 3개의 API로 이를 활용하여 여러 Wrapper 클래스 매니저 구현 가능함`
5. **`Intent API`**
   - `AOSP에 이미 구현된 기능을 활용해 Message를 전달 하는 방식에서 벗어나 처리 결과를 기다릴 수 있는 기능을 추가함`
   - `Async/Sync 두가지 동기화 방식을 모두 지원하며, Get/Set API 를 Intent 로 처리할 수 있게하는 핵심 기능임`

<br />

### Index API
![ASAPI_IndexAPI](https://user-images.githubusercontent.com/64515146/156327561-1061ada9-eb43-4735-83c6-39da9c6258ff.png)

#### Description
`이 모든 API 호출의 핵심은 Key라는 문자열을 이용합니다.`

`시스템에서 직접 실행되는 StaticServer 이름이 “STATIC_SERVER”로 등록되어서 어디서든 호출 가능한 서비스로 동작하며,`

`StaticServer 는 DynamicService 를 이용해 각기 다른 용도의 Service 들을 실행합니다.`

`실행과정에서 각기 다른 용도의 Service들은 그들만의 고유 이름으로 서비스를 등록을 합니다.`

`호출 가능한 API들은 Enum 문자열들로 서비스 내에 구현되어 있으며,`

`이는 API 호출시 Method를 찾기위한 키워드로 사용됩니다.`

`마찬가지로 Enum 문자열들은 Intent API방법을 쓸때에도 활용합니다.`

`API 호출시 전달되는 “PORT_UART_OPEN” 문자열을 Tube 클래스에서,`

`앞 문자열 “PORT_UART” 이름으로 서비스를 찾습니다.`

`목표 서비스인 UartService 는 수신된 키워드를 가지고 분기하여`

`원하는 Method를 호출한 후 처리된 결과를 Bundle에 담아 결과를 리턴합니다.`

`이로써 AIDL 의 Index 문제를 일으키지 않고 자유롭게 API 를 추가 삭제 변경 할 수 있게됩니다.`

`또, 이는 자연스레 Hidden API 형태를 취하게되어 공개하고 싶지 않은 API 들은 쉽게 감출 수 있으며,`

`공식 문서로 공개하는 API들만 사용하도록 강제하는 효과를 볼 수 있습니다.`

<br />

### Bundle API
![ASAPI_BundleAPI](https://user-images.githubusercontent.com/64515146/156327578-8f3ecb0c-2dd2-4501-9a1c-06ee10fee9ad.png)

#### Example
```java
Bundle param = new Bundle();

param.putInt(To.P0, 1);

param.putBoolean(To.P1, true);

param.putString(To.P2, “TEST”);

param.putByteArray(To.P3, new byte[100]);

param.putParcelable(To.P4, new KeyEvent());

param.putSerializable(To.P5, new HashMap<String, String>());

Bundle result = Tube.getValue(“UTIL_TEST_PARAM”, param);

Log.e(TAG, “Integer: ” + result.getInt(To.R0));

Log.e(TAG, “Boolean: ” + result.getBoolean(To.R1));

Log.e(TAG, “String: ” + result.getString(To.R2));

Log.e(TAG, “Byte[]: ” + result.getByteArray(To.R3));

Log.e(TAG, “Parcelable: ” + result.getParcelable(To.R4));

Log.e(TAG, “Serializable: ” + result.getSerializable(To.R5));
```

#### Description
`ITube.aidl의 3개의 API로만 서비스들을 구성해 API의 단일화를 구현합니다.`

`Tube.java를 이용해 언제 어디서든 서비스들을 호출해 API를 실행 할 수 있습니다.`

`AOSP의 BaseBundle.java, Bundle.java에 구현되어 있는 Method들로 모든 데이터 형에 대해 RPC가 가능합니다.`

`기본형, 배열에 대해서는 AOSP에 API가 있으며, 사용자 정의 클래스들은`

`Parcelable, Serializable을 구현하여 전달 가능합니다.`

`AIDL로 구현된 Callback은 Object 형태의 IBinder로 RPC가 가능한 것을 확인했습니다.`

`이론상 모든 데이터 형을 Bundle로 주고 받는 것이 가능합니다.`

<br />

### Intent API
![ASAPI_IntentAPI](https://user-images.githubusercontent.com/64515146/156327587-c8a32959-34a9-4fb5-85b5-bd8c97539cfd.png)

#### Example
```java
// 비동기식 API 호출

Sender async = new Sender(this);

async.setReceiver(new BroadcastReceiver() {

    @Override

    public void onReceive(Context c, Intent i) {

        Log.e(TAG, “Result: “ + intent);

    }

});

// 동기식 API 호출

Sender sync = new Sender(this);

sync.sendBroadcast(“UTIL_TEST_PARAM”, null);

Intent intent = sync.getResult(1000);

Log.e(TAG, “Result: “ + intent);
```

#### Description
`com.amolla.sdk.Sender.java를 이용해 Intent로 동기/비동기 API 호출이 가능합니다.`

`Sender 클래스의 Method 사용 방법에 따라 동기/비동기 API 호출을 할 수 있도록 만들었습니다.`

`응답을 받기위해 Intent 생성 시 ACTION 이름을 동적으로 생성해 서비스로 전달하게 됩니다.`

`서비스에서는 처리한 결과를 수신한 ACTION 이름으로 Intent를 전달하며 Sender는 이를 수신 합니다.`

`동기 API 호출은 결과를 받기까지 Lock을 실행하고, 비동기 API 호출은 Receiver를 등록해 결과를 기다립니다.`

`반환 결과를 받지 않으려면 sendBroadcast() 만 호출하면 됩니다.`

`이론상 Intent를 이용하여 Bundle을 사용하여 모든 API를 호출 하는 것이 가능합니다.`

<br />

### Structure : SDK, Service
![ASAPI_Struct1](https://user-images.githubusercontent.com/64515146/156327607-d5589835-dc01-4c02-abea-cd7be5b20df6.png)

#### com.amolla.sdk.jar
`① JNI에서 반환 할 결과 값을 C/C++ Errno.h 를 채용하여 전달 하기 위해 작성되었습니다.`

`Java에서 통용되는 에러나 Exception은 2000 번대로 Define 합니다.`

`현재는 Java Exception 만 정의되어 있으며 toString()으로 설명을 확인 할 수 있습니다.`

<br />

`② 현 프로젝트에서 생성되는 모든 서비스의 부모 클래스 로서 3개의 API 만 존재합니다.`

`어떤 서비스든지 동일 한 형태의 구조를 가지고 사용되며, API 순서와 API 변경에서 자유로워 집니다.`

<br />

`③ 각 서비스들의 모든 API들은 Index API 방식으로 IntentFilter에 등록되어 있어 Intent로 호출 가능하게 됩니다.`

`결과를 반환 받기 위해 정확한 Target 위치가 필요한데, 이를 도와줄 Sender 클래스를 추가합니다.`

`내부적으로 동적으로 ACTION 이름을 생성해 Intent를 보내고, 그 결과를 받는 즉시 Lock을 해제하는 구조입니다.`

<br />

`④ 공통적으로 사용하는 Enumeration과 P0 - P9, R0 - R9 을 String으로 정의합니다.`

`이는 Bundle에 Parameter 값과 Return 값을 넣을때 사용되는 순서에 해당합니다.`

`P0, P1, P2로 Bundle 에 담으면 수신측은 P0, P1, P2 순서대로 전달 받아야 합니다.`

<br />

`⑤ 모든 SDK의 클래스들은 이 클래스를 사용하며 static으로 선언되어 있어 생성 없이 바로 사용 가능합니다.`

`호출 위치에서 ServiceManager::getService() 를 함으로 API 호출에 대한 병목현상을 방지합니다.`

`SDK 버전과 Intent API를 위한 Prefix가 선언되어 있으며, 현시점에서 구동되는 서비스 이름이 정의되어 있습니다.`

`findName() 을 사용해 문자열로 서비스를 찾아 API 호출을 위한 1차 분류를 진행합니다.`

<br />

`⑥ 3rd party 에서 사용할 API들의 모음입니다.`

`Tube 클래스를 직접 사용하면 구현부가 복잡해지므로 이를 간략히 사용 할 수 있도록 Wrapper를 제공합니다.`

`큰 범주의 폴더로 나뉘어 있으며 Hidden API가 존재하지 않습니다.`

`Hidden API처럼 사용하려는 API들은 직접 Tube 클래스만을 이용해 직접 서비스를 호출해야 합니다.`

<br />

#### com.amolla.service.jar
`① 코드 간결화를 위해 제작된 서비스에서 사용하는 모든 Controller들의 부모 클래스 입니다.`

`KERNEL_INTERFACE_MAP을 가지고 있으며 Handler, Thread 들을 기본으로 생성합니다.`

`AOSP 서비스를 가져오는 부분으로 인해 자식 클래스들은 API 호출만 하면 됩니다.`

<br />

`② Intent API 방법을 위해 사용되는 클래스 입니다.`

`서비스에서 이 클래스를 이용해 Enumeration 된 Index API를 IntentFilter로 모두 등록합니다.`

`DynamicRecever가 등록되어 있는 서비스는 수신된 Intent를 구문 분석하여 API Method를 찾아갑니다.`

`처리된 결과는 Bundle에 담겨 호출자에게 전달됩니다.`

<br />

`③ 부팅 시에만 동작하는 서비스로, 하위 서비스들을 등록, 실행하는 역할을 합니다.`

`Android Service 클래스를 상속받아 단일 클래스로 만들어지며,`
   
`STATIC_SERVICE, RUNTIME_SERVICE 들을 각기 다른 방법으로 생성해 실행합니다.`

<br />

`④ API 호출에 사용되는 서비스들의 모음입니다.`

`큰 법주의 폴더로 나뉘어 있으며 Controller들로 클래스를 나누어 복잡도를 분산합니다.`

`Controller들은 DynamicController 클래스를 상속받아 사용하도록 만들어졌습니다.`

`각 서비스들은 ITube.Stub을 상속받아 3개의 API를 구현합니다.`

`API가 호출되면 여러 Enum 중 원하는 곳으로 찾아가기 위한 Key 문자열로 2차 분류가 이루어지며,`
   
`각 Controller를 호출하기 위한 3차 분류가 이루어 집니다.`

`Enumeration으로 되어 있는 Index API는 문자열을 이용한 분류에 사용됩니다.`

`Enumeration을 DynamicReceiver에 등록하여 Intent API가 동작되도록 합니다.`

<br />

### Structure : Server, JNI
![ASAPI_Struct2](https://user-images.githubusercontent.com/64515146/156327618-04dbd83d-fbe5-4561-92c9-f90f9452ef74.png)

#### com.amolla.server.jar
`① BUILDIN_SDK_VERSION과 HashMap<String, String>으로 KERNEL_INTERFACE_MAP을 정의합니다.`

`이는 전체 서비스에서 사용되는 Configuration으로 여러 빌드 모델들의`

`설정을 달리 하기 위한 Build-Time에 결정하는 값입니다.`

`Branding 또는 Support 여부를 여기서 확인해 분기 될 수 있도록 정의합니다.`

<br />

`② Tube.Stub을 상속받아 동작하는 Build-Time에 종속적인 서비스 입니다.`

`Java reflection으로 com.amolla.service.jar 안의 서비스들을 생성, 등록합니다.`

`부팅 시 바로 설정되어야 하는 초기 값들을 지정할 수 있습니다.`

`AOSP에 Dependency가 많은 구현 사항들은 여기에서 동작하도록 구성합니다.`

<br />

#### com.amolla.jni.jar
`① Java 파일은 한개의 cpp 파일과 연결되어 있습니다.`

`큰 범주 밑의 각 폴더들은 각기 하나의 라이브러리로 빌드됩니다.`

`각 so 라이브러리들은 Java 파일에서 Load 되며 JNI가 필요하지 않다면 구현하지 않습니다.`

`서비스들의 큰 범주의 폴더 이름을 여기 사용하여 가독성을 높입니다.`

`앱으로 제작되는 경우나 Android 9+ 버전에서의 개발을 위하여 Old Style JNI 를 사용합니다.`

<br />

### Boot Sequence
![ASAPI_BootSequence](https://user-images.githubusercontent.com/64515146/156327631-a3c088ab-1a2f-4fce-8ce8-bf2efd514680.png)

#### Description
`AOSP의 SystemServer에서 com.amolla.server.StaticServer를 시작합니다.`

`Java reflection으로 com.amolla.server.jar를 Load 합니다.`

`객체를 생성한 후, 생성자에서 서비스 구동을 위한 여러 설정 작업을 진행합니다. (IntentFilter 등록)`

`준비가 다 되면 DynamicService를 호출하여 com.amolla.service.jar에 있는 서비스들을 생성, 등록합니다.`

`고정되어 있는 서비스 외에 동적으로 설정된 RUNTIME_SERVICE들을 생성, 등록합니다.`

`객체 생성이 완료되면 SystemServer에 “STATIC_SERVER” 이름으로 서비스를 등록하여 작업을 끝냅니다.`

<br />

### Call Sequence
![ASAPI_CallSequence](https://user-images.githubusercontent.com/64515146/156352840-26819d5b-70ec-4b1c-8b0f-aeadc354c0e5.png)

#### Description
`API 호출 시 클래스의 객체를 SingleTone 패턴으로 가지고 옵니다.`

`넘겨받은 인자값을 Bundle에 담아 Tube 객체로 전달합니다.`

`Key 문자열로 호출 할 서비스를 찾고 Remote Process Call 합니다. (1차 분류)`

`서비스에서 여러 Enum 과 Key 문자열을 대조하여 분기 합니다. (2차 분류)`

`선택된 Enum 문자열과 Key 문자열을 대조하여 각각에 맞는 Controller를 호출합니다. (3차 분류)`

`처리된 결과를 Bundle로 재구성하여 호출 Stack으로 반환합니다.`

<br />

---

Copyright (C) 2019 by J.J. (make.exe@gmail.com)
