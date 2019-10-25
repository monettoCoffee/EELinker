package eel.entry;

import java.util.Map;

/**
 * @author monetto
 */
public class Node<K, V> implements Map.Entry{

    private K key;
    private V value;

    public Node(Object k, Object v){
        this.key = (K)k;
        this.value = (V)v;
    }

    @Override
    public K getKey() {
        return this.key;
    }

    @Override
    public V getValue() {
        return this.value;
    }

    @Override
    public V setValue(Object value) {
        Object beforeValue = this.value;
        this.value = (V)value;
        return (V)beforeValue;
    }
}
