## AQS

### ReentrantLock
* lock
  - 第一个线程lock
    - `tryAcquire(arg)` 为 `true`
  - 第二个线程lock(第一个未释放锁) 每次循环都会请求锁
    - `tryAcquire(arg)` 为 `false`
    - `acquire(null, arg, false, false, false, 0L)`  
      - 第一次循环: 
         - 新建node节点,node.waiter设置为当前线程 
      - 第二次循环(初始化队列)
         - node节点的prev指向tail(null)
         - 新建哨兵节点h
         - head和tail设为哨兵节点h
      - 第三次循环
         - node节点的prev指向tail,即哨兵节点
         - 将tail指向node节点
         - 将哨兵节点的next指向node
      - 第四次循环
         - 设置node的状态为WAITING
      - 第五次循环
         - `LockSupport.park(this)` 线程阻塞等待
    - 线程成功获取锁
  - 第三个及以上线程
    - 同第二个线程,但是只执行第二个线程的第三次循环
    
* unlock
         
    
```java
public abstract class AbstractOwnableSynchronizer
        implements java.io.Serializable {
    private transient Thread exclusiveOwnerThread;   //表示当前占用锁的线程
}
class AbstractQueuedSynchronizer extends AbstractOwnableSynchronizer{
    abstract static class Node {
        volatile Node prev;       // initially attached via casTail
        volatile Node next;       // visibly nonnull when signallable
        Thread waiter;            // visibly nonnull when enqueued
        volatile int status;      // written by owner, atomic bit ops by others
    }
    public final void acquire(int arg) {
        if (!tryAcquire(arg))     //NonFairSync的tryAcquire()方法
            acquire(null, arg, false, false, false, 0L); 
    }
    final int acquire(Node node, int arg, boolean shared,
                      boolean interruptible, boolean timed, long time) {
        Thread current = Thread.currentThread();
        byte spins = 0, postSpins = 0;   // retries upon unpark of first thread
        boolean interrupted = false, first = false;
        Node pred = null;                // predecessor of node when enqueued

        /*
         * Repeatedly:
         *  Check if node now first
         *    if so, ensure head stable, else ensure valid predecessor
         *  if node is first or not yet enqueued, try acquiring
         *  else if node not yet created, create it
         *  else if not yet enqueued, try once to enqueue
         *  else if woken from park, retry (up to postSpins times)
         *  else if WAITING status not set, set and retry
         *  else park and clear WAITING status, and check cancellation
         */
        for (;;) {
            if (!first && (pred = (node == null) ? null : node.prev) != null &&
                    !(first = (head == pred))) {
                if (pred.status < 0) {
                    cleanQueue();           // predecessor cancelled
                    continue;
                } else if (pred.prev == null) {
                    Thread.onSpinWait();    // ensure serialization
                    continue;
                }
            }
            if (first || pred == null) {
                boolean acquired;
                try {
                    if (shared)
                        acquired = (tryAcquireShared(arg) >= 0);
                    else
                        acquired = tryAcquire(arg);
                } catch (Throwable ex) {
                    cancelAcquire(node, interrupted, false);
                    throw ex;
                }
                if (acquired) {
                    if (first) {                 //将出队的节点当成新的哨兵节点
                        node.prev = null;
                        head = node;
                        pred.next = null;
                        node.waiter = null;
                        if (shared)
                            signalNextIfShared(node);
                        if (interrupted)
                            current.interrupt();
                    }
                    return 1;
                }
            }
            if (node == null) {                 // allocate; retry before enqueue
                if (shared)
                    node = new SharedNode();
                else
                    node = new ExclusiveNode(); //创建排他节点
            } else if (pred == null) {          // try to enqueue
                node.waiter = current;
                Node t = tail;
                node.setPrevRelaxed(t);         // avoid unnecessary fence
                if (t == null)
                    tryInitializeHead();        //初始化队列,添加哨兵节点
                else if (!casTail(t, node))     //原子设置node为尾结点
                    node.setPrevRelaxed(null);  //原子设置当前节点的prev节点
                else
                    t.next = node;
            } else if (first && spins != 0) {
                --spins;                        // reduce unfairness on rewaits
                Thread.onSpinWait();            //自旋
            } else if (node.status == 0) {
                node.status = WAITING;          // enable signal and recheck
            } else {
                long nanos;
                spins = postSpins = (byte)((postSpins << 1) | 1);
                if (!timed)
                    LockSupport.park(this);     //线程等待
                else if ((nanos = time - System.nanoTime()) > 0L)
                    LockSupport.parkNanos(this, nanos);
                else
                    break;
                node.clearStatus();
                if ((interrupted |= Thread.interrupted()) && interruptible)
                    break;
            }
        }
        return cancelAcquire(node, interrupted, interruptible);
    }
}
public class ReentrantLock{
    public void lock() {
        sync.lock();
    }
    class Sync extends AbstractQueuedSynchronizer {
        final void lock() {
            if (!initialTryLock())     //  NonFairSync.initialTryLock()
                acquire(1);
        }
    }
    class NonFairSync extends Sync {
        final boolean initialTryLock() {
            Thread current = Thread.currentThread();
            if (compareAndSetState(0, 1)) { // first attempt is unguarded
                setExclusiveOwnerThread(current);
                return true;
            } else if (getExclusiveOwnerThread() == current) {
                int c = getState() + 1;
                if (c < 0) // overflow
                    throw new Error("Maximum lock count exceeded");
                setState(c);
                return true;
            } else
                return false;
        }
        protected final boolean tryAcquire(int acquires) {
            if (getState() == 0 && compareAndSetState(0, acquires)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }
    }
}

```