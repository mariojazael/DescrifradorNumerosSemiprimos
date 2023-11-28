package Controller;

import View.MainJframe;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MiInterfazRemota extends Remote {
    Respuesta miMetodo1(int target) throws RemoteException;
    void miMetodo2(String a) throws RemoteException;
    void pintarGUI(String elemento, MainJframe mainJframe) throws RemoteException;
}
