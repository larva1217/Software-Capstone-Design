# 📊 소프트웨어 캡스톤 디자인  
## 주식 차트 기반 모의 투자 웹 서비스

### 👨‍💻 팀 정보
- 팀명 : S2L
- 팀원 : 신재민, 이건희, 이한

---

## 📌 프로젝트 개요
최근 금융 시장에 대한 접근성이 향상되며 개인 투자자의 시장 참여가 크게 증가하고 있습니다.  
하지만 투자 경험과 금융 지식이 부족한 상태에서 시장에 진입하여 경제적 손실을 겪는 사례 또한 빈번하게 발생하고 있습니다.  

본 프로젝트는 이러한 문제를 해결하기 위해,  
**사용자가 실제 자금을 잃을 위험 없이 투자 과정을 경험할 수 있는 모의 투자 웹 서비스**를 개발하는 것을 목표로 합니다.

---

## 🎯 개발 목표
- 실제 주식 데이터를 기반으로 한 모의 투자 환경 제공  
- 사용자가 투자 의사결정 과정을 직접 경험할 수 있도록 지원  
- 투자 결과 분석을 통해 전략 개선 가능  

---

## ✨ 프로젝트 특징
- AI 기반 시장 분석 및 투자 의견 제공
- 실제 주식 데이터를 활용한 실시간 모의 투자 환경 구현
- TradingView 차트를 통한 직관적인 데이터 시각화

---

## ⚙️ 주요 기능

### 🔍 종목 검색 및 정보 조회
- 사용자가 원하는 주식 종목 검색  
- 종목의 기본 정보 및 가격 데이터 제공  

### 📈 주식 차트 시각화
- 주가 변동을 직관적으로 확인할 수 있는 차트 제공  
- 외부 API를 활용한 데이터 시각화  

### 💰 모의 투자 기능
- 가상의 자금을 이용한 주식 매수 및 매도  
- 실제 투자와 유사한 거래 환경 제공  

### 🧾 거래 내역 관리
- 모든 거래 기록을 데이터베이스에 저장  
- 거래 히스토리 조회 기능 제공  

### 📊 포트폴리오 관리
- 보유 자산 및 수익률 확인  
- 종목별 투자 성과 분석 기능 제공  

### 📰 경제 뉴스 조회 기능
- 최신 경제 및 주식 관련 뉴스 제공  
- 시장 흐름과 이슈를 빠르게 확인 가능  

### 👥 커뮤니티 기능
- 사용자 간 자유로운 정보 공유 가능  
- 투자 의견 및 시장 이슈 토론 기능 제공  

### 🤖 AI 시장 지수 분석
- AI를 활용하여 주요 시장 지수 흐름 분석  
- 거시 경제 상황 및 시장 분위기 요약 제공  

### 📈 AI 개별 종목 분석
- AI 기반 개별 종목 분석 기능 제공  
- 종목의 흐름, 특징 및 투자 판단에 도움이 되는 의견 제공  

---

## 🛠️ 기술 스택

| 분야 | 기술 |
|---|---|
| Backend | Java, Spring Boot, Spring MVC, Spring Data JPA |
| Frontend | HTML, CSS, JavaScript, Thymeleaf |
| Database | MySQL |
| API | Yahoo Finance API, Alpha Vantage API, Google Gemini API |
| Chart | TradingView Embedded Widget |
| Tool | IntelliJ IDEA, GitHub |
---

## 📂 프로젝트 구조 

### 🔹 Backend & Frontend 
- **위치:** `src/main/java/stock/cpastonedesign` 및 `src/main/resources`

```text
📁 cpastonedesign  [ Backend ]
├── 📁 config (프로젝트 환경 설정 관리)
│   └── 📄 AppConfig.java
├── 📁 domain (DB 테이블과 연결되는 Entity 객체)
│   ├── 📄 Portfolio.java
│   ├── 📄 Transaction.java
│   └── 📄 User.java
├── 📁 repository (데이터베이스 처리)
│   ├── 📄 PortfolioRepository.java
│   ├── 📄 PostRepository.java
│   ├── 📄 TransactionRepository.java
│   └── 📄 UserRepository.java
├── 📁 service (비즈니스 로직 처리)
│   ├── 📄 AiService.java
│   ├── 📄 MarketService.java
│   ├── 📄 NewsService.java
│   ├── 📄 StockService.java
│   ├── 📄 TradingService.java
│   └── 📄 UserService.java
└── 📁 web (웹 요청 및 응답 처리 계층)
    ├── 📁 controller (사용자 요청 URL 처리)
    │   ├── 📄 AiController.java
    │   ├── 📄 AuthPageController.java
    │   ├── 📄 ChartController.java
    │   ├── 📄 CommunityController.java
    │   ├── 📄 GlobalModelAdvice.java
    │   ├── 📄 MarketIndexController.java
    │   ├── 📄 NewsController.java
    │   ├── 📄 TradingController.java
    │   └── 📄 UserController.java
    └── 📁 dto (데이터 전달 전용 객체)
        ├── 📄 MarketDataDto.java
        ├── 📄 OrderRequestDto.java
        ├── 📄 Post.java
        ├── 📄 UserLoginDto.java
        └── 📄 UserSignupDto.java

📁 resources  [ Frontend & Config ]
├── 📁 static (CSS 및 JavaScript 정적 파일)
│   ├── 📄 app-community.js
│   ├── 📄 app-markets.js
│   ├── 📄 app-news.js
│   ├── 📄 auth-pages.js
│   ├── 📄 common.js
│   └── 📄 styles.css
├── 📁 templates (HTML 템플릿)
│   ├── 📁 fragments (공통 HTML)
│   │   └── 📄 header-actions.html
│   ├── 📄 chart.html
│   ├── 📄 community.html
│   ├── 📄 index.html
│   ├── 📄 login.html
│   ├── 📄 news.html
│   └── 📄 signup.html
├── 📄 application.properties  
├── 📄 data.sql
└── 📄 schema.sql


