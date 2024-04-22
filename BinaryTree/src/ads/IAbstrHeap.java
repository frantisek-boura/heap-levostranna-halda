package ads;

import java.util.Iterator;

public interface IAbstrHeap<T extends Comparable<T>> {

    void vybuduj(T[] objs) throws AbstrHeapException;
    void prebuduj() throws AbstrHeapException;
    void zrus();
    boolean jePrazdny();

    void vloz(T obj);
    T odeberMax() throws AbstrHeapException;
    T zpristupniMax() throws AbstrHeapException;
    Iterator iterator();


}
