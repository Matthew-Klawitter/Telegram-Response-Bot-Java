package cafe.seafarers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A map that only keeps the most recent items
 * 
 * @author mark
 *
 */
public class MapStack<K, V> {
	private int maxCount;
	private HashMap<K, V> map;
	private Queue<K> queue;

	public MapStack(int maxCount) {
		this.maxCount = maxCount;
		map = new HashMap<K, V>();
		queue = new LinkedList<K>();
	}

	public V get(K key) {
		return map.get(key);
	}

	public void put(K key, V value) {
		// If our queue is full, remove the last element
		if (queue.size() >= maxCount) {
			K toRemove = queue.remove();
			map.remove(toRemove);
		}
		map.put(key, value);
		queue.add(key);
	}

	public int size() {
		return map.size();
	}
}
