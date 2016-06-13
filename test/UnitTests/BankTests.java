/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UnitTests;

import bank.bankieren.Bank;
import bank.bankieren.IBank;
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
    
    public BankTests() {        
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
         bank = new Bank("BBBank");
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void testOpenRekeningEersteRekening(){
      assertNotEquals(-1, bank.openRekening("Ruud", "Hegelsom"));
    }
    @Test
    public void testOpenRekeningExtraRekening(){
        bank.openRekening("Ruud","Hegelsom");
        assertNotEquals(-1, bank.openRekening("Ruud","Hegelsom"));
    }
    @Test
    public void testOpenRekeningGeenNaam(){
        assertEquals(-1, bank.openRekening("", "Eindhoven"));
    }
    @Test
    public void testOpenRekeningGeenWoonplaats() {
        assertEquals(-1, bank.openRekening("Ruud", ""));
    }
    @Test
    public void testOpenRekeingGeenAlles(){
        assertEquals(-1, bank.openRekening("", ""));
    }
}
