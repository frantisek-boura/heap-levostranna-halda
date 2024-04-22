package ads;

import java.util.Iterator;

public interface IAbstrDoubleList<T> extends Iterable<T> {

    void zrus();
    boolean jePrazdny();

    void vlozPrvni(T data);
    void vlozPosledni(T data);
    void vlozNaslednika(T data) throws AbstrDoubleListException;
    void vlozPredchudce(T data) throws AbstrDoubleListException;

    T zpristupniAktualni() throws AbstrDoubleListException;
    T zpristupniPrvni() throws AbstrDoubleListException;
    T zpristupniPosledni() throws AbstrDoubleListException;
    T zpristupniNaslednika() throws AbstrDoubleListException;
    T zpristupniPredchudce() throws AbstrDoubleListException;

    T odeberAktualni() throws AbstrDoubleListException;
    T odeberPrvni() throws AbstrDoubleListException;
    T odeberPosledni() throws AbstrDoubleListException;
    T odeberNaslednika() throws AbstrDoubleListException;
    T odeberPredchudce() throws AbstrDoubleListException;

    Iterator<T> iterator();

}
