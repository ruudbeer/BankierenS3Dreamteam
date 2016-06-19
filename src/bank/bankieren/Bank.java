package bank.bankieren;

import CentraleBank.OverboekCentrale;
import fontys.util.NumberDoesntExistException;
import fontyspublisher.Publisher;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bank implements IBank, IRekeningMuteerder {

	/**
	 *
	 */
	private static final long serialVersionUID = -8728841131739353765L;
	private Map<Integer, IRekeningTbvBank> accounts;
	private Collection<IKlant> clients;
	private int nieuwReknr;
	private String name;
	private OverboekCentrale centrale;
	
	public Bank(String name) throws RemoteException, NotBoundException{
		accounts = new HashMap<Integer, IRekeningTbvBank>();
		clients = new ArrayList<IKlant>();
		nieuwReknr = 100000000;
		this.name = name;
		
		Registry reg;
		try {
			reg = LocateRegistry.getRegistry("127.0.0.1",777);
			centrale = (OverboekCentrale)reg.lookup("centrale");
			centrale.registreerBank(this);
		} catch (RemoteException ex) {
			
		}
		
	}

	public synchronized int openRekening(String name, String city) {
		if (name.equals("") || city.equals("")) {
			return -1;
		}

		IKlant klant = getKlant(name, city);
		IRekeningTbvBank account = null;
		try {
			account = new Rekening(nieuwReknr, klant, Money.EURO);
		} catch (RemoteException ex) {
			Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
		}
		accounts.put(nieuwReknr, account);
		nieuwReknr++;
		return nieuwReknr - 1;
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

	public IRekening getRekening(int nr) {
		return accounts.get(nr);
	}

	public synchronized boolean maakOver(int source, int destination, Money money)
			throws NumberDoesntExistException {
		if (source == destination) {
			throw new RuntimeException(
					"cannot transfer money to your own account");
		}
		if (!money.isPositive()) {
			throw new RuntimeException("money must be positive");
		}

		IRekeningTbvBank source_account = (IRekeningTbvBank) getRekening(source);
		if (source_account == null) {
			throw new NumberDoesntExistException("account " + source
					+ " unknown at " + name);
		}

		Money negative = Money.difference(new Money(0, money.getCurrency()),
				money);
		boolean success = source_account.muteer(negative);
		if (!success) {
			return false;
		}

		IRekeningTbvBank dest_account = (IRekeningTbvBank) getRekening(destination);
		if (dest_account == null) {
			throw new NumberDoesntExistException("account " + destination
					+ " unknown at " + name);
		}
		success = dest_account.muteer(money);

		if (!success) // rollback
		{
			source_account.muteer(money);
		}
		return success;
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
