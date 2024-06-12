# 북바오 백엔드 서버 
<div style="text-align: center;">
    <img src="https://github.com/FourBao-A/Backend/assets/129048011/769e9beb-cd45-4af8-a749-e00738f26050" alt="Image" width="150">
</div>


이 프로젝트는 Spring Boot 프레임워크를 사용하여 구축된 중고 도서 판매 **북바오**의 백엔드 서버입니다.   
 <u>**세종대학교 구성원**</u>은 이 서버를 통해 중고 도서를 등록, 조회, 구매할 수 있습니다.


## ✔️시스템 요구사항
Java 17   
Gradle 7.6.x 이상   
Spring Boot 3.2.5   
Windows 또는 Linux OS   
MySQL 8.x 이상

## ✔️기타 정보
- 해당 프로젝트는 세종대학교 구성원 인증을 통한 로그인이므로 세종대학교 구성원만 사용이 가능합니다.
- 프로젝트 버전: 0.0.1-SNAPSHOT   
- Git이 설치되어 있어야 합니다. 


## ✔️설치 및 실행 방법
1. 프로젝트를 로컬에 clone 합니다.  
``` 
git clone https://github.com/FourBao-A/Backend.git
```

2. 프로젝트 디렉토리로 이동합니다.
```
cd bookbao-backend
```

3. 이 프로젝트는 MySQL 데이터베이스를 사용합니다. 프로젝트를 실행하기 전에 MySQL 서버를 설정하고 필요한 데이터베이스 및 사용자 권한을 구성해야 합니다.    
- 데이터베이스 생성:
```
CREAET DATABASE bookbao;
```
- 사용자 생성 및 권한 부여:
```
CREATE USER 'bookbao'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON bookbao.* TO 'bookbao'@'localhost';
FLUSH PRIVILEGES;

```

4. application.yml 파일 설정   
프로젝트의 src/main/resources/application.yml 파일에서 MySQL 연결 설정을 업데이트합니다. 파일에 아래와 같은 설정을 추가하거나 수정합니다:
```
spring:
  output:
    ansi:
      enabled: always

  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: ${DATABASE_URL}  // 환경변수 처리
    username: ${DATABASE_USER}
    password: ${DATABASE_PASSWORD}


  jpa:
    database: mysql
    show-sql: true
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        show_sql: true
        formal_sql: true
```

5. 다음 사이트에서 JWT secret key를 생성합니다. <https://jwt.io/#debugger>  
이 프로젝트는 토큰 기반의 로그인을 구현했습니다. JWT secret을 생성해 application.yml 하단에 다음과 같이 작성합니다.
```
jwt:
  secret: ${JWT_SECRET}
```

6. Gradle Wrapper를 사용하여 프로젝트 빌드합니다.   
프로젝트 디렉토리에서 아래 명령어를 실행하여 프로젝트를 빌드합니다:
```
./gradlew clean build
```
7. 빌드가 완료되면 다음 명령어로 애플리케이션을 실행합니다. <br>build/libs/ 디렉토리에 생성된 JAR 파일을 실행하여 애플리케이션을 시작할 수 있습니다.   

```
java -jar build/libs/used-book-backend-0.0.1-SNAPSHOT.jar
```
<br>
브라우저에서 `http://localhost:8080`으로 접속하여 애플리케이션을 확인합니다.



## ✔️주요 기능
1. **마이페이지**: 사용자의 학번, 이메일, 판매 중인 도서 내역을 조회할 수 있습니다.   
2. **도서 등록**: 사용자가 도서 정보를 등록할 수 있습니다.
3. **도서 조회**: 등록된 도서 목록을 조회할 수 있습니다.
4. **도서 삭제**: 판매 등록한 도서를 삭제할 수 있습니다.
5. **도서 구매**: 사용자가 원하는 중고 도서를 구매할 수 있습니다.


## ✔️API 문서
다음 URL로 API 문서를 확인할 수 있습니다. 애플리케이션을 실행한 후 URL로 접속하세요:
```
https://www.notion.so/API
```



   
      

## 💻Code
### LoginController
**/api/v1/login**으로 들어오는 요청을 처리합니다. 사용자로부터 학번, 포털 비밀번호, 이메일을 입력받아 LoginService 클래스의 **loginToPortal 메서드**에서 로그인을 수행합니다.   

### UserController
**/api/v1/user**로 들어오는 요청을 처리합니다.    
- **/mypage**는 UserService의 **getMyPage 메서드**와 연결되어 유저의 마이페이지를 조회할 수 있습니다.   
- **/update-email**은 UserService의 **updateEmail 메서드**와 연결되어 유저의 이메일을 업데이트할 수 있게 구현되어 있습니다.

### BookController
**/api/v1/book**으로 들어오는 요청을 처리합니다.   
- **/enroll**은 BookService의 **saveBook 메서드**를 호출해 도서를 등록하는 메서드입니다.   
- **/search**는 BookService의 **searchBooks 메서드**를 호출해 찾고자 하는 도서의 제목을 입력하면 도서 목록을 반환할 수 있도록 구현했습니다.   
- **/update**는 BookService의 **updateBookInfo 메서드**를 호출하여 도서의 정보를 수정할 수 있는 메서드입니다.   
- **/detail**은 BookService의 **getBookDetail 메서드**를 호출하여 도서의 상세 정보를 BookDetailResponse에 맞춰 반환하는 메서드입니다.    
- **/delete**는 BookService의 **deleteBook 메서드**를 호출하여 도서를 삭제하는 메서드입니다.    


***/api/v1/login**을 제외한 모든 메서드는 로그인된 사용자만 접근할 수 있습니다.*