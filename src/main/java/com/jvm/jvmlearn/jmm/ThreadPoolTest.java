package com.jvm.jvmlearn.jmm;

import java.util.concurrent.*;

/**
 * 线程池
 * 1. 使用线程
 *   - Thread
 *   - Runnable
 *   - Callable
 *   - ThreadPoolExecutor(int corePoolSize,                     //常驻线程核心数
 *                        int maximumPoolSize,
 *                        long keepAliveTime,                   //空闲线程的存活时间
 *                        TimeUnit unit,                        //keepAliveTime的单位
 *                        BlockingQueue<Runnable> workQueue)    //等待执行任务的队列
 *     - this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,Executors.defaultThreadFactory(), defaultHandler);
 *                        defaultThreadFactory                  //生成工作线程的默认工厂类
 *                        defaultHandler                        //拒绝策略,等待队列满了后如何应对继续来的任务
 *     - Executors.newFixedThreadPool
 *       - ThreadPoolExecutor(nThreads, nThreads,0L,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>())
 *     - Executors.newSingleThreadExecutor
 *       - ThreadPoolExecutor(1, 1,0L,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>())
 *     - Executors.newCachedThreadPool 可扩容
 *       - ThreadPoolExecutor(0, Integer.MAX_VALUE,60L,TimeUnit.SECONDS,new SynchronousQueue<Runnable>())
 * 2. 原理
 *  - 判断线程是否正在运行,w.isLocked()
 *  - 运行中的线程 runWorker() 持续运行队列中的任务
 *  - 运行过程
 *    - 当创建线程池后,等待提交过来的任务请求
 *    - 当调用execute()方法添加一个请求任务时:
 *     - 正在运行线程数小于corePoolSize,马上创建线程运行这个任务
 *     - 正在运行线程数大于或等于corePoolSize,将这个任务放入队列
 *     - 如果队列满且正在运行的线程数量小于maximumPoolSize,创建非核心线程执行这个任务
 *     - 如果队列满且正在运行的线程数大于或等于maximumPoolSize,线程池启动饱和拒接策略执行
 *  - 当线程完成任务,从队列中取出下一个任务执行
 *  - 当一个线程空闲超过一定时间 keepAliveTime时,线程会判断
 *     - 如果当前运行线程数大于corePoolSize,这个线程被停掉
 *  - 线程池的所有任务完成后它最终会收缩到corePoolSize的大小
 *
 *     public void execute(Runnable command) {
           if (command == null)
               throw new NullPointerException();
           int c=ctl.get(); //获取当前工作线程数
            if(workerCountOf(c)<corePoolSize){ //如果当前工作线程小于核心线程数,添加线程
                if(addWorker(command,true))    //增加线程
                   return;
                c=ctl.get();//重新获取当前工作线程数
            }
            //核心线程数已满或者添加失败,会到这一步,
            if(isRunning(c)&&workQueue.offer(command)){//将任务加入阻塞队列
                int recheck=ctl.get();
                if(!isRunning(recheck)&&remove(command))
                    reject(command);
                else if(workerCountOf(recheck)==0)
                    addWorker(null,false);
            }
            //如果阻塞队列已满
            else if(!addWorker(command,false))
                reject(command);
     }
 * 3. 拒绝策略 RejectedExecutionHandler
 *  - DiscardOldestPolicy
 *   - 抛弃队列中等待最久的任务,然后把当前任务加入队列中尝试再次提交任务
 *  - AbortPolicy
 *   - 直接抛出RejectedExecutionException异常阻止系统正常运行
 *  - CallerRunsPolicy
 *   - "调用者运行"一种调节机制,将任务回退到调用者
 *  - DiscardPolicy
 *   - 直接丢弃任务,不予任务处理也不抛出异常
 * 4. 建议直接定义ThreadPoolExecutor
 * 5. 线程池最佳合理参数配置
 *   - CPU密集型
 *    - CPU数+1个线程的线程池
 *   - IO密集型 尽量让CPU排满
 *    - CPU核数 * 2
 *    - CPU核数/(1-阻塞系数) 阻塞系数在0.8 ~ 0.9 之间
 */
public class ThreadPoolTest {
    public static void main(String[] args) {
        ExecutorService threadPool0 = Executors.newFixedThreadPool(5);
        ExecutorService threadPool1 = Executors.newSingleThreadExecutor();
        ExecutorService threadPool2 = Executors.newCachedThreadPool();
        ExecutorService threadPool3 = new ThreadPoolExecutor(
                2,
                5,
                1L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(3),
                Executors.defaultThreadFactory(),
                //new ThreadPoolExecutor.CallerRunsPolicy() //主业务处理
                //new ThreadPoolExecutor.AbortPolicy()    //RejectedExecutionException
                //new ThreadPoolExecutor.DiscardOldestPolicy() //丢弃时间最久的
                new ThreadPoolExecutor.DiscardPolicy()
        );
        try {
            for (int i = 0; i < 18; i++) {
                threadPool3.execute(()->{
                    System.out.println(Thread.currentThread().getName() + "\t" + "办理业务");
                });
                //TimeUnit.MICROSECONDS.sleep(200);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            threadPool3.shutdown();
        }

    }
}
