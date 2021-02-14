package com.github.alexthe666.rats.server.pathfinding;
/*
    All of this code is used with permission from Raycoms, one of the developers of the minecolonies project.
 */

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.pathfinding.pathjobs.AbstractPathJob;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.concurrent.ThreadTaskExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

import java.util.Set;
import java.util.concurrent.*;
/**
 * Static class the handles all the Pathfinding.
 */
public final class Pathfinding {
    private static final Set<Class<?>> loadedJobs = new CopyOnWriteArraySet<>();
    private static final BlockingQueue<Runnable> ratsJobQueue = new LinkedBlockingDeque<>();
    private static ThreadPoolExecutor pathExecutor;

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
    public static ThreadPoolExecutor getPathExecutor() {
        if (pathExecutor == null) {
            pathExecutor = new ThreadPoolExecutor(1, RatConfig.ratsPathfindingThreads, 0, TimeUnit.SECONDS, ratsJobQueue, new RatsThreadFactory());
        }
        return pathExecutor;
    }

    /**
     * Stops all running threads in this thread pool
     */
    public static void shutdown() {
        getPathExecutor().shutdownNow();
        ratsJobQueue.clear();
        pathExecutor = null;
    }

    /**
     * Add a job to the queue for processing.
     *
     * @param job PathJob
     * @return a Future containing the Path
     */
    public static Future<Path> enqueue(final AbstractPathJob job) {
        if (!loadedJobs.contains(job.getClass())) {
            ThreadTaskExecutor<?> workqueue = LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
            CompletableFuture<Path> result = workqueue.isOnExecutionThread() ? CompletableFuture.completedFuture(job.call()) : CompletableFuture.supplyAsync(job::call, workqueue);
            return result.thenApply(path -> {
                loadedJobs.add(job.getClass());
                return path;
            });
        }
        if(getPathExecutor().isShutdown() || getPathExecutor().isTerminating() || getPathExecutor().isTerminated()){
            return null;
        }
        return getPathExecutor().submit(job);
    }


    /**
     * Rats specific thread factory.
     */
    public static class RatsThreadFactory implements ThreadFactory {
        /**
         * Ongoing thread IDs.
         */
        public static int id;

        private static final ClassLoader classLoader;

        static {
            ThreadTaskExecutor<?> workqueue = LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
            if (workqueue.isOnExecutionThread())
                classLoader = Thread.currentThread().getContextClassLoader();
            else
                classLoader = CompletableFuture.supplyAsync(() -> Thread.currentThread().getContextClassLoader(), workqueue).join();
        }

        @Override
        public Thread newThread(final Runnable runnable) {
            final Thread thread = new Thread(runnable, "Rats Mod Pathfinding Worker #" + (id++));
            thread.setDaemon(true);
            thread.setPriority(Thread.MAX_PRIORITY);
            if (thread.getContextClassLoader() != classLoader) {
                RatsMod.LOGGER.info("Corrected CCL of new Rats Pathfinding Thread, was: " + thread.getContextClassLoader().toString());
                thread.setContextClassLoader(classLoader);
            }
            thread.setUncaughtExceptionHandler((thread1, throwable) -> RatsMod.LOGGER.error("Rats Pathfinding Thread errored! ", throwable));
            return thread;
        }
    }
}
