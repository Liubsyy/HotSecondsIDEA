#!/bin/bash

PIDFILE="pid"

if [ -f "$PIDFILE" ]; then
    PID=$(cat "$PIDFILE")
    if ps -p $PID > /dev/null; then
       echo "进程已经启动 (PID: $PID)."
       exit 1
    else
       echo "PID文件存在但进程不在运行，将重新启动进程。"
       rm "$PIDFILE"
       nohup java -jar HotSecondsProxy.jar > hotproxy.log 2>&1 & echo $! > $PIDFILE
       tail -f hotproxy.log
    fi
else
    nohup java -jar HotSecondsProxy.jar > hotproxy.log 2>&1 & echo $! > $PIDFILE
    tail -f hotproxy.log
fi
