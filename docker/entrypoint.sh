#!/bin/bash
set -e

source /opt/ros/foxy/setup.bash
source /workspace/dragonfly/install/setup.bash

export ROS_MASTER_URI=http://172.18.0.2:11311

ros2 launch rosbridge_server rosbridge_websocket_launch.xml &

exec "$@"
