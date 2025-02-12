package it.unicam.cs.ids2425.utilities.wrappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "key")
public class Pair<K, V> {
    private K key;
    private V value;
}
