package clases;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Scanner;

//      -- Atributos MySQL --
//  Venta (numero, fecha, total, puntosUsados, totalBeneficio, IVA, subtotalBeneficio, tarjetaMemb, nivelTarjeta)
//  TarjetaMemb (numero, duracion, fechaCreacion, fechaActiva, fechaExpiracion, estatus, puntosActuales, puntosAcumulados, empleado, miembro, nivelTarjeta)
//  NivelTarjeta (codigo, nombre, descripcion, color, costo, descuentoPermanente, limitePuntos, gananciaPuntos, valorPunto)

public class ventas extends nivelDeTarjeta {
    Scanner s = new Scanner(System.in);
    private boolean bandera;
    private int puntosUsados;

    //Atributos necesarios de la tabla TARJETAMEMB (para almacenar a quien se le va a registrar la venta)
    private int numeroMembresia;
    private int puntosActuales;
    private int puntosAcumulados;
    private String nivelTarjeta;

    //Atributos necesarios de la tabla VENTA
    private double monto;
    private double subtotal;
    private double IVA;
    private double total;
    private int puntosGanados;
    private int numeroVenta;
    private double dineroUsuario;

    //Booleano para indicar si el pago se realizo con efectivo. Si es asi, se indica el valor del cambio
    private boolean pagoEfectivo;

    //OJO. Objeto para implementar la clase PAGO
    pago pagos = new pago();
    tarjetaMembresia adminTarjeta = new tarjetaMembresia(getConexion());

    //Objeto para implementar la clase CONEXION
    conexion conexion = getConexion();

    // Formato decimal para mostrar solo 2 decimales
    DecimalFormat df = new DecimalFormat("0.00");

    public ventas (conexion conexion) {
        super(conexion);
    }

    public ventas(){

    }
    
