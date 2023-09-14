#!/bin/sh

IS_BLUE=$(docker ps | grep jtoon-blue)

NGINX_CONF="${PWD}/nginx/nginx.conf"

docker-compose up -d nginx

if [ -n "$IS_BLUE" ]; then
        echo "### BLUE => GREEN ###"
        echo "1. jtoon-green 이미지 가져오고 실행"
        docker-compose pull jtoon-green
        docker-compose up -d jtoon-green
        while [ 1 = 1 ]; do
                echo "2. jtoon-green health check"
                sleep 5
                REQUEST=$(docker exec nginx curl http://jtoon-green:${SERVER_PORT})
                if [ -n "$REQUEST" ]; then
                        echo "health check 성공"
                        break;
                fi
        done;
        sed -i 's/jtoon-blue/jtoon-green/g' $NGINX_CONF
        echo "3. nginx 설정파일 reload"
        docker exec nginx service nginx reload
        echo "4. jtoon-blue 컨테이너 종료"
        docker-compose stop jtoon-blue
else
        echo "### GREEN => BLUE ###"
        echo "1. jtoon-blue 이미지 가져오고 실행"
        docker-compose pull jtoon-blue
        docker-compose up -d jtoon-blue
        while [ 1 = 1 ]; do
                echo "2. jtoon-blue health check"
                sleep 5
                REQUEST=$(docker exec nginx curl http://jtoon-blue:${SERVER_PORT})
                if [ -n "$REQUEST" ]; then
                        echo "health check 성공"
                        break;
                fi
        done;
        sed -i 's/jtoon-green/jtoon-blue/g' $NGINX_CONF
	echo $NGINX_CONF
        echo "3. nginx 설정파일 reload"
        docker exec nginx service nginx reload
        echo "4. jtoon-green 컨테이너 종료"
        docker-compose stop jtoon-green
fi
