package bank.bankieren;

import CentraleBank.ICentrale;
import CentraleBank.OverboekCentrale;
import fontys.util.NumberDoesntExistException;
import fontyspublisher.Publisher;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bank extends UnicastRemoteObject implements IBank, IRekeningMuteerder {

	/**
	 *
	 */
	private static final long serialVersionUID = -8728841131739353765L;
	private Map<Integer, IRekeningTbvBank> accounts;
	private Collection<IKlant> clients;
	private int nieuwReknr;
	private String name;
	private ICentrale centrale;
	
	public Bank(String name) throws RemoteException, NotBoundException{
		accounts = new HashMap<>();
		clients = new ArrayList<>();
		nieuwReknr = 100000000;
		this.name = name;
		
		Registry reg;
		try {
			reg = LocateRegistry.getRegistry("127.0.0.1",1099);
			centrale = (ICentrale)reg.lookup("centrale");
			centrale.registreerBank(this);
		} catch (RemoteException ex) {
			
		}
		
	}

	@Override
	public synchronized int openRekening(String name, String city) {
		if (name.equals("") || city.equals("")) {
			return -1;
		}

		IKlant klant = getKlant(name, city);
		IRekeningTbvBank account = null;
		int nieuwNummer = 0;
		try {
			nieuwNummer = centrale.getNieuwRekeningNummer(this.name);
			account = new Rekening(nieuwNummer, klant, Money.EURO);
		} catch (RemoteException ex) {
			Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
		}
		accounts.put(nieuwNummer, account);
		System.out.println(account.getNr());
		return nieuwNummer;
	}

	private IKlant getKlant(String name, String city) {
		for (IKlant k : clients) {
			if (k.getNaam().equals(name) && k.getPlaats().equals(city)) {
				return k;
			}
		}
		IKlant klant = new Klant(name, city);
		clients.add(klant);
		return klant;
	}

	@Override
	public IRekening getRekening(int nr) {
		return accounts.get(nr);
	}

	@Override
	public synchronized boolean maakOver(int source, int destination, Money money)
			throws NumberDoesntExistException {
		if (source == destination) {
			throw new RuntimeException(
					"cannot transfer money to your own account");
		}
		if (!money.isPositive()) {
			throw new RuntimeException("money must be positive");
		}
		System.out.println("Ga nu proberen over te maken vanuit BANK");
		try {
			System.out.println("CENTRALE MAAK OVER");
			centrale.maakOver(source, destination, money);
			System.out.println("CENTRALE MAAK OVER GEDAAN");
			return true;
		} catch (RemoteException ex) {
			Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean muteer(int rekeningNummer, Money amount) {
		Rekening rekening = (Rekening) getRekening(rekeningNummer);
		if (rekening != null) {
			rekening.muteer(amount);
			return true;
		}
		return false;

	}

}
