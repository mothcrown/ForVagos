/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.cifpcm.forvagos;

import java.util.*;
import java.nio.file.*;



/**
 * FORVAGOS: aplicación para consola
 * @autor Marcos Dacosta
 * 
 */
public class ForVagos {
    
    // Aquí debería ir una lista corriente y moliente, pero eso no tiene gracia
    static Tabla hoteles;
    
    /**
     * Clase main de todo esto. Subrepticiamente simple. Declaramos el usuario
     * inicial, creamos la Tabla hoteles y nos lanzamos a la montaña rusa que es
     * mostrarMenu().
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String usuario = "cliente";
        hoteles = creaTabla();
        
        do {
            usuario = mostrarMenu(usuario);
        } while (!usuario.equals("SALIR"));
    }
    
    /**
     * Iros a por un café o algo. Avisadme cuando volváis.
     * 
     * Ya?
     * 
     * Ok, si entendéis esto, el resto del programa (salvo alguna cosilla)
     * es pan comido. Metemos usuario, el usuario entra como parámetro a 
     * menuTexto(), menuTexto() es un switch muy majo que muestra texto y 
     * devuelve modo de opciones (porque pulsar el 1 como cliente o el 1 como
     * admin no es lo mismo). validaOpcion() comprueba que el usuario no ha
     * escrito Pepe y terminamos en opcionesMenu() que llama a los métodos
     * que pide el usuario.
     * 
     * Ah, y devolvemos usuario para mantener el bucle. Muy chulo todo!
     * 
     * @param usuario cliente, admin o buscador.
     * @return usuario
     */
    public static String mostrarMenu(String usuario) {
        int modo = menuTexto(usuario);
        Scanner sc = new Scanner(System.in);
        System.out.printf("Introduzca opción: ");
        int op = validaOpcion(sc.nextLine(), false);
        String user = (op >= 0) ? opcionesMenu(op, modo) : usuario;
        return user;
    }
    /**
     * Entra al switch, muestra texto de menú según usuario y devuelve valor
     * para modo de opcionesMenu().
     * 
     * @param usuario
     * @return modo
     */
    public static int menuTexto(String usuario) {
        int valor = -1;
        switch(usuario) {
            case "cliente":     menuCliente();
                                valor = 0;
                                break;
            case "admin":       menuAdmin();
                                valor = 1;
                                break;
//            case "edicion":       menuEditar();
//                                  valor = 2;
//                                  break;
//
// Si, esto casi pasa. Me he quedado con las ganas!
//
            case "buscador":    menuBuscador();
                                valor = 2;
                                break;
            default:            break;
        }
        return valor;
    }
    
    /**
     * Estos son los menús, nada muy interesante aquí.
     */
    public static void menuCliente(){
        System.out.print("\n================ FOR VAGOS ================\n");
        System.out.print("-- ¡Échese una siestecita mientras nosotros buscamos!\n");
        System.out.print("1.\tBusque hotel\n");
        System.out.print("0.\tSalir\n");
        System.out.print("--------------------------------\n");
        System.out.print("9.\tAdministrador\n");
    }
    
    public static void menuAdmin() {
        System.out.print("\n================ FOR VAGOS ================\n");
        System.out.print("1.\tAñadir hotel\n");
        System.out.print("2.\tBorrar hotel\n");
        System.out.print("0.\tSalir\n");
    }
    
    public static void menuBuscador() {
        System.out.print("\n================ FOR VAGOS ================\n");
        System.out.print("1.\tListar todos los hoteles\n");
        System.out.print("2.\tBuscar por nombre\n");
        System.out.print("3.\tBuscar por localidad\n");
        System.out.print("4.\tBuscar por estrellas\n");
        System.out.print("0.\tSalir\n");
    }
    /**
     * Esto debería entenderse bastante bien: entra la tecla pulsada por el 
     * usuario (op), que redireccionamos a las distintas opciones de menú
     * según el modo de usuario (cliente, admin, buscador...).
     *
     * @param op
     * @param modo
     * @return usuario
     */
    public static String opcionesMenu(int op, int modo) {
        String usuario = "cliente";
        switch(modo) {
            case 0:     usuario = opcionesCliente(op);
                        break;
            case 1:     usuario = opcionesAdmin(op);
                        break;
            case 2:     usuario = opcionesBuscador(op);
                        break;
            default:    break;
        }
        return usuario;
    }
    
