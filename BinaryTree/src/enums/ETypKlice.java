package enums;

public enum ETypKlice {
    
    NAZEV("Nazev"),
    GPS("GPS");

    private String nazev;

    ETypKlice(String nazev) {
        this.nazev = nazev;
    }

    @Override
    public String toString() {
        return this.nazev;
    }

}
