package ch.heig.amt.g4mify.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ldavid
 * @created 1/25/17
 */
public class KeyedLock {

    private Map<String, Lock> locks = new HashMap<>();

    public synchronized Lock get(String key) {
        if (!locks.containsKey(key)) {
            locks.put(key, new ReentrantLock());
        }

        return locks.get(key);
    }

}
