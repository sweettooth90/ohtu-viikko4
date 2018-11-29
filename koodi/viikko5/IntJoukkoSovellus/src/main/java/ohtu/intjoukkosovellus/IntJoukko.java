
package ohtu.intjoukkosovellus;

public class IntJoukko {

    public final static int KAPASITEETTI = 5, OLETUSKASVATUS = 5;
    private int kasvatuskoko;
    private int[] ljono;
    private int alkioLkm;


    public IntJoukko() {
        ljono = new int[KAPASITEETTI];
        taytaTaulukko(ljono);
        alkioLkm = 0;
        this.kasvatuskoko = OLETUSKASVATUS;
    }


    public IntJoukko(int kapasiteetti) {
        if (kapasiteetti < 0) {
            return;
        }
        ljono = new int[kapasiteetti];
        taytaTaulukko(ljono);
        alkioLkm = 0;
        this.kasvatuskoko = OLETUSKASVATUS;
    }
    

    public IntJoukko(int kapasiteetti, int kasvatuskoko) {
        ljono = new int[kapasiteetti];
        taytaTaulukko(ljono);
        this.kasvatuskoko = kasvatuskoko;
    }


    public boolean lisaa(int luku) {
        if (!lukuKuuluu(luku)) {
            ljono[alkioLkm] = luku;
            alkioLkm++;
            if (alkioLkm % ljono.length == 0) {
                int[] taulukkoVanha = ljono;
                kopioiTaulukko(ljono, taulukkoVanha);
                ljono = new int[alkioLkm + kasvatuskoko];
                kopioiTaulukko(taulukkoVanha, ljono);
            }
            return true;
        }
        return false;
    }


    public boolean lukuKuuluu(int luku) {
        for (int i = 0; i < alkioLkm; i++) {
            if (luku == ljono[i]) {
                return true;
            }
        }
        return false;
    }


    public boolean poista(int luku) {
        for (int i = 0; i < alkioLkm; i++) {
            if (luku == ljono[i]) {
                ljono[i] = ljono[alkioLkm-1];
                ljono[alkioLkm-1] = 0;
                alkioLkm--;
                return true;
            }
        }
        return false;
    }


    public void taytaTaulukko(int[] ljono) {
        for (int i = 0; i < ljono.length; i++) {
            ljono[i] = 0;
        }
    }


    private void kopioiTaulukko(int[] vanha, int[] uusi) {
        for (int i = 0; i < vanha.length; i++) {
            uusi[i] = vanha[i];
        }
    }


    public int mahtavuus() {
        return alkioLkm;
    }
   

    public static IntJoukko yhdiste(IntJoukko a, IntJoukko b) {
        int[] aTaulu = a.toIntArray();
        int[] bTaulu = b.toIntArray();
        IntJoukko yhdista = new IntJoukko();
        for (int i = 0; i < aTaulu.length; i++) {
            yhdista.lisaa(aTaulu[i]);
        }
        for (int i = 0; i < bTaulu.length; i++) {
            yhdista.lisaa(bTaulu[i]);
        }
        return yhdista;
    }


    public static IntJoukko leikkaus(IntJoukko a, IntJoukko b) {
        int[] aTaulu = a.toIntArray();
        int[] bTaulu = b.toIntArray();
        IntJoukko leikkaa = new IntJoukko();
        for (int i = 0; i < aTaulu.length; i++) {
            for (int j = 0; j < bTaulu.length; j++) {
                if (aTaulu[i] == bTaulu[j]) {
                    leikkaa.lisaa(bTaulu[j]);
                }
            }
        }
        return leikkaa;
    }


    public static IntJoukko erotus (IntJoukko a, IntJoukko b) {
        int[] aTaulu = a.toIntArray();
        int[] bTaulu = b.toIntArray();
        IntJoukko erota = new IntJoukko();
        for (int i = 0; i < aTaulu.length; i++) {
            erota.lisaa(aTaulu[i]);
        }
        for (int i = 0; i < bTaulu.length; i++) {
            erota.poista(i);
        }
        return erota;
    }


    @Override
    public String toString() {
        if (alkioLkm == 0) {
            return "{}";
        } else if (alkioLkm == 1) {
            return "{" + ljono[0] + "}";
        } else {
            String tuotos = "{";
            for (int i = 0; i < alkioLkm - 1; i++) {
                tuotos += ljono[i] + ", ";
            }
            tuotos += ljono[alkioLkm - 1] + "}";
            return tuotos;
        }
    }

    
    public int[] toIntArray() {
        int[] taulu = new int[alkioLkm];
        for (int i = 0; i < taulu.length; i++) {
            taulu[i] = ljono[i];
        }
        return taulu;
    }
        
}