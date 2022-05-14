#!/bin/bash
set -e

source /opt/ros/melodic/setup.bash
source /workspace/dragonfly/devel/setup.bash

# export ROS_MASTER_URI=http://172.18.0.2:11311
# export ROS_MASTER_URI=http://10.0.0.3:11311

# roslaunch rosbridge_server rosbridge_websocket.launch &

exec "$@"
