package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;

public class RunLoadTest {

	public static Cache<Integer,byte[]> testCache=null;
	public static final int THREADS_AMOUNT = 10;
	public static final int TRANSACTIONS_PER_THREAD = 100 * 1000;
	private DefaultCacheManager cm;
	
	public static void main(String[] args) {


        if(args.length != 1) {
            System.err.println("RunLoadTest.class, Usage: java RunLoadTest [active/passive]");
            return;
        }

        System.out.println("RunLoadTest.class, starting process");
		RunLoadTest runLoadTest = new RunLoadTest();

        if(args[0].equals("active"))
            runActiveNode();
        else if(args[0].equals("passive"))
            runPassiveNode();
        else
            System.err.println("RunLoadTest.class, Usage: java RunLoadTest [active/passive]");
    }

	// Initialize cache
	public RunLoadTest() {		
		try {
			cm = new DefaultCacheManager("/home/bela/infinispan.xml");
			testCache = cm.getCache("distCache");

		} catch (IOException e) {
            System.err.println(getClass() + ": Failed to initialize cache - "+e);
		}

	}

	private static void runPassiveNode() {
        System.out.println("Running as passive node...");
		try {
			Thread.sleep(1000000000);
		} catch (InterruptedException e) {
            System.err.println(e.toString());
		}
	}

	private static void runActiveNode() {
        System.out.println("Running as active node...");

		// create {THREADS_AMOUNT} threads and start them.
        List<Thread> threads=new ArrayList<Thread>(THREADS_AMOUNT);
		for (int threadIndex = 0 ; threadIndex < THREADS_AMOUNT ; threadIndex++) {
            System.out.println("Starting thread number " + threadIndex);
			Transaction trans = new Transaction(threadIndex);
			Thread thread = new Thread(trans);
            threads.add(thread);
			thread.start(); // TODO: Use threadPoolExecutor here
		}

        for(Thread thread: threads) {
            try {
                thread.join();
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(testCache != null)
            testCache.stop();
		
	}	
}
