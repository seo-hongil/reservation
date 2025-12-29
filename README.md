# 예약 프로그램 프로젝트

## 소개
이 레포는 **예약 프로그램**을 만들기 위해 생성되었습니다.  
구체적으로는 **테이블링처럼 음식점, 전시회 등 다양한 종류의 예약 및 티케팅이 가능한 사이트**를 구현하는 것이 목표입니다.

---

## 목표 및 진행 상황
- **회원 기능**: 회원가입, 로그인 (보안: Spring Security + Session)
- **예약 기능**: 음식점 웨이팅 기능 구현 예정
- **향후 확장 계획**: 전시회/다양한 티켓팅 기능 추가

---

## 기술 스택 및 역할

| 계층 | 기술 | 역할 / 설명 |
|------|------|-------------|
| Backend | Spring Boot | REST/HTML 컨트롤러, 서비스 로직 |
| ORM | JPA / Spring Data JPA | DB CRUD, 관계형 데이터 관리 |
| Template | Thymeleaf | 서버사이드 렌더링 HTML |
| Search | Elasticsearch | 음식점 이름, 웨이팅 상태 빠른 검색 |
| Messaging | Kafka | 웨이팅 상태 변경 실시간 알림 (publish/subscribe) |
| Database | MySQL | 사용자 정보 및 웨이팅 데이터 |
| Security | Spring Security + Session | 로그인 및 권한 관리 |
| Frontend | Thymeleaf + 최소 JS | 폼 처리, 실시간 알림 WebSocket 연동 |
| Logging / Monitoring | ELK (Elasticsearch + Logstash + Kibana) | 로그 수집, 검색, 시각화 및 문제 추적 |
| Cloud | AWS (EC2, RDS, S3, MSK 등) | 인프라 배포, DB, Kafka 서비스 등 |
| Build / Deploy | Maven/Gradle + Docker + GitHub Actions | 자동 빌드 및 배포 |

---

## 프로젝트 구조 요약
- **회원 관리 → 예약 기능 → 실시간 알림 → 검색 기능** 순으로 단계별 구현
- REST API와 서버사이드 렌더링(Thymeleaf) 병행
- 실시간 데이터 처리 및 알림을 위해 Kafka 사용
- DB 관리 및 검색 최적화를 위해 JPA + Elasticsearch 활용
- 클라우드 환경(AWS) 기반으로 인프라 배포 계획

---

## 향후 계획
1. 음식점 웨이팅 기능 완성
2. 전시회/다양한 티켓 예약 기능 추가
3. 예약 상태 실시간 알림 개선 및 WebSocket 적용
4. UI/UX 개선 및 프론트엔드 강화
