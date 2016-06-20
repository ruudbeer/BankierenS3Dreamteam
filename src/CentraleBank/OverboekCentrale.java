/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CentraleBank;

import bank.bankieren.IBank;
import bank.bankieren.IRekening;
import bank.bankieren.IRekeningMuteerder;
import bank.bankieren.Money;
import bank.bankieren.Rekening;
import fontys.util.NumberDoesntExistException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Joel
 */
public class OverboekCentrale extends UnicastRemoteObject implements ICentrale {

	final Map<Integer, String> rekeningen;
	List<IRekeningMuteerder> banken;
	int newRekeningNummer = 10000000;

	public OverboekCentrale() throws RemoteException {
		rekeningen = new HashMap<>();
		banken = new ArrayList<>();
	}

	@Override
	public int getNieuwRekeningNummer(String bankNaam) throws RemoteException {
		synchronized (rekeningen) {
			newRekeningNummer++;
			rekeningen.put(newRekeningNummer, bankNaam);
			return newRekeningNummer;
		}

	}

	@Override
	public boolean registreerBank(IRekeningMuteerder bank) throws RemoteException {
		if (!banken.contains(bank)) {
			banken.add(bank);
			System.out.println(bank.getName());
			return true;

		} else {
			return false;
		}
	}

	@Override
	public boolean maakOver(int source, int destination, Money amount) throws RemoteException, NumberDoesntExistException {
		IRekeningMuteerder sourceBank = getBankVanRekeningNummer(source);
		IRekeningMuteerder DestinationBank = getBankVanRekeningNummer(destination);

		Money afschrijfGeld = new Money(amount.getCents() * -1, amount.getCurrency());

		boolean from  = sourceBank.muteer(source, afschrijfGeld);
		boolean to = DestinationBank.muteer(destination, amount);
                System.out.println("from:"+ from);
                System.out.println("dest:"+to);
		return true;
	}

	private IRekeningMuteerder getBankVanRekeningNummer(int rekeningNummer) throws NumberDoesntExistException, RuntimeException, RemoteException {
		String bank = rekeningen.get(rekeningNummer);
		if (bank != null) {
			return getBankVanNaam(bank);
		}
		throw new NumberDoesntExistException("Kan bank niet vinden");

	}

	private IRekeningMuteerder getBankVanNaam(String bankNaam) throws RuntimeException, RemoteException {
		for (IRekeningMuteerder b : banken) {
			if (b.getName().equals(bankNaam)) {
				return b;
			}
		}
		throw new RuntimeException("Kan bank niet vinden");
	}
}
