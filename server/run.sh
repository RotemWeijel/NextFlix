#!/bin/bash

# Unset or override the localhost host
unset RECOMMENDATION_SERVER_HOST

# Function to start the recommendation server
start_recommendation_server() {
    local port=$1
    /usr/local/bin/NetflixServer "$port" &
    RECOMMEND_PID=$!
    sleep 2
}

# Function to start the web server
start_web_server() {
    local port=$1
    local recommend_port=$2
    cd /usr/src/netflix/src/web-server
    export PORT=$port
    export RECOMMENDATION_SERVER_PORT=$recommend_port
    export RECOMMENDATION_SERVER_HOST=localhost
    node server.js $port $recommend_port &
    WEB_PID=$!
}

# Main startup logic
WEB_PORT=${PORT:-3000}
RECOMMEND_PORT=${RECOMMENDATION_SERVER_PORT:-6000}

# Start recommendation server
start_recommendation_server $RECOMMEND_PORT

# Start web server
start_web_server $WEB_PORT $RECOMMEND_PORT

# Wait for either process to exit
wait -n $RECOMMEND_PID $WEB_PID

# If one server exits, kill the other and exit
kill $RECOMMEND_PID $WEB_PID 2>/dev/null
exit 1