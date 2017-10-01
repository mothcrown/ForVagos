package es.cifpcm.forvagos;

import java.io.File;
import java.util.*;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.CopyOption;
import java.nio.file.Files;


/**
 * Uff. Vale.
 * 
 * La clase Tabla se comporta como una Tabla de base de datos, pero en virtual.
 * Tiene su nombre, su fichero donde está guardada, su lista de campos, lista
 * de primary keys (aunque no lo he usado mucho) y un cómodo contador de tuplas.
 * Así sé cuántos registros hay!
 *  
 * @autor Marcos
 */
public class Tabla {
    private String fichero;
    public String nombre;
    public ArrayList<Campo> campos;
    public ArrayList<String> primaryKeys;
    private int numTuplas;
    
    /**
     * Constructor!
     * 
     * @param fichero
     * @param nombre
     * @param campos 
     */
    public Tabla(String fichero, String nombre, ArrayList<Campo> campos) {
        this.fichero = fichero;
        this.nombre = nombre;
        this.campos = campos;
        this.primaryKeys = sacarPrimaryKeys();
        // Abrimos fichero si existe, creamos si no
        abreTabla();
    }
    
    /**
     * Podría explicar todo esto aquí, pero no. A ver, la idea así a grandes 
     * rasgos es que vamos a poder borrar tuplas individuales del fichero.
     * ¿Cómo? Pues vamos a crear un nuevo fichero, vamos a copiar el viejo
     * saltándonos las líneas que queremos borrar y luego reemplazamos el viejo
     * con el nuevo.
     * 
     * No he encontrado una forma menos rebuscada de hacerlo y
     * el renameTo() no funciona porque el mundo es un lugar frío y gris
     * y Java quiere arruinarme el finde.
     * 
     * @param hotel
     * @return mensaje de éxito/error!
     */
    public String borraTupla(Hotel hotel) {
        BufferedReader br = null;
        BufferedWriter bw = null;
        
        // Declaramos ficheros para esta aventura!
        File foriginal = new File(fichero);
        File ftemp = new File(Paths.get(System.getProperty("java.io.tmpdir"), "temp.dat").toString());
        
        // encuentraTupla() es una maravilla que funciona con magia, 
        // esparadrapo e ilusión.
        int posicion = encuentraTupla(hotel);
        
        String mensaje = null;
        int nTuplas = numTuplas;
        String valor = null;
        int cont = 0;
        ArrayList<String> aux = new ArrayList<>();
        
        if (posicion == -1) {
            mensaje = "\n¡No se ha podido encontrar el hotel a borrar!.\n";
        }
        else {
            // Nos cargamos el registro en la Tabla, pero guardamos en aux 
            // porque nos viene de perlas para borrar luego en fichero. 
            // Ya veréis.
            for (int i = 0; i < campos.size(); i++) {
                aux.add(campos.get(i).valores.get(posicion));
                campos.get(i).valores.remove(posicion);
            }
            
            // Tupla = 4 strings. La posición 4 es la segunda tupla (1)
            posicion = posicion * 4;
            
            try {
                br = new BufferedReader(new FileReader(foriginal));
                bw = new BufferedWriter(new FileWriter(ftemp));

                // Esto funciona. Yo también lo flipo. Shh, no hagáis ruido.
                while((valor = br.readLine()) != null) {
                    if (valor.equals(aux.get(0)) && cont == posicion) {
                        for (int i = 0; i < 3; i++) {
                            br.readLine();
                        }
                        cont+=4;
                    } else {
                        bw.write(valor + "\n");
                        cont++;
                    }
                }
                
                //bw.write(valor + "\n");
                bw.flush();
                mensaje = "\nHotel borrado.\n";
                nTuplas--;

            } catch (Exception e) {

            } finally {
                try {
                    if (bw != null)
                        bw.close();
                    if (br != null)
                        br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                                
                // Que renameTo() no funciona y quieres reescribir un archivo
                // con otro? CopyOption y Files.copy() al rescate!
                Path desde = Paths.get(System.getProperty("java.io.tmpdir"), "temp.dat");
                Path hacia = Paths.get(fichero);
                
                CopyOption[] opciones = new CopyOption[]{
                  StandardCopyOption.REPLACE_EXISTING,
                  StandardCopyOption.COPY_ATTRIBUTES
                }; 
                
                try {
                    Files.copy(desde, hacia, opciones);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                numTuplas = nTuplas;
            }
        }
        
        return mensaje;
        
    }
    
    /**
     * Si existe conecta, si no, crea fichero. Simple, de veras.
     */
    private void abreTabla() {
        File datos = new File(fichero);
        if (datos.exists()) {
            conectaDatos();
        } else {
            creaFichero(datos);
        }
    }
    
    /**
     * Cargamos la tabla desde Fichero. Hay partes moviditas pero se entiende
     * casi todo: pasad sin miedo!
     */
    private void conectaDatos() {
        FileReader fr = null;
        BufferedReader br = null;
        ArrayList<String> tupla = new ArrayList<>();
        String valor;
        int nTuplas = 0;
        
        try {
            fr = new FileReader(fichero);
            br = new BufferedReader(fr);
            
            do {
                // Leemos líneas y vamos llenando tuplas, cuando llegamos a 4
                // strings (en este caso) tenemos una nueva tupla y rellenamos
                // la tabla con ella. Sumamos nTuplas y limpiamos la lista
                // para cargar una nueva tupla.
                valor = br.readLine();
                if (valor != null) {
                    tupla.add(valor);
                    for (int i = 0; i < campos.size() - 1; i++) 
                        tupla.add(br.readLine());
                    
                    for (int i = 0; i < campos.size(); i++)
                        campos.get(i).valores.add(tupla.get(i));
                    
                    nTuplas++;
                    tupla.clear();
                }
            } while (valor != null);
        } catch (Exception e) {
            
        } finally {
            try {
                if (fr != null)
                    fr.close();
                if (br != null)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            numTuplas = nTuplas;
        }
    }
    
    private void creaFichero(File datos) {
        try {
            // createNewFile() = MVP del partido
            datos.createNewFile();
            numTuplas = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Apenas usamos esto al final, pero nunca está de más.
     * 
     * @return array con nombres de las Primary Keys de la tabla.
     */
    private ArrayList<String> sacarPrimaryKeys() {
        ArrayList<String> pKs = new ArrayList<>();
        
        for (Campo campo : campos) 
            if (campo.getKey()) 
                pKs.add(campo.getNombre());
        
        return pKs;
    }
    
    /**
     * Esto es una chapuza total e impide que la clase tabla sea mucho más 
     * genérica. Estoy todavía probando algoritmos para esto, pero se me echó
     * el tiempo encima. Solo deciros que funciona y que cuando alguien
     * piensa que este método no funciona en algún sitio se muere un gatito.
     * 
     * Vosotros sabréis.
     * 
     * @param hotel
     * @return posición (con suerte)
     */
    public int encuentraTupla(Hotel hotel) {
        int posicion = -1;
        int exito = 0;
        int m = 0;
        Boolean sw = false;
        ArrayList<String> valores = new ArrayList<>();
        ArrayList<Integer> pKs = new ArrayList<>();
        
        valores.add(hotel.getNombre());
        valores.add(hotel.getLocalidad());
        valores.add(Integer.toString(hotel.getEstrellas()));
        valores.add(Integer.toString(hotel.getPrecioNoche()));
        
        // Esto me hizo falta. Por algún motivo.
        for (int i = 0; i < campos.size(); i++) {
            if (campos.get(i).getKey()) {
                pKs.add(i);
            }
        }
        
        /*
                ARREGLA ESTO POR DIOS
        */
        for (int i = 0; i < pKs.size(); i++) {
            for (int j = 0; j < campos.get(0).valores.size(); j++) {
                if (valores.get(pKs.get(i)).equals(campos.get(pKs.get(i)).valores.get(j))) {
                    i++;
                    if (valores.get(pKs.get(i)).equals(campos.get(pKs.get(i)).valores.get(j))) {
                        posicion = j;
                    }
                    else{
                    i--;
                    }
                }
            }
        }
        
        return posicion;
    }
    
    /**
     * Esto graba tuplas, si os fijáis las pasamos primero a Strings, luego
     * componemos un Array y es así como grabamos la Tabla en el fichero.
     * Estuve un tiempo jugando con la idea de aprovechar la escritura binaria 
     * para guardar los ints como ints y tal, pero lo dejo para el futuro, que
     * ya me estaba complicando la vida demasiado.
     * 
     * @param hotel 
     */
    public void grabaTupla(Hotel hotel) {
        BufferedWriter bw = null;
        FileWriter fw = null;
        ArrayList<String> valores = new ArrayList<>();
        int nTuplas = numTuplas;
        
        valores.add(hotel.getNombre());
        valores.add(hotel.getLocalidad());
        valores.add(Integer.toString(hotel.getEstrellas()));
        valores.add(Integer.toString(hotel.getPrecioNoche()));
        
        for (int i = 0; i < campos.size(); i++)
            campos.get(i).valores.add(valores.get(i));
        
        try {
            fw = new FileWriter(fichero, true);
            bw = new BufferedWriter(fw);
            for (int i = 0; i < valores.size(); i++) 
                bw.write(valores.get(i) + "\n");
            
            bw.flush();
            nTuplas++;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw != null)
                    fw.close();
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            numTuplas = nTuplas;
        }
    }
    
    public String getFichero() {
        return fichero;
    }
    
    public void setFichero(String fichero) {
        this.fichero = fichero;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public ArrayList<Campo> getCampos() {
        return campos;
    }
    
    public void setCampos(ArrayList<Campo> campos) {
        this.campos = campos;
    }
    
    public ArrayList<String> getPrimaryKeys() {
        return primaryKeys;
    }
    
    public void setPrimaryKeys(ArrayList<String> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }
    
    public int getNumTuplas() {
        return numTuplas;
    }
    
    public void setNumTuplas(int numTuplas) {
        this.numTuplas = numTuplas;
    }
    
}
