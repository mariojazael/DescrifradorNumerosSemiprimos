package Controller;

import View.MainJframe;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

public class Respuesta implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    Integer [] parametros;
    SerializableFunction<Integer[], HashMap<Boolean, String>> funcion;
    MainJframe mainJframe;

    public Respuesta(Integer[] parametros, SerializableFunction<Integer[], HashMap<Boolean, String>> funcion) {
        this.parametros = parametros;
        this.funcion = funcion;
    }

    public Integer[] getParametros() {
        return parametros;
    }

    public void setParametros(Integer[] parametros) {
        this.parametros = parametros;
    }

    public SerializableFunction<Integer[], HashMap<Boolean, String>> getFuncion() {
        return funcion;
    }

    public void setFuncion(SerializableFunction<Integer[], HashMap<Boolean, String>> funcion) {
        this.funcion = funcion;
    }
}
