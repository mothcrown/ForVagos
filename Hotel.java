package es.cifpcm.forvagos;


import java.io.Serializable;

/**
 * Aquí no he tocado nada. No, de veras.
 * 
 * @autor Marcos
 */
public class Hotel implements Serializable {
    private String nombre;
    private String localidad;
    private int estrellas;
    private int precioNoche;
    
    public Hotel() {
        this.nombre = "";
        this.localidad = "";
        this.estrellas = 0;
        this.precioNoche = 0;
    }
    
    public Hotel(String nombre, String localidad, int estrellas, int precioNoche) {
        this.nombre = nombre;
        this.localidad = localidad;
        this.estrellas = estrellas;
        this.precioNoche = precioNoche;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getLocalidad() {
        return localidad;
    }
    
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }
    
    public int getEstrellas() {
        return estrellas;
    }
    
    public void setEstrellas(int estrellas) {
        this.estrellas = estrellas;
    }
    
    public int getPrecioNoche() {
        return precioNoche;
    }
    
    public void setPrecioNoche(int precioNoche) {
        this.precioNoche = precioNoche;
    }
    
}
    

