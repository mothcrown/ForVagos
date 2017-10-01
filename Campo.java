/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.cifpcm.forvagos;

import java.util.*;

/**
 * Es un campo! De Tabla! Como en las Bases de Datos de verdad!
 * 
 * @autor Marcos
 */
public class Campo {
    private String nombre;
    private Boolean key;
    // Esto es la leche de útil, pero no he tenido tiempo para implementarlo
    // en modo guay. Queda la idea.
    private int tipo;
    public ArrayList<String> valores;
    
    public Campo(String nombre, Boolean key, int tipo) {
        this.nombre = nombre;
        this.key = key;
        this.tipo = tipo;
        this.valores = new ArrayList<>();
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public Boolean getKey() {
        return key;
    }
    
    public void setKey(Boolean key) {
        this.key = key;
    }
    
    public int getTipo() {
        return tipo;
    }
    
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    
//    public ArrayList<String> getValores() {
//        return valores;
//    }
//    
//    public void setValores(ArrayList<String> valores) {
//        this.valores = valores;
//    }
}
