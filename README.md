# J-T**👀**N

![image](https://github.com/prgrms-be-devcourse/BE-04-JTOON/assets/31675711/47e8d454-442b-4538-9b19-e4e4fb382694)

<br/>

## 📚 조셉의 제이툰

> 웹툰 서비스

<br/>

## 📚 Team 소개

|                                      김영명                                       |                                      김희빈                                       |                                    박세연(PO)                                     |                                      신재윤                                       |                                    홍혁준(SM)                                     |                                       조셉                                       |                                      김훈기                                       |
|:------------------------------------------------------------------------------:|:------------------------------------------------------------------------------:|:------------------------------------------------------------------------------:|:------------------------------------------------------------------------------:|:------------------------------------------------------------------------------:|:------------------------------------------------------------------------------:|:------------------------------------------------------------------------------:|
|                                   DEVELOPER                                    |                                   DEVELOPER                                    |                                   DEVELOPER                                    |                                   DEVELOPER                                    |                                   DEVELOPER                                    |                                       멘토                                       |                                      서브멘토                                      |
| <img src="https://avatars.githubusercontent.com/u/83266154?v=4" width="250" /> | <img src="https://avatars.githubusercontent.com/u/72112845?v=4" width="250" /> | <img src="https://avatars.githubusercontent.com/u/54196094?v=4" width="250" /> | <img src="https://avatars.githubusercontent.com/u/87688023?v=4" width="250" /> | <img src="https://avatars.githubusercontent.com/u/31675711?v=4" width="250" /> | <img src="https://avatars.githubusercontent.com/u/42864786?v=4" width="250" /> | <img src="https://avatars.githubusercontent.com/u/66348135?v=4" width="250" /> |
|                     [ymkim97](https://github.com/ymkim97)                      |                      [kmebin](https://github.com/kmebin)                       |                     [parksey](https://github.com/parksey)                      |                   [DevUni](https://github.com/Shin-Jae-Yoon)                   |                 [HongDosan](https://github.com/HyuckJuneHong)                  |                  [joseph-100](https://github.com/joseph-100)                   |                    [HunkiKim](https://github.com/HunkiKim)                     |

<br/>

## 🛠 기술스택

### 개발 환경

![Java](https://img.shields.io/badge/javaㅤ17-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
![Spring Boot](https://img.shields.io/badge/springㅤbootㅤ3.1.2-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)
![JUnit](https://img.shields.io/badge/junitㅤ5-25A162?style=for-the-badge&logo=JUnit5&logoColor=white)
![MySQL](https://img.shields.io/badge/mysqlㅤ8-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)

### 협업 툴

![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)
![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white)
![Jira](https://img.shields.io/badge/jira-%230A0FFF.svg?style=for-the-badge&logo=jira&logoColor=white)
![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)

<br/>

## 🗂️ 프로젝트 구조

해당 구조는 우아한 기술 블로그에서 권용근님의 [멀티 모듈 설계 이야기](https://techblog.woowahan.com/2637/)를 참고 하였습니다.

```.
├─ ...
├─ module-application
│   └── app-api
│       └── src.main.java.shop.jtoon
│          ├── global
│          ├── member
│          ├── payment
│          └── webtoon
│
├── module-core
│   └── src.main.java.shop.jtoon
│       ├── util
│       └── type
│
├── module-domain
│   ├── domain-member
│   ├── domain-payment
│   └── domain-webtoon
│
├── module-internal
│   ├── core-web
│   ├── iamport-client
│   ├── s3-client
│   ├── oauth2-client
│   └── smtp-client
│
└── settings.gradle
```
