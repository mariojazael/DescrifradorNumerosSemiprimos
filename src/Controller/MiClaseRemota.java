package Controller;

import View.MainJframe;

import javax.swing.*;
import java.io.Serial;
import java.io.Serializable;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MiClaseRemota extends UnicastRemoteObject implements MiInterfazRemota, Serializable {
    @Serial
    private static final long serialVersionUID = -6044598747301230549L;
    public final AtomicInteger contador = new AtomicInteger();
    public AtomicBoolean start = new AtomicBoolean(false);
    static int prueba = 20000;
    static int pruebaFinal = prueba / 4;
    int sliceSize = pruebaFinal / 2;
    public static AtomicInteger limiteInferior = new AtomicInteger(0);
    public AtomicInteger limiteSuperior = new AtomicInteger(prueba);
    SerializableFunction<Integer [], HashMap<Boolean, String>> function = (a) -> {
        int recorrido = a[1] - a[0];
        int sliceSize = recorrido / 4;
        AtomicBoolean isSemiprime = new AtomicBoolean(false);
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        AtomicReference<String> result = new AtomicReference<>("");
        for(int k = 0; k < 4; k++){
            int finalK = k;
            executorService.submit(()->{
                for(int i = (a[0] + finalK * sliceSize) + a[3]; i < (a[0] + finalK * sliceSize) + sliceSize; i = i + 2){
                    if(isPrime(i)) {
                        for(int j = 0; j < a[1]; j++){
                            if(isSemiprime.get()) break;
                            if(isPrime(j)){
                                if(i * j == a[2] && i > 1 && j > 1) {
                                    isSemiprime.set(true);
                                    result.set(i + " * " + j);
                                    break;
                                }
                            }
                        }
                    }
                    if(isSemiprime.get()) break;
                }
            });
        }

        executorService.shutdown();
        while(!executorService.isTerminated()){
            if(isSemiprime.get()) executorService.shutdownNow();
        }

        HashMap<Boolean, String> hashMap = new HashMap<>();
        hashMap.put(isSemiprime.get(), result.get());

        // isSemiprime.set(false);
        return hashMap;
    };

    public MiClaseRemota() throws RemoteException {
        contador.set(0);
    }

    public AtomicBoolean getStart() {
        return start;
    }

    public void setStart(AtomicBoolean start) {
        this.start = start;
    }

    public AtomicInteger getContador() {
        return contador;
    }

    public Respuesta miMetodo1(int target) throws RemoteException {
// Aquí ponemos el código que queramos
        int contadorEstatico = contador.incrementAndGet();
        System.out.println(contador.get());

        while(!start.get()){}

        Integer [] parametros = {limiteInferior.get(), limiteSuperior.get(), target, contadorEstatico};

        // limiteInferior.set(limiteInferior.get() + sliceSize);
        // limiteSuperior.set(limiteSuperior.get() + sliceSize);

        contador.set(0);
        return new Respuesta(parametros, function);
    }

    public static boolean isPrime(int i){
        for(int j = 2; j < Math.sqrt(i); j++){
            if(i % j == 0) return false;
        }
        return true;
    }


    public void miMetodo2(String elemento) throws RemoteException {
        System.out.println("Elemento encontrado: " + elemento);
    }

    public void pintarGUI(String elemento, MainJframe mainJframe){
        SwingUtilities.invokeLater(()-> mainJframe.txtAreaResultados.setText(mainJframe.txtAreaResultados.getText() + "\n" + elemento));
    }



    public void up(String args) throws RemoteException {
        System.out.println(args );
        try {
            Registry registry = LocateRegistry.createRegistry(Integer.parseInt(args));

            MiInterfazRemota mir = new MiClaseRemota();

            java.rmi.Naming.rebind("//" +
                    java.net.InetAddress.getLocalHost().getHostAddress() +
                    ":" + args + "/PruebaRMI", mir);

            /*Respuesta respuesta = mir.miMetodo1();

            return respuesta;
             .out.println(Arrays.toString(respuesta.parametros));

            long startTime = System.currentTimeMillis();

            HashMap<Boolean, String> hashMap = respuesta.getFuncion().apply(respuesta.parametros);

            long endTime = System.currentTimeMillis();
            if(hashMap.containsKey(true)) {
                hashMap.put(true, hashMap.get(true) + " " + (endTime - startTime) + " milisegundos");
                mir.miMetodo2(hashMap.get(true));
            }
            else {
                hashMap.put(false, hashMap.get(false) + " " + (endTime - startTime) + " milisegundos");
                mir.miMetodo2(hashMap.get(false));
            } */


        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
