
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MiClaseRemota extends UnicastRemoteObject implements MiInterfazRemota {
    private final AtomicInteger contador = new AtomicInteger();
    static int prueba = 100000;
    static int pruebaFinal = prueba / 2;
    int sliceSize = pruebaFinal / 2;
    AtomicInteger limiteInferior = new AtomicInteger(0);
    AtomicInteger limiteSuperior = new AtomicInteger(sliceSize);

    public MiClaseRemota() throws RemoteException {
        contador.set(0);
    }

    public Respuesta miMetodo1() throws RemoteException {
// Aquí ponemos el código que queramos
        contador.incrementAndGet();
        System.out.println(contador.get());
        while(contador.get() <= 1){}

        Integer [] parametros = {limiteInferior.get(), limiteSuperior.get(), prueba};

        limiteInferior.set(limiteInferior.get() + sliceSize);
        limiteSuperior.set(limiteSuperior.get() + sliceSize);

        SerializableFunction<Integer [], HashMap<Boolean, String>> function = (a) -> {
            int recorrido = a[1] - a[0];
            int sliceSize = recorrido / 4;
            AtomicBoolean isSemiprime = new AtomicBoolean(false);
            ExecutorService executorService = Executors.newFixedThreadPool(4);
            AtomicReference<String> result = new AtomicReference<>("");
            for(int k = 0; k < 4; k++){
                int finalK = k;
                executorService.submit(()->{
                    for(int i = a[0] + finalK * recorrido; i < (a[0] * finalK + recorrido) + recorrido; i++){
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



    public static void main(String[] args) throws RemoteException {
        System.out.println(prueba);
        try {
            Registry registry = LocateRegistry.createRegistry(
                    Integer.parseInt(args[0]));

            MiInterfazRemota mir = new MiClaseRemota();

            java.rmi.Naming.rebind("//" +
                    java.net.InetAddress.getLocalHost().getHostAddress() +
                    ":" + args[0] + "/PruebaRMI", mir);

            Respuesta respuesta = mir.miMetodo1();

            System.out.println(Arrays.toString(respuesta.parametros));

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
            }


        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
