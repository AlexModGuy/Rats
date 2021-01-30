package com.github.alexthe666.rats.server.pathfinding;
/*
    All of this code is used with permission from Raycoms, one of the developers of the minecolonies project.
 */

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.pathfinding.pathjobs.AbstractPathJob;
import net.minecraft.pathfinding.Path;

import java.util.concurrent.*;
/**
 * Static class the handles all the Pathfinding.
 */
public final class Pathfinding {
    private static final BlockingQueue<Runnable> jobQueue = new LinkedBlockingDeque<>();
    private static ThreadPoolExecutor executor;

    private Pathfinding() {
        //Hides default constructor.
    }

    public static boolean isDebug() {
        return false;
    }

    /**
     * Creates a new thread pool for pathfinding jobs
     *
     * @return the threadpool executor.
     */
    public static ThreadPoolExecutor getExecutor() {
        if (executor == null) {
            executor = new ThreadPoolExecutor(1, RatConfig.ratsPathfindingThreads, 0, TimeUnit.SECONDS, jobQueue, new RatsThreadFactory());
        }
        return executor;
    }

    /**
     * Stops all running threads in this thread pool
     */
    public static void shutdown() {
        getExecutor().shutdownNow();
        jobQueue.clear();
        executor = null;
    }

    /**
     * Add a job to the queue for processing.
     *
     * @param job PathJob
     * @return a Future containing the Path
     */
    public static Future<Path> enqueue(final AbstractPathJob job) {
        if(getExecutor().isShutdown() || getExecutor().isTerminating() || getExecutor().isTerminated()){
            return null;
        }
        return getExecutor().submit(job);
    }


    /**
     * Rats specific thread factory.
     */
    public static class RatsThreadFactory implements ThreadFactory {
        /**
         * Ongoing thread IDs.
         */
        public static int id;

        @Override
        public Thread newThread(final Runnable runnable) {
            final Thread thread = new Thread(runnable, "Rats Pathfinding Worker #" + (id++));
            thread.setDaemon(true);

            thread.setPriority(Thread.NORM_PRIORITY);
            thread.setUncaughtExceptionHandler((thread1, throwable) -> RatsMod.LOGGER.error("Rats Pathfinding Thread errored! ", throwable));
            return thread;
        }
    }
}
