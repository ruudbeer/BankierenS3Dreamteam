/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.bankieren;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Joel
 */
public interface IRekeningMuteerder extends Remote{
	/**
         * Deze methode roept de muteer functie aan bij het opgegeven rekeningnummer aan en geeft hier een bedrag amount aan mee
         * @param rekeningNummer het rekeningnummer waarvan het geld gemuteerd moet worden
         * @param amount het bedrag dat overgeschreven moet worden
         * @return true als het geld overgeschreven is false als het rekeningnummer niet bekend is
         * @throws RemoteException 
         */
	boolean muteer(int rekeningNummer,Money amount) throws RemoteException;
	/**
         * geeft de banknaam terug.
         * @return de bank naam
         * @throws RemoteException 
         */
	String getName() throws RemoteException;
}
