#!/bin/sh

SLACK_WEB_HOOK="${slack_web_hook_url}"
IS_BLUE=$(docker ps | grep jtoon-blue)

NGINX_CONF="${PWD}/nginx/nginx.conf"

docker-compose up -d nginx
docker-compose up -d redis

if [ -n "$IS_BLUE" ]; then
    echo "### BLUE => GREEN ###"
    echo "1. jtoon-green 이미지 가져오고 실행"
    docker-compose pull jtoon-green
    docker-compose up -d jtoon-green

    attempt=1
    while [ $attempt -le 24 ]; do
        echo "2. jtoon-green health check (Attempt: $attempt)"
        sleep 5
        REQUEST=$(docker exec nginx curl http://jtoon-green:${SERVER_PORT})

        if [ -n "$REQUEST" ]; then
            echo "jtoon-green health check 성공"
            sed -i 's/jtoon-blue/jtoon-green/g' $NGINX_CONF
            echo "3. nginx 설정파일 reload"
            docker exec nginx service nginx reload
            echo "4. jtoon-blue 컨테이너 종료"
            docker-compose stop jtoon-blue

            echo "slack 알람 발송 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"
            json="{ \"text\": \"jtoon-green 배포 성공 !! -> 배포 성공 시각: $(date '+%Y-%m-%d %H:%M:%S')\" }"
            curl -X POST -H 'Content-type: application/json' --data "$json" "$slack_web_hook"

            break;
        fi

        if [ $attempt -eq 24 ]; then
            echo "jtoon-green 배포 중 문제 발생 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"
            echo "slack 알람 발송 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"
            json="{ \"text\": \"jtoon-green 배포 중 문제가 발생하여 배포가 비정상 중단되었으니 확인 부탁드립니다 -> 문제 발생 시각: $(date '+%Y-%m-%d %H:%M:%S')\" }"
            curl -X POST -H 'Content-type: application/json' --data "$json" "$slack_web_hook"

            docker-compose stop jtoon-green

            exit 1;
        fi

        attempt=$((attempt+1))
    done;
else
    echo "### GREEN => BLUE ###"
    echo "1. jtoon-blue 이미지 가져오고 실행"
    docker-compose pull jtoon-blue
    docker-compose up -d jtoon-blue

    attempt=1
    while [ $attempt -le 24 ]; do
        echo "2. jtoon-blue health check (Attempt: $attempt)"
        sleep 5
        REQUEST=$(docker exec nginx curl http://jtoon-blue:${SERVER_PORT})

        if [ -n "$REQUEST" ]; then
            echo "jtoon-blue health check 성공"
            sed -i 's/jtoon-green/jtoon-blue/g' $NGINX_CONF
            echo "3. nginx 설정파일 reload"
            docker exec nginx service nginx reload
            echo "4. jtoon-green 컨테이너 종료"
            docker-compose stop jtoon-green

            echo "slack 알람 발송 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"
            json="{ \"text\": \"jtoon-blue 배포 성공 !! -> 배포 성공 시각: $(date '+%Y-%m-%d %H:%M:%S')\" }"
            curl -X POST -H 'Content-type: application/json' --data "$json" "$slack_web_hook"

            break;
        fi

        if [ $attempt -eq 24 ]; then
            echo "jtoon-blue 배포 중 문제 발생 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"
            echo "slack 알람 발송 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"
            json="{ \"text\": \"jtoon-blue 배포 중 문제가 발생하여 배포가 비정상 중단되었으니 확인 부탁드립니다 -> 문제 발생 시각: $(date '+%Y-%m-%d %H:%M:%S')\" }"
            curl -X POST -H 'Content-type: application/json' --data "$json" "$slack_web_hook"

            docker-compose stop jtoon-blue

            exit 1;
        fi

        attempt=$((attempt+1))
    done;
fi
