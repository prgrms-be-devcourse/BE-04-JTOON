# J-T**👀**N

![image](https://github.com/prgrms-be-devcourse/BE-04-JTOON/assets/31675711/47e8d454-442b-4538-9b19-e4e4fb382694)

<br><br>

## 👨‍👨‍👧 Team 소개

|                                      김영명                                       |                                      김희빈                                       |                                    박세연(PO)                                     |                                      신재윤                                       |                                    홍혁준(SM)                                     |                                       조셉                                       |                                      김훈기                                       |
|:------------------------------------------------------------------------------:|:------------------------------------------------------------------------------:|:------------------------------------------------------------------------------:|:------------------------------------------------------------------------------:|:------------------------------------------------------------------------------:|:------------------------------------------------------------------------------:|:------------------------------------------------------------------------------:|
|                                   DEVELOPER                                    |                                   DEVELOPER                                    |                                   DEVELOPER                                    |                                   DEVELOPER                                    |                                   DEVELOPER                                    |                                       멘토                                       |                                      서브멘토                                      |
| <img src="https://avatars.githubusercontent.com/u/83266154?v=4" width="250" /> | <img src="https://avatars.githubusercontent.com/u/72112845?v=4" width="250" /> | <img src="https://avatars.githubusercontent.com/u/54196094?v=4" width="250" /> | <img src="https://avatars.githubusercontent.com/u/87688023?v=4" width="250" /> | <img src="https://avatars.githubusercontent.com/u/31675711?v=4" width="250" /> | <img src="https://avatars.githubusercontent.com/u/42864786?v=4" width="250" /> | <img src="https://avatars.githubusercontent.com/u/66348135?v=4" width="250" /> |
|                     [ymkim97](https://github.com/ymkim97)                      |                      [kmebin](https://github.com/kmebin)                       |                     [parksey](https://github.com/parksey)                      |                   [DevUni](https://github.com/Shin-Jae-Yoon)                   |                 [HongDosan](https://github.com/HyuckJuneHong)                  |                  [joseph-100](https://github.com/joseph-100)                   |                    [HunkiKim](https://github.com/HunkiKim)                     |

<br><br>

## 📚 조셉의 제이툰

> **웹툰 서비스** <br><br>
> 네이버 웹툰을 클론 코딩한 프로젝트 입니다. <br>
> 저희 조셉팀에서는 멀티모듈을 기반으로 프로젝트를 설계해봤습니다. <br>
> 도메인은 크게 회원, 결제, 웹툰으로 구성됩니다. <br><br>
> 다양한 기능 구현에 초점을 두었다기 보다는<br>
> **다양한 기술에 도전해보는 것과, 지속적인 리팩토링을 통한 코드 개선에 집중**했습니다.

<br>

```
├── 회원
│   ├── 일반(JToon) 로그인
│   ├── 일반(JToon) 회원가입
│   ├── SMTP 기반 이메일 인증
│   └── OAuth 2.0 로그인 및 회원가입
│
├── 결제
│   ├── Portone-Iamport 기반 결제 승인 검증 및 생성
│   ├── Portone-Iamport 기반 결제 취소
│   └── 결제 내역 조회
│
├── 웹툰
│   ├── 웹툰 생성 (작가 권한 필요)
│   ├── 회차 생성
│   ├── s3 이미지 업로드
│   ├── s3 이미지 삭제
│   ├── 웹툰 리스트 조회
│   ├── 웹툰 정보 조회
│   ├── 회차 리스트 조회
│   ├── 회차 정보 조회
│   └── 회차 구매
```

<br><br>

## 🛠 기술스택

### Backend

<img width="1056" alt="image" src="https://github.com/prgrms-be-devcourse/BE-04-JTOON/assets/87688023/c395838c-bee5-4d51-9b64-256b53c2da3d">

### Infra

<img width="1056" alt="image" src="https://github.com/prgrms-be-devcourse/BE-04-JTOON/assets/87688023/e0804bec-2802-437d-ad32-0fcc1e439143">

### CI 파이프라인

<img width="1056" alt="image" src="https://github.com/prgrms-be-devcourse/BE-04-JTOON/assets/87688023/ab3963af-5a7b-40bb-9956-417be44d070b">

### CD 파이프라인

<img width="1056" alt="image" src="https://github.com/prgrms-be-devcourse/BE-04-JTOON/assets/87688023/4a138a68-e18d-43f5-bbd8-bdf95ffadc16">

<br><br>

## AWS 아키텍쳐

<img width="1168" alt="image" src="https://github.com/prgrms-be-devcourse/BE-04-JTOON/assets/87688023/03d55a3e-9cdd-471a-b0e4-f6c1c915f97c">

<br><br>

## 🗂️ 프로젝트 구조

해당 구조는 우아한 기술 블로그에서 권용근님의 [멀티 모듈 설계 이야기](https://techblog.woowahan.com/2637/)를 참고 하였습니다.
- [module-application](https://github.com/prgrms-be-devcourse/BE-04-JTOON/tree/develop/module-application)
- [module-core](https://github.com/prgrms-be-devcourse/BE-04-JTOON/tree/develop/module-core)
- [module-domain](https://github.com/prgrms-be-devcourse/BE-04-JTOON/tree/develop/module-domain)
- [module-internal](https://github.com/prgrms-be-devcourse/BE-04-JTOON/tree/develop/module-internal)

```.
├─ ...
├─ module-application
│   └── app-api
│       └── src.main.java.shop.jtoon
│          ├── global
│          ├── member
│          ├── payment
│          ├── security
│          └── webtoon
│
├── module-core
│   └── src.main.java.shop.jtoon
│       ├── util
│       ├── type
│       └── exception
│
├── module-domain
│   ├── domain-member
│   ├── domain-payment
│   ├── domain-webtoon
│   ├── domain-jpa
│   └── domain-redis
│
├── module-internal
│   └── core-web
│       ├── security
│       ├── annotation
│       ├── config
│       └── error
│   ├── iamport-client
│   ├── s3-client
│   └── smtp-client
│
└── settings.gradle
```

<br><br>

## 📂 ERD

<img width="1693" alt="image" src="https://github.com/prgrms-be-devcourse/BE-04-JTOON/assets/87688023/8ae19f50-9484-49bb-84a8-7097c065fe87">

<br><br>

## 🌈 협업을 잘하는 방법

<img width="1060" alt="image" src="https://github.com/prgrms-be-devcourse/BE-04-JTOON/assets/87688023/db950edb-df7a-455f-9980-d95ded1d5b4e">
