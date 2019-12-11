package cpuTime;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;

public class Main {
	
    public static void main(String[] args) throws Exception {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        threadMXBean.setThreadCpuTimeEnabled(true);
    	
    	int numThreads = 10;
        long start = System.currentTimeMillis();
        List<Thread> threads=new ArrayList<Thread>();
        for (int i = 0; i < numThreads; ++i) {
            Thread thread=new MyThread();
            thread.start();
            threads.add(thread);
        }

        long[] allThreadIds = threadMXBean.getAllThreadIds();
        for(long id:allThreadIds) {
        	System.out.println(id);
        	System.out.println(threadMXBean.getThreadCpuTime(id));
        }
        long nano = 0;

        for(Thread thread:threads) {
//        	thread.join();
        	long id=thread.getId();
        	long nn=threadMXBean.getThreadCpuTime(id);
        	System.out.println(id+" "+nn);
            nano += nn;
        } 
        System.out.printf("Total cpu time: %s ms; real time: %s", nano / 1E6, (System.currentTimeMillis() - start));
    }
}

class MyThread extends Thread {
    public void run() {
        int sum = 0;
        for (int i = 0; i < 1000000; ++i) {
            sum += i;
        }
        sum = sum + 1;
    }
}