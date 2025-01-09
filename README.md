## Description
인테리어 업자들을 위한 맞춤 매칭 서비스

## Date
`2025.01.31`까지 백엔드 개발 완료

## TODO
1. OAuth 설정(구글) `~ 25.01.08일까지`
2. 데이터베이스 연동 `~ 25.01.09일까지`
3. 테스트 코드 작성 `API 개발할 때마다 작성`
4. 스웨거로 API 문서 작성하기 `API 개발할 때마다 작성`
5. CI만 진행 - 도커 이미지 만들어지는 과정만 자동화, 배포는 필요없음
6. 메인 기능 개발하면 태그 따서 Release
7. 필요하면 Redis 연동
8. 백엔드 완료하면 Svelte 프론트엔드 개발

## Note
- Git Branch Convention 지키기
- feature 개발 후 PR 로 승인 받기
- Spring Security 이용해서 간단하게 admin, user 권한별로 엔드포인트 제한하기
- dev/prod property 분리하기
- 모든 건 도커 컨테이너로 관리하기(MySQL, Redis, Backend Server 등)


## Dockerfile
```shell
#-- Dockerfile 을 가지고 현재 백엔드 서버 빌드하기
$ docker build -t tjsguddl96/zzinterior:0.0.0 .

#-- Docker Hub 에 푸시하기
$ docker push tjsguddl96/zzinterior:0.0.0

#-- 빌드한 도커 이미지 실행하기
#-- -d: 백그라운드에서 실행
#-- --name: 컨테이너 이름
#-- -p: 포트포워딩할 <현재 로컬에서 접속할 포트>:<백엔드 서버가 실행되는 포트>
$ docker run -d --name zzinterior -p 8080:8080 tjsguddl96/zzinterior:0.0.0

#-- 백그라운드로 실행 중인 컨테이너 로그 보기
$ docker logs -f zzinterior

#-- 컨테이너 내부로 들어가기
$ docker exec -it zzinterior bash

#-- 모든 컨테이너 보기
$ docker ps -a

#-- 모든 이미지 보기
$ docker images

#-- 컨테이너 중지하기
$ docker stop zzinterior

#-- 중지된 컨테이너 재시작하기
$ docker restart zzinterior

#-- 컨테이너 삭제하기
$ docker rm -f zzinterior

#-- mysql 컨테이너 실행
$ docker run -d --name 컨테이너이름 -e MYSQL_ROOT_PASSWORD=비밀번호 -p 3306:3306 이미지이름
```