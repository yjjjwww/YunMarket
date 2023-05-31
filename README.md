# 다양한 상품을 구매/판매하는 커머스 프로젝트
다양한 카테고리의 상품들을 구매 및 판매하는 커머스 서비스입니다.
___
## 프로젝트 기능 및 설계
- 회원가입
  - 구매자/판매자 나누어서 회원가입한다. 구매자는 CUSTOMER 권한을, 판매자는 SELLER 권한을 지닌다.
  - 회원가입시 이메일과 패스워드를 입력받으며, 아이디는 unique 해야한다.
- 로그인
  - 로그인시 JWT 토큰을 발급한다.
  - 로그인시 회원가입 때 사용한 이메일과 비밀번호가 일치해야 한다.
- 상품 등록하기
  - SELLER 권한을 가진 판매자는 판매할 상품을 등록할 수 있다.
  - 상품 이름, 카테고리, 가격, 설명, 수량을 등록해야 한다.
- 상품 조회하기
  - 로그인하지 않은 사용자를 포함한 모든 사용자는 상품을 최신순, 가격순으로 조회할 수 있다.
  - 페이징 처리를 해서 20개씩 상품을 조회한다.
- 상품 검색하기
  - 로그인하지 않은 사용자를 포함한 모든 사용자는 상품 키워드로 검색하면 관련 상품들을 조회할 수 있다.
  - 페이징 처리를 해서 20개씩 상품을 조회한다.
- 상품 상세 정보 불러오기
  - 로그인하지 않은 사용자를 포함한 모든 사용자는 특정 상품의 상세 정보를 볼 수 있다.
  - 해당 상품의 이름, 카테고리, 가격, 설명, 남은 수량을 볼 수 있다.
- 장바구니 담기/수정/삭제하기
  - CUSTOMER 권한을 가진 구매자는 장바구니에 사고 싶은 상품을 담을 수 있다. 장바구니는 상품 정보와 수량 정보를 갖는다.
  - 장바구니에 담긴 상품의 수량 변경 및 상품 삭제를 할 수 있다.
- 주문하기
  - 장바구니 내용을 가져와서 transaction 정보를 저장한다.(구매 일자, 결제 금액, 고객 정보, 판매자 정보, 상품 정보)
  - 장바구니를 초기화한다.
- 주문 정보 보기
  - 구매자는 자신의 주문 정보를 조회할 수 있다.
- 리뷰쓰기
  - 구매자는 자신이 주문 완료한 상품에 한해서 상품 리뷰 작성 가능하다.
  - 평점과 리뷰 내용을 등록할 수 있다.
- 리뷰 최신순 조회하기
  - 상품에 대한 구매자들의 리뷰들을 최신순으로 조회할 수 있다.
  - 페이징 처리를 해서 20개씩 리뷰를 조회한다.
- 판매자가 리뷰에 댓글 달기
  - 판매자는 자신의 상품에 구매자가 리뷰를 작성하면 해당 리뷰에 댓글을 작성할 수 있다.
- 적립금 시스템
  - 적릭금은 구매자가 상품 구매시 함께 사용할 수 있으며 구매 금액에서 차감할 수 있다.
  - 구매자는 상품 구매시 구매 금액의 1%를 적립할 수 있다.
- 최근 본 상품과 관련 인기 상품 불러오기
  - 접속한 IP에서 최근 본 상품과 해당 상품 카테고리의 인기 상품들을 조회할 수 있다.
- 상품 문의
  - 구매자는 상품에 대한 문의를 작성할 수 있다.
  - 판매자는 구매자가 작성한 문의에 답변을 할 수 있다.

___
## ERD
<img width="1302" alt="스크린샷 2023-06-01 오전 12 21 30" src="https://github.com/yjjjwww/YunMarket/assets/100079037/3e7946ba-1ddb-4909-8855-a573d54042e8">

___
## Tech Stack
<div align=center> 
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> 
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
  <img src="https://img.shields.io/badge/redis-darkred?style=for-the-badge&logo=redis&logoColor=white"> 
  <img src="https://img.shields.io/badge/elasticsearch-yellow?style=for-the-badge&logo=elasticsearch&logoColor=white"> 
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
</div>