    /**
     * Aquí nos detenemos un momentito: esta es la trastienda del menú de 
     * cliente. Si pulsamos 1, pasamos a usuario buscador, que desbloquea el 
     * menú de buscador. Si pulsamos 9 entramos en modo admin. El usuario 
     * "SALIR" sale!
     * 
     * @param op
     * @return usuario, como de costumbre
     */
    public static String opcionesCliente(int op) {
        String usuario = "cliente";
        switch(op) {
            case 1:     usuario = "buscador";
                        break;
            case 9:     usuario = "admin";
                        break;
            case 0:     usuario = "SALIR";
                        break;
            default:    System.out.print("\n¡Elige una opción válida!\n");
                        break;
        }
        return usuario;
    }
    
    /**
     * Aquí no cambiamos de modo de usuario (salvo que queramos salir del menú
     * de admin, claro), sino que llamamos a los métodos de añadir y borrar
     * hoteles.
     * 
     * @param op
     * @return usuario
     */
    public static String opcionesAdmin(int op) {
        String usuario = "admin";
        switch(op) {
            case 1:     anadirHotel();
                        break;
            case 2:     borrarHotel();
                        break;
            case 0:     usuario = "cliente";
                        break;
            default:    System.out.print("\n¡Elige una opción válida!\n");
        }
        return usuario;
    }
    
    public static String opcionesBuscador(int op) {
        String usuario = "buscador";
        switch(op) {
            case 1:     listarHoteles();
                        break;
            case 2:     buscarHotel(0);
                        break;
            case 3:     buscarHotel(1);
                        break;
            case 4:     buscarHotel(2);
                        break;
            case 0:     usuario = "cliente";
                        break;
            default:    System.out.print("\n¡Elige una opción válida!\n");
        }
        return usuario;
    }
    
    /**
     * Y algunos os estaréis preguntando: pero para qué. A santo de qué tanta
     * línea y montarnos clases Tabla y demás? Pues para este método tan majo! 
     * Busco lo que quiera, como quiera y cuando quiera! Paso parámetro de 
     * campo a buscar y me devuelve una lista filtrada. Es una maravilla!
     * 
     * Ahora bien... mas nunca, eh? Mas nunca.
     * 
     * @param para parámetro de búsqueda
     */    
    public static void buscarHotel(int para) {
        Scanner sc = new Scanner(System.in);
        StringBuffer sb = new StringBuffer();
        Boolean sw = false;
        System.out.print("\nIntroduzca parámetro de búsqueda: ");
        String busca = sc.nextLine();
        
        int lista = 1;
        
        
        sb.append("\nResultado de la búsqueda");
        sb.append("\n----------------");
        
        for (int i = 0; i < hoteles.campos.get(para).valores.size(); i++) {
            if (busca.equals(hoteles.campos.get(para).valores.get(i))) {
                sb.append("\n".concat(Integer.toString(lista)));
                sb.append(". \tNombre: ".concat(hoteles.campos.get(0).valores.get(i)));
                sb.append("\n\tLocalidad: ".concat(hoteles.campos.get(1).valores.get(i)));
                sb.append("\n\tEstrellas: ".concat(hoteles.campos.get(2).valores.get(i)));
                sb.append("\n\tPrecio/Noche: ".concat(hoteles.campos.get(3).valores.get(i) + " euros\n"));
                
                sw = true;
                lista++;
            }
        }
        System.out.print(sb.toString());
        
        if (!sw) {
            System.out.print("\n¡No se han encontrado resultados!\n");
        }
    }
    
    /**
     * Añadimos hotel tanto a la tabla hoteles como al fichero. Y de paso nos 
     * aseguramos de validarlo todo y que no nos metan hoteles de 20 estrellas
     * (aunque creo que hay ya de 6 y de 7, ojo).
     * 
     */
    public static void anadirHotel() {
        Scanner sc = new Scanner(System.in);
        Hotel hotel = new Hotel();
        int valor = -1;
        
        System.out.print("\nIntroduce nombre del hotel: ");
        hotel.setNombre(sc.nextLine());
        System.out.print("Introduce localidad del hotel: ");
        hotel.setLocalidad(sc.nextLine());
        do {
            System.out.print("Introduce número de estrellas del hotel: ");
            valor = validaOpcion(sc.nextLine(), true);
        } while (valor < 0 && valor > 5);
        hotel.setEstrellas(valor);
        do {
            System.out.print("Introduce precio por noche de estancia: ");
            valor = validaOpcion(sc.nextLine(), true);
        } while (valor == -1);
        hotel.setPrecioNoche(valor);
        
        hoteles.grabaTupla(hotel);
    }
    
