package ads;

import java.util.Iterator;

public interface IAbstrLifoFifo<T> extends Iterable<T> {

    void zrus();
    boolean jePrazdny();

    void vloz(T data);
    T odeber() throws AbstrDoubleListException;

    @Override
    Iterator<T> iterator();

}
