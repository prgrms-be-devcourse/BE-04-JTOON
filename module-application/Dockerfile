### Docker 이미지를 생성할 때 기반이 되는 베이스 이미지를 설정한다.
FROM amazoncorretto:17
### 조셉팀 ^-^
MAINTAINER 박세연, 신재윤, 홍혁준, 김영명, 김희빈
### 경로에 해당하는 파일을 Docker 이미지 내부로 복사한다.
COPY ${PWD}/module-application/build/libs/module-application-0.0.1-SNAPSHOT.jar jtoon.jar
### Docker 컨테이너가 시작될 때 실행할 명령을 지정한다.
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/jtoon.jar"]
