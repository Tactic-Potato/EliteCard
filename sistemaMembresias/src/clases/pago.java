package clases;
import java.text.DecimalFormat;
import java.time.YearMonth;
import java.util.Scanner;

public class pago {

    private boolean bandera;
    private double dineroUsuario;
    private double cambio;
    private String tarjeta = " "; 
    DecimalFormat df = new DecimalFormat("0.00");

    public pago(){

    }

    Scanner s = new Scanner(System.in);

    public boolean pagarConTarjeta(double monto) {
        String numeroTarjeta, fechaVencimiento, codigoSeguridad;

        System.out.println("\nIngrese el número de tarjeta de crédito:");
        System.out.println(" * Ingrese 'r' para cancelar el pago en cualquier momento");
        while (true) {
            numeroTarjeta = s.nextLine();
    
            if (numeroTarjeta.equalsIgnoreCase("r")) {
                System.out.println(colores.ROJO+"Operacion cancelada"+colores.RESET);
                return false;
            }
    
            if (numeroTarjeta.length() != 16 || !numeroTarjeta.matches("\\d+")) {
                System.out.println(colores.ROJO+"Numero de tarjeta invalido. Por favor, intentelo de nuevo"+colores.RESET);
                continue;
            }
    
            break;
        }
        
        YearMonth fechaActual = YearMonth.now(); // Obtiene el año y mes actual

        System.out.println("Ingrese la fecha de vencimiento (MM/AA):");
        while (true) {
            fechaVencimiento = s.nextLine();

            if (fechaVencimiento.equalsIgnoreCase("r")) {
                System.out.println("Operacion cancelada");
                return false; // Termina la ejecución del método main
            }

            String[] partes = fechaVencimiento.split("/");
            if (partes.length != 2 || !partes[0].matches("\\d{2}") || !partes[1].matches("\\d{2}")) {
                System.out.println("Fecha de vencimiento invalida. Por favor, intentelo de nuevo");
                continue;
            }

            try {
                int mes = Integer.parseInt(partes[0]);
                int anio = Integer.parseInt(partes[1]) + 2000; // Añade 2000 para convertir a formato de año completo

                if (mes < 1 || mes > 12) {
                    System.out.println("Mes de vencimiento invalido");
                    continue;
                }

                YearMonth fechaIngresada = YearMonth.of(anio, mes);
                if (fechaIngresada.isBefore(fechaActual)) {
                    System.out.println("La tarjeta esta vencida");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Fecha de vencimiento invalida. Por favor, intentelo de nuevo");
            }
        }

        System.out.println("Fecha de vencimiento valida");
    
        System.out.println("Ingrese el código de seguridad (3 dígitos numéricos):");
        while (true) {
            
            codigoSeguridad = s.nextLine();
    
            if (codigoSeguridad.equalsIgnoreCase("r")) {
                System.out.println(colores.ROJO+"Operacion cancelada"+colores.RESET);
                return false;
            }
    
            if (codigoSeguridad.length() != 3 || !codigoSeguridad.matches("\\d+")) {
                System.out.println(colores.ROJO+"Codigo de seguridad invalido. Por favor, intentelo de nuevo"+colores.RESET);
                continue;
            }
    
            break;
        }

        System.out.println("Verificando información...");
        try {
            Thread.sleep(2000); // 2000 milisegundos = 2 segundos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    
        // Procesar el pago...
        System.out.println("Pago de $" + df.format(monto) + " realizado con éxito con su tarjeta de credito.");
        System.out.println("\nPulse enter para continuar");
        s.nextLine();
        limpiarConsola();
        return true;
    }


    //Metodo para pagar con efectivo
    public double pagarConEfectivo(double total) {
        System.out.println("\nHa seleccionado pagar con efectivo. Seleccione la cantidad a pagar (o 'r' para cancelar la operacion):");
        do{
            try {
                bandera=false;
                String input = s.next();
                if (input.equalsIgnoreCase("r")) {
                    System.out.println(colores.ROJO+"Operacion cancelada\n"+colores.RESET);
                    return -1; // o cualquier valor que indique que se canceló la operación
                } else {
                    dineroUsuario = Double.parseDouble(input);
                    if (dineroUsuario>=total) {
                        cambio = dineroUsuario - total;
                    }else {
                        System.out.println(colores.ROJO+"Pague con un monto mayor o igual al total"+colores.RESET);
                        bandera=true;
                    } 
                }
            } catch (Exception e) {
                bandera=true;
                s.next();
                System.out.println("Error. Debe ingresar obligatoriamente un valor numérico");
            }
        }while (bandera);

        System.out.println("\nPulse enter para continuar");
        s.nextLine();
        //Retorna el dineroUsuario para luego poder imprimirlo en otra clase.
        //Osea, que en otra clase puedas declarar un dineroUsuario = pagos.pagarConEfectivo(total) y ya lo imprimes
        return dineroUsuario;
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

}
