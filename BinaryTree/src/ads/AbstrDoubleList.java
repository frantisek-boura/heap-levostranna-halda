package ads;

import java.util.Iterator;

public class AbstrDoubleList<T> implements IAbstrDoubleList<T> {

    private Node<T> prvni;
    private Node<T> aktualni;
    private Node<T> posledni;

    private static class Node<T> {

        private final T data;
        private Node<T> dalsi;
        private Node<T> predchozi;

        public Node(T data) {
            this.data = data;
            this.predchozi = null;
            this.dalsi = null;
        }
        
    }

    public AbstrDoubleList() {
        this.prvni = null;
        this.aktualni = null;
        this.posledni = null;
    }

    @Override
    public void zrus() {
        this.prvni = null;
        this.aktualni = null;
        this.posledni = null;
    }

    @Override
    public boolean jePrazdny() {
        return this.prvni == null && this.aktualni == null && this.posledni == null;
    }

    private void vlozDoPrazdneho(T data) {
        this.prvni = new Node<T>(data);
        this.prvni.predchozi = this.prvni;
        this.prvni.dalsi = this.prvni;
        this.posledni = this.prvni;
        this.aktualni = this.prvni;
    }

    @Override
    public void vlozPrvni(T data) {
        if (data == null) throw new NullPointerException();

        if (jePrazdny()) {
            vlozDoPrazdneho(data);
        } else {
            // inicializace vkladaneho prvku
            // urceni mist predchudce a naslednika v seznamu
            Node<T> vkladany = new Node<T>(data);
            Node<T> pred = this.posledni;
            Node<T> po = this.prvni;

            // navazani vztahu s nove pridanym prvkem
            vkladany.dalsi = po;
            po.predchozi = vkladany;
            vkladany.predchozi = pred;
            pred.dalsi = vkladany;

            // zmena ukazatele na prvni prvek v seznamu na nove vlozeny
            this.prvni = vkladany;
        }
    }

    @Override
    public void vlozPosledni(T data) {
        if (data == null) throw new NullPointerException();

        // pokud je seznam prazdny, tak tato metoda inicializuje seznam
        if (jePrazdny()) {
            vlozDoPrazdneho(data);
        } else {
            // inicializace vkladaneho prvku
            // urceni mist predchudce a naslednika v seznamu
            Node<T> vkladany = new Node<T>(data);
            Node<T> pred = this.posledni;
            Node<T> po = this.prvni;

            // navazani vztahu s nove pridanym prvkem
            vkladany.dalsi = po;
            po.predchozi = vkladany;
            vkladany.predchozi = pred;
            pred.dalsi = vkladany;

            // zmena ukazatele na posledni prvek v seznamu na nove vlozeny
            this.posledni = vkladany;
        }
    }

    @Override
    public void vlozNaslednika(T data) throws AbstrDoubleListException {
        if (data == null) throw new NullPointerException();
        if (jePrazdny()) throw new AbstrDoubleListException("Seznam prazdny");

        // pokud je seznam prazdny, tak tato metoda inicializuje seznam
        if (this.aktualni == this.posledni) {
            vlozPosledni(data);
        } else {
            // inicializace vkladaneho prvku
            // urceni mist predchudce a naslednika v seznamu
            Node<T> vkladany = new Node<T>(data);
            Node<T> pred = this.aktualni;
            Node<T> po = this.aktualni.dalsi;

            // navazani vztahu s nove pridanym prvkem
            vkladany.dalsi = po;
            po.predchozi = vkladany;
            vkladany.predchozi = pred;
            pred.dalsi = vkladany;
        }
    }

    @Override
    public void vlozPredchudce(T data) throws AbstrDoubleListException {
        if (data == null) throw new NullPointerException();
        if (jePrazdny()) throw new AbstrDoubleListException("Seznam prazdny");

        if (this.aktualni == this.prvni) {
            vlozPrvni(data);
        } else {
            // inicializace vkladaneho prvku
            // urceni mist predchudce a naslednika v seznamu
            Node<T> vkladany = new Node<T>(data);
            Node<T> pred = this.aktualni.predchozi;
            Node<T> po = this.aktualni;

            // navazani vztahu s nove pridanym prvkem
            vkladany.dalsi = po;
            po.predchozi = vkladany;
            vkladany.predchozi = pred;
            pred.dalsi = vkladany;
        }
    }

