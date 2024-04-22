package ads;

import java.util.Iterator;

import enums.ETypProhlidky;

public class AbstrTable<K extends Comparable<K>, V> implements IAbstrTable<K, V> {

    private Node koren = null;

    private class Node {

        private K klic;
        private V hodnota;
        private Node leva;
        private Node prava;

        public Node(K klic, V hodnota) {
            this.klic = klic;
            this.hodnota = hodnota;
            this.leva = null;
            this.prava = null;
        }

        @Override
        public String toString() {
            return hodnota.toString();
        }

    }

    @Override
    public void zrus() {
        this.koren = null;
    }

    @Override
    public boolean jePrazdny() {
        return this.koren == null;
    }

    @Override
    public V najdi(K klic) {
        if (klic == null) throw new NullPointerException("Spatne zadany klic");
        if (jePrazdny()) return null;

        return najdiRekurze(this.koren, klic);
    }

    private V najdiRekurze(Node koren, K klic) {
        // pokud neexistuje korenovy prvek
        // neni co hledat
        if (koren == null) return null;

        // pokud je poskytnuty klic mensi nez klic korenoveho prvku
        // hledani rekurzivne pokracuje v levem prvku korenu
        if (klic.compareTo(koren.klic) < 0) return najdiRekurze(koren.leva, klic);

        // pokud je poskytnuty klic vetsi nez klic korenoveho prvku
        // hledani rekurzivne pokracuje v pravem prvku korenu
        if (klic.compareTo(koren.klic) > 0) return najdiRekurze(koren.prava, klic);

        // pokud je poskytnuty klic stejny jako klic korenoveho prvku
        // tak je vracena hodnota prvku
        return koren.hodnota;
    }

    @Override
    public void vloz(K klic, V hodnota) {
        if (klic == null) throw new NullPointerException("Spatne zadany klic");
        if (hodnota == null) throw new NullPointerException("Spatne zadana hodnota");

        this.koren = vlozRekurze(this.koren, klic, hodnota);
    }

    private Node vlozRekurze(Node koren, K klic, V hodnota) {
        // pokud neexistuje korenovy prvek
        // je vytvoren s poskytnutymi daty
        if (koren == null) koren = new Node(klic, hodnota);

        // pokud je poskytnuty klic mensi nez klic korenoveho prvku
        // hledani vhodneho mista na vlozeni rekurzivne pokracuje po leve strane korenoveho prvku
        if (klic.compareTo(koren.klic) < 0) {
            koren.leva = vlozRekurze(koren.leva, klic, hodnota);
        }

        // pokud je poskytnuty klic vetsi nez klic korenoveho prvku
        // hledani vhodneho mista na vlozeni rekurzivne pokracuje po prave strane korenoveho prvku
        if (klic.compareTo(koren.klic) > 0) koren.prava = vlozRekurze(koren.prava, klic, hodnota);

        return koren;
    }

    @Override
    public V odeber(K klic) throws AbstrTableException {
        if (jePrazdny()) throw new AbstrTableException("Strom je prazdny");
        if (klic == null) throw new NullPointerException("Spatne zadany klic");

        return odeberRekurze(this.koren, klic).hodnota;
    }

    private Node odeberRekurze(Node koren, K klic) {
        // pokud neexistuje korenovy prvek
        // v rekurznim zasobniku se vracime o krok zpet
        if (koren == null) return koren;

        // pokud je poskytnuty klic mensi nez klic korenoveho prvku
        // hledani odebiraneho prvku rekurzivne pokracuje po leve strane korenoveho prvku
        if (klic.compareTo(koren.klic) < 0) koren.leva = odeberRekurze(koren.leva, klic);

        // pokud je poskytnuty klic mensi nez klic korenoveho prvku
        // hledani odebiraneho prvku rekurzivne pokracuje po leve strane korenoveho prvku
        else if (klic.compareTo(koren.klic) > 0) koren.prava = odeberRekurze(koren.prava, klic);
        
        // pokud se rovnaji
        // pokracuje se vyhodnocenim, zda koren ma jeden, dva ci zadneho potomka
        else {
            // pokud koren ma pouze jeden prvek
            // v rekurzivnim zasobniku pokracujeme k existujicimu potomkovi
            if (koren.leva == null) return koren.prava;
            else if (koren.prava == null) return koren.leva;

            // pokud ma koren dva potomky
            // hledame nahradu za odebirany prvek v prave casti naseho korene
            // v podobe nejblizsiho naslednika odebiraneho prvku v danem substromu
            koren.klic = nejmensiNaslednik(koren.prava);

            // odstraneni nejmensiho naslednika
            // jelikoz nahradil odebirany prvek
            koren.prava = odeberRekurze(koren.prava, koren.klic);
        }

        return koren;
    }

    private K nejmensiNaslednik(Node koren) {
        K nejmensiKlic = koren.klic;
        while (koren.leva != null) {
            nejmensiKlic = koren.leva.klic;
            koren = koren.leva;
        }
        return nejmensiKlic;
    }

    @Override
    public Iterator<V> iterator(ETypProhlidky typProhlidky) {
        if (typProhlidky == null) throw new IllegalArgumentException("Spatne zadany typ prohlidky");

        IAbstrLifoFifo<Node> struktura = typProhlidky == ETypProhlidky.HLUBOKA ? new AbstrLifo<Node>() : new AbstrFifo<Node>();
        struktura.vloz(this.koren);

        return new Iterator<V>() {
            private boolean init = true;
                    
            @Override
            public boolean hasNext() {
                return !struktura.jePrazdny() || init;
            }

            @Override
            public V next() {
                if (init) init = false;

                try {
                    Node odebirany = struktura.odeber();

                    if (struktura instanceof AbstrLifo) {
                        if (odebirany.prava != null) struktura.vloz(odebirany.prava);
                        if (odebirany.leva != null) struktura.vloz(odebirany.leva);
                    } else {
                        if (odebirany.leva != null) struktura.vloz(odebirany.leva);
                        if (odebirany.prava != null) struktura.vloz(odebirany.prava);
                    }

                    return odebirany.hodnota;
                } catch (AbstrDoubleListException e) {
                    return null;
                }
            }
        };
    }
    
}
