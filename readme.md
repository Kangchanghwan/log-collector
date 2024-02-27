LogCollector 는 SpringAop와 ThreadLocal에 기반한 요청 메소드에 대한 정보 수집 및 처리를 위한 로그서비스입니다.

- 서비스 로그를 한번에 보기 위함.
- spring boot 3버전 이상 사용가능.


사용결과 예시 

성공시
```text
INFO  [65dd2686b3b60e600bb360ca3fb2cc72, b46e505cc2d68561] 2024-02-27 09:02:15.151 [collectorAsyncExecutor-3] [] [AopLogCollector:27] - {
  "appName" : "application",
  "host" : "0:0:0:0:0:0:0:1",
  "port" : 80,
  "clientIp" : "0:0:0:0:0:0:0:1",
  "reqUrl" : "http://localhost/v1/admin/login",
  "httpMethod" : "POST",
  "headers" : {
    "Cookie" : "Idea-4d9913f4=2113ffea-fdab-4677-917c-a38fea209a34; Idea-4d9913f6=c497e292-ceb0-4dc1-870e-41a8ac8c26f2; sid=Fe26.2**7a2e9ff0840ab24b7fd38ffb40e4b7f15c9f6231c4c1aaf4852533cfcf0fb405*tC1XTdR_oYHn4xu8Nhl03A*qWE6zMnk9S1qi8NExMIEhD1DZGXwg-IzSVL0j6iZuCdn2tq507zwJA7RQF2loxbwBaCoYfxnOmaaXlfvwL9whgax3if_4OxQ9mlYogWbBz5jyz0SerqHGQa8t9xod81xjzLOeD14Y5eBgMlYVLR9tvfi_xxSeWD4iRFh-zFD0eZ0dZEj4Jyln4acp01Gn0rp0xnBWvPYqut93pYXB19I3Tu-TdetRUUmj4ICTj7OhrQ**0714f6ca43c09ea2683efdef418d30851cef38b95076319eb03b18edce86ee14*mSIo0xRQeQGLZfZs38mqitelqgfkRHbQ6z2nxR41W-4; accessToken=eyJhbGciOiJIUzM4NCJ9.eyJ0eXBlIjoiQWRtaW4iLCJzdWIiOiIyYzEyZjhkMy05ZjhjLTExZWUtYWYyNi0wMjQyYWMxMTAwMDIiLCJpYXQiOjE3MDg5OTIxMDQsImV4cCI6MTcwODk5MjE2NH0.WuxOm1ZfZmGhn-_HfmlTvwDeW7N2NW2SJc0fo_ZFY-FI77CqcHOzuFXn3o6v4Hzj; refreshToken=eyJhbGciOiJIUzM4NCJ9.eyJ0eXBlIjoiQWRtaW4iLCJzdWIiOiIyYzEyZjhkMy05ZjhjLTExZWUtYWYyNi0wMjQyYWMxMTAwMDIiLCJpYXQiOjE3MDg5OTIxMDQsImV4cCI6MTczNDkxMjEwNH0.NJbnJ6EG1Vtkr9kCy0Sd8zEfREX0cjykVfICTINdPr3nO7ZT1en91u8ZEtzY4K-k",
    "content-type" : "application/json"
  },
  "tag" : "controller",
  "content" : {
    "[2024-02-27T09:02:15.024043][AdminAuthenticationService:78]" : "Find admin admin : admin",
    "[2024-02-27T09:02:15.036780][AdminAuthenticationService:47]" : "find Success! adminId : 2c12f8d3-9f8c-11ee-af26-0242ac110002",
    "[2024-02-27T09:02:15.116845][AdminAuthenticationService:51]" : "Password Success!",
    "[2024-02-27T09:02:15.116927][AdminAuthenticationService:53]" : "Login Success!",
    "[2024-02-27T09:02:15.121462][AdminAuthenticationService:58]" : "Credential Setting Success!"
  },
  "method" : "com.api.propoint.adapter.in.rest.admin.controller.AdminController#login",
  "args" : [ {
    "loginId" : "admin",
    "password" : "11111111"
  } ],
  "respBody" : {
    "success" : true,
    "code" : "0",
    "msg" : "성공하였습니다.",
    "traceId" : "65dd2686b3b60e600bb360ca3fb2cc72"
  },
  "logDate" : "2024-02-27T00:02:15.012+00:00",
  "costTime" : 137,
  "threadName" : "http-nio-80-exec-1",
  "threadId" : 57,
  "success" : true
} 
```



