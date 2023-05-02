package com.liubs.ff.util;

import com.liubs.ff.uitools.LogUtil;

import java.util.concurrent.*;

public class ThreadPoolUtil {
    private static ExecutorService rmiClassExecutorService =new ThreadPoolExecutor(10, 20,
            5, TimeUnit.SECONDS, new LinkedBlockingQueue<>(30),
            (runnable, poolExecutor) -> {
                LogUtil.error("队列已满，请重启IDEA和服务器");
                throw new RejectedExecutionException("队列已满");
            });

    private static ExecutorService rmiFileExecutorService = Executors.newFixedThreadPool(5);

    public static void runClassDeploy(Runnable runnable) {
        rmiClassExecutorService.execute(runnable);
    }
    public static  void runFileDeploy(Runnable runnable) {
        rmiFileExecutorService.execute(runnable);
    }
}
