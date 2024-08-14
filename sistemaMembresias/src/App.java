import java.sql.*;
import java.util.Scanner;
import clases.*;
import java.time.*;
public class App{
    public static void main(String[] args) throws SQLException {
        //Comentario para probar
        conexion conexion = new conexion("jdbc:mysql://localhost:3306/elitecard", "root", "");
        Connection con = conexion.conectar();
        nivelDeTarjeta adminNiveles = new nivelDeTarjeta(conexion);
        empleado admEmpleado = new empleado(conexion);
        empleado adminEmpld = new empleado(conexion);
        miembro adMiembro = new miembro(conexion);
        ventas adminVentas = new ventas(conexion);
        tarjetaMembresia adminTarjetas = new tarjetaMembresia(conexion);
        String cond;
        Scanner s = new Scanner(System.in);
        boolean bandera;
        boolean banderadmin;
        boolean banderalogin;
        LocalDate fechaHoy = LocalDate.now();
        Date fecha = Date.valueOf(fechaHoy);
        int numSession;
        // Primer trycatch para verificar la conexion con la BD 
        try {
            limpiarConsola();
            System.out.println(colores.ORO + "\n");
            System.out.println(" ██████████████████████████████\r\n" + //
                                "███                          ███\r\n" + //]
                                "████████████████████████████████     _____ _ _ _        ____              _ \r\n" + //
                                "███                          ███    | ____| (_) |_ ___ / ___|__ _ _ __ __| |\r\n" + //
                                "███                          ███    |  _| | | | __/ _ \\ |   / _` | '__/ _` |\r\n" + //
                                "███  ████  ████████      ██  ███    | |___| | | ||  __/ |__| (_| | | | (_| |\r\n" + //
                                "███  ████  ████████    ████  ███    |_____|_|_|\\__\\___|\\____\\__,_|_|  \\__,_|\r\n" + //
                                "███                          ███\r\n" + //
                                " ███████████████    ███████████  \r\n" + //
                                "   ");
            System.out.println(colores.PURPURA+"\n---------------------------");
            System.out.println("  BIENVENIDO A ELITE CARD" + colores.RESET + "\n  Fecha actual: "+ fecha);
            System.out.println(colores.PURPURA+"---------------------------");
            do{
                    System.out.println(colores.AMARILLO + "\n¿Que desea hacer?"
                                        + colores.CIAN + "\n\ta) Iniciar sesión"
                                        + colores.PURPURA +"\n\tb) Cerrar el programa" + colores.RESET
                    );
                    cond = s.nextLine();
                    limpiarConsola();
                    
                    switch (cond.toLowerCase()) {
                        case "a":
                        do{
                            banderalogin = false;
                            System.out.println(colores.AZUL + "\nINICIO DE SESIÓN");
                            if(adminEmpld.iniciarsesion() == false){
                                break;
                            } else {
                            numSession = adminEmpld.getNumero();
                            limpiarConsola();
                            // System.out.println("NUMERO DE EMPLEADO" + numSession);
                            if(adminEmpld.getPuesto().equals("PT4") || adminEmpld.getPuesto().equals("PT1")){
                                System.out.println(colores.ORO);
                                System.out.println("                      __");
                                System.out.println("                    // \\");
                                System.out.println("                     \\/_/ //");
                                System.out.println("   ''-.._.-''-.._.. -(||)(')");
                                System.out.println("                     '''");
                                System.out.println(colores.CIAN + "\nBienvenido al sistema de tarjetas de membresías, "+admEmpleado.retornarNombre(numSession)+" \n¡Gracias por iniciar sesión!" + colores.RESET);
                                do {
                                System.out.println("\nIngrese una de las siguientes opciones");
                                System.out.println(  colores.PURPURA + "\ta) Ingresar al apartado de gestión\n"
                                                    +"\tb) Ingresar al apartado de venta\n"
                                                    +"\tc) Mostrar estadisticas del sistema\n"
                                                    +"\td) Cerrar la sesion" + colores.RESET
                                );
                                cond = s.nextLine();
                                limpiarConsola();
                                switch (cond.toLowerCase()) {
                                    case "a":
                                        boolean gestionBandera;
                                        do {
                                            gestionBandera = false;
                                            System.out.println(colores.AZUL +"\nSeleccione alguna de las opciones: \n" 
                                                                + "\ta) Gestionar tarjetas de membresías\n"
                                                                + "\tb) Gestionar empleados\n"
                                                                + "\tc) Gestionar miembros\n"
                                                                + "\td) Gestionar niveles de tarjetas de membresías\n"
                                                                + "\tr) Regresar");
                                            cond = s.nextLine();
                                            limpiarConsola();
                                            switch (cond.toLowerCase()) {
                                                case "a":
                                                    boolean membresiasBandera;
                                                    do {
                                                        membresiasBandera = false;
                                                        System.out.println(colores.VERDE +"\nSeleccione alguna de las opciones: \n" 
                                                                    + "\ta) Ver información de todas las tarjetas de membresías\n"
                                                                    + "\tb) Ver información de una tarjeta de membresía en específico\n"
                                                                    + "\tc) Ver actividad una tarjeta de membresía\n"
                                                                    + "\td) Ver el balance de puntos de una tarjeta de membresía\n"
                                                                    + "\tr) Regresar" + colores.AZUL);
                                                        cond = s.nextLine();
                                                        limpiarConsola();
                                                        switch (cond.toLowerCase()) {
                                                            case "a":
                                                                adminTarjetas.imprimirRegistros();
                                                                break;
                                                            case "b":
                                                                adminTarjetas.verRegistro();
                                                                break;
                                                            case "c":
                                                                adminTarjetas.verActividadMembresia();
                                                                break;
                                                            case "d":
                                                                adminTarjetas.verBalancePuntos();
                                                                break;
                                                            case "r":
                                                                membresiasBandera = true;
                                                                break;
                                                            default:
                                                                System.out.println(colores.ROJO + "\nOpción inválida \nFavor de ingresar una de las opciones\n" + colores.RESET);
                                                                break;
                                                        }
                                                    } while (!membresiasBandera);
                                                    break;
                                                
                                                case "b":
                                                    boolean empleadosBandera;
                                                    do {
                                                        empleadosBandera = false;
                                                        System.out.println(colores.CIAN + "\nSeleccione alguna de las opciones: \n" 
                                                                + "\ta) Ver datos de un empleado\n"
                                                                + "\tb) Ver datos de todos los empleados\n"
                                                                + "\tc) Modificar un empleado\n"
                                                                + "\td) Registrar un empleado\n"
                                                                + "\te) Despedir empleado\n"
                                                                + "\tr) Regresar" + colores.AZUL);
                                                        cond = s.nextLine();
                                                        limpiarConsola();
                                                        switch (cond.toLowerCase()) {
                                                            case "a":
                                                                admEmpleado.verUnRegistros();
                                                                break;
                                                            case "b":
                                                                admEmpleado.verRegistros();
                                                                break;
                                                            case "c":
                                                                admEmpleado.updateEmpld();
                                                                break;
                                                            case "d":
                                                                admEmpleado.agregarEmpld();
                                                                break;
                                                            case "e":
                                                                admEmpleado.deleteEmpld();
                                                                break;
                                                            case "f":
                                                                adminEmpld.iniciarsesion();
                                                            case "r":
                                                                empleadosBandera = true;
                                                                break;
                                                            default:
                                                            System.out.println(colores.ROJO + "\nOpción inválida \nFavor de ingresar una de las opciones\n" + colores.RESET);
                                                                break;
                                                        }
                                                    } while (!empleadosBandera);
                                                    break;
                                                
                                                case "c":
                                                    boolean miembrosBandera;
                                                    do {
                                                        miembrosBandera = false;
                                                        System.out.println(colores.PURPURA + "\nSeleccione alguna de las opciones: \n" 
                                                                + "\ta) Ver datos de un miembro\n"
                                                                + "\tb) Ver datos de todos los miembros\n"
                                                                + "\tc) Modificar un miembro\n"
                                                                + "\td) Dar de baja un miembro\n"
                                                                + "\te) Mostrar la(s) tarjeta(s) de membresia de un miembro\n"
                                                                + "\tr) Regresar"+colores.RESET);
                                                        cond = s.nextLine();
                                                        limpiarConsola();
                                                        switch (cond.toLowerCase()) {
                                                            case "a":
                                                                adMiembro.verUnRegistros();
                                                                break;
                                                            case "b":
                                                                adMiembro.verRegistros();
                                                                break;
                                                            case "c":
                                                                adMiembro.updateMiembro();
                                                                break;
                                                            case "d":
                                                                adMiembro.deleteMiemb();
                                                                break;
                                                            case "e":
                                                                adMiembro.mostrarMembCliente();
                                                                break;
                                                            case "r":
                                                                miembrosBandera = true;
                                                                break;
                                                            default:
                                                                System.out.println(colores.ROJO + "\nOpción inválida \nFavor de ingresar una de las opciones\n" + colores.RESET);
                                                                break;
                                                        }
                                                    } while (!miembrosBandera);
                                                    break;
                                                
                                                case "d":
                                                    boolean nivelesBandera;
                                                    do {
                                                        nivelesBandera = false;
                                                        System.out.println(colores.AMARILLO + "\nSeleccione alguna de las opciones: \n" 
                                                                + "\ta) Costo\n"
                                                                + "\tb) Descuento permanente del nivel\n"
                                                                + "\tc) Valor de puntos\n"
                                                                + "\td) Límite de puntos\n"
                                                                + "\tr) Regresar" + colores.AZUL);
                                                        cond = s.nextLine();
                                                        limpiarConsola();
                                                        switch (cond.toLowerCase()) {
                                                            case "a":
                                                                adminNiveles.actualizarCosto();
                                                                break;
                                                            case "b":
                                                                adminNiveles.actualizarDescPermanente();
                                                                break;
                                                            case "c":
                                                                adminNiveles.actualizarValorPunto();
                                                                break;
                                                            case "d":
                                                                adminNiveles.actualizarLimitePuntos();
                                                                break;
                                                            case "r":
                                                                nivelesBandera = true;
                                                                break;
                                                            default:
                                                                System.out.println(colores.ROJO + "\nOpción inválida \nFavor de ingresar una de las opciones\n" + colores.RESET);
                                                                break;
                                                        }
                                                    } while (!nivelesBandera);
                                                    break;
                                                
                                                case "r":
                                                    gestionBandera = true;
                                                    break;
                                                
                                                default:
                                                    System.out.println(colores.ROJO + "\nOpción inválida \nFavor de ingresar una de las opciones\n" + colores.RESET);
                                                    break;
                                            }
                                        } while (!gestionBandera);
                                        System.out.println("" + colores.RESET);
                                        banderadmin = true;
                                        break;
                                        
                                    case "b":
                                    boolean ventasBandera;
                                    do {
                                        ventasBandera = false;
                                        System.out.println(colores.VERDE + "\nSeleccione alguna de las opciones: \n" 
                                            + "\ta) Realizar una nueva venta\n"
                                            + "\tb) Registrar un nuevo miembro\n"
                                            + "\tc) Asignar una nueva tarjeta de membresia\n"
                                            + "\td) Renovar una tarjeta de membresia\n"
                                            + "\te) Cancelar una tarjeta de membresia\n"
                                            + "\tf) Mostrar información de los niveles de tarjetas de membresías\n" 
                                            + "\tr) Regresar" + colores.RESET);
                                        cond = s.nextLine();
                                        limpiarConsola();
                                        switch (cond.toLowerCase()) {
                                            case "a":
                                            boolean nuevaVenta;
                                            do {
                                                adminVentas.registrarCompra();
                                                System.out.println("\nQuieres realizar una nueva venta?");
                                                System.out.println("\ta) Si");
                                                System.out.println("\tb) No");
                                                String respuesta = s.nextLine();
                                                switch (respuesta.toLowerCase()) {
                                                    case "a":
                                                        nuevaVenta = true;
                                                        break;
                                                    case "b":
                                                        nuevaVenta = false;
                                                        break;
                                                    default:
                                                        System.out.println(colores.ROJO + "\nOpción inválida \nFavor de ingresar una de las opciones\n" + colores.RESET);
                                                        nuevaVenta = false;
                                                        break;
                                                }
                                                limpiarConsola();
                                            } while (nuevaVenta);
                                            break;
                                            case "b":
                                                adMiembro.agregarMiemb(numSession);
                                                break;  
                                            case "c":
                                                adminTarjetas.asignarTarjeta(con, numSession, 0);
                                                break;
                                            case "d":
                                                adminTarjetas.renovarTarjeta();
                                                break;
                                            case "e":
                                                adminTarjetas.cancelarTarjeta();
                                                break;
                                            case "f":
                                                boolean nivelesBandera;
                                                do {
                                                    nivelesBandera = false;
                                                    System.out.println(colores.VERDE + "\nSeleccione alguna de las opciones: \n" 
                                                        + "\ta) Ver los beneficios de un nivel de tarjeta de membresia\n"
                                                        + "\tb) Ver precio de un nivel de tarjeta de membresía\n" 
                                                        + "\tc) Imprimir la cantidad de beneficios por nivel\n"
                                                        + "\tr) Regresar" + colores.RESET);
                                                    String nivelesCond = s.nextLine();
                                                    limpiarConsola();
                                                    switch (nivelesCond.toLowerCase()) {
                                                        case "a":
                                                            adminNiveles.imprimirBeneficios();
                                                            break;
                                                        case "b":
                                                            adminNiveles.imprimirPrecios();
                                                            break;
                                                        case "c":
                                                            adminNiveles.imprimirCantBeneficios();
                                                            break;
                                                        case "r":
                                                            nivelesBandera = true;
                                                            break;
                                                        default:
                                                            System.out.println(colores.ROJO + "\nOpción inválida \nFavor de ingresar una de las opciones\n" + colores.RESET);
                                                            break;
                                                    }
                                                } while (!nivelesBandera);
                                                break;
                                            case "r":
                                                ventasBandera = true;
                                                break;
                                            default:
                                                System.out.println(colores.ROJO + "\nOpción inválida \nFavor de ingresar una de las opciones\n" + colores.RESET);
                                                break;
                                        }
                                    } while (!ventasBandera);
                                    banderadmin = true;
                                    break;

                                    case "c":
                                        boolean estadisticaBandera;
                                        do {
                                            estadisticaBandera = false;
                                            System.out.println(colores.VERDE + "\nSeleccione alguna de las opciones: \n" 
                                                                + "\ta) Ver la cantidad de membresías asocidadas por nivel\n"
                                                                + "\tb) Membresías vendidas en un mes determinado\n"
                                                                + "\tc) Cantidad de membresías vendidas por día en un mes determinado\n"
                                                                + "\tr) Regresar" + colores.RESET);
                                            cond = s.nextLine();
                                            limpiarConsola();
                                            switch (cond.toLowerCase()) {
                                                case "a":
                                                    adminNiveles.imprimirCantidad();
                                                    break;
                                                case "b":
                                                    adminTarjetas.verVentasPorMes();
                                                    break;
                                                case "c":
                                                    adminTarjetas.verVentasPorDia();
                                                    break;
                                                case "r":
                                                    estadisticaBandera = true;
                                                    break;
                                                default:
                                                    System.out.println(colores.ROJO + "\nOpción inválida \nFavor de ingresar una de las opciones\n" + colores.RESET);
                                                    break;
                                            }
                                        }while(!estadisticaBandera);
                                        banderadmin = true;
                                        break; 
                                    case "d":
                                        banderalogin = false;
                                        banderadmin = false;
                                        break;
                                    
                                    default:
                                    System.out.println(colores.ROJO + "OPCIÓN INVÁLIDA");
                                    banderadmin = true;
                                    break;
                                }
                            } while (banderadmin);
                            }else{
                                System.out.println(colores.ORO);
                                System.out.println("                      __");
                                System.out.println("                    // \\");
                                System.out.println("                     \\/_/ //");
                                System.out.println("   ''-.._.-''-.._.. -(||)(')");
                                System.out.println("                     '''");
                                System.out.println(colores.AZUL +"\nBienvenido al menú de ventas, " +admEmpleado.retornarNombre(numSession)+" \n¡Gracias por iniciar sesión!" + colores.RESET);
                                boolean ventasBandera;
                                        do {
                                            ventasBandera = false;
                                            System.out.println(colores.VERDE+ "\nSeleccione alguna de las opciones: \n" 
                                                                            + "\ta) Realizar una nueva venta\n"
                                                                            + "\tb) Registrar un nuevo miembro\n"
                                                                            + "\tc) Asignar una nueva tarjeta de membresia\n"
                                                                            + "\td) Renovar una tarjeta de membresia\n"
                                                                            + "\te) Cancelar una tarjeta de membresia\n"
                                                                            + "\tf) Mostrar información de los niveles de tarjeta\n"
                                                                            + "\tr) Cerrar sesión" + colores.RESET);
                                            cond = s.nextLine();
                                            limpiarConsola();
                                            switch (cond.toLowerCase()) {
                                                case "a":
                                                boolean nuevaVenta;
                                                do {
                                                    adminVentas.registrarCompra();
                                                    System.out.println("\nQuieres realizar una nueva venta?");
                                                    System.out.println("\ta) Si");
                                                    System.out.println("\tb) No");
                                                    String respuesta = s.nextLine();
                                                    switch (respuesta.toLowerCase()) {
                                                        case "a":
                                                            nuevaVenta = true;
                                                            break;
                                                        case "b":
                                                            nuevaVenta = false;
                                                            break;
                                                        default:
                                                            System.out.println(colores.ROJO + "\nOpción inválida \nFavor de ingresar una de las opciones\n" + colores.RESET);
                                                            nuevaVenta = false;
                                                            break;
                                                    }
                                                    limpiarConsola();
                                                } while (nuevaVenta);
                                                break;
                                                case "b":
                                                    adMiembro.agregarMiemb(numSession);
                                                    break;  
                                                case "c":
                                                    adminTarjetas.asignarTarjeta(con, numSession, 0);
                                                    break;
                                                case "d":
                                                    adminTarjetas.renovarTarjeta();
                                                    break;
                                                case "e":
                                                    adminTarjetas.cancelarTarjeta();
                                                    break;
                                                case "f":
                                                    boolean nivelesBandera;
                                                    do {
                                                        nivelesBandera = false;
                                                        System.out.println(colores.VERDE + "\nSeleccione alguna de las opciones: \n" 
                                                            + "\ta) Ver los beneficios de un nivel de tarjeta de membresia\n"
                                                            + "\tb) Ver precio de un nivel de tarjeta de membresía\n" 
                                                            + "\tc) Imprimir la cantidad de beneficios por nivel\n"
                                                            + "\tr) Regresar" + colores.RESET);
                                                        String nivelesCond = s.nextLine();
                                                        limpiarConsola();
                                                        switch (nivelesCond.toLowerCase()) {
                                                            case "a":
                                                                adminNiveles.imprimirBeneficios();
                                                                break;
                                                            case "b":
                                                                adminNiveles.imprimirPrecios();
                                                                break;
                                                            case "c":
                                                                adminNiveles.imprimirCantBeneficios();
                                                                break;
                                                            case "r":
                                                                nivelesBandera = true;
                                                                break;
                                                            default:
                                                                System.out.println(colores.ROJO + "\nOpción inválida \nFavor de ingresar una de las opciones\n" + colores.RESET);
                                                                break;
                                                        }
                                                    } while (!nivelesBandera);
                                                    break;
                                                case "r":
                                                    banderalogin = false;
                                                    ventasBandera = true;
                                                    break;
                                                default:
                                                    System.out.println(colores.ROJO + "\nOpción inválida \nFavor de ingresar una de las opciones\n" + colores.RESET);
                                                    break;
                                            }
                                        }while(!ventasBandera);
                                }
                            }
                            }while(banderalogin);
                            bandera = true;
                            break;
                        case "b":
                        try {
                            System.out.print(colores.AZUL + "Cerrando el programa");
                            Thread.sleep(1000);
                            System.out.print(" - ");
                            Thread.sleep(1000);
                            System.out.print(" - ");
                            Thread.sleep(1000);
                            System.out.print(" - \n");
                            Thread.sleep(1000);
                            System.out.print("A");
                            Thread.sleep(400);
                            System.out.print("d");
                            Thread.sleep(400);
                            System.out.print("i");
                            Thread.sleep(400);
                            System.out.print("o");
                            Thread.sleep(400);
                            System.out.print("o");
                            Thread.sleep(400);
                            System.out.print("o");
                            Thread.sleep(400);
                            System.out.print("s");
                            Thread.sleep(500);
                            System.out.print(".");
                            Thread.sleep(500);
                            System.out.print(".");
                            Thread.sleep(500);
                            System.out.print(".");
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                            System.out.println( colores.ORO +
                                                "\n\n       ████  ████                          \r\n" + //
                                                "      ▒▒░░░░▒▒░░░░▒▒                        \r\n" + //
                                                "      ██░░▒▒▒▒░░██                          \r\n" + //
                                                "      ██████▓▓▓▓░░                          \r\n" + //
                                                "    ▓▓░░░░██  ██▓▓                          \r\n" + //
                                                "    ░░    ██  ██                            \r\n" + //
                                                "  ██  ▓▓  ██  ██  ██▓▓██      ▓▓▓▓  ▓▓▓▓    \r\n" + //
                                                "  ██      ██  ██  ██        ██░░░░▓▓░░░░▓▓  \r\n" + //
                                                "    ▒▒    ██  ██▒▒          ██░░▒▒▒▒░░▒▒    \r\n" + //
                                                "    ░░▓▓████▓▓██░░          ██▓▓██▓▓▓▓░░    \r\n" + //
                                                "                          ██    ██  ████    \r\n" + //
                                                "          ████  ████    ██      ██  ▓▓  ██  \r\n" + //
                                                "        ▓▓░░░░▓▓░░░░▓▓  ██  ██  ██  ▓▓  ██▓▓\r\n" + //
                                                "        ██░░██░░░░██    ██      ██  ▓▓  ██  \r\n" + //
                                                "        ██▓▓██▓▓▓▓░░    ░░▓▓    ██  ██▓▓░░  \r\n" + //
                                                "      ██    ██  ██▓▓        ██▓▓██▓▓▓▓      \r\n" + //
                                                "    ██      ██  ██  ██                      \r\n" + //
                                                "    ██  ▓▓  ██  ██  ██▓▓▓▓                  \r\n" + //
                                                "    ██      ██  ██  ██                      \r\n" + //
                                                "    ░░▒▒    ██  ██░░░░                      \r\n" + //
                                                "        ▒▒▒▒██▒▒██"+colores.RESET);
                            bandera = false;
                            break;
                        default:
                            System.out.println(colores.ROJO + "OPCIÓN INVÁLIDA \nFavor de ingresar alguna de nuestras opciones\n" + colores.RESET);
                            bandera = true;
                            break;
                    }
            }while(bandera);
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        s.close();
    }

    public static void limpiarConsola() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}