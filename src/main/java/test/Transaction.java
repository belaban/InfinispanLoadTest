package test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Transaction implements Runnable {
	int threadIndex;
	public static AtomicInteger doneThreadsAmount = new AtomicInteger(0);
	public static final float PRINT_PROGRESS_PERCENTAGE = 0.25f; // Print log messages every X% of thread's progress.
    public static final int PRINT=(int)(RunLoadTest.TRANSACTIONS_PER_THREAD * PRINT_PROGRESS_PERCENTAGE);


    protected static final AtomicLong total_get_time=new AtomicLong(0),
      total_put_time=new AtomicLong(0), total_remove_time=new AtomicLong(0); // nanos

    protected static final AtomicInteger num_gets=new AtomicInteger(0), num_puts=new AtomicInteger(0), num_removes=new AtomicInteger(0);


	public Transaction(int threadIndex) {
		this.threadIndex = threadIndex;
	}

	@Override
	public void run() {		
		double averageTime = 0;
		double transactionsInOneMillisecond = 0;
		int transStartIndex = threadIndex * RunLoadTest.TRANSACTIONS_PER_THREAD;
		int transEndIndex = transStartIndex + RunLoadTest.TRANSACTIONS_PER_THREAD;

        long start=System.currentTimeMillis();
		for (int index = transStartIndex ; index < transEndIndex ; index++) {
            startTransaction(index);
            if ((index + 1 - transStartIndex) % PRINT == 0) {
                System.out.println("finished "+(100*(index + 1 - transStartIndex)/RunLoadTest.TRANSACTIONS_PER_THREAD)+"% of the thread's transactions");
			}
		}		
		
		// Is this the last finished thread ?
		if (doneThreadsAmount.incrementAndGet() == RunLoadTest.THREADS_AMOUNT) {
			// Calculate how much time this thread was running.
			long run_time = System.currentTimeMillis() - start;
			int totalTransactions = (RunLoadTest.TRANSACTIONS_PER_THREAD * RunLoadTest.THREADS_AMOUNT);
            System.out.println(RunLoadTest.THREADS_AMOUNT +" threads run " + RunLoadTest.TRANSACTIONS_PER_THREAD + " transactions per thread => total of " + totalTransactions + " transactions." );
            System.out.println("Run time is " + run_time + " milliseconds.");
			if (run_time != 0) {
				averageTime = run_time / (double)totalTransactions;
				transactionsInOneMillisecond = totalTransactions / run_time;
			}
            System.out.println("Average transaction time is " + averageTime + " ms");
            System.out.println("Amount of transactions in one millisecond is " + transactionsInOneMillisecond);
            double tx_per_sec=totalTransactions / (run_time/ 1000.0);
            System.out.println("Transactions / sec: " + tx_per_sec);


            double nanos_per_get=total_get_time.get() / num_gets.get();
            double nanos_per_put=total_put_time.get() / num_puts.get();
            double nanos_per_rem=total_remove_time.get() / num_removes.get();

            System.out.println(nanos_per_get + " ns / get, " + nanos_per_put + " ns / put, " + nanos_per_rem + " ns / remove");

		}
		
	}

	private static void startTransaction(int index) {
		byte[] buffer=new byte[5];


        long start, diff;

        start=System.nanoTime();
		RunLoadTest.testCache.get(index);
		RunLoadTest.testCache.get(index);
        diff=System.nanoTime() - start;
        total_get_time.addAndGet(diff);
        num_gets.addAndGet(2);

        start=System.nanoTime();
		RunLoadTest.testCache.put(index, buffer);
        diff=System.nanoTime() - start;
        num_puts.incrementAndGet();
        total_put_time.addAndGet(diff);

        start=System.nanoTime();
		RunLoadTest.testCache.get(index);
		RunLoadTest.testCache.get(index);
		RunLoadTest.testCache.get(index);
		RunLoadTest.testCache.get(index);
        diff=System.nanoTime() - start;
        num_gets.addAndGet(4);
        total_get_time.addAndGet(diff);

        start=System.nanoTime();
		RunLoadTest.testCache.remove(index);
        diff=System.nanoTime() - start;
        num_removes.incrementAndGet();
        total_remove_time.addAndGet(diff);

        start=System.nanoTime();
		RunLoadTest.testCache.get(index);
		RunLoadTest.testCache.get(index);
		RunLoadTest.testCache.get(index);
		RunLoadTest.testCache.get(index);
        diff=System.nanoTime() - start;
        num_gets.addAndGet(4);
        total_get_time.addAndGet(diff);
	}

}
