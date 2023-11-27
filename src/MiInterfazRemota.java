import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MiInterfazRemota extends Remote {
    public Respuesta miMetodo1() throws RemoteException;
    public void miMetodo2(String a) throws RemoteException;
}
