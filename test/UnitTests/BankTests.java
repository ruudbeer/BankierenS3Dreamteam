/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UnitTests;

import bank.bankieren.Bank;
import bank.bankieren.IBank;
import bank.bankieren.IKlant;
import bank.bankieren.IRekening;
import bank.bankieren.Klant;
import bank.bankieren.Money;
import bank.bankieren.Rekening;
import fontys.util.NumberDoesntExistException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ruud en joël
 */
public class BankTests {

    IBank bank;
    int rek;
    IRekening rekening;
    int rek2;
    IKlant klant;

    public BankTests() {
    }

    @Before
    public void setUp() {
        bank = new Bank("BBBank");
        rek = bank.openRekening("Henk", "eindje");
        rek2 = bank.openRekening("toine", "hegelsom");
        rekening = bank.getRekening(rek);
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void testOpenRekeningEersteRekening() {
        assertNotEquals(-1, bank.openRekening("Ruud", "Hegelsom"));
    }

    @Test
    public void testOpenRekeningExtraRekening() {
        bank.openRekening("Ruud", "Hegelsom");
        assertNotEquals(-1, bank.openRekening("Ruud", "Hegelsom"));
    }

    @Test
    public void testOpenRekeningGeenNaam() {
        assertEquals(-1, bank.openRekening("", "Eindhoven"));
    }

    @Test
    public void testOpenRekeningGeenWoonplaats() {
        assertEquals(-1, bank.openRekening("Ruud", ""));
    }

    @Test
    public void testOpenRekeingGeenAlles() {
        assertEquals(-1, bank.openRekening("", ""));
    }

    @Test
    public void testMaakOverGoed() throws NumberDoesntExistException {
        assertTrue(bank.maakOver(rek, rek2, new Money(50, "€")));
    }

    @Test(expected = RuntimeException.class)
    public void testMaakOverJezelf() throws NumberDoesntExistException {
        bank.maakOver(rek, rek, new Money(50, "€"));
    }

    @Test(expected = RuntimeException.class)
    public void testMaakOverNull() throws NumberDoesntExistException {
        bank.maakOver(rek, rek2, new Money(-10, "€"));
    }

    @Test(expected = NumberDoesntExistException.class)
    public void testMaakOverVerkeerdRek() throws NumberDoesntExistException {
        bank.maakOver(rek, 596465, new Money(20, "€"));
    }

    @Test
    public void testGetRekening() {
        assertEquals(rekening, bank.getRekening(rek));
    }
    
    @Test 
    public void testGetRekeningFout(){
       assertEquals(null,bank.getRekening(5477878));
    }

    @Test
    public void testGetName(){
        assertEquals("BBBank", bank.getName());
    }
    
}
