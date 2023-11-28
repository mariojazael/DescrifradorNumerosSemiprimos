package Controller;

import View.MainJframe;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Biprime {
    static AtomicBoolean isSemiprimee = new AtomicBoolean();
    static AtomicBoolean isNon_semiprimee = new AtomicBoolean();
    static long endTime, startTime, elapsedTime;
    /*public static void main(String[] args) throws InterruptedException {
        int prueba =  20000;
        isSemiprimee.set(false);
        isNon_semiprimee.set(false);
        System.out.println(prueba);

        startTime = System.currentTimeMillis();
        paralelo(prueba);
        endTime = System.currentTimeMillis();
        elapsedTime = endTime - startTime;
        System.out.println("Tiempo transcurrido paralelo: " + elapsedTime + " milisegundos" + ", Es semiprimo: " + isSemiprimee);
        isSemiprimee.set(false);
        isNon_semiprimee.set(false);

        /*startTime = System.currentTimeMillis();
        isSemiprime(1, prueba, prueba);
        endTime = System.currentTimeMillis();
        elapsedTime = endTime - startTime;
        System.out.println("Tiempo transcurrido secuencial: " + elapsedTime + " milisegundos" + ", Es semiprimo: " + isSemiprimee);
        isSemiprimee.set(false);
        isNon_semiprimee.set(false);*/
    //}

    public static boolean isSemiprime(int limiteInferior, int limiteSuperior, int target, String source,MainJframe mainJframe) {
        isSemiprimee.set(false);
        boolean isSemiprime = false;
        for(int i = limiteInferior; i < limiteSuperior; i++){
            if(isPrime(i)) {
                for(int j = 0; j < limiteSuperior; j++){
                    if(isSemiprimee.get()) break;
                    if(isPrime(j)){
                        if(i * j == target) {
                            int finalI = i;
                            int finalJ = j;
                            isSemiprime = true;
                            isSemiprimee.set(true);
                            endTime = System.currentTimeMillis();
                            elapsedTime = endTime - startTime;
                            System.out.println(i + " * " + j + " Tiempo transcurrido " + source + elapsedTime + ": milisegundos");
                            SwingUtilities.invokeLater(()-> mainJframe.txtAreaResultados.setText(mainJframe.txtAreaResultados.getText()  + "\n" + finalI + " * " + finalJ + ", Numero" + target + ", Tiempo transcurrido:  " + elapsedTime + " milisegundos, " + source + "\n\n"));
                            break;
                        }
                    }
                }
            }
            if(isSemiprimee.get()) break;
        }
        return isSemiprime;
    }

    public static void secuencial(int prueba, MainJframe mainJframe){
        startTime = System.currentTimeMillis();
        isSemiprime(0, prueba, prueba,"secuencial", mainJframe);
        endTime = System.currentTimeMillis();
        elapsedTime = endTime - startTime;
        System.out.println("\n Tiempo transcurrido secuencial: " + elapsedTime + " milisegundos\n");
        SwingUtilities.invokeLater(()-> mainJframe.txtAreaResultados.setText(mainJframe.txtAreaResultados.getText() + "\nNumero " + prueba + ", Tiempo transcurrido secuencial: " + elapsedTime + " milisegundos\n\n"));
    }

    public static void concurrente(int prueba, MainJframe mainJframe) throws InterruptedException {
        startTime = System.currentTimeMillis();
        Lock lock = new ReentrantLock();
        Runtime runtime = Runtime.getRuntime();
        int availableProcessors = runtime.availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(availableProcessors / 2);
        int sliceSize = prueba / availableProcessors;
        for(int i = 0; i < availableProcessors; i++){
            int finalI = i;
            executorService.submit(()->{
                isSemiprime((finalI * sliceSize), (((finalI + 1) * sliceSize)), prueba,"concurrente", mainJframe);
            });
        }
        executorService.shutdown();
        while(!executorService.isTerminated()){
            if(isSemiprimee.get()) executorService.shutdownNow();
        }
        endTime = System.currentTimeMillis();
        elapsedTime = endTime - startTime;
        System.out.println("\n Tiempo transcurrido concurrente: " + elapsedTime + " milisegundos\n");
        SwingUtilities.invokeLater(()-> mainJframe.txtAreaResultados.setText("\n" + mainJframe.txtAreaResultados.getText() + "\nNumero: " + prueba + ", Tiempo transcurrido concurrente: " + elapsedTime + " milisegundos\n\n"));

        isNon_semiprimee.set(true);
    }

    public static boolean isPrime(int i){
        for(int j = 2; j < i / 2; j++){
            if(i % j == 0) return false;
        }
        return true;
    }
}
