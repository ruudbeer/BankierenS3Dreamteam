/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CentraleBank;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 *
 * @author Joel
 */
public class OverboekCentraleServer {
	public static void main(String[] arg) {
		
		try {
			OverboekCentrale centrale = new OverboekCentrale();
            Registry reg = LocateRegistry.createRegistry(1099);
            reg.rebind("centrale", centrale);
            System.out.println("centrale zit in registry");
            new Scanner(System.in).next();
            System.out.println("Done");
        } catch (RemoteException e) {
			System.out.println("RemoteExeption, check of poort niet al in gebruik is");
        }
		
	}
}
