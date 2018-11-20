package ohtu.verkkokauppa;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class VerkkokauppaTest {

    Kauppa kauppa;
    Kirjanpito kirjanpito;
    Ostoskori ostoskori;
    Pankki pankki;
    Varasto varasto;
    Viitegeneraattori viitegeneraattori;

    @Before
    public void setUp() {
        ostoskori = mock(Ostoskori.class);
        pankki = mock(Pankki.class);
        varasto = mock(Varasto.class);
        viitegeneraattori = mock(Viitegeneraattori.class);
        kauppa = new Kauppa(varasto, pankki, tuote);

        when(viitegeneraattori.uusi()).thenReturn(42);

        // tuote 1
        when(varasto.saldo(1)).thenReturn(8); 
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "ipa", 5));

        // tuote 2
        when(varasto.saldo(2)).thenReturn(5);
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "stout", 10));

        //tuote 3
        when(varasto.saldo(3)).thenReturn(-1);
        when(varasto.haeTuote(3)).thenReturn(new Tuote(3, "porter", 12));
    }

    @Test
    public void afterBuyCallMethodTilisiirto() {
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("pekka", "12345");
        verify(pankki).tilisiirto(eq("pekka"), anyInt(), eq("12345"), anyString(),eq(5));   
    }

    @Test
    public void afterBuyTwoCallMethodTilisiirto() {
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("pekka", "12345");
        verify(pankki).tilisiirto(eq("pekka"), anyInt(), eq("12345"), anyString(), eq(15));
    }

    @Test
    public void afterBuyTwoSameCallMethodTilisiirto() {
        when(varasto.saldo(1)).thenReturn(8);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "ipa", 5));

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("pekka", "12345");
        verify(pankki).tilisiirto(eq("pekka"), anyInt(), eq("12345"), anyString(), eq(20));
    }

    @Test
    public void buyWithNegativeAndPositiveSaldoAndCallMethodTilisiirto() {
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(3);
        kauppa.tilimaksu("pekka", "12345");
        verify(pankki).tilisiirto(eq("pekka"), anyInt(), eq("12345"), anyString(), eq(5));
    }

    @Test
    public void aloitaAsiointiResetsPreviousBuy() {
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(2);
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);     
        kauppa.tilimaksu("pekka", "12345");
        verify(pankki).tilisiirto(anyString(), anyInt(), anyString(), anyString(), eq(5));
    }

    @Test
    public void newReferenceNumberForEveryEvent() {
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("pekka", "12345");
        verify(viitegeneraattori, times(1)).uusi();

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("pekka", "12345");
        verify(viitegeneraattori, times(1)).uusi();
    }

    @Test
    public void poistaKoristaDeletesItem() {
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.poistaKorista(1);
        verify(pankki).tilisiirto(anyString(), anyInt(), anyString(), anyString(), eq(0));
    }

}