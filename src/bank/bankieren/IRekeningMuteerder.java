/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.bankieren;

import java.rmi.Remote;

/**
 *
 * @author Joel
 */
public interface IRekeningMuteerder extends Remote{
	
	boolean muteer(int rekeningNummer,Money amount);
	
	String getName();
}
