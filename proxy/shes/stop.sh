#!/bin/bash

PIDFILE="pid"

if [ -f "$PIDFILE" ]; then
    PID=$(cat "$PIDFILE")
    kill -9 $PID
    rm "$PIDFILE"
    echo "进程已关闭"
    exit 0
fi
echo "代理服务器不在运行中"