    @Override
    public T zpristupniAktualni() throws AbstrDoubleListException {
        if (jePrazdny()) throw new AbstrDoubleListException("Seznam prazdny");

        Node<T> vybrany = this.aktualni;
        return vybrany.data;
    }

    @Override
    public T zpristupniPrvni() throws AbstrDoubleListException {
        if (jePrazdny()) throw new AbstrDoubleListException("Seznam prazdny");

        this.aktualni = this.prvni;
        return zpristupniAktualni();
    }

    @Override
    public T zpristupniPosledni() throws AbstrDoubleListException {
        if (jePrazdny()) throw new AbstrDoubleListException("Seznam prazdny");
        
        this.aktualni = this.posledni;
        return zpristupniAktualni();
    }

    @Override
    public T zpristupniNaslednika() throws AbstrDoubleListException {
        if (jePrazdny()) throw new AbstrDoubleListException("Seznam prazdny");

        this.aktualni = this.aktualni.dalsi;
        return zpristupniAktualni();
    }

    @Override
    public T zpristupniPredchudce() throws AbstrDoubleListException {
        if (jePrazdny()) throw new AbstrDoubleListException("Seznam prazdny");

        this.aktualni = this.aktualni.predchozi;
        return zpristupniAktualni();
    }

    @Override
    public T odeberAktualni() throws AbstrDoubleListException {
        if (jePrazdny()) throw new AbstrDoubleListException("Seznam prazdny");

        // pokud je ukazatel aktualni nastaven na prvni prvek v seznamu
        // odebirani aktualniho prvku se chova stejne, jako odebirani prvniho prvku
        if (this.aktualni == this.prvni) {
            T odebirany = odeberPrvni();

            // po odebrani aktualniho se ukazatel aktualni nastavi na prvni prvek v seznamu
            this.aktualni = this.prvni;

            return odebirany;
        }

        // pokud je ukazatel aktualni nastaven na posledni prvek v seznamu
        // odebirani aktualniho prvku se chova stejne, jako odebirani posledniho prvku
        if (this.aktualni == this.posledni) {
            T odebirany = odeberPosledni();

            // po odebrani aktualniho se ukazatel aktualni nastavi na prvni prvek v seznamu
            this.aktualni = this.prvni;

            return odebirany;
        }

        // inicializace odebiraneho prvku
        // urceni mist predchudce a naslednika v seznamu
        Node<T> odebirany = this.aktualni;
        Node<T> pred = this.aktualni.predchozi;
        Node<T> po = this.aktualni.dalsi;

        // navazani vztahu predchudce a naslednika tak, 
        // aby zmizel odebirany prvek
        pred.dalsi = po;
        po.predchozi = pred;

        // po odebrani aktualniho se ukazatel aktualni nastavi na prvni prvek v seznamu
        this.aktualni = this.prvni;

        return odebirany.data;
    }

    @Override
    public T odeberPrvni() throws AbstrDoubleListException {
        if (jePrazdny()) throw new AbstrDoubleListException("Seznam prazdny");

        // v pripade, ze aktualne vybrany prvek je posledni prvek
        // je aktualni (tedy posledni) prvek smazan
        // a ukazatel aktualni je nastaven na prvni prvek v seznamu
        boolean smazanAktualni = (this.prvni == this.aktualni);

        // inicializace odebiraneho prvku
        // urceni mist predchudce a naslednika v seznamu
        Node<T> odebirany = this.prvni;
        Node<T> pred = this.prvni.predchozi;
        Node<T> po = this.prvni.dalsi;

        // v pripade, ze v seznamu je pouze jeden prvek
        // vymaz cely seznam
        if (odebirany == pred && odebirany == po && pred == po) {
            zrus();
            return odebirany.data;
        }

        // navazani vztahu predchudce a naslednika tak, 
        // aby zmizel odebirany prvek
        pred.dalsi = po;
        po.predchozi = pred;

        // zmena ukazatele na prvni prvek v seznamu na jeho naslednika
        this.prvni = po;

        if (smazanAktualni) {
            this.aktualni = this.prvni;
        }

        return odebirany.data;
    }

