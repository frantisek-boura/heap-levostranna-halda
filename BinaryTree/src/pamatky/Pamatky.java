package pamatky;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import ads.AbstrHeap;
import ads.AbstrHeapException;
import ads.AbstrTable;
import ads.AbstrTableException;
import enums.ETypKlice;
import enums.ETypProhlidky;

public class Pamatky implements IPamatky {

    private AbstrTable strom;
    private AbstrHeap halda;
    private ETypKlice aktualniKlic;

    private static final float PREVOD_NA_STUPNE = 1/60f;

    public Pamatky() {
        this.strom = new AbstrTable();
        this.halda = new AbstrHeap();
        this.aktualniKlic = ETypKlice.GPS;
    }

    @Override
    public int importDat(String soubor) throws PamatkyException, FileNotFoundException {
        if (soubor.isEmpty()) throw new IllegalArgumentException("Vybrany soubor neexistuje");
        if (!Files.exists(Paths.get(soubor))) throw new FileNotFoundException("Vybrany soubor neexistuje");

        try (
            BufferedReader reader = new BufferedReader(new FileReader(soubor));
            ) 
        {
            strom.zrus();

            String radek;
            int pocet = 0;

            while ((radek = reader.readLine()) != null) {
                if (radek.length() == 0 || radek.isEmpty()) break;

                String[] data = radek.split(" ");

                int id = Integer.parseInt(data[0]);
                float sirka = Float.parseFloat(data[1]);
                float delka = Float.parseFloat(data[2]);
                String nazev = data[3].replace("_", " ");

                GPS lokace = new GPS(sirka, delka);
                Zamek zamek = new Zamek(nazev, lokace);
                zamek.setId(id);

                vlozZamek(zamek);

                pocet++;
            }
            
            return pocet;
        } catch (IOException | NumberFormatException e) {
            strom.zrus();
            throw new PamatkyException("Chyba pri nacitani souboru");
        }
    }

    @Override
    public String exportDat(ETypProhlidky typProhlidky) {
        if (typProhlidky == null) throw new NullPointerException("Spatne zvoleny typ prohlidky");
        StringBuilder fileContent = new StringBuilder();

        Iterator<Zamek> iterator = this.strom.iterator(typProhlidky);
        while (iterator.hasNext()) {
            Zamek x = iterator.next();
            fileContent.append(x.getId() + " " + x.getLokace().getSirka() + " " + x.getLokace().getDelka() + " " + x.getNazev().replace(" ", "_") + "\n");
        }

        return fileContent.toString();
    }

    @Override
    public void generaceDat(int pocet) {
        if (pocet <= 0) {
            throw new IllegalArgumentException("Neplatny pocet nahodne generovanych dat");
        }

        Random rnd = new Random();
        rnd.setSeed(new Date().getTime());
        this.strom.zrus();
    
        String nazev;
        float sirka;
        float delka;
        for (int i = 0; i < pocet; i++) {
            sirka = rnd.nextFloat(GPS.MAX_SIRKA - GPS.MIN_SIRKA) + GPS.MIN_SIRKA;
            delka = rnd.nextFloat(GPS.MAX_DELKA - GPS.MIN_DELKA) + GPS.MIN_DELKA;
            nazev = "Zamek " + i;

            Zamek x = new Zamek(nazev, new GPS(sirka, delka));

            if (this.aktualniKlic == ETypKlice.GPS) this.strom.vloz(x.getLokace(), x);
            else this.strom.vloz(x.getNazev(), x);
        }
    }

    @Override
    public int importDatZTXT(String soubor) throws PamatkyException, FileNotFoundException {
        if (soubor.isEmpty()) throw new IllegalArgumentException("Vybrany soubor neexistuje");
        if (!Files.exists(Paths.get(soubor))) throw new FileNotFoundException("Vybrany soubor neexistuje");

        try (
            BufferedReader reader = new BufferedReader(new FileReader(soubor));
            ) 
        {
            strom.zrus();

            String radek;
            int pocet = 0;

            while ((radek = reader.readLine()) != null) {
                if (radek.length() == 0 || radek.isEmpty()) break;

                if(pocet == 0) radek = radek.substring(3);

                radek = radek.replaceAll("^", " ");
                String nazev = radek.substring(69, 90).trim();

                radek = radek.replaceAll("\\s+"," ").trim();
                String[] radekData = radek.split(" ");

                float sirkaS = Float.parseFloat(radekData[2].substring(1));
                float sirkaM = Float.parseFloat(radekData[3]);
                float delkaS = Float.parseFloat(radekData[4].substring(2));
                float delkaM = Float.parseFloat(radekData[5]);
                float sirka = sirkaS + sirkaM * PREVOD_NA_STUPNE;
                float delka = delkaS + delkaM * PREVOD_NA_STUPNE;

                GPS lokace = new GPS(sirka, delka);
                Zamek zamek = new Zamek(nazev, lokace);

                vlozZamek(zamek);

                pocet++;
            }
            
            return pocet;
        } catch (IOException | NumberFormatException e) {
            strom.zrus();
            throw new PamatkyException("Chyba pri nacitani souboru");
        }
    }

    @Override
    public void vlozZamek(Zamek zamek) throws PamatkyException {
        if (zamek == null) throw new NullPointerException("Spatne zadana pamatka");

        Zamek vlozeny;
        switch (this.aktualniKlic) {
            case GPS -> {
                vlozeny = (Zamek) this.strom.najdi(zamek.getLokace());
                if (vlozeny != null) {
                    throw new PamatkyException("Takova pamatka jiz ve stromu existuje");
                } else {
                    this.strom.vloz(zamek.getLokace(), zamek);
                }
            } case NAZEV -> {
                vlozeny = (Zamek) this.strom.najdi(zamek.getNazev());
                if (vlozeny != null) {
                    throw new PamatkyException("Takova pamatka jiz ve stromu existuje");
                } else {
                    this.strom.vloz(zamek.getNazev(), zamek);
                }
            }
        }

    }

    @Override
    public Zamek najdiZamek(String klic) throws PamatkyException, AbstrTableException {
        if (this.strom.jePrazdny()) throw new PamatkyException("Neni kde vyhledavat, strom je prazdny");
        if (klic == null) throw new NullPointerException("Prazdny klic");
        if (klic.isEmpty()) throw new IllegalArgumentException("Spatne zadany nazev nebo souradnice");

        switch (this.aktualniKlic) {
            case GPS -> {
                return (Zamek) this.strom.najdi(new GPS(klic));
            }
            case NAZEV -> {
                return (Zamek) this.strom.najdi(klic);
            }
        }

        return null;
    }

    @Override
    public Zamek odeberZamek(String klic) throws PamatkyException, AbstrTableException {
        if (this.strom.jePrazdny()) throw new PamatkyException("Neni co odebirat, strom je prazdny");
        if (klic == null) throw new NullPointerException("Prazdny klic");
        if (klic.isEmpty()) throw new IllegalArgumentException("Spatne zadany nazev nebo souradnice");

        switch (this.aktualniKlic) {
            case GPS -> {
                return (Zamek) this.strom.odeber(new GPS(klic));
            }
            case NAZEV -> {
                return (Zamek) this.strom.odeber(klic);
            }
        }

        return null;
    }


    @Override
    public Zamek najdiNejbliz(String klic) throws PamatkyException, AbstrTableException {
        if (this.aktualniKlic != ETypKlice.GPS) throw new PamatkyException("Spatne predvoleny typ klice");
        if (this.strom.jePrazdny()) throw new PamatkyException("Neni kde vyhledavat, strom je prazdny");
        if (klic == null) throw new NullPointerException("Prazdny klic");
        if (klic.isEmpty()) throw new IllegalArgumentException("Spatne zadany nazev nebo souradnice");

        // pomoci iteratoru se vlozi vsechny zamky do kolekce arraylist
        Iterator<Zamek> it = this.strom.iterator(ETypProhlidky.SIROKA);
        ArrayList<Zamek> vsechnyZamky = new ArrayList<Zamek>();
        while (it.hasNext()) vsechnyZamky.add(it.next());

        // za pomoci klice je vytvoren objekt gps 
        GPS lokace = new GPS(klic);

        // kolekce arraylist je prevedena na stream a vzestupne serazena
        // v zavislosti na vzdalenosti od lokace, ktera je zjistena z parametru klic
        // po serazeni je ze streamu nalezen a vracen prvni prvek
        Zamek nejblizsi = vsechnyZamky.stream()
            .sorted((z1, z2) -> {
                if (z1.getLokace().vzdalenostOd(lokace) < z2.getLokace().vzdalenostOd(lokace)) return -1;
                else if (z1.getLokace().vzdalenostOd(lokace) > z2.getLokace().vzdalenostOd(lokace)) return 1;
                else return 0;
            })
            .findFirst()
            .get();

        return nejblizsi;
    }

    @Override
    public void zrus() {
        strom.zrus();

        Zamek.count = 0;
    }

    @Override
    public void prebuduj() throws PamatkyException {
        if (this.strom.jePrazdny()) throw new PamatkyException("Neni co prebudovat, strom je prazdny");

        Iterator<Zamek> it = this.strom.iterator(ETypProhlidky.SIROKA);
        ArrayList<Zamek> vsechnyZamky = new ArrayList<Zamek>();
        while (it.hasNext()) vsechnyZamky.add(it.next());

        ArrayList<Zamek> serazene = new ArrayList<Zamek>(vsechnyZamky.stream()
        .sorted((z1, z2) -> {
            if (this.aktualniKlic == ETypKlice.GPS) return z1.getLokace().compareTo(z2.getLokace());
            else return z1.getNazev().compareTo(z2.getNazev());
        }).toList());

        this.strom.zrus();

        prebudujRekurze(serazene);
    }

    private void prebudujRekurze(ArrayList<Zamek> serazene) {
        if (!serazene.isEmpty()) {
            Zamek koren = serazene.get((int) (serazene.size() / 2));
            this.strom.vloz(this.aktualniKlic == ETypKlice.GPS ? koren.getLokace() : koren.getNazev(), koren);

            prebudujRekurze(new ArrayList<Zamek>(serazene.subList(0, serazene.indexOf(koren))));
            prebudujRekurze(new ArrayList<Zamek>(serazene.subList(serazene.indexOf(koren) + 1, serazene.size())));
        }
    }

    @Override
    public void nastavKlic(ETypKlice typKlice) {
        this.aktualniKlic = typKlice;
    }

    @Override
    public Iterator<Zamek> iterator(ETypProhlidky typProhlidky) throws PamatkyException {
        if (this.strom.jePrazdny()) throw new PamatkyException("Neni co prohlizet, strom je prazdny");
        if (typProhlidky == null) throw new NullPointerException("Spatne zvoleny typ prohlidky");

        return strom.iterator(typProhlidky);
    }

    public Iterator<Zamek> iteratorHalda() throws PamatkyException {
        if (this.halda.jePrazdny()) throw new PamatkyException("Neni co prohlizet, halda je prazdna");

        return (Iterator<Zamek>) this.halda.iterator();
    }

    public void nastavAktualniPozici(GPS lokace) throws PamatkyException {
        if (lokace == null) throw new PamatkyException("Nespravne zadana aktualni pozice");

        Iterator<Zamek> it = (Iterator<Zamek>) this.strom.iterator(ETypProhlidky.SIROKA);
        while (it.hasNext()) {
            Zamek obj = (Zamek) it.next();
            obj.getLokace().setVzdalenost(lokace);
        }
    }

    public void vybudujHaldu() throws PamatkyException {
        if (this.strom.jePrazdny()) throw new PamatkyException("Neni z ceho vybudovat, strom je prazdny");

        this.halda.zrus();
        Iterator<Zamek> it = (Iterator<Zamek>) this.strom.iterator(ETypProhlidky.SIROKA);
        while (it.hasNext()) {
            Zamek obj = (Zamek) it.next();
            if (obj.getLokace().getVzdalenost() == -1f) throw new PamatkyException("Nebyla nastavena aktualni pozice");
            this.halda.vloz((Zamek) it.next());
        }
    }

    public Zamek odeberMaxHalda() throws PamatkyException {
        try {
            return (Zamek) this.halda.odeberMax();
        } catch (AbstrHeapException e) {
            throw new PamatkyException("Neni z ceho odebirat, halda je prazdna");
        }
    }

    public ETypKlice getAktualniKlic() {
        return aktualniKlic;
    }
    
}
