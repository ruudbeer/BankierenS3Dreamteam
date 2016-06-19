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

	public OverboekCentrale() throws RemoteException {
		rekeningen = new HashMap<>();
		banken = new ArrayList<>();
	}

	@Override
	public int getNieuwRekeningNummer() throws RemoteException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
		
		Money afschrijfGeld = new Money(amount.getCents()*-1, amount.getCurrency());

		sourceBank.muteer(destination, amount);
		DestinationBank.muteer(source, afschrijfGeld);
		return true;
	}

	private IRekeningMuteerder getBankVanRekeningNummer(int rekeningNummer) throws NumberDoesntExistException{
		String bank = rekeningen.get(rekeningNummer);
		if (bank != null) {
			return getBankVanNaam(bank);
		}
		throw new NumberDoesntExistException("Kan bank niet vinden");
		
	}

	private IRekeningMuteerder getBankVanNaam(String bankNaam) throws RuntimeException {
		for (IRekeningMuteerder b : banken) {
			if (b.getName().equals(bankNaam)){
				return b;
			}
		}
		throw new RuntimeException("Kan bank niet vinden");
	}
}
