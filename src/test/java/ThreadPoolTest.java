import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ThreadPoolTest {
    private ThreadPool threadPool;
    private BlockingQueue<Runnable> queue;

    @BeforeEach
    void setUp() {
        queue = new ArrayBlockingQueue<>(10);
        threadPool = new ThreadPool(2, 4, 10, TimeUnit.SECONDS, queue);
    }

    @AfterEach
    void tearDown() {
        threadPool.shutdown();
    }

    @Test
    void testBeforeExecute() {
        Runnable task = () -> System.out.println("Executando tarefa");        threadPool.execute(task);

        // Aguarda a execução da tarefa
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            fail("ThreadPool não finalizou corretamente");
        }
    }

    @Test
    void testAfterExecute() {
        Runnable task = () -> System.out.println("Executando tarefa");
        threadPool.execute(task);

        threadPool.shutdown();
        try {
            threadPool.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            fail("ThreadPool não finalizou corretamente");
        }
    }

    @Test
    void testTerminated() {
        threadPool.shutdown();
        try {
            assertTrue(threadPool.awaitTermination(5, TimeUnit.SECONDS), "ThreadPool não finalizou corretamente");
        } catch (InterruptedException e) {
            fail("ThreadPool foi interrompida inesperadamente");
        }
    }
}
