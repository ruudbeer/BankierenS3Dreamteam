/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UnitTests;

import bank.bankieren.Bank;
import bank.internettoegang.Balie;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Joel
 */
public class BalieTest {
	
	public BalieTest() {
	}
	
	Bank bank;
	Balie balie;
	String naam;
	String plaats;
	String wachtwoord;
	
	String login;
	@Before
	public void setUp() throws RemoteException, NotBoundException {
		bank = new Bank("bankie");
		balie = new Balie(bank);
		
		
		naam = "joel";
		plaats = "Eindhoven";
		wachtwoord = "12341234";
		login = balie.openRekening(naam, plaats, wachtwoord);
		
	}

	/**
	 * Test of openRekening method, of class Balie.
	 */
	@Test
	public void testOpenRekening() {
        assertNotNull(balie.openRekening(naam, plaats, wachtwoord));
	}
	
	/**
	 * Test of openRekening method, of class Balie.
	 */
	@Test
	public void testOpenRekeningLegeNaam() {
        assertEquals("String naam is leeg.", null, balie.openRekening("", plaats, wachtwoord));     
	}
	/**
	 * Test of openRekening method, of class Balie.
	 */
	@Test
	public void testOpenRekeningLegePlaats() { 
        assertEquals("String plaats is leeg.", null, balie.openRekening(naam, "", wachtwoord));
	}
	/**
	 * Test of openRekening method, of class Balie.
	 */
	@Test
	public void testOpenRekeningLegeWachtwoord() {      
        assertEquals("String wachtwoord is leeg.", null, balie.openRekening(naam, plaats, ""));
	}
	/**
	 * Test of openRekening method, of class Balie.
	 */
	@Test
	public void testOpenRekeningWWTeKort() {
        assertEquals("String wachtwoord is korter dan 4 tekens.", null, balie.openRekening(naam, plaats, "123"));
	}
	/**
	 * Test of openRekening method, of class Balie.
	 */
	@Test
	public void testOpenRekeningWWTeLang() {
        assertEquals("String wachtwoord is langer dan 8 tekens.", null, balie.openRekening(naam, plaats, "123456789"));
	}
	/**
	 * Test of openRekening method, of class Balie.
	 */
	@Test
	public void testOpenRekeningWWPerfect() {  
        assertEquals("String accountname is 8 karakters lang.", 8, balie.openRekening(naam, plaats, wachtwoord).length());
	}
	
	
	
	  /**
   * er wordt een sessie opgestart voor het login-account met de naam
   * accountnaam mits het wachtwoord correct is
   * @param accountnaam
   * @param wachtwoord
   * @return de gegenereerde sessie waarbinnen de gebruiker 
   * toegang krijgt tot de bankrekening die hoort bij het betreffende login-
   * account mits accountnaam en wachtwoord matchen, anders null
	 * @throws java.rmi.RemoteException
   */
    @Test
    public void testLogInleegNaam() throws RemoteException {
        try {
            
            
            assertEquals("String accountname is leeg", null, balie.logIn("", wachtwoord));
            
         } catch (RemoteException ex) {
            System.out.println("RemoteException at testLogin: " + ex.getMessage());
        }
            
            
    }
	@Test
    public void testLogInLeegWW() {
        try {
           assertEquals("String wachtwoord is leeg", null, balie.logIn(login, ""));
            
            assertNotNull(balie.logIn(login, wachtwoord));
        } catch (RemoteException ex) {
            System.out.println("RemoteException at testLogin: " + ex.getMessage());
        }
    }
	@Test
    public void testLogIn() {
		try {
        assertNotNull(balie.logIn(login, wachtwoord));
        } catch (RemoteException ex) {
            System.out.println("RemoteException at testLogin: " + ex.getMessage());
        }
    }

	
}