    @Override
    public T odeberPosledni() throws AbstrDoubleListException {
        if (jePrazdny()) throw new AbstrDoubleListException("Seznam prazdny");

        // v pripade, ze aktualne vybrany prvek je posledni prvek
        // je aktualni (tedy posledni) prvek smazan
        // a ukazatel aktualni je nastaven na prvni prvek v seznamu
        boolean smazanAktualni = (this.posledni == this.aktualni);

        // inicializace odebiraneho prvku
        // urceni mist predchudce a naslednika v seznamu
        Node<T> odebirany = this.posledni;
        Node<T> pred = this.posledni.predchozi;
        Node<T> po = this.posledni.dalsi;

        // v pripade, ze v seznamu je pouze jeden prvek
        // vymaz cely seznam
        if (odebirany == pred && odebirany == po && pred == po) {
            zrus();
            return odebirany.data;
        }

        // navazani vztahu predchudce a naslednika tak, 
        // aby zmizel odebirany prvek
        pred.dalsi = po;
        po.predchozi = pred;

        // zmena ukazatele na posledni prvek v seznamu na jeho predchudce
        this.posledni = pred;

        if (smazanAktualni) {
            this.aktualni = this.prvni;
        }

        return odebirany.data;

    }

    @Override
    public T odeberNaslednika() throws AbstrDoubleListException {
        if (jePrazdny()) throw new AbstrDoubleListException("Seznam prazdny");

        // v pripade, ze naslednik je posledni, metoda se chova jako pri odebirani posledniho
        if (this.aktualni.dalsi == this.posledni) return odeberPosledni();

        // v pripade, ze naslednik je prvni, metoda se chova jako pri odebirani prvniho
        if (this.aktualni.dalsi == this.prvni) return odeberPrvni();

        // inicializace odebiraneho prvku
        // urceni mist predchudce a naslednika v seznamu
        Node<T> odebirany = this.aktualni.dalsi;
        Node<T> pred = this.aktualni;
        Node<T> po = this.aktualni.dalsi.dalsi;

        // v pripade, ze v seznamu je pouze jeden prvek
        // vymaz cely seznam
        if (odebirany == pred && odebirany == po && pred == po) {
            zrus();
            return odebirany.data;
        }

        // navazani vztahu predchudce a naslednika tak, 
        // aby zmizel odebirany prvek
        pred.dalsi = po;
        po.predchozi = pred;

        return odebirany.data;
    }

    @Override
    public T odeberPredchudce() throws AbstrDoubleListException {
        if (jePrazdny()) throw new AbstrDoubleListException("Seznam prazdny");

        // v pripade, ze predchozi je posledni, metoda se chova jako pri odebirani posledniho
        if (this.aktualni.predchozi == this.posledni) return odeberPosledni();

        // v pripade, ze predchozi je prvni, metoda se chova jako pri odebirani prvniho
        if (this.aktualni.predchozi == this.prvni) return odeberPrvni();

        // inicializace odebiraneho prvku
        // urceni mist predchudce a naslednika v seznamu
        Node<T> odebirany = this.aktualni.predchozi;
        Node<T> pred = this.aktualni.predchozi.predchozi;
        Node<T> po = this.aktualni;

        // v pripade, ze v seznamu je pouze jeden prvek
        // vymaz cely seznam
        if (odebirany == pred && odebirany == po && pred == po) {
            zrus();
            return odebirany.data;
        }

        // navazani vztahu predchudce a naslednika tak, 
        // aby zmizel odebirany prvek
        pred.dalsi = po;
        po.predchozi = pred;

        return odebirany.data;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private Node<T> akt = prvni;
            private boolean init = false;

            @Override
            public boolean hasNext() {
                if (jePrazdny()) return false;

                return !(akt == prvni && init);
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    return null;
                }

                init = true;

                T data = akt.data;
                akt = akt.dalsi;
                return data;
            }
        };
    }

}
