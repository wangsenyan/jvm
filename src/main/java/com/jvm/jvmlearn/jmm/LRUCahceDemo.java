package com.jvm.jvmlearn.jmm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU算法
 * 1. 获取的时候,放到链表头
 * 2. 删除的时候,删除尾部
 */
public class LRUCahceDemo<K,V> extends LinkedHashMap<K,V> {
    private int capacity;
    public LRUCahceDemo(int capacity) {
        super(capacity,0.75F,true);//true for access-order
        this.capacity = capacity;
    }

    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return super.size()>capacity;
    }
    public static void main(String[] args) {
        LRUCahceDemo lru = new LRUCahceDemo(3);
        lru.put(1,"a");
        lru.put(2,"b");
        lru.put(3,"c");
        System.out.println(lru);
        lru.put(4,"c");
        System.out.println(lru);
    }
}

/** put方法,table哈希表长度,e当前节点指针
 *  https://juejin.cn/post/6844903799748821000
 *  https://youzhixueyuan.com/the-underlying-structure-and-principle-of-hashmap.html
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        //如果table尚未初始化，则此处进行初始化数组，并赋值初始容量，重新计算阈值
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        //通过hash找到下标，如果hash值指定的位置数据为空，则直接将数据存放进去
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        else {
            //如果通过hash找到的位置有数据，发生碰撞
            Node<K,V> e; K k;
            //如果需要插入的key和当前hash值指定下标的key一样，先将e数组中已有的数据
            if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
            //如果此时桶中数据类型为 treeNode，使用红黑树进行插入
            else if (p instanceof TreeNode)
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
                 //此时桶中数据类型为链表
                 // 进行循环
                for (int binCount = 0; ; ++binCount) {
                    //如果链表中没有最新插入的节点，将新放入的数据放到链表的末尾
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value, null);
                        //如果链表过长，达到树化阈值，将链表转化成红黑树
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);
                        break;
                    }
                    //如果链表中有新插入的节点位置数据不为空，则此时e 赋值为节点的值，跳出循环
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            //经过上面的循环后，如果e不为空，则说明上面插入的值已经存在于当前的hashMap中，那么更新指定位置的键值对
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
        //如果此时hashMap size大于阈值，则进行扩容
        if (++size > threshold)
            resize();
        afterNodeInsertion(evict);
        return null;
    }
*/
/** resize()
final Node<K,V>[] resize() {
    Node<K,V>[] oldTab = table;
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    int newCap, newThr = 0;
    //1、table已经初始化，且容量 > 0
    if (oldCap > 0) {
        //如果旧的容量已近达到最大值，则不再扩容，阈值直接设置为最大值
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        //如果旧的容量不小于默认的初始容量，则进行扩容，容量扩张为原来的二倍
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1; // double threshold
    }
    //2、阈值大于0 threshold 使用 threshold 变量暂时保存 initialCapacity 参数的值
    else if (oldThr > 0) // initial capacity was placed in threshold
        newCap = oldThr;
    //3、 threshold 和 table 皆未初始化情况，此处即为首次进行初始化
    //也就在此处解释了构造方法中没有对threshold 和 初始容量进行赋值的问题
    else {               // zero initial threshold signifies using defaults
        newCap = DEFAULT_INITIAL_CAPACITY;
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    //newThr 为 0 时，按阈值计算公式进行计算，容量*负载因子
    if (newThr == 0) {
        float ft = (float)newCap * loadFactor;
        newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                (int)ft : Integer.MAX_VALUE);
    }
    //更新数组桶
    threshold = newThr;
    @SuppressWarnings({"rawtypes","unchecked"})
    Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    table = newTab;
    if (oldTab != null) {
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            //如果指定下标下有数据
            if ((e = oldTab[j]) != null) {
                //1、将指定下标数据置空
                oldTab[j] = null;
                //2、指定下标只有一个数据
                if (e.next == null)
                    newTab[e.hash & (newCap - 1)] = e;
                else if (e instanceof TreeNode)
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                else { // preserve order
                    Node<K,V> loHead = null, loTail = null;
                    Node<K,V> hiHead = null, hiTail = null;
                    Node<K,V> next;
                    do {
                        next = e.next;
                        if ((e.hash & oldCap) == 0) {
                            if (loTail == null)
                                loHead = e;
                            else
                                loTail.next = e;
                            loTail = e;
                        }
                        else {
                            if (hiTail == null)
                                hiHead = e;
                            else
                                hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
    return newTab;
}
*/

class MyLRU{
    class Node<K,V>{
        K key;
        V value;
        Node<K,V> prev;
        Node<K,V> next;
        public Node(){
            this.prev = this.next = null;
        }
        public Node(K  key,V value){
            this.key = key;
            this.value = value;
            this.prev = this.next = null;
        }
    }
    //构造双向链表
    class DLinkList<K,V>{
        Node<K,V> head;
        Node<K,V> tail;
        public DLinkList(){
            head = new Node<>();
            tail = new Node<>();
            head.next = tail;
            tail.prev = head;
        }

        /**
         * 添加到头
         */
        public void addHead(Node<K,V> node)
        {
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
        }

        /**
         * 删除节点
         */
        public void delNode(Node<K,V> node){
            node.next.prev = node.prev;
            node.prev.next = node.next;
            node.prev = null;
            node.next = null;

        }

        /**
         * 获取最后一个
         */
        public Node getLast(){
            if(head.next!=tail)
                return tail.prev;
            return null;
        }
    }

    private int size;
    Map<Integer,Node<Integer,Integer>>map;
    DLinkList<Integer,Integer> dLinkList;

    public MyLRU(int size){
        this.size = size;
        map = new HashMap<>();
        dLinkList = new DLinkList<>();
    }
    public int get(int key){
       if(map.containsKey(key)){
           return -1;
       }
       Node<Integer,Integer> node = map.get(key);
       dLinkList.delNode(node);
       dLinkList.addHead(node);
       return node.value;
    }
    public void put(int key,int value){
        if(map.containsKey(key)){
            Node<Integer,Integer> node = map.get(key);
            node.value = value;
            map.put(key,node);
            dLinkList.delNode(node);
            dLinkList.addHead(node);
        }else{
            if(map.size()== size)
            {
                Node<Integer,Integer> lastNode = dLinkList.getLast();
                map.remove(lastNode.key);
                dLinkList.delNode(lastNode);
            }
            Node<Integer,Integer> newNode = new Node<>(key,value);
            map.put(key,newNode);
            dLinkList.addHead(newNode);
        }
    }

    public static void main(String[] args) {
        MyLRU lru = new MyLRU(3);
        lru.put(1,3);
        lru.put(2,5);
        lru.put(3,6);
        System.out.println(lru);
        lru.put(4,4);
        System.out.println(lru);
    }
}