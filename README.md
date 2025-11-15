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