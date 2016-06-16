/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UnitTests;

import bank.bankieren.Bank;
import bank.bankieren.IBank;
import bank.bankieren.IRekening;
import bank.bankieren.Money;
import bank.internettoegang.Balie;
import bank.internettoegang.Bankiersessie;
import bank.internettoegang.IBankiersessie;
import fontys.util.InvalidSessionException;
import fontys.util.NumberDoesntExistException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class BankiersessieTest {

	public BankiersessieTest() {
	}

	private IBank bank;
	private IBankiersessie sessie;
	private int rekening1;
	private int rekening2;

	@Before
	public void setUp() {
		bank = new Bank("Bank1");
		rekening1 = bank.openRekening("Rekening1", "Eindhoven");
		rekening2 = bank.openRekening("Rekening2", "Eindhoven");
		sessie = null;
		try {
			sessie = new Bankiersessie(rekening1, bank);
		} catch (RemoteException ex) {
			System.out.println("RemoteException: " + ex.getMessage());
		}
	}

	/**
	 * Test of isGeldig method, of class Bankiersessie.
	 */
	@Test
	public void testIsGeldig() throws InvalidSessionException, RemoteException {
		sessie.getRekening();
		boolean result = sessie.isGeldig();

		boolean expResult = true;
		assertEquals("Geldige sessie", expResult, result);
	}

	/**
	 * Test of isGeldig method, of class Bankiersessie.
	 */
	@Test
	public void testIsNietGeldig() throws InvalidSessionException, RemoteException, InterruptedException {
		sessie.getRekening();
		Thread.sleep(IBankiersessie.GELDIGHEIDSDUUR + 30l);

		assertFalse(sessie.isGeldig());
	}

	/**
	 * Test of de MaakOver methode ook echt werkt en saldo af- en bijschrijft.
	 */
	@Test
	public void testMaakOver() throws NumberDoesntExistException, InvalidSessionException, RemoteException {
		Money SaldoRekening1 = bank.getRekening(rekening1).getSaldo();
		Money SaldoRekening2 = bank.getRekening(rekening2).getSaldo();
		Money Plus = new Money(1000, Money.EURO);
		Money Min = new Money(-1000, Money.EURO);

		boolean bool = sessie.maakOver(rekening2, Plus);

		assertEquals("Niks afgeschreven van rekening 1", bank.getRekening(rekening1).getSaldo(), Money.sum(SaldoRekening1, Min));
		assertEquals("Niks toegevoegd op rekening 2", bank.getRekening(rekening2).getSaldo(), Money.sum(SaldoRekening2, Plus));
		assertTrue("Geld is overgemaakt", bool);
	}

	/**
	 * Test of de MaakOver methode ook echt werkt en saldo af- en bijschrijft.
	 */
	@Test
	public void testMaakOverNegatiefGeld() throws NumberDoesntExistException, InvalidSessionException, RemoteException {

		Money Min = new Money(-1000, Money.EURO);

		boolean bool = sessie.maakOver(rekening2, Min);
		assertFalse("Geld is niet overgemaakt", bool);
	}

	/**
	 * Test of de MaakOver methode ook echt werkt en saldo af- en bijschrijft.
	 */
	@Test(expected = NumberDoesntExistException.class)
	public void testMaakOverInvalidNumber() throws NumberDoesntExistException {
		Money Plus = new Money(1000, Money.EURO);
		try {
			boolean bool = sessie.maakOver(23456, Plus);

		} catch (InvalidSessionException ex) {
			Logger.getLogger(BankiersessieTest.class.getName()).log(Level.SEVERE, null, ex);
		} catch (RemoteException ex) {
			Logger.getLogger(BankiersessieTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Test of de MaakOver methode ook echt werkt en saldo af- en bijschrijft.
	 */
	@Test(expected = InvalidSessionException.class)
	public void testMaakOverInvalidSession() throws InvalidSessionException, InterruptedException {
		Thread.sleep(IBankiersessie.GELDIGHEIDSDUUR + 30);
		Money Plus = new Money(1000, Money.EURO);
		try {
			boolean bool = sessie.maakOver(23456, Plus);
		} catch (NumberDoesntExistException ex) {
			Logger.getLogger(BankiersessieTest.class.getName()).log(Level.SEVERE, null, ex);
		} catch (RemoteException ex) {
			Logger.getLogger(BankiersessieTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Test of de MaakOver methode ook echt werkt en saldo af- en bijschrijft.
	 */
	@Test
	public void testLogUit() throws RemoteException {
		assertTrue(sessie.isGeldig());
		sessie.logUit();
		assertFalse(sessie.isGeldig());
	}

	@Test
	public void TestGetRekening() throws RemoteException {
		try {
			assertEquals("Rekening gegevens zijn hetzelfde.", rekening1, sessie.getRekening().getNr());
		} catch (InvalidSessionException ex) {
			fail("InvalidSessionException" + ex.getMessage());
			Logger.getLogger(BankiersessieTest.class.getName()).log(Level.SEVERE, null, ex);

		}
	}

}
