package bank.internettoegang;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import bank.bankieren.IBank;
import bank.bankieren.IRekening;
import bank.bankieren.Money;
import bank.bankieren.Rekening;

import fontys.util.InvalidSessionException;
import fontys.util.NegativeNumberException;
import fontys.util.NumberDoesntExistException;
import fontys.util.PropertieNamen;
import fontyspublisher.IRemotePropertyListener;
import fontyspublisher.Publisher;
import java.util.Observable;
import java.util.Observer;
import javafx.beans.InvalidationListener;

public class Bankiersessie extends UnicastRemoteObject implements
		IBankiersessie,Observer{

	private static final long serialVersionUID = 1L;
	private long laatsteAanroep;
	private int reknr;
	private IBank bank;
	
	private Publisher publisher;

	public Bankiersessie(int reknr, IBank bank) throws RemoteException {
		laatsteAanroep = System.currentTimeMillis();
		this.reknr = reknr;
		this.bank = bank;
		
		this.publisher = new Publisher();
		this.publisher.registerProperty(PropertieNamen.SALDO);
		addObserverToRekening();
	}
	
	private void addObserverToRekening(){
		Rekening rekening = (Rekening) bank.getRekening(reknr);
		rekening.addObserver(this);
	}

	@Override
	public boolean isGeldig() {
		return System.currentTimeMillis() - laatsteAanroep < GELDIGHEIDSDUUR;
	}

	@Override
	public boolean maakOver(int bestemming, Money bedrag)
			throws NumberDoesntExistException, InvalidSessionException,
			RemoteException, NegativeNumberException{

		updateLaatsteAanroep();
		
		if (reknr == bestemming)
			throw new RuntimeException(
					"source and destination must be different");
		if (!bedrag.isPositive())
			throw new NegativeNumberException("amount must be positive");

		System.out.println("Ga nu overmaken vanuit bankierSessie");
		return bank.maakOver(reknr, bestemming, bedrag);
	}
	
	private void updateLaatsteAanroep() throws InvalidSessionException {
		if (!isGeldig()) {
			throw new InvalidSessionException("session has been expired");
		}
		
		laatsteAanroep = System.currentTimeMillis();
	}

	@Override
	public IRekening getRekening() throws InvalidSessionException,
			RemoteException {

		updateLaatsteAanroep();
		
		return bank.getRekening(reknr);
	}

	@Override
	public void logUit() throws RemoteException {
		UnicastRemoteObject.unexportObject(this, true);
	}

	@Override
	public void subscribeRemoteListener(IRemotePropertyListener listener, String property) throws RemoteException {
		this.publisher.subscribeRemoteListener(listener, property);
	}

	@Override
	public void unsubscribeRemoteListener(IRemotePropertyListener listener, String property) throws RemoteException {
		this.publisher.unsubscribeRemoteListener(listener, property);
	}
	@Override
		public void update(Observable o, Object arg)
		{
			Bankiersessie.this.publisher.inform(PropertieNamen.SALDO, null, arg);
		}





}
