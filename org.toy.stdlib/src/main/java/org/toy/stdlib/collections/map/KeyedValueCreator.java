package org.toy.stdlib.collections.map;

public interface KeyedValueCreator<K, V> {
	V create(K k);
}
