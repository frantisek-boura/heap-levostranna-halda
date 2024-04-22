package ads;

import java.util.Iterator;
import enums.ETypProhlidky;

public interface IAbstrTable<K extends Comparable<K>, V> {

    void zrus();
    boolean jePrazdny();

    V najdi(K key) throws AbstrTableException;
    void vloz(K key, V value);
    V odeber(K key) throws AbstrTableException;
    Iterator<V> iterator(ETypProhlidky typProhlidky);

}
