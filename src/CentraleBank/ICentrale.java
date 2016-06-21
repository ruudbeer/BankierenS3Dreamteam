/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CentraleBank;

import bank.bankieren.IBank;
import bank.bankieren.IRekeningMuteerder;
import bank.bankieren.Money;
import fontys.util.NumberDoesntExistException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Joel
 */
public interface ICentrale extends Remote{
	
	/**
	 * Haalt een nieuw rekening nummer op. Het nummer is uniek bij de centrale bank.
	 * 
	 * @param bankNaam : Bank waar het rekeningNummer bij moet gaan horen.
	 * @return int : een nieuw en uniek rekeningNummer
	 * @throws RemoteException 
	 */
	int getNieuwRekeningNummer(String bankNaam)throws RemoteException;
	
	
	/**
	 * Registreer een bank bij de centrale. 
	 * 
	 * @param bank : De bank die geregistreed moet worden.
	 * @return boolean : true als het gelukt is om een nieuwe bank te registreren. False als het niet gelukt is.
	 * @throws RemoteException 
	 */
	boolean registreerBank(IRekeningMuteerder bank)throws RemoteException;
	
	
	/**
	 * Maak een bedrag over via de centrale bank.
	 * 
	 * @param source : het rekeningNummer waar het verzoek tot overmaken vandaan komt.
	 * @param destination : het rekeningNummer waar het verzoek tot overmaken naar toe moet.
	 * @param amount : Het Money object dat de currency en waarde bevat.
	 * @return
	 * @throws RemoteException
	 * @throws NumberDoesntExistException : Als het rekeningNummer niet bestaad wordt deze exception opgegooird.
	 */
	boolean maakOver(int source, int destination, Money amount) throws RemoteException, NumberDoesntExistException;

}
