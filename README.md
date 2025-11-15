# smart-distance-dispatch
스마트 거리 배차 시스템


src/main/java/com/ieum/dispatch/
├── order/
│   ├── domain/
│   │   ├── Order.java (Aggregate Root)
│   │   ├── OrderItem.java (Entity)
│   │   ├── OrderStatus.java (Enum)
│   │   └── event/
│   │       └── OrderCreatedEvent.java
│   ├── application/
│   │   └── OrderService.java
│   ├── infrastructure/
│   │   └── OrderRepository.java
│   └── presentation/
│       └── OrderController.java
│
├── dispatch/
│   ├── domain/
│   │   ├── DispatchRequest.java (Aggregate Root)
│   │   ├── RiderLocation.java (Value Object)
│   │   ├── service/
│   │   │   ├── RiderSelectionService.java
│   │   │   └── DistanceCalculationService.java
│   │   └── event/
│   │       ├── DispatchRequestedEvent.java
│   │       └── RiderAssignedEvent.java
│   ├── application/
│   │   └── DispatchService.java
│   └── infrastructure/
│       └── DispatchRepository.java
│
├── graph/
│   ├── domain/
│   │   ├── DistanceGraph.java (Aggregate Root)
│   │   ├── GraphEdge.java (Value Object)
│   │   ├── Location.java (Value Object)
│   │   ├── service/
│   │   │   ├── GraphCalculationService.java
│   │   │   └── GraphCleanupService.java
│   │   └── event/
│   │       └── GraphCreatedEvent.java
│   ├── application/
│   │   └── GraphService.java
│   └── infrastructure/
│       └── GraphRepository.java
│
├── delivery/
│   ├── domain/
│   │   ├── Delivery.java (Aggregate Root)
│   │   ├── DeliveryStatus.java (Enum)
│   │   └── event/
│   │       └── DeliveryCompletedEvent.java
│   ├── application/
│   │   └── DeliveryService.java
│   └── infrastructure/
│       └── DeliveryRepository.java
│
├── rider/
│   ├── domain/
│   │   ├── Rider.java (Aggregate Root)
│   │   └── event/
│   │       └── RiderLocationUpdatedEvent.java
│   ├── application/
│   │   └── RiderService.java
│   └── infrastructure/
│       └── RiderRepository.java
│
└── common/
└── kafka/
├── KafkaProducer.java
└── KafkaConsumer.java


주문
- 주문 생성 > 가게:주문받기
- 주문 상태 수정


가게
- 주문 받기 > 배달: 배달 생성

고객
- (1차 시작)주문 하기 > 주문:주문 생성

배차 : (2차 시작)레디스에 있으면 계속 매칭
- 배달원 찾기(레디스 쓰기 이벤트 발생 시) > 배달: 시작처리
  - 배달원 정보 수집(배달원:현위치 반환)
  - 레디스 정보 수집(레디스:읽기)
  - 알고리즘 돌리기 (배차 완료될때까지 진행)******************
- private 알고리즘 함수

그래프
- 노드 생성 > 그래프:거리계산
- 거리 계산 > 레디스:쓰기
- 노드 삭제 > 레디스:삭제

레디스(실거리 저장)
- 쓰기(어떤지역, 어떤 가게, 어떤 주문자, 레디스) > 1차 완료(이벤트를 던지거나 로그를 찍거나 배차에 배달원찾기를 실행하거나)
- 읽기(지역주면 정보 다 주기)
- 삭제(배달완료시 삭제)
- 수정(해야함?)

배달
- 생성 > 그래프: 노드 생성
- 완료처리 > 그래프: 노드 삭제
- 시작처리 > 배달원: 시작처리


배달원
- 현위치 반환
- (3차 시작)배달 완료 신호 > 배달: 상태 수정
- 배달 시작처리 > 2차 완료







