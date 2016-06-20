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
	int getNieuwRekeningNummer(String bankNaam)throws RemoteException;
	
	boolean registreerBank(IRekeningMuteerder bank)throws RemoteException;
	
	boolean maakOver(int source, int destination, Money amount) throws RemoteException, NumberDoesntExistException;

}
