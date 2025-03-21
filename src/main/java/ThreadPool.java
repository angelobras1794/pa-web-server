import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool extends ThreadPoolExecutor {

    public ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void beforeExecute(Thread thread, Runnable r) {
        super.beforeExecute(thread, r);
        System.out.println("Vai ser criada uma nova thread");
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r,t);
        System.out.println("Foi acabada a thread a thread");
    }

    @Override
    protected void terminated() {
        super.terminated();
        System.out.println(" tHREAD POOL Terminada");
    }

}