    //Metodo para registrar una compra
    public void registrarCompra() throws SQLException {

        // Comando para conectar a la BD
        Connection con = conexion.conectar();
        Statement st = con.createStatement();

        // Comando para realizar CONSULTAS (en este caso es para consultar los datos de la tabla del nivel de la tarjeta)

        System.out.println(colores.AZUL+"\n//////////// COMPRA ////////////\n"+colores.RESET);
        System.out.println("Ingrese el numero de la tarjeta de membresia a realizar la compra: ");
        System.out.println(" * Ingrese 'r' para cancelar la operación en cualquier momento");
        do {
            do {
                try {
                    String input = s.next();
                    if (input.equalsIgnoreCase("r")) {
                        System.out.println("Operación cancelada.");
                        return; // Salir del método
                    }
                    numeroMembresia = Integer.parseInt(input);
                    bandera = false;
                } catch (Exception e) {
                    System.out.println("Debe ingresar un valor numérico. Por favor, ingréselo de nuevo");
                    bandera = true;
                }
            } while (bandera);
            bandera = verificarMembresia(numeroMembresia);
        } while (bandera == false);

        if (adminTarjeta.verificarEstatus(numeroMembresia) &&! adminTarjeta.verificarCancelada(numeroMembresia)) {
            char respuesta;
            do {
                System.out.println("\n¿Desea continuar con la compra? Ingrese:");
                System.out.println("\ta) Si. \n\tr) No, regresar");
                respuesta = s.next().charAt(0);
                if (respuesta != 'a' && respuesta != 'A' && respuesta != 'r' && respuesta != 'R') {
                    System.out.println("Opción inválida. Por favor, ingrese 'a' o 'r'.");
                }
            } while (respuesta != 'a' && respuesta != 'A' && respuesta != 'r' && respuesta != 'R');

            if (respuesta == 'r' || respuesta == 'R') {
                System.out.println("Operación cancelada");
                return;
            }
            
            System.out.println("\nIngrese el monto de la compra: ");
            do {
                try {
                    String input = s.next();
                    if (input.equalsIgnoreCase("r")) {
                        System.out.println("Operación cancelada.");
                        return; // Salir del método
                    }
                    monto = Double.parseDouble(input);
                    if (monto <= 0) {
                        System.out.println("El monto debe ser mayor a cero. Por favor, ingréselo de nuevo");
                    } else {
                        bandera = true;
                    }
                } catch (Exception e) {
                    System.out.println("Debe ingresar un valor numérico. Por favor, ingréselo de nuevo");
                    bandera = false;
                }
            } while (bandera == false);
    
            //Obtener el total de puntos del usuario
            ResultSet rs = st.executeQuery("SELECT * FROM tarjetaMemb where numero = \"" +numeroMembresia+"\"");
    
            while (rs.next()) {
                nivelTarjeta = rs.getString("nivelTarjeta");
                puntosActuales = rs.getInt("puntosActuales");
                puntosAcumulados = rs.getInt("puntosAcumulados");
            }
    
            System.out.println("\nEl usuario cuenta con " +puntosActuales+  " puntos");
            if (puntosActuales==0){
                System.out.println("");
                s.nextLine();
            } else {
                do {
                    try {
                        bandera = true;
                        System.out.println("Ingrese la cantidad de puntos a usar (o 'r' para cancelar la compra):");
                        String input = s.next();
                        s.nextLine();
                        if (input.equalsIgnoreCase("r")) {
                            System.out.println("Compra cancelada");
                            // Aquí puedes agregar código para cancelar la venta
                            return; // o break; dependiendo de la lógica de tu método
                        } else {
                            puntosUsados = Integer.parseInt(input);
                            if (puntosUsados > puntosActuales) {
                                System.out.println("No tiene suficientes puntos para realizar la compra");
                                bandera = false;
                            } else if (puntosUsados < 0){
                                System.out.println("No puede usar puntos negativos");
                                bandera = false;
                            }
                        }
                    } catch (Exception e) {
                        bandera = false;
                        
                        System.out.println("Debe ingresar un valor numerico. Por favor, ingreselo de nuevo");
                    }
                } while (bandera == false);
            }
            
            total = monto - (monto * (super.obtenerDescuentoPermanente(nivelTarjeta) / 100)) - (puntosUsados * (super.obtenerValorPunto(nivelTarjeta) / 100));
            if (total < 0){
                total = 0;
            }

            IVA = total * 0.16;
            subtotal = total - IVA;
            puntosGanados = (int) (total * (super.obtenerGananciaPuntos(nivelTarjeta) / 100));
    
            // Validar que los puntos ganados no excedan el limite de puntos de la tarjeta
            if (puntosActuales - puntosUsados + puntosGanados > super.obtenerLimitePuntos(nivelTarjeta)){
                puntosGanados = super.obtenerLimitePuntos(nivelTarjeta) - puntosActuales + puntosUsados;
            }
    
            puntosActuales = puntosActuales - puntosUsados + puntosGanados;
    
            // Verificar que la suma de los puntos actuales no supere el limite de puntos
            if (puntosActuales > super.obtenerLimitePuntos(nivelTarjeta)){
                puntosActuales = super.obtenerLimitePuntos(nivelTarjeta);
            }
    
            // Verificar que no haya puntos negativos para la tarjeta
            puntosAcumulados = puntosAcumulados + puntosGanados;
            
            if (total < 0){
                total = 0;
            }
            System.out.println(colores.VERDE+"\nEl total de la compra se ha reducido a $"+df.format(total)+" pesos"+colores.RESET);
            if (total > 0){
                System.out.println("\n/////////////////////////////");
            System.out.println("Seleccione el metodo de pago:");
            System.out.println("a. Efectivo \nb. Tarjeta de credito \n *Seleccione otra opcion para cancelar la compra*");
            System.out.println("/////////////////////////////");
            String cond = s.nextLine().toLowerCase();
            switch (cond) {
                case "a":
                    dineroUsuario = pagos.pagarConEfectivo(total);
                    if (dineroUsuario == -1) {
                        bandera = false;
                    } else {
                        bandera = true;
                        pagoEfectivo = true;
                    }
                    break;

                case "b":
                    if (pagos.pagarConTarjeta(total)) {
                        bandera = true;
                        pagoEfectivo = false;
                    } else {
                        bandera = false;
                    }
                    break;
                
                default:
                    System.out.println(colores.ROJO+"La compra ha sido cancelada"+colores.RESET);
                    bandera = false;
                    break;
            }
            } else {
                System.out.println("Sus beneficios hicieron que la compra sea gratis ! ! !");
                bandera = true;
            }
    
            if (bandera == true){
    
                String comando =  "insert into Venta (numero, fecha, total, puntosUsados, puntosGanados, totalBeneficio, IVA, subtotalBeneficio, tarjetaMemb, nivelTarjeta) VALUES (DEFAULT, DATE(NOW()), '"+monto+"', '"+puntosUsados+"', '"+puntosGanados+"', '"+total+"', '"+IVA+"', '"+subtotal+"', '"+numeroMembresia+"', '"+nivelTarjeta+"')";
                st.executeUpdate(comando);
                st.executeUpdate("UPDATE tarjetaMemb SET puntosActuales = " + puntosActuales + " WHERE numero = " +numeroMembresia+"");
                st.executeUpdate("UPDATE tarjetaMemb SET puntosAcumulados = " + puntosAcumulados + " WHERE numero = " +numeroMembresia+"");
                System.out.println("La compra se realizo correctamente");
        
                //Codigo para imprimir los datos de la ultima venta realizada
        
                comando = "SELECT LAST_INSERT_ID() AS numero";
                rs = st.executeQuery(comando);
                if (rs.next()) {
                    System.out.println("\n---------------------------------------");
                    System.out.println("\t  TICKET DE COMPRA\n\n");
                    numeroVenta = rs.getInt("numero");
                    
                }

                // Imprimir datos del establecimiento
                rs = st.executeQuery("select * from establecimiento where codigo = \"EST\"");
                while (rs.next()) {
                    System.out.println(rs.getString("nombre"));
                    System.out.println(rs.getString("calle")+", "+rs.getString("numero")+", "+rs.getString("colonia")+", "+rs.getString("codigoPostal"));
                    System.out.println(rs.getString("ciudad"));
                    System.out.println("---------------------------------------");
                }
        
                rs = st.executeQuery("select " +
                    "m.numero as NumeroCliente, " +
                    "CONCAT (m.nombre,' ', m.primerApellido, ' ', IFNULL(CONCAT(m.segundoApellido, ' '),' ')) as Miembro, " +
                    "t.numero as NumeroMembresia, " +
                    "v.fecha as fecha " +
                    "from venta as v " +
                    "inner join tarjetamemb as t on v.tarjetamemb = t.numero " +
                    "inner join miembro as m on t.miembro = m.numero " +
                    "where v.numero = " +numeroVenta+"");

                while (rs.next()) {
                    System.out.println("Número de compra: " + numeroVenta);
                    System.out.println("---------------------------------------");
                    System.out.println("Numero de tarjeta de memb.: " + numeroMembresia);
                    System.out.println("Numero de cliente: " + rs.getString("numeroCliente"));
                    System.out.println("Cliente: "+ rs.getString("Miembro"));
                    Date fechaVenta = rs.getDate("fecha");
                    System.out.println("---------------------------------------");
                    System.out.println("Fecha de compra: " + fechaVenta);
                    System.out.println("Monto Total (sin beneficios): $" + monto+" \npesos");
                    System.out.println("Subtotal: $"+df.format(subtotal)+" pesos");
                    System.out.println("IVA: $"+df.format(IVA)+" pesos");
                    if (pagoEfectivo == false) {
                        System.out.println("Total pagado con tarjeta de \ncredito: $"+df.format(total)+" pesos");
                    } else {
                        System.out.println("Total a pagar: $"+df.format(total)+" pesos");
                        System.out.println("Efectivo: $"+df.format(dineroUsuario)+" pesos");
                        System.out.println("Cambio: $"+df.format(dineroUsuario - total)+" pesos");
    
                    }
                    
                    System.out.println("---------------------------------------");
                    System.out.println("Beneficios aplicados: ");
                    System.out.println("Descuento permanente del "+super.obtenerDescuentoPermanente(nivelTarjeta)+"%");
                    System.out.println("Puntos usados: "+puntosUsados+" puntos");
                    System.out.println("Puntos ganados por la compra: "+puntosGanados+ " puntos");
                    System.out.println( "Puntos actuales: "+puntosActuales+ " puntos");
                    System.out.println("En total ahorraste $"+ df.format((monto - total))+" pesos");
                    System.out.println("---------------------------------------\n");
                }
            }
        } else {
            if (adminTarjeta.verificarCancelada(numeroMembresia)) {
                System.out.println(colores.ROJO+"Error: La tarjeta está cancelada."+colores.RESET);
            } else {
                System.out.println(colores.ROJO+"La tarjeta está vencida y necesita renovación."+colores.RESET);
            }
        }
    }

