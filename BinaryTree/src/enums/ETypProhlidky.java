package enums;

public enum ETypProhlidky {
    
    SIROKA("Siroka"),
    HLUBOKA("Hluboka");

    private String nazev;

    ETypProhlidky(String nazev) {
        this.nazev = nazev;
    }

    @Override
    public String toString() {
        return this.nazev;
    }

}
