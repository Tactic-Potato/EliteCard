package clases;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class persona {
    private int numero;
    private String nombre;
    private String primer_AP;
    private String segundo_AP;
    private String correo;

    // Constructor por defecto
    public persona() {}

    // Constructor con conexión a la base de datos
 
    // Getters y Setters

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrimer_AP() {
        return primer_AP;
    }

    public void setPrimer_AP(String primer_AP) {
        this.primer_AP = primer_AP;
    }

    public String getSegundo_AP() {
        return segundo_AP;
    }

    public void setSegundo_AP(String segundo_AP) {
        this.segundo_AP = segundo_AP;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void imprimirDatos() {
        System.out.println("Número: " + numero);
        System.out.println("Nombre: " + nombre);
        System.out.println("Primer Apellido: " + primer_AP);
        System.out.println("Segundo Apellido: " + segundo_AP);
        System.out.println("Correo: " + correo);
    }
    public static void limpiarConsola() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //METODOS DE VERIFICACION
    public static boolean noContieneNum(String cadena) {
        // Verificar si la cadena contiene algún dígito.
        for (char c : cadena.toCharArray()) {
            if (Character.isDigit(c)) {
                return false; // La cadena contiene un número.
            }
        }
        return true; // La cadena no contiene números.
    }

    public boolean longitud(String cadena) {
        return cadena.length() > 3;
    }

    public static boolean validarEmail (String correo){
        String patron = "^[\\w._%+-]+@(gmail\\.com|hotmail\\.com)$";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();
   }
}
