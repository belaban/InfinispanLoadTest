package test;

import java.util.concurrent.atomic.AtomicInteger;

public class Transaction implements Runnable {
	int threadIndex;
	public static AtomicInteger doneThreadsAmount = new AtomicInteger(0);
	public static final float PRINT_PROGRESS_PERCENTAGE = 0.25f; // Print log messages every X% of thread's progress.
    public static final int PRINT=(int)(RunLoadTest.TRANSACTIONS_PER_THREAD * PRINT_PROGRESS_PERCENTAGE);

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
		}
		
	}

	private static void startTransaction(int index) {
		byte[] buffer=new byte[5];

        // RunLoadTest.testCache.startBatch();
		RunLoadTest.testCache.get(index);
		RunLoadTest.testCache.get(index);
		RunLoadTest.testCache.put(index, buffer);
		RunLoadTest.testCache.get(index);
		RunLoadTest.testCache.get(index);
		RunLoadTest.testCache.get(index);
		RunLoadTest.testCache.get(index);
		RunLoadTest.testCache.remove(index);
		RunLoadTest.testCache.get(index);
		RunLoadTest.testCache.get(index);
		RunLoadTest.testCache.get(index);
		RunLoadTest.testCache.get(index);
        // RunLoadTest.testCache.endBatch(true);
	}

}