실패시 

```text
ERROR [65dd26e422a4506d94854d9ecf58cd20, 75468089026ba3dc] 2024-02-27 09:03:48.928 [collectorAsyncExecutor-4] [] [AopLogCollector:29] - {
  "appName" : "application",
  "host" : "0:0:0:0:0:0:0:1",
  "port" : 80,
  "clientIp" : "0:0:0:0:0:0:0:1",
  "reqUrl" : "http://localhost/v1/admin/login",
  "httpMethod" : "POST",
  "headers" : {
    "Cookie" : "Idea-4d9913f4=2113ffea-fdab-4677-917c-a38fea209a34; Idea-4d9913f6=c497e292-ceb0-4dc1-870e-41a8ac8c26f2; sid=Fe26.2**7a2e9ff0840ab24b7fd38ffb40e4b7f15c9f6231c4c1aaf4852533cfcf0fb405*tC1XTdR_oYHn4xu8Nhl03A*qWE6zMnk9S1qi8NExMIEhD1DZGXwg-IzSVL0j6iZuCdn2tq507zwJA7RQF2loxbwBaCoYfxnOmaaXlfvwL9whgax3if_4OxQ9mlYogWbBz5jyz0SerqHGQa8t9xod81xjzLOeD14Y5eBgMlYVLR9tvfi_xxSeWD4iRFh-zFD0eZ0dZEj4Jyln4acp01Gn0rp0xnBWvPYqut93pYXB19I3Tu-TdetRUUmj4ICTj7OhrQ**0714f6ca43c09ea2683efdef418d30851cef38b95076319eb03b18edce86ee14*mSIo0xRQeQGLZfZs38mqitelqgfkRHbQ6z2nxR41W-4; accessToken=eyJhbGciOiJIUzM4NCJ9.eyJ0eXBlIjoiQWRtaW4iLCJzdWIiOiIyYzEyZjhkMy05ZjhjLTExZWUtYWYyNi0wMjQyYWMxMTAwMDIiLCJpYXQiOjE3MDg5OTIxMzUsImV4cCI6MTcwODk5MjE5NX0.2kkAOmc2sTRbRMaoaVB0xOEOTK7Dt1FbEFVWKhrthmjUiRacOXczXion6hKRUkA6; refreshToken=eyJhbGciOiJIUzM4NCJ9.eyJ0eXBlIjoiQWRtaW4iLCJzdWIiOiIyYzEyZjhkMy05ZjhjLTExZWUtYWYyNi0wMjQyYWMxMTAwMDIiLCJpYXQiOjE3MDg5OTIxMzUsImV4cCI6MTczNDkxMjEzNX0.0O-eqVNqOzuLz1okUkHwj453yeAG6q4YREyI9AlxvEVCRgtaYSkSsYB-qqC7CrTi",
    "content-type" : "application/json"
  },
  "tag" : "controller",
  "content" : {
    "[2024-02-27T09:03:48.772453][AdminAuthenticationService:78]" : "Find admin admin : admin",
    "[2024-02-27T09:03:48.784484][AdminAuthenticationService:47]" : "find Success! adminId : 2c12f8d3-9f8c-11ee-af26-0242ac110002",
    "[2024-02-27T09:03:48.926074][AopLogProcessor:126]" : "Fail : 'com.api.propoint.config.error.CustomRuntimeException' occurred at com.api.propoint.application.usecase.AdminAuthenticationService.login(AdminAuthenticationService.java:49) message is COMMON_1 : 비밀번호가 틀렸습니다.\n"
  },
  "method" : "com.api.propoint.adapter.in.rest.admin.controller.AdminController#login",
  "args" : [ {
    "loginId" : "admin",
    "password" : "111112111"
  } ],
  "logDate" : "2024-02-27T00:03:48.746+00:00",
  "costTime" : 180,
  "threadName" : "http-nio-80-exec-4",
  "threadId" : 60,
  "success" : false
}
```



