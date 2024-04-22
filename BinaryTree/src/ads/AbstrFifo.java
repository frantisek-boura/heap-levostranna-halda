package ads;

import java.util.Iterator;

public class AbstrFifo<T> implements IAbstrLifoFifo<T> {
    
    private AbstrDoubleList<T> struktura;

    public AbstrFifo() {
        struktura = new AbstrDoubleList<T>();
    }

    @Override
    public void zrus() {
        struktura.zrus();
    }

    @Override
    public boolean jePrazdny() {
        return struktura.jePrazdny();
    }

    @Override
    public void vloz(T data) {
        struktura.vlozPosledni(data);
    }

    @Override
    public T odeber() throws AbstrDoubleListException {
        return struktura.odeberPrvni();
    }

    @Override
    public Iterator<T> iterator() {
        return struktura.iterator();
    }

}
