package Controller;

import java.rmi.Naming;
import java.util.Arrays;
import java.util.HashMap;

public class MiClienteRMI {
    public static void main(String[] args) {
        try {
            MiInterfazRemota mir =
                    (MiInterfazRemota)Naming.lookup("//" +
                            args[0] + ":" + args[1] + "/PruebaRMI");

// Imprimimos miMetodo1() tantas veces como devuelva miMetodo2()

            // mir.miMetodo2(" Mario");

            Respuesta respuesta = mir.miMetodo1(Integer.parseInt(args[2]));

            long startTime = System.currentTimeMillis();

            System.out.println(Arrays.toString(respuesta.parametros));

            HashMap<Boolean, String> hashMap = respuesta.getFuncion().apply(respuesta.parametros);

            long endTime = System.currentTimeMillis();
            if(hashMap.containsKey(true)) {
                hashMap.put(true, hashMap.get(true) + " " + (endTime - startTime) + " milisegundos");
                mir.pintarGUI(hashMap.get(true), respuesta.mainJframe);
            }
            else {
                hashMap.put(false, hashMap.get(false) + " " + (endTime - startTime) + " milisegundos");
                mir.pintarGUI(hashMap.get(true), respuesta.mainJframe);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
