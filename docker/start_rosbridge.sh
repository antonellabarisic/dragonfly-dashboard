#!/bin/bash
set -e

roslaunch rosbridge_server rosbridge_websocket.launch &

exec "$@"