import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SeparateChaining<K, V> {
    List<HashNode<K, V>> buckets;
    private int capacity;
    private int countOfItems;

    private static final double LOAD_FACTOR = 0.75;
    private static final int INITIAL_CAPASITY = 10;

    public SeparateChaining() {
        countOfItems = 0;
        capacity = INITIAL_CAPASITY;
        buckets = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            buckets.add(null);
        }
    }

    public int size() {
        return countOfItems;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int hash(K key) {
        return Objects.hashCode(key);
    }

    private int getBucketIndex(K key) {
        int hashCode = hash(key);
        return hashCode & (capacity - 1);
    }

    public V delete(K key) {
        int bucketIndex = getBucketIndex(key);
        int hash = hash(key);

        HashNode<K, V> head = buckets.get(bucketIndex);

        HashNode<K, V> prev = null;
        while (head != null) {
            if (head.key.equals(key) && hash == head.HashCode) {
                break;
            }
            prev = head;
            head = head.next;
        }
        if (head == null) {
            return null;
        }

        countOfItems--;

        if (prev != null) {
            prev.next = head.next;
        } else {
            buckets.set(bucketIndex, head.next);
        }
        return head.value;
    }

    public V get(K key) {
        int bucketIndex = getBucketIndex(key);
        int hash = hash(key);

        HashNode<K, V> head = buckets.get(bucketIndex);

        while (head != null) {
            if (head.key.equals(key) && head.hashCode() == hash) {
                return head.value;
            }
            head = head.next;
        }
        return null;
    }

    public void add(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        int hash = hash(key);

        HashNode<K, V> head = buckets.get(bucketIndex);

        while (head != null) {
            if (head.key.equals(key) && head.hashCode() == hash) {
                head.value = value;
                return;
            }
            head = head.next;
        }

        countOfItems++;
        head = buckets.get(bucketIndex);
        HashNode<K, V> newNode = new HashNode<K, V>(key, value, hash);
        newNode.next = head;
        buckets.set(bucketIndex, newNode);


        if ((LOAD_FACTOR * capacity) == size()) {
            List<HashNode<K, V>> temp = buckets;
            buckets = new ArrayList<>();
            capacity = 2 * capacity;
            countOfItems = 0;
            for (int i = 0; i < capacity; i++) {
                buckets.add(null);
            }
            for (HashNode<K, V> headNode : temp) {
                while (headNode != null) {
                    add(headNode.key, headNode.value);
                    headNode = headNode.next;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "SeparateChaining{" +
                "buckets=" + buckets +
                ", capacity=" + capacity +
                ", countOfItems=" + countOfItems +
                '}';
    }
}
