# smart-distance-dispatch
스마트 거리 배차 시스템

```
src/main/java/com/smartdistance/sdd/
│
├── order/                                          
│   ├── domain/                                     
│   │   ├── Order.java                              # 주문
│   │   ├── OrderItem.java                          # 주문 엔티티
│   │   ├── OrderStatus.java                        # 주문 상태 열거형 (ENUM)
│   │   └── event/
│   │       └── OrderCreatedEvent.java              # 주문 생성 이벤트
│   ├── application/                                
│   │   └── OrderService.java                       # 주문 비즈니스 로직
│   ├── infrastructure/                             
│   │   ├── OrderRepository.java                    # 주문 데이터 저장소
│   │   └── messaging/
│   │       └── OrderEventProducer.java             # 주문 이벤트 Kafka 발행
│   └── presentation/                               
│       └── OrderController.java                    # 주문 REST API 컨트롤러
│
├── store/                                          
│   ├── domain/
│   │   ├── Store.java                              # 가게
│   │   └── StoreLocation.java                      # 가게 위치 값 객체 (위도, 경도)  
│   ├── application/
│   │   └── StoreService.java                       # 가게 비즈니스 로직
│   └── infrastructure/
│       ├── StoreRepository.java                    # 가게 데이터 저장소
│       └── messaging/
│           └── StoreEventProducer.java             # 가게 이벤트 Kafka 발행
│
├── dispatch/                                       
│   ├── domain/
│   │   ├── DispatchRequest.java                    # 배차
│   │   ├── RiderLocation.java                      # 라이더 위치 값 객체
│   │   ├── DispatchStatus.java                     # 배차 상태 (요청, 할당, 수락, 거절)
│   │   ├── service/
│   │   │   ├── RiderSelectionService.java          # 최적 라이더 선정 도메인 서비스
│   │   │   └── DistanceCalculationService.java     # 거리 계산 도메인 서비스
│   │   └── event/
│   │       ├── DispatchRequestedEvent.java         # 배차 요청 이벤트
│   │       ├── RiderAssignedEvent.java             # 라이더 배정 이벤트
│   │       ├── DispatchAcceptedEvent.java          # 배차 수락 이벤트
│   │       └── DispatchRejectedEvent.java          # 배차 거절 이벤트
│   ├── application/
│   │   └── DispatchService.java                    # 배차 요청, 라이더 선정 로직
│   └── infrastructure/
│       ├── DispatchRepository.java                 # 배차 데이터 저장소
│       └── messaging/
│           ├── OrderEventConsumer.java             # 주문 이벤트 구독 → 배차 시작
│           └── DispatchEventProducer.java          # 배차 이벤트 Kafka 발행
│
├── graph/                                          
│   ├── domain/
│   │   ├── DistanceGraph.java                      # 실거리
│   │   ├── GraphNode.java                          # 그래프 노드 값 객체 (가게/픽업지)
│   │   ├── GraphEdge.java                          # 그래프 간선 값 객체 (거리 정보)
│   │   ├── Location.java                           # 위치 값 객체 (위도, 경도)
│   │   ├── service/
│   │   │   ├── GraphCalculationService.java        # 실거리 계산 도메인 서비스
│   │   │   └── GraphCleanupService.java            # 그래프 정리 도메인 서비스
│   │   └── event/
│   │       ├── GraphCreatedEvent.java              # 그래프 생성 이벤트
│   │       └── GraphDeletedEvent.java              # 그래프 삭제 이벤트
│   ├── application/
│   │   └── GraphService.java                       # 그래프 생성, 조회, 삭제 로직
│   └── infrastructure/
│       ├── GraphRepository.java                    # 그래프 데이터 저장소
│       └── messaging/
│           ├── StoreEventConsumer.java             # 가게 이벤트 구독 → 그래프 생성
│           ├── OrderEventConsumer.java             # 주문 이벤트 구독 → 그래프 생성
│           └── DeliveryEventConsumer.java          # 배달 완료 구독 → 그래프 정리
│
├── delivery/                                       
│   ├── domain/
│   │   ├── Delivery.java                           # 배달 
│   │   ├── DeliveryStatus.java                     # 배달 상태 (배달중, 완료)
│   │   └── event/
│   │       ├── DeliveryStartedEvent.java           # 배달 시작 이벤트
│   │       ├── DeliveryInProgressEvent.java        # 배달중 이벤트
│   │       └── DeliveryCompletedEvent.java         # 배달 완료 이벤트
│   ├── application/
│   │   └── DeliveryService.java                    # 배달 시작, 완료 처리 로직
│   └── infrastructure/
│       ├── DeliveryRepository.java                 # 배달 데이터 저장소
│       └── messaging/
│           ├── DispatchEventConsumer.java          # 배차 수락 구독 → 배달 시작
│           └── DeliveryEventProducer.java          # 배달 이벤트 Kafka 발행
│
├── rider/                                          
│   ├── domain/
│   │   ├── Rider.java                              # 라이더
│   │   ├── RiderStatus.java                        # 라이더 상태 (대기, 배달중, 휴식)
│   │   └── event/
│   │       └── RiderLocationUpdatedEvent.java      # 라이더 위치 업데이트 이벤트
│   ├── application/
│   │   └── RiderService.java                       # 라이더 등록, 위치 업데이트 로직
│   └── infrastructure/
│       ├── RiderRepository.java                    # 라이더 데이터 저장소
│       └── messaging/
│           └── RiderEventProducer.java             # 라이더 이벤트 Kafka 발행
│
├── common/                                         
│   ├── kafka/
│   │   ├── config/
│   │   │   ├── KafkaProducerConfig.java            # Kafka Producer 설정
│   │   │   └── KafkaConsumerConfig.java            # Kafka Consumer 설정
│   │   ├── producer/
│   │   │   └── KafkaEventProducer.java             # 공통 Kafka 이벤트 발행 유틸
│   │   └── consumer/
│   │       └── KafkaEventConsumer.java             # 공통 Kafka 이벤트 구독 유틸
│   ├── event/
│   │   └── DomainEvent.java                        # 도메인 이벤트 공통 인터페이스
│   └── exception/
│       ├── BusinessException.java                  # 비즈니스 예외 클래스
│       └── ErrorCode.java                          # 에러 코드 열거형
│
└── simulator/                                      # 시뮬레이터 (테스트용)
    ├── OrderSimulator.java                         # 자동 주문 생성 시뮬레이터
    ├── RiderSimulator.java                         # 라이더 위치 자동 업데이트
    └── DispatchSimulator.java                      # 배차 수락/거절 시뮬레이션
```

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







