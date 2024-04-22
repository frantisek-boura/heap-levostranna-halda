package pamatky;

import java.io.FileNotFoundException;
import java.util.Iterator;

import ads.AbstrTable;
import ads.AbstrTableException;
import enums.ETypKlice;
import enums.ETypProhlidky;

public interface IPamatky {
    
    int importDatZTXT(String soubor) throws FileNotFoundException, PamatkyException;
    int importDat(String soubor) throws PamatkyException, FileNotFoundException;
    String exportDat(ETypProhlidky typProhlidky);
    void generaceDat(int pocet);
    void vlozZamek(Zamek zamek) throws PamatkyException;
    
    Zamek najdiZamek(String klic) throws PamatkyException, AbstrTableException;
    Zamek odeberZamek(String klic) throws PamatkyException, AbstrTableException;
    Zamek najdiNejbliz(String klic) throws PamatkyException, AbstrTableException;

    void zrus();
    void prebuduj() throws PamatkyException;
    void nastavKlic(ETypKlice typKlice);
    
    Iterator<Zamek> iterator(ETypProhlidky typProhlidky) throws PamatkyException;

}