    //Codigo para verificar que la membresia ingresada exista. Se utiliza dentro del metodo registrarCompra
    public boolean verificarMembresia(int numeroMembresia){
        try {
            Connection con = conexion.conectar();
            // Verificar si el número de tarjeta existe en la base de datos
            PreparedStatement pstmt = con.prepareStatement("SELECT t.numero AS NumeroTarjeta, " +
                     "n.nombre as Niveltarj, "+
                     "t.fechaExpiracion as fechaExp, "+
                     "CONCAT(m.nombre, ' ', " +
                     "m.primerApellido, ' ', " +
                     "IFNULL(CONCAT(m.segundoApellido, ' '), ' ')) as Miembro " +
                     "FROM tarjetamemb as t " +
                     "inner join miembro m ON t.miembro = m.numero " +
                     "inner join niveltarjeta as n on t.niveltarjeta = n.codigo"+
                     " WHERE t.numero = ?");
            pstmt.setInt(1, numeroMembresia);
            ResultSet resultado = pstmt.executeQuery();

            if (resultado.next()) {
                // Si número de tarjeta existe, se procede con el registro de la compra
                boolean cancelada = adminTarjeta.verificarCancelada(numeroMembresia);
                System.out.println("Tarjeta encontrada.");
                System.out.println("\n//////////// CLIENTE ////////////\n");
                System.out.println(resultado.getString("Miembro"));
                System.out.println("Nivel de tarjeta: " + resultado.getString("NivelTarj"));
                if (cancelada){
                    System.out.println();
                    } else {
                    System.out.println("Valida hasta: "+ resultado.getDate("fechaExp")+"\n");
                }
                return true;
            } else {
                System.out.println("Error: El número de tarjeta no existe, ingreselo de nuevo");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar la base de datos: " + e.getMessage());
            return false;
        }
    }
}