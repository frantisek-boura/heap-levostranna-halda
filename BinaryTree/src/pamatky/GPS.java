package pamatky;

public class GPS implements IGPS, Comparable<GPS> {
    
    private float sirka;
    private float delka;

    public static float MIN_SIRKA = 48f;
    public static float MAX_SIRKA = 52f;
    public static float MIN_DELKA = 12f;
    public static float MAX_DELKA = 19f;

    private float vzdalenost;

    public GPS(float sirka, float delka) {
        if (sirka < GPS.MIN_SIRKA || sirka > GPS.MAX_SIRKA || delka < GPS.MIN_DELKA || delka > GPS.MAX_DELKA) {
            throw new IllegalArgumentException("Pouze Ceske uzemi");
        }

        this.sirka = sirka;
        this.delka = delka;
        this.vzdalenost = -1f;
    }

    public GPS(String souradnice) {
        String[] data = souradnice.split(" ");
        if (data.length != 2) throw new IllegalArgumentException("Spatne zadana lokace");

        try {
            float sirka = Float.parseFloat(data[0]);
            float delka = Float.parseFloat(data[1]);
            
            if (sirka < GPS.MIN_SIRKA || sirka > GPS.MAX_SIRKA || delka < GPS.MIN_DELKA || delka > GPS.MAX_DELKA) throw new IllegalArgumentException("Pouze Ceske uzemi");

            this.sirka = sirka;
            this.delka = delka;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Spatne zadana lokace");
        }
    } 

    public float getSirka() {
        return sirka;
    }
    
    public float getDelka() {
        return delka;
    }

    @Override
    public float vzdalenostOd(GPS gps) {
        if (gps == null || gps.equals(this)) return 0f;

        final double POLOMER_ZEME = 6.37100;

        double f1 = this.sirka * Math.PI / 180;
        double f2 = gps.getSirka() *  Math.PI / 180;
        double df = (gps.getSirka() - this.sirka) * Math.PI / 180;
        double dd = (gps.getDelka() - this.delka) * Math.PI / 180;

        double a = Math.sin(df/2) * Math.sin(df/2) + Math.cos(f1) * Math.cos(f2) * Math.sin(dd/2) * Math.sin(dd/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return (float) (POLOMER_ZEME * c * 1000);
    }



    @Override
    public String toString() {
        return this.sirka + " " + this.delka;
    }

    @Override
    public int compareTo(GPS gps) {
        if (this.sirka + this.delka < gps.sirka + gps.delka) return -1;
        else if (this.sirka + this.delka > gps.sirka + gps.delka) return 1;
        else return 0;
    }

    public void setVzdalenost(GPS gps) {
        this.vzdalenost = vzdalenostOd(gps);
    }

    public float getVzdalenost() {
        return this.vzdalenost;
    }
    
}
