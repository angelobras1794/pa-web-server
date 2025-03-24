import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Custom ThreadPool implementation that extends ThreadPoolExecutor.
 * It provides logging before and after task execution, as well as when the pool is terminated.
 */
public class ThreadPool extends ThreadPoolExecutor {

    /**
     * Constructs a new ThreadPool instance.
     *
     * @param corePoolSize    the number of threads to keep in the pool, even if they are idle
     * @param maximumPoolSize the maximum number of threads allowed in the pool
     * @param keepAliveTime   the maximum time that excess idle threads will wait for new tasks before terminating
     * @param unit            the time unit for the keepAliveTime argument
     * @param workQueue       the queue to use for holding tasks before they are executed
     */
    public ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    /**
     * Method executed before a thread starts running a task.
     *
     * @param thread the thread executing the task
     * @param r      the runnable task
     */
    @Override
    protected void beforeExecute(Thread thread, Runnable r) {
        super.beforeExecute(thread, r);
        System.out.println("Vai ser criada uma nova thread");
    }

    /**
     * Method executed after a thread completes execution of a task.
     *
     * @param r the runnable task that has finished executing
     * @param t the throwable object capturing any exception thrown during execution, or null if no exception occurred
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        System.out.println("Foi acabada a thread a thread");
    }

    /**
     * Method executed when the thread pool is shutting down.
     */
    @Override
    protected void terminated() {
        super.terminated();
        System.out.println("tHREAD POOL Terminada");
    }
}