    /**
     * Borramos hotel. No es muy espectacular y sí un poco farragoso, pero 
     * funciona. O eso espero!
     */
    public static void borrarHotel() {
        listarHoteles();
        
        Hotel hotel = new Hotel();
        String mensaje = null;
        Scanner sc = new Scanner(System.in);
        System.out.print("\nIntroduzca número del hotel a borrar (¡0 para salir¡): ");
        int op = Integer.parseInt(sc.nextLine());
        
        if (op != 0) {
            if (op > hoteles.getNumTuplas())
                System.out.print("\n¡No has introducido un número válido!\n");
            else {
                // op - 1 porque al listar hoteles sumamos 1 a su posición en la tabla.
                hotel.setNombre(hoteles.campos.get(0).valores.get(op - 1));
                hotel.setLocalidad(hoteles.campos.get(1).valores.get(op - 1));
                hotel.setEstrellas(Integer.parseInt(hoteles.campos.get(2).valores.get(op - 1)));
                hotel.setPrecioNoche(Integer.parseInt(hoteles.campos.get(3).valores.get(op - 1)));
                
                mensaje = hoteles.borraTupla(hotel);
                System.out.print(mensaje);
            }
        }
        else if (op < 0)
            System.out.print("\n¡No has introducido un número válido!\n");
        
    }
    
    public static void listarHoteles() {
        StringBuffer sb = new StringBuffer();
        sb.append("\nLista de hoteles");
        sb.append("\n----------------");
        for (int i = 0; i < hoteles.getNumTuplas(); i++) {
            sb.append("\n".concat(Integer.toString(i + 1)));
            sb.append(". \tNombre: ".concat(hoteles.campos.get(0).valores.get(i)));
            sb.append("\n\tLocalidad: ".concat(hoteles.campos.get(1).valores.get(i)));
            sb.append("\n\tEstrellas: ".concat(hoteles.campos.get(2).valores.get(i)));
            sb.append("\n\tPrecio/Noche: ".concat(hoteles.campos.get(3).valores.get(i) + " euros\n"));
        }
        sb.append("\n");
        System.out.print(sb.toString());
    }
    
    
    /**
     * Primero definimos el fichero (por cierto, Inma, te acuerdas que te 
     * comenté que había metido la pata en la anterior práctica? Fue por no
     * usar el Paths.get()!), luego el nombre de la tabla y preparamos los 
     * campos (más info sobre ellos en su clase).
     * 
     * @return Tabla de hoteles 
     */
    public static Tabla creaTabla() {
        String fichero = Paths.get(System.getProperty("java.io.tmpdir"), "hoteles.dat").toString();
        String nombre = "Hotel";
        ArrayList<Campo> campos = new ArrayList<>();
        
        campos.add(new Campo("Nombre", true, 0));
        campos.add(new Campo("Localidad", true, 0));
        campos.add(new Campo("Estrellas", false, 1));
        campos.add(new Campo("PrecioNoche", false, 1));
        
        Tabla tablahoteles = new Tabla(fichero, nombre, campos);
        return tablahoteles;
    }
    
    /**
     * Validamos cosas. El modo es true o false según si quiero controlar
     * negativos o no. TRUE = positivos solo; FALSE = welcome negativos!
     * 
     * @param valor
     * @param modo
     * @return int mondo y lirondo
     */
    public static int validaOpcion(String valor, Boolean modo) {
        int num = -1;
        try {
            num = Integer.parseInt(valor);
            if (modo && num < 0)
                System.out.print("\n¡No has introducido un número válido!\n");
        } catch(NumberFormatException e) {
            System.out.print("\n¡No has introducido un número entero!\n");
        }
        return num;
    }
}
