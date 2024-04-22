package ads;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;

public class AbstrHeap<T extends Comparable<T>> implements IAbstrHeap<T> {

    private T[] halda;

    public AbstrHeap() {
        this.halda = (T[]) Array.newInstance(Comparable.class, 0);
    }

    private int rodic(int i) {
        return (i - 1) / 2;
    }

    @Override
    public void vybuduj(T[] objs) throws AbstrHeapException {
        if (objs.length == 0) throw new AbstrHeapException("Neni z ceho vybudovat haldu, poskytnute pole prazdne");
        if (objs == null) throw new NullPointerException("Neni z ceho vybudovat haldu, poskytnute pole neexistuje");

        this.halda = objs;

        prebuduj();
    }

    @Override
    public void prebuduj() throws AbstrHeapException {
        if (this.halda.length == 0) throw new AbstrHeapException("Neni co prebudovat, halda je prazdna");

        T[] novaHalda = (T[]) Array.newInstance(Comparable.class, this.halda.length);
        System.arraycopy(this.halda, 0, novaHalda, 0, this.halda.length);
        this.halda = (T[]) Array.newInstance(Comparable.class, 0);

        for (int i = 0; i < novaHalda.length; i++) {
            vloz(novaHalda[i], i);
        }
    }

    private void vloz(T obj, int i) {
        
        rozsir();
        this.halda[this.halda.length - 1] = obj;
        

        while (i > 0) {
            T parent = this.halda[rodic(i)];
            if (obj.compareTo(parent) < 0) {
                this.halda[rodic(i)] = this.halda[i];
                this.halda[i] = parent;

                i = rodic(i);
            } else break;
        }
    }

    private void rozsir() {
        T[] novaHalda = (T[]) Array.newInstance(Comparable.class, this.halda.length + 1);
        System.arraycopy(this.halda, 0, novaHalda, 0, this.halda.length);
        this.halda = novaHalda;
    }
    

    @Override
    public void zrus() {
        this.halda = (T[]) Array.newInstance(Comparable.class, 0);
    }

    @Override
    public boolean jePrazdny() {
        return this.halda.length == 0;
    }

    @Override
    public void vloz(T obj) {
        if (obj == null) throw new NullPointerException("Nelze vlozit poskytnuty prvek, protoze neexistuje");

        rozsir();
        this.halda[this.halda.length - 1] = obj;

        try {
            prebuduj();
        } catch (AbstrHeapException e) {
            //po vlozeni nebude heap nikdy prazdny
            // tato situace nikdy nenastane
        }
    }

    @Override
    public T odeberMax() throws AbstrHeapException {
        if (this.halda.length == 0) throw new AbstrHeapException("Prazdna halda");

        T obj = this.halda[0];
        T[] newHeap = (T[]) Array.newInstance(Comparable.class, this.halda.length - 1);

        System.arraycopy(this.halda, 1, newHeap, 0, newHeap.length);
        this.halda = newHeap;

        if (!jePrazdny()) prebuduj();

        return obj;
    }

    @Override
    public T zpristupniMax() throws AbstrHeapException {
        if (this.halda.length == 0) throw new AbstrHeapException("Prazdna halda");

        return this.halda[0];
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return !jePrazdny() && index < halda.length;
            }

            @Override
            public T next() {
                return halda[index++];
            }
        };
    }
}