package clases;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Scanner;
public class tarjetaMembresia {

    private int tarjetaID;
    private String motivo;
    private int puntosActuales, puntosAcumulados, puntosGastados;
    private boolean estatus;
    private int empleado;
    private int miembro;
    private String nivelTarj;
    private pago metodosPago;//instancia de la clase pago

    private boolean bandera;
    private double dineroUsuario;
    private int numeroMembresia;
    private int numeroRenovacionAsignado, numeroCancelacionAsignado;
    private Date fechaCreacion, fechaExpiracion, nuevaFechaExpiracion;
    private int mes, anio;
    
    //Booleano para indicar si el pago se realizo con efectivo. Si es asi, se indica el valor del cambio
    private boolean pagoEfectivo;

    //Scanner
    Scanner r = new Scanner(System.in);

    //////////conexion base de datos
    conexion conexion;

    // Formato decimal para mostrar solo 2 decimales
    DecimalFormat df = new DecimalFormat("0.00");

    SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

    public tarjetaMembresia(conexion conexion){
        this.metodosPago = new pago();//inicializar la clase pago
        this.conexion=conexion;

        
    }/////////////////////
    public tarjetaMembresia(){}
        
    //SELECT 
    public void imprimirRegistros() throws SQLException {
        // Comando para conectar a la BD
        Connection con = conexion.conectar();
        Statement st = con.createStatement();
        // Comando para realizar CONSULTAS 
        ResultSet rs = st.executeQuery("SELECT * FROM tarjetamemb  ");
        System.out.println("\n\t//////////// Tarjetas de membresías ////////////\n");
        while (rs.next()) {
            
            System.out.println("---------------------------------------------------------------");
            System.out.println("Número de tarjeta de membresia: "+rs.getInt("numero")+".");
            System.out.println("Fecha de creación: " + rs.getString("fechaCreacion")+"\nProxima fecha para renovar la membresía: "+rs.getString("fechaExpiracion"));
            System.out.println("El estado de la tarjeta es: " + (rs.getBoolean("estatus") ? "Activa":"Inactiva"));
            System.out.println("Puntos actuales: "+rs.getInt("puntosActuales")+", Puntos acumulados: "+ rs.getInt("puntosAcumulados"));
            System.out.println("Número del empleado que la asignó: "+rs.getInt("empleado"));
            System.out.println("Número del miembro: "+rs.getInt("miembro")+", Nivel de la tarjeta de membresía: "+rs.getString("nivelTarjeta"));
            System.out.println("---------------------------------------------------------------\n\n");
        }
        rs.close();
        st.close();
        con.close();
    }

    ///////////////////////////////////////
    //SELECT de verificar
    public void verRegistro() throws SQLException{
        try{
        Connection con = conexion.conectar();
        Statement st = con.createStatement();
        System.out.println(colores.AZUL+"\n   - - - - CONSULTAR INFORMACION DE MEMBRESIA - - - - - "+colores.RESET);
        System.out.println("\nIngresa el número de la tarjeta de membresía a consultar (o 'r' para cancelar): ");
        do {
            do {
                try {
                    String input = r.next();
                    if (input.equalsIgnoreCase("r")) {
                        System.out.println("Operación cancelada.");
                        return; // Salir del método
                    }
                    numeroMembresia = Integer.parseInt(input);
                    bandera = false;
                } catch (Exception e) {
                    System.out.println("Debe ingresar un valor numérico. Por favor, ingréselo de nuevo");
                    r.nextLine();
                    bandera = true;
                }
            } while (bandera);
            bandera = verificarMembresia(numeroMembresia, false);
        } while (bandera == false);
        
        verificarEstatus(numeroMembresia);
        ResultSet rs = st.executeQuery("SELECT " +
                                        "m.numero AS NumeroCliente, " +
                                        "CONCAT(m.nombre, ' ', m.primerApellido, ' ', IFNULL(CONCAT(m.segundoApellido, ' '),' ')) AS Miembro, " +
                                        "t.numero AS NumeroMembresia, " +
                                        "t.estatus AS Estatus, " +
                                        "t.fechaCreacion AS fechaCreacion, " +
                                        "t.fechaExpiracion AS fechaExpiracion, " +
                                        "t.nivelTarjeta AS NivelTarj, " +
                                        "t.puntosActuales AS PuntosActuales, " +
                                        "t.puntosAcumulados AS PuntosAcumulados, " +
                                        "CONCAT(n.nombre, ' (', n.descripcion, ').') AS nivelTarjeta, " +
                                        "CONCAT(e.nombre, ' ', e.primerApellido, ' ', IFNULL(CONCAT(e.segundoApellido, ' '),' ')) AS Empleado " +
                                        "FROM tarjetamemb AS t " +
                                        "INNER JOIN miembro AS m ON t.miembro = m.numero " +
                                        "INNER JOIN empleado AS e ON t.empleado = e.numero " +
                                        "INNER JOIN niveltarjeta AS n ON t.niveltarjeta = n.codigo " +
                                        "WHERE t.numero ="+numeroMembresia+"  ");
        
        //obtiene el boolean de la base de datos, lo envia a la variable que cree aqui y al imprimir le asigna un String dependiendo del true o false
        if(rs.next()){
            estatus = rs.getBoolean("estatus");
            System.out.println(colores.CIAN);
            System.out.println("--------------------------------------------------------------------");
            System.out.println("\t\t  Datos de la tarjeta de membresia");
            System.out.println("--------------------------------------------------------------------");
            System.out.println(colores.RESET+"Número de la tarjeta de membresia: "+rs.getInt("NumeroMembresia")+".");
            System.out.println("Nivel de la tarjeta de membresía: "+rs.getString("nivelTarjeta"));
            if (verificarCancelada(numeroMembresia)) {
                System.out.println(colores.ROJO+"La tarjeta está CANCELADA."+colores.RESET);
            } else {
                System.out.print("El estado de la tarjeta es: ");
                if(estatus) {
                    System.out.println(colores.VERDE + "Activa" + colores.RESET);
                    System.out.println("Proxima fecha para renovar la membresía: "+rs.getString("fechaExpiracion"));
                } else {
                    System.out.println(colores.ROJO + "Inactiva. Debe renovarse." + colores.RESET);
                }
                System.out.println("Puntos actuales: "+rs.getInt("puntosActuales")+"\nPuntos acumulados desde la creacion de la tarjeta de membresia: "+ rs.getInt("puntosAcumulados"));
            }
            System.out.println("Fecha de creación de la tarjeta: " + rs.getString("fechaCreacion"));
            System.out.println(colores.CIAN+"--------------------------------------------------------------------");
            System.out.println("\t   Datos del titular de la tarjeta de membresia");
            System.out.println("--------------------------------------------------------------------"+colores.RESET);
            System.out.println("Nombre del miembro titular de la tarjeta: "+rs.getString("Miembro"));
            System.out.println("Numero del miembro: "+ rs.getString("NumeroCliente"));
            System.out.println(colores.CIAN+"--------------------------------------------------------------------"+colores.RESET);
            System.out.println("Empleado que la asignó: "+rs.getString("Empleado"));
            System.out.println(colores.CIAN+"--------------------------------------------------------------------"+colores.RESET);
        } else { 
            System.out.println(colores.RESET+"\nNo se encontró la tarjeta de membresía con el número proporcionado"+colores.RESET);
        }

        //Clean console
        try {
            Thread.sleep(2000); // 2000 milisegundos = 2 segundos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println(colores.CIAN+"\n  >>Pulse enter para continuar<<"+colores.RESET);
        r.nextLine();
        r.nextLine();
        limpiarConsola();

        rs.close();
        st.close();
        con.close();
    }
    catch(SQLException e){
        System.err.println("Error al ejecutar la consulta: " + e.getMessage());
        }
    }

    //INSERT METODO LISTO-------------------------------------------
    public void asignarTarjeta(Connection con, int numSession, int numMiembro) {
        try {
            // Instanciar objeto metodos de pago
            double monto = 0.00;
            // Instanciar fechas
            LocalDate fechaActual = LocalDate.now();
            LocalDate fechaExpira = fechaActual.plusYears(1);
            Date fechaCreacion = Date.valueOf(fechaActual);
            Date fechaExpiracion = Date.valueOf(fechaExpira);
    
            empleado = numSession;
            miembro = numMiembro;
    
            System.out.println(colores.AZUL+"\n- - - - ASIGNAR UNA NUEVA TARJETA DE MEMBRESIA - - - - - \n"+colores.RESET);
    
            if (numMiembro == 0) {
                System.out.println("Ingrese el número del miembro a asignar una nueva tarjeta de membresia (o 'r' para cancelar)");
                do {
                    do {
                        try {
                            String input = r.next();
                            if (input.equalsIgnoreCase("r")) {
                                System.out.println("\nOperación cancelada.");
                                return; // Salir del método
                            }
                            miembro = Integer.parseInt(input);
                            bandera = false;
                        } catch (Exception e) {
                            System.out.println("\nDebe ingresar un valor numérico. Por favor, ingréselo de nuevo");
                            bandera = true;
                        }
                    } while (bandera);
                    bandera = verificarMiembro(miembro);
                } while (!bandera);
                r.nextLine();
            }
    
            // Verificar el estado del miembro
            String queryEstado = "SELECT estado FROM miembro WHERE numero = ?";
            try (PreparedStatement psEstado = con.prepareStatement(queryEstado)) {
                psEstado.setInt(1, miembro);
                try (ResultSet rsEstado = psEstado.executeQuery()) {
                    if (rsEstado.next()) {
                        boolean estado = rsEstado.getBoolean("estado");
                        if (!estado) {
                            System.out.println(colores.ROJO + "El miembro no está activo y no puede obtener una tarjeta.");
                            return;
                        }
                    } else {
                        System.out.println("No se encontró el miembro especificado.");
                        return;
                    }
                }
            }
    
            try (Statement st = con.createStatement()) {
                ResultSet rs = st.executeQuery("SELECT numero, fechaRegistro, numTel, nombre, primerApellido, segundoApellido, correo, empleado FROM miembro WHERE numero = " + miembro);
                if (rs.next()) {
                    nivelTarj = seleccionarNivel();
                    if (nivelTarj.equals("CANCEL")) {
                        return; // Salir del método
                    }
    
                    // Obtener el costo de la membresía que se va a asignar
                    rs = st.executeQuery("SELECT * FROM nivelTarjeta WHERE codigo = \"" + nivelTarj + "\"");
                    while (rs.next()) {
                        monto = rs.getDouble("costo");
                    }
    
                    System.out.println("El monto a pagar por la tarjeta de membresía es: $" + monto + " pesos\n");
    
                    // Método de pago
                    System.out.println("\n/////////////////////////////");
                    System.out.println("Seleccione el método de pago:");
                    System.out.println("a. Efectivo \nb. Tarjeta de credito \n *Ingrese otro carácter para cancelar la compra*");
                    System.out.println("/////////////////////////////");
    
                    String cond = r.nextLine().toLowerCase();
                    switch (cond) {
                        case "a":
                            dineroUsuario = metodosPago.pagarConEfectivo(monto);
                            if (dineroUsuario == -1) {
                                bandera = false;
                            } else {
                                bandera = true;
                                pagoEfectivo = true;
                            }
                            break;
    
                        case "b":
                            if (metodosPago.pagarConTarjeta(monto)) {
                                bandera = true;
                                pagoEfectivo = false;
                            } else {
                                bandera = false;
                            }
                            break;
    
                        default:
                            System.out.println("La venta ha sido cancelada");
                            bandera = false;
                            break;
                    }
    
                    if (bandera) {
                        String comando = "INSERT INTO tarjetamemb (numero, fechaCreacion, fechaExpiracion, estatus, puntosActuales, puntosAcumulados, empleado, miembro, nivelTarjeta) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?)";
    
                        try (PreparedStatement ps = con.prepareStatement(comando)) {
                            ps.setDate(1, fechaCreacion);
                            ps.setDate(2, fechaExpiracion);
                            ps.setBoolean(3, true);
                            ps.setInt(4, 0);
                            ps.setInt(5, 0);
                            ps.setInt(6, empleado);
                            ps.setInt(7, miembro);
                            ps.setString(8, nivelTarj);
                            ps.executeUpdate();
                            System.out.println(colores.VERDE + "Se asignó la tarjeta de membresía correctamente \n" + colores.RESET);
                        }
    
                        // Código para imprimir los datos de la última venta realizada
                        comando = "SELECT LAST_INSERT_ID() AS numero";
                        rs = st.executeQuery(comando);
                        if (rs.next()) {
                            int numeroMembresiaAsignada = rs.getInt("numero");
                            System.out.println("-------------------------------------------------");
                            System.out.println("COMPROBANTE DE COMPRA DE LA TARJETA DE MEMBRESÍA");
                            System.out.println("-------------------------------------------------");
                            rs = st.executeQuery("SELECT " +
                                    "m.numero as NumeroCliente, " +
                                    "CONCAT(m.nombre, ' ', m.primerApellido, ' ', IFNULL(CONCAT(m.segundoApellido, ' '), ' ')) as Miembro, " +
                                    "t.numero as NumeroMembresia, " +
                                    "t.nivelTarjeta as NivelTarj, " +
                                    "t.fechaCreacion as fechaCreacion, " +
                                    "t.fechaExpiracion as fechaExpiracion, " +
                                    "CONCAT(e.nombre, ' ', e.primerApellido, ' ', IFNULL(CONCAT(e.segundoApellido, ' '), ' ')) as Empleado " +
                                    "FROM tarjetamemb t " +
                                    "INNER JOIN miembro m ON t.miembro = m.numero " +
                                    "INNER JOIN empleado e ON t.empleado = e.numero " +
                                    "WHERE t.numero = " + numeroMembresiaAsignada);
                            while (rs.next()) {
                                System.out.println("Numero de tarjeta de membresia asignado: " + numeroMembresiaAsignada);
                                System.out.println("Nombre del cliente: " + rs.getString("Miembro"));
                                System.out.println("Numero del cliente: " + rs.getString("NumeroCliente"));
                                nivelTarj = rs.getString("NivelTarj");
                                switch (nivelTarj) {
                                    case "NV1":
                                        System.out.println(colores.BRONCE + "Tarjeta de membresía de nivel básico " + colores.RESET);
                                        break;
    
                                    case "NV2":
                                        System.out.println(colores.PLATA + "Tarjeta de membresía de nivel intermedio " + colores.RESET);
                                        break;
    
                                    case "NV3":
                                        System.out.println(colores.ORO + "Tarjeta de membresía de nivel premium" + colores.RESET);
                                        break;
    
                                    default:
                                        break;
                                }
                                fechaCreacion = rs.getDate("fechaCreacion");
                                System.out.println("Fecha de compra de la tarjeta de \nmembresía: " + fechaCreacion);
                                fechaExpiracion = rs.getDate("fechaExpiracion");
                                System.out.println("Fecha de próxima renovación: " + fechaExpiracion);
                                System.out.println("-------------------------------------------------");
                                if (!pagoEfectivo) {
                                    System.out.println("Total pagado con tarjeta de crédito: $" + df.format(monto) + " pesos");
                                } else {
                                    System.out.println("Total a pagar: " + df.format(monto));
                                    System.out.println("Efectivo: " + df.format(dineroUsuario));
                                    System.out.println("Cambio: " + df.format(dineroUsuario - monto));
                                }
    
                                System.out.println("-------------------------------------------------");
                                System.out.println("Atendido por: " + rs.getString("Empleado"));
                                System.out.println("-------------------------------------------------");
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar: " + e.getMessage());
        }
    }
    

    ///////////////////
    //UPDATE RENOVARTARJETA METODO COMPLETO---------------------------
    public void renovarTarjeta() throws SQLException {
        try {
            Connection con = conexion.conectar();
            Statement st = con.createStatement();
            ResultSet rs;
            // Instanciar objeto metodos de pago
            double monto = 0.00;

            System.out.println("\n- - - - RENOVAR TARJETA DE MEMBRESIA - - - - - ");
            System.out.println("\nSeleccione una tarjeta de membresía a renovar (o 'r' para cancelar): ");
            do {
                bandera = true;
                try {
                    String input = r.next();
                    if (input.equalsIgnoreCase("r")) {
                        System.out.println("Operación cancelada.");
                        return;
                    } else if (input.matches("\\d+")) { // Verificar si es un número
                        numeroMembresia = Integer.parseInt(input);
                        bandera = verificarMembresia(numeroMembresia, true);
                    } else {
                        System.out.println("Debe ingresar un valor numérico. Por favor, ingréselo de nuevo");
                        bandera = false;
                    }
                } catch (Exception e) {
                    System.out.println("Error inesperado. Inténtalo de nuevo.");
                    bandera = false;
                }
            } while (bandera == false);

            if (! verificarCancelada(numeroMembresia)) {
                //Obtener el nivel de la membresia que se va a asignar
                rs = st.executeQuery("SELECT * FROM tarjetamemb WHERE numero = \"" +numeroMembresia+"\"");
                while (rs.next()){
                    nivelTarj = rs.getString("nivelTarjeta");
                }

                //Obtener el costo de la membresia que se va a asignar
                rs = st.executeQuery("SELECT * FROM nivelTarjeta WHERE codigo = \"" +nivelTarj+"\"");
                while (rs.next()){
                    monto = rs.getInt("costo");
                }

                //Instanciar fechas
                LocalDate fechaHoy = LocalDate.now();
                Date fechaActual = Date.valueOf(fechaHoy);

                //Obtener las fechas de las consultas
                rs = st.executeQuery("select*from tarjetaMemb WHERE numero = " +numeroMembresia+"");
                while (rs.next()) {
                fechaCreacion = rs.getDate("fechaCreacion");
                fechaExpiracion = rs.getDate("fechaExpiracion");
                }

                Boolean renovar;

                if (fechaExpiracion.after(fechaActual)) {
                    // Si la fechaExpiracion es mayor que hoy, sumar un año
                    nuevaFechaExpiracion = new Date(fechaExpiracion.getTime() + 31536000000L); // 31536000000L = 1 año en milisegundos
                    renovar = true;
                } else {
                    // Si la fechaExpiracion es menor que hoy, la nueva fechaExpiracion es la fecha de hoy + 1 año
                    nuevaFechaExpiracion = new Date(fechaActual.getTime() + 31536000000L);
                    renovar = true;
                }


                //Si se puede renovar, entonces procede a hacer los pagos
                if (renovar == true){

                    //Pagar la renovacion
                    System.out.println("El monto a pagar por la tarjeta de membresía es: $"+monto+" pesos\n");

                    //Metodo de pago
                    System.out.println("/////////////////////////////");
                    System.out.println("Seleccione el método de pago:");
                    System.out.println("a. Efectivo \nb. Tarjeta de credito\n *Ingrese otro carácter para cancelar la compra*");
                    System.out.println("/////////////////////////////");

                    r.nextLine();
                    String cond = r.nextLine().toLowerCase();
                    switch (cond) {
                        case "a":
                            dineroUsuario = metodosPago.pagarConEfectivo(monto);
                            if (dineroUsuario == -1) {
                                bandera = false;
                            } else {
                                bandera = true;
                                pagoEfectivo = true;
                            }
                            break;

                        case "b":
                            if (metodosPago.pagarConTarjeta(monto)) {
                                bandera = true;
                                pagoEfectivo = false;
                            } else {
                                bandera = false;
                            }
                            break;
                        
                        default:
                            System.out.println("La renovacion ha sido cancelada");
                            bandera = false;
                            break;
                    }

                }

                if (renovar == true & bandera == true){
                    // Actualizar la fechaExpiracion en la BD
                    PreparedStatement ps = con.prepareStatement("UPDATE tarjetamemb SET fechaExpiracion = ? WHERE numero = ?");
                    ps.setDate(1, nuevaFechaExpiracion);
                    ps.setInt(2, numeroMembresia);
                    ps.executeUpdate();

                    String comando = "INSERT INTO renovacion (numero, fechaRenovacion, monto, tarjetaMemb) VALUES (DEFAULT, ?, ?, ?)";
                            ps = con.prepareStatement(comando);
                            ps.setDate(1, fechaActual);
                            ps.setDouble(2, monto);
                            ps.setInt(3, numeroMembresia);
                            ps.executeUpdate();           

                    System.out.println("Tarjeta de membresía renovada con éxito");

                    //Codigo para imprimir los datos de la ultima venta realizada
        
                    comando = "SELECT LAST_INSERT_ID() AS numero";
                    rs = st.executeQuery(comando);
                    if (rs.next()) {
                        System.out.println("\n-----------------------------------------------------");
                        System.out.println("COMPROBANTE DE RENOVACION DE LA TARJETA DE MEMBRESÍA");
                        System.out.println("-----------------------------------------------------");
                        numeroRenovacionAsignado = rs.getInt("numero");
                    }
            
                    rs= st.executeQuery("SELECT " +
                                        "r.numero AS NumeroRenovacion, " +
                                        "CONCAT(m.nombre,' ', m.primerApellido, ' ', IFNULL(CONCAT(m.segundoApellido, ' '),' ')) as Miembro, " +
                                        "t.numero AS NumeroMembresia, " +
                                        "m.numero as NumeroCliente, " +
                                        "t.fechaExpiracion AS fechaExpiracion, " +
                                        "n.nombre as niveltarj " +
                                        "FROM renovacion AS r " +
                                        "INNER JOIN tarjetamemb AS t ON r.tarjetamemb = t.numero " +
                                        "INNER JOIN miembro AS m ON t.miembro = m.numero " +
                                        "inner join niveltarjeta as n on t.niveltarjeta = n.codigo " +
                                        "where r.numero = " +numeroRenovacionAsignado+"");
                    while (rs.next()) {
                        System.out.println("Numero de renovacion: " + numeroRenovacionAsignado);
                            System.out.println("Nombre del cliente: " + rs.getString("Miembro"));
                            System.out.println("Numero del cliente: "+rs.getString("NumeroCliente"));
                            System.out.println("-----------------------------------------------------");
                            System.out.println("Numero de tarjeta de membresia renovada: "+rs.getString("NumeroMembresia"));
                            System.out.println("   -Renovación de membresia por 1 año");                            
                            fechaExpiracion = rs.getDate("fechaExpiracion");
                            System.out.println("Fecha de próxima renovación: " + fechaExpiracion);
                            System.out.println("Nivel de tarjeta renovada: "+ rs.getString("niveltarj"));
                            System.out.println("-----------------------------------------------------");
                            if (pagoEfectivo == false) {
                                System.out.println("Total pagado con tarjeta de\n crédito: $"+df.format(monto)+" pesos");
                            } else {
                                System.out.println("Total a pagar: "+df.format(monto));
                                System.out.println("Efectivo: "+df.format(dineroUsuario));
                                System.out.println("Cambio: "+df.format(dineroUsuario - monto));
                                
                            }
                            System.out.println("-----------------------------------------------------");
                            ps.close();
                        }

                    ps.close();
                } 
            } else {
                System.out.println(colores.ROJO+"Error: La tarjeta está cancelada."+colores.RESET);
            }
            
            st.close();
            con.close();
            
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
        }   
    }

    //CANCELAR
    public void cancelarTarjeta() throws SQLException {
        try{
            LocalDate fechaActual = LocalDate.now();//instanciar e incializar variables a string a fecha
            Date fechaCancelacion = Date.valueOf(fechaActual);

            System.out.println("\n- - - - CANCELAR TARJETA DE MEMBRESIA - - - - - ");

            // Comando para conectar a la BD
            Connection con = conexion.conectar();
            Statement st = con.createStatement();
            System.out.println("Ingresa el código de la tarjeta de membresía a cancelar: ");
            System.out.println(" * Ingrese 'r' para cancelar la operación en cualquier momento");
            do {
                do {
                    try {
                        String input = r.next();
                        if (input.equalsIgnoreCase("r")) {
                            System.out.println("Operación cancelada.");
                            return; // Salir del método
                        }
                        numeroMembresia = Integer.parseInt(input);
                        bandera = false;
                    } catch (Exception e) {
                        System.out.println("Debe ingresar un valor numérico. Por favor, ingréselo de nuevo");
                        r.next();
                        bandera = true;
                    }
                } while (bandera);
                bandera = verificarMembresia(numeroMembresia, true);
            } while (bandera == false);
           
            if(! verificarCancelada(numeroMembresia)){
                System.out.println("Ingresa el motivo de cancelación: ");
                r.nextLine();
                motivo = r.nextLine();
                switch (motivo) {
                    case "r":
                        System.out.println("Se canceló la operación exitosamente.");
                    break;
                
                    default:
                    try {
                        // Actualiza el estado a cancelado de la tarjeta seleccionada
                        String updateComando = "UPDATE tarjetamemb SET estatus = false, fechaExpiracion = ? WHERE numero = ?";
                        PreparedStatement ps = con.prepareStatement(updateComando);
                        ps.setDate(1, fechaCancelacion);
                        ps.setInt(2, numeroMembresia);
                        ps.executeUpdate();
                        // Agrega la tarjeta a la tabla de cancelación
                        String comando = "INSERT INTO cancelacion (numero, fechaCancelacion, motivo, tarjetaMemb) VALUES (DEFAULT, ?, ?, ?)";
                        ps = con.prepareStatement(comando);
                        ps.setDate(1, fechaCancelacion);
                        ps.setString(2, motivo);
                        ps.setInt(3, numeroMembresia);
                        ps.executeUpdate();           
                        System.out.println("\nSe canceló la tarjeta de membresía.");

                        //Codigo para imprimir los datos de la ultima venta realizada
        
                        comando = "SELECT LAST_INSERT_ID() AS numero";
                        ResultSet rs = st.executeQuery(comando);
                        if (rs.next()) {
                            System.out.println("\n-----------------------------------------------------");
                            System.out.println("COMPROBANTE DE CANCELACIÓN DE LA TARJETA DE MEMBRESÍA");
                            System.out.println("-----------------------------------------------------");
                            numeroCancelacionAsignado = rs.getInt("numero");
                        }
                
                        rs= st.executeQuery("SELECT " +
                                            "c.numero AS NumCancelacion, " +
                                            "c.fechaCancelacion AS fecha, " +
                                            "c.tarjetamemb AS tarjeta, " +
                                            "c.motivo AS motivo, " +
                                            "t.numero AS NumTarj, " +
                                            "m.numero as NumMiembro, " +
                                            "CONCAT(m.nombre, ' ', m.primerApellido, ' ', IFNULL(CONCAT(m.segundoApellido, ' '),' ')) AS Miembro " +
                                            "FROM cancelacion AS c " +
                                            "INNER JOIN tarjetamemb AS t ON c.tarjetamemb = t.numero " +
                                            "INNER JOIN miembro AS m ON t.miembro = m.numero " +
                                            "WHERE c.numero = "+numeroCancelacionAsignado);

                        while (rs.next()) {
                            System.out.println("Numero de cancelacion: " + numeroRenovacionAsignado);
                                System.out.println("Nombre del cliente: " + rs.getString("Miembro"));
                                System.out.println("Numero del cliente: "+rs.getString("NumMiembro"));
                                System.out.println("-----------------------------------------------------");
                                System.out.println("Numero de tarjeta de membresia cancelada: "+rs.getString("NumTarj"));
                                System.out.println("Fecha de cancelacion: " + rs.getDate("fecha"));
                                System.out.println("-----------------------------------------------------");
                                ps.close();
                            }
                    } catch (SQLException e) {System.err.println("Error al ejecutar consulta: "+ e.getMessage());}
                    break;
                } 
            } else {
                    System.out.println(colores.ROJO+"Error: La tarjeta YA está cancelada."+colores.RESET);
            }
           st.close();
           con.close();
        }catch(SQLException e){System.err.println("Error al ejecutar consulta: "+ e.getMessage());}
    }

    // CONSULTA #4
    public void verActividadMembresia() throws SQLException {
    try {
        Connection con = conexion.conectar();
        Statement st = con.createStatement();
        ResultSet rs;
        
        System.out.println(colores.AZUL + "\n- - - - MOSTRAR ACTIVIDAD DE TARJETA DE MEMBRESIA - - - - - ");
        System.out.println(colores.RESET + "\nIngresa el número de la tarjeta de membresía a consultar (o 'r' para cancelar): ");
        
        do {
            do {
                try {
                    bandera = false;
                    String input = r.next();
                    if (input.equalsIgnoreCase("r")) {
                        System.out.println(colores.ROJO + "\nOperación cancelada" + colores.RESET);
                        return;
                    }
                    tarjetaID = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println(colores.ROJO + "\nDebe ingresar un valor numérico o 'r'. Por favor, ingréselo de nuevo" + colores.RESET);
                    bandera = true;
                }
            } while (bandera);
            bandera = verificarMembresia(tarjetaID, false);
        } while (!bandera);
        
        // Verificar actividad
        boolean hayActividad = false;

        // Verificar ventas
        rs = st.executeQuery("SELECT 1 FROM TarjetaMemb tm "
                            + "INNER JOIN Venta v ON tm.numero = v.tarjetaMemb "
                            + "WHERE tm.numero = " + tarjetaID);
        if (rs.next()) {
            hayActividad = true;
        }
        rs.close();
        
        // Verificar renovaciones
        rs = st.executeQuery("SELECT 1 FROM TarjetaMemb tm "
                            + "INNER JOIN Renovacion r ON tm.numero = r.tarjetaMemb "
                            + "WHERE tm.numero = " + tarjetaID);
        if (rs.next()) {
            hayActividad = true;
        }
        rs.close();
        
        // Verificar cancelaciones
        rs = st.executeQuery("SELECT 1 FROM TarjetaMemb tm "
                            + "INNER JOIN Cancelacion c ON tm.numero = c.tarjetamemb "
                            + "WHERE tm.numero = " + tarjetaID);
        if (rs.next()) {
            hayActividad = true;
        }
        rs.close();
        
        if (hayActividad) {
            // Consulta para obtener los datos de la tarjeta
            rs = st.executeQuery("SELECT tm.numero AS membresia, m.numero AS cliente, "
                                + "CONCAT(m.nombre, ' ', m.primerApellido, ' ', IFNULL(CONCAT(m.segundoApellido, ' '), '')) AS Miembro "
                                + "FROM TarjetaMemb tm "
                                + "INNER JOIN Miembro m ON tm.miembro = m.numero "
                                + "WHERE tm.numero = " + tarjetaID);
            if (rs.next()) {
                System.out.println(colores.AZUL + "\n\t//////////// ACTIVIDAD DE LA TARJETA DE MEMBRESÍA ////////////\n");
                System.out.println("Membresía:           " + colores.RESET + rs.getInt("membresia"));
                System.out.println(colores.AZUL + "Cliente:             " + colores.RESET + rs.getInt("cliente"));
                System.out.println(colores.AZUL + "Nombre del cliente:  " + colores.RESET + rs.getString("Miembro"));
                System.out.println();
                
                System.out.println(colores.CIAN + "+------------------------+------------------------+" + colores.RESET);
                System.out.println(colores.CIAN + "|" + colores.RESET + "        ACTIVIDAD       " + colores.CIAN + "|" + colores.RESET + "          FECHA         " + colores.CIAN + "|" + colores.RESET);
                System.out.println(colores.CIAN + "+------------------------+------------------------+" + colores.RESET);

                // Consulta para obtener las ventas
                rs = st.executeQuery("SELECT 'Venta' AS actividad, v.fecha AS fecha_actividad "
                                    + "FROM TarjetaMemb tm "
                                    + "INNER JOIN Venta v ON tm.numero = v.tarjetaMemb "
                                    + "WHERE tm.numero = " + tarjetaID);
                while (rs.next()) {
                    System.out.printf(colores.CIAN + "|" + colores.RESET + " %-22s " + colores.CIAN + "|" + colores.RESET + " %-22s " + colores.CIAN + "|\n", rs.getString("actividad"), rs.getDate("fecha_actividad"));
                    System.out.println(colores.CIAN + "+------------------------+------------------------+" + colores.RESET);
                }
                rs.close();
                
                // Consulta para obtener las renovaciones
                rs = st.executeQuery("SELECT 'Renovacion' AS actividad, r.fechaRenovacion AS fecha_actividad "
                                    + "FROM TarjetaMemb tm "
                                    + "INNER JOIN Renovacion r ON tm.numero = r.tarjetaMemb "
                                    + "WHERE tm.numero = " + tarjetaID);
                while (rs.next()) {
                    System.out.printf(colores.CIAN + "|" + colores.RESET + " %-22s " + colores.CIAN + "|" + colores.RESET + " %-22s " + colores.CIAN + "|\n", rs.getString("actividad"), rs.getDate("fecha_actividad"));
                    System.out.println(colores.CIAN + "+------------------------+------------------------+" + colores.RESET);
                }
                rs.close();
                
                // Consulta para obtener las cancelaciones
                rs = st.executeQuery("SELECT 'Cancelacion' AS actividad, c.fechaCancelacion AS fecha_actividad "
                                    + "FROM TarjetaMemb tm "
                                    + "INNER JOIN Cancelacion c ON tm.numero = c.tarjetamemb "
                                    + "WHERE tm.numero = " + tarjetaID);
                while (rs.next()) {
                    System.out.printf(colores.CIAN + "|" + colores.RESET + " %-22s " + colores.CIAN + "|" + colores.RESET + " %-22s " + colores.CIAN + "|\n", rs.getString("actividad"), rs.getDate("fecha_actividad"));
                    System.out.println(colores.CIAN + "+------------------------+------------------------+" + colores.RESET);
                }
                rs.close();
                
            } else {
                System.out.println("No se encontraron datos para la tarjeta de membresía proporcionada");
            }
        } else {
            System.out.println("No hay actividad en esta membresía\n");
        }

        // Limpiar consola
        try {
            Thread.sleep(2000); // 2000 milisegundos = 2 segundos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println(colores.CIAN + "\n  >>Pulse enter para continuar<<" + colores.RESET);
        r.nextLine();
        r.nextLine();
        limpiarConsola();
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    // CONSULTA #5
    public void verBalancePuntos() throws SQLException {
        try {
            Connection con = conexion.conectar();
            Statement st = con.createStatement();

            System.out.println(colores.AZUL+"\n- - - - VER BALANCE DE PUNTOS DE UNA TARJETA DE MEMBRESIA - - - - - "+colores.RESET);
            
            System.out.println("\nIngresa el número de la membresía a consultar (o 'r' para cancelar): ");
            do {
                try {
                    String input = r.next();
                    if (input.equalsIgnoreCase("r")) {
                        System.out.println("Operación cancelada.");
                        return; 
                    } else if (input.matches("\\d+")) {
                        tarjetaID = Integer.parseInt(input);
                        bandera = verificarMembresia(tarjetaID, false);
                        if (bandera == false) {
                        }
                    } else {
                        System.out.println(colores.ROJO + "\nDebe ingresar un valor numérico o 'r'. Por favor, ingréselo de nuevo" + colores.RESET);
                        bandera = false;
                    }
                } catch (Exception e) {
                    System.out.println(colores.ROJO + "\nDebe ingresar un valor numérico o 'r'. Por favor, ingréselo de nuevo" + colores.RESET);
                    bandera = false;
                }
            } while (bandera == false);
            
            ResultSet rs = st.executeQuery("select t.numero as numero, "
                    + "t.puntosActuales as puntosActuales, "
                    + "t.puntosAcumulados - t.puntosActuales as puntosGastados, "
                    + "t.puntosAcumulados as puntosGenerados, "
                    + "CONCAT(m.nombre, ' ', m.primerApellido, ' ', IFNULL(CONCAT(m.segundoApellido, ' '),' ')) AS Miembro "
                    + "from tarjetamemb as t "
                    + "inner join miembro as m on t.miembro = m.numero "
                    + "where t.numero = " + tarjetaID + " ");
            
            if (rs.next()) {
                System.out.println(colores.AZUL+"\n\n\t//////////// BALANCE DE PUNTOS ////////////\n"+colores.RESET);
                System.out.println(colores.AZUL+"Número de la tarjeta de membresía:  "+colores.RESET + tarjetaID);
                System.out.println(colores.AZUL+"Nombre del cliente:                 "+colores.RESET+rs.getString("miembro"));
                puntosAcumulados = rs.getInt("puntosGenerados");
                puntosGastados = rs.getInt("puntosGastados");
                puntosActuales = rs.getInt("puntosActuales");
                System.out.println("\n");
    
                rs = st.executeQuery("SELECT " +
                        "v.numero AS NumVenta, " +
                        "v.puntosUsados AS puntosUsados, " +
                        "v.puntosGanados AS puntosGanados, " +
                        "v.fecha as Fecha, " +
                        "t.numero AS NumTarjeta " +
                        "FROM tarjetamemb AS t " +
                        "INNER JOIN venta AS v ON v.tarjetamemb = t.numero " +
                        "WHERE t.numero = " + tarjetaID);
    
                System.out.println(colores.CIAN + "+------------------------+------------------------+------------------------+------------------------+" + colores.RESET);
                System.out.println(colores.CIAN + "|" + colores.RESET + "      Número Venta      " + colores.CIAN + "|" + colores.RESET + "          Fecha         " + colores.CIAN + "|" + colores.RESET + "     Puntos Ganados     " + colores.CIAN + "|" + colores.RESET + "      Puntos Usados     " + colores.CIAN + "|" + colores.RESET);
                System.out.println(colores.CIAN + "+------------------------+------------------------+------------------------+------------------------+" + colores.RESET);
                
                while (rs.next()) {
                    System.out.printf(colores.CIAN + "|" + colores.RESET + " %-22s " + colores.CIAN + "|" + colores.RESET + " %-22s " + colores.CIAN + "|" + colores.RESET + " %-22s " + colores.CIAN + "|" + colores.RESET + " %-22s " + colores.CIAN + "|\n",
                            rs.getString("NumVenta"),
                            rs.getDate("Fecha"),
                            rs.getString("puntosGanados"), // Nota: parece que habías invertido puntosUsados y puntosGanados
                            rs.getString("puntosUsados"));
                }
    
                System.out.println(colores.CIAN+"+------------------------+------------------------+------------------------+------------------------+"+colores.RESET);
    
                System.out.println(colores.AZUL+"\n                                                   Total de puntos ganados:   " +colores.RESET + (puntosAcumulados));
                System.out.println(colores.AZUL+"                                                   Total de puntos gastados:  " +colores.RESET+ puntosGastados);
                System.out.println(colores.AZUL+"                                                   Puntos actuales:           " +colores.RESET+ puntosActuales);
    
            } else {
                System.out.println("No se encontró la tarjeta de membresía con el número proporcionado");
            }

            //Clean console
            try {
                Thread.sleep(2000); // 2000 milisegundos = 2 segundos
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            System.out.println(colores.CIAN+"\n  >>Pulse enter para continuar<<"+colores.RESET);
            r.nextLine();
            r.nextLine();
            limpiarConsola();

            rs.close();
            st.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
        }
    }

    //Metodo para verificar el estatus de una tarjeta sea valida
    public Boolean verificarEstatus(int numeroMembresia) throws SQLException{
        Connection con = conexion.conectar();
        Statement st = con.createStatement();
        ResultSet rs;

        //Instanciar fechas
        LocalDate fechaHoy = LocalDate.now();
        Date fechaActual = Date.valueOf(fechaHoy);

        //Obtener las fechas de las consultas
        rs = st.executeQuery("select*from tarjetaMemb WHERE numero = " +numeroMembresia+"");
        while (rs.next()) {
            fechaExpiracion = rs.getDate("fechaExpiracion");
        }

        boolean estatus = fechaExpiracion.after(fechaActual);

        //Actualiza el estatus de la tarjeta en la BD
        PreparedStatement ps = con.prepareStatement("UPDATE tarjetamemb SET estatus = ? WHERE numero = ?");
        ps.setBoolean(1, estatus);
        ps.setInt(2, numeroMembresia);
        ps.executeUpdate();

        rs.close();
        st.close();
        con.close();

        if (estatus == true){
            return true;
        } else {
            return false;
        }
    }

    //Codigo para verificar que la membresia ingresada exista. Se utiliza dentro del metodo registrarCompra
    public boolean verificarMembresia(int numeroMembresia, boolean imprimir){
        try {
            Connection con = conexion.conectar();
            // Verificar si el número de tarjeta existe en la base de datos
            PreparedStatement pstmt = con.prepareStatement("SELECT t.numero AS NumeroTarjeta, " +
                     "n.nombre as Niveltarj, "+
                     "t.fechaExpiracion as fechaExp, "+
                     "CONCAT(m.nombre, ' ', " +
                     "m.primerApellido, ' ', " +
                     "IFNULL(CONCAT(m.segundoApellido, ' '),' ')) as Miembro " +
                     "FROM tarjetamemb as t " +
                     "inner join miembro m ON t.miembro = m.numero " +
                     "inner join niveltarjeta as n on t.niveltarjeta = n.codigo"+
                     " WHERE t.numero = ?");
            pstmt.setInt(1, numeroMembresia);
            ResultSet resultado = pstmt.executeQuery();

            if (resultado.next()) {
                // Si número de tarjeta existe, se procede con el registro de la compra
                System.out.println("\nTarjeta encontrada!");
                if (imprimir){
                    System.out.println("\n//////////// CLIENTE ////////////\n");
                    System.out.println(resultado.getString("Miembro"));
                    System.out.println("Nivel de tarjeta: " + resultado.getString("NivelTarj"));
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

    public boolean verificarCancelada(int numeroMembresia) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conexion.conectar();
            ps = con.prepareStatement("select t.numero as NumeroMembresia, c.numero as Cancelacion from cancelacion as c inner join tarjetamemb as t on c.tarjetamemb = t.numero where t.numero =?");
            ps.setInt(1, numeroMembresia);
            rs = ps.executeQuery();
            
            return rs.next(); // Si hay un resultado, significa que la tarjeta está cancelada
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        } finally {
            if (rs!= null) {
                rs.close();
            }
            if (ps!= null) {
                ps.close();
            }
            if (con!= null) {
                con.close();
            }
        }
    }

    //Codigo para verificar que el miembro ingresado exista. Se utiliza dentro del metodo registrarCompra
    public boolean verificarMiembro(int miembro){
        try {
            Connection con = conexion.conectar();
            PreparedStatement pstmt = con.prepareStatement("SELECT CONCAT(nombre, ' ', primerApellido, ' ', IFNULL(CONCAT(segundoApellido, ' '),' ')) AS nombre FROM miembro WHERE numero = ?");
            pstmt.setInt(1, miembro);
            ResultSet resultado = pstmt.executeQuery();

            if (resultado.next()) {
                System.out.println("\n//////////// CLIENTE ////////////\n");
                System.out.println(resultado.getString("nombre")+"\n");
                return true;
            } else {
                System.out.println("Error: El número de miembro no existe, ingréselo de nuevo");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar la base de datos: " + e.getMessage());
            return false;
        }
    }

    //Codigo para verificar que el empleado ingresado exista. Se utiliza dentro del metodo registrarCompra
    public boolean verificarEmpleado(int empleado){
        try {
            Connection con = conexion.conectar();
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM empleado WHERE numero =?");
            pstmt.setInt(1, empleado);
            ResultSet resultado = pstmt.executeQuery();

            if (resultado.next()) {
                return true;
            } else {
                System.out.println("Error: El número de empleado no existe, ingréselo de nuevo");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar la base de datos: " + e.getMessage());
            return false;
        }
    }

    //Seleccionar un nivel de tarjeta
    public String seleccionarNivel(){
        System.out.println("\nSeleccione el nivel de la tarjeta de membresía: ");
        System.out.println(colores.BRONCE+"\ta) Nivel básico");
        System.out.println(colores.PLATA+"\tb) Nivel intermedio");
        System.out.println(colores.ORO+"\tc) Nivel premium");
        System.out.println(colores.RESET+"\tr) Cancelar");
        do { 
            String cond = r.nextLine().toLowerCase();
            if (cond.equals("r")) {
                System.out.println("Operación cancelada.");
                return "CANCEL"; // o cualquier valor que indique que se canceló la selección
            }
            switch (cond) {
                case "a":
                    bandera = true;
                    nivelTarj = "NV1";
                    break;
                    
                case "b":
                    bandera = true;
                    nivelTarj = "NV2";
                    break;
                
                case "c":
                    bandera = true;
                    nivelTarj = "NV3";
                    break;
                
                default:
                    bandera = false;
                    System.out.println("El valor ingresado es incorrecto. Volver a ingresar.");
                    break;
            }
        } while (bandera == false);
        return nivelTarj;
    }

    // CONSULTA #3
    // Ventas de membresías en un mes determinado
    public void verVentasPorMes() throws SQLException{

        System.out.println(colores.AZUL+"\n- - - - VER MEMBRESIAS VENDIDAS EN UN MES - - - - - "+colores.RESET);
        
        // Verificar que se ingrese un mes correcto
        do {
            try {
                System.out.print("\nIngrese el mes a consultar (1-12) o 'r' para cancelar: ");
                String input = r.next();
                if (input.equalsIgnoreCase("r")) {
                    System.out.println("Operación cancelada.");
                    return; // Salir del método
                }
                mes = Integer.parseInt(input);
                if (mes < 1 || mes > 12) {
                    System.out.println(colores.ROJO+"Error: El mes debe ser un número entre 1 y 12."+colores.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(colores.ROJO+"Error: Debe ingresar un número o 'r' para cancelar."+colores.RESET);
            }
        } while (mes < 1 || mes > 12);

        // Validación del año
        do {
            try {
                System.out.print("\nIngrese el año a consultar (o 'r' para cancelar): ");
                String input = r.next();
                if (input.equalsIgnoreCase("r")) {
                    System.out.println("Operación cancelada.");
                    return; // Salir del método
                }
                anio = Integer.parseInt(input);
                if (anio < 2019 || anio > 2024) {
                    System.out.println(colores.ROJO+"Error: El año debe ser entre 2019 y 2024."+colores.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(colores.ROJO+"Error: Debe ingresar un número o 'r' para cancelar."+colores.RESET);
            }
        } while (anio < 2019 || anio > 2024);

        Connection con = conexion.conectar();

    String comando= "SELECT " +
                    "DATE_FORMAT(t.fechaCreacion, \"%d/%m/%Y\") AS fecha, " +
                    "t.numero AS NumeroMembresia, " +
                    "m.numero AS NumeroCliente, " +
                    "CONCAT(m.nombre, ' ', " +
                    "m.primerApellido, ' ', " +
                    "IFNULL(CONCAT(m.segundoApellido, ' '),' ')" +
                    ") AS Miembro " +
                    "from tarjetamemb as t " +
                    "INNER JOIN miembro AS m ON t.miembro = m.numero " +
                    "WHERE MONTH(fechaCreacion) = ? AND YEAR(fechaCreacion) = ?";

        PreparedStatement ps = con.prepareStatement(comando);
        ps.setInt(1, mes);
        ps.setInt(2, anio);
        ResultSet rs = ps.executeQuery();
        boolean bandera = false;
    
        while (rs.next()) {
            bandera = true;
        }
    
        if (bandera) {
            System.out.println(colores.AZUL+"\n\n                              //////////// VENTAS DE MEMBRESÍAS ////////////\n"+colores.RESET);
            System.out.println(colores.CIAN + "+------------------------+------------------------+------------------------+----------------------------------+" + colores.RESET);
            System.out.println(colores.CIAN + "|" + colores.RESET + "         Fecha          " + colores.CIAN + "|" + colores.RESET + "   Numero de membresia  " + colores.CIAN + "|" + colores.RESET + "   Número del cliente   " + colores.CIAN + "|" + colores.RESET + "              Miembro             " + colores.CIAN + "|" + colores.RESET);
            System.out.println(colores.CIAN + "+------------------------+------------------------+------------------------+----------------------------------+" + colores.RESET);

            rs.beforeFirst(); // Volver al principio del ResultSet

            while (rs.next()) {
                System.out.printf(colores.CIAN + "|" + colores.RESET + " %-22s " + colores.CIAN + "|" + colores.RESET + " %-22s " + colores.CIAN + "|" + colores.RESET + " %-22s " + colores.CIAN + "|" + colores.RESET + " %-32s " + colores.CIAN + "|\n",
                        rs.getString("fecha"),
                        rs.getString("NumeroMembresia"),
                        rs.getString("NumeroCliente"),
                        rs.getString("Miembro"));
                System.out.println(colores.CIAN + "+------------------------+------------------------+------------------------+----------------------------------+" + colores.RESET);
            }
        } else {
            System.out.println(colores.ROJO+"\nNo hay registros para mostrar"+colores.RESET);
        }
        
            //Clean console
            try {
                Thread.sleep(2000); // 2000 milisegundos = 2 segundos
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            System.out.println(colores.CIAN+"\n  >>Pulse enter para continuar<<"+colores.RESET);
            r.nextLine();
            r.nextLine();
            limpiarConsola();
    }

    // CONSULTA #10
    // Ventas de membresías por dia
    public void verVentasPorDia() throws SQLException{

        System.out.println(colores.AZUL+"\n- - - - VER CANTIDAD DE MEMBRESIAS VENDIDAS POR DIA - - - - - "+colores.RESET);

        // Verificar que se ingrese un mes correcto
        do {
            try {
                System.out.print("\nIngrese el mes a consultar (1-12) o 'r' para cancelar: ");
                String input = r.next();
                if (input.equalsIgnoreCase("r")) {
                    System.out.println("Operación cancelada.");
                    return; // Salir del método
                }
                mes = Integer.parseInt(input);
                if (mes < 1 || mes > 12) {
                    System.out.println(colores.ROJO+"Error: El mes debe ser un número entre 1 y 12."+colores.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(colores.ROJO+"Error: Debe ingresar un número o 'r' para cancelar."+colores.RESET);
            }
        } while (mes < 1 || mes > 12);

        // Validación del año
        do {
            try {
                System.out.print("\nIngrese el año a consultar (o 'r' para cancelar): ");
                String input = r.next();
                if (input.equalsIgnoreCase("r")) {
                    System.out.println("Operación cancelada.");
                    return; // Salir del método
                }
                anio = Integer.parseInt(input);
                if (anio < 2019 || anio > 2024) {
                    System.out.println(colores.ROJO+"Error: El año debe ser entre 2019 y 2024."+colores.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(colores.ROJO+"Error: Debe ingresar un número o 'r' para cancelar."+colores.RESET);
            }
        } while (anio < 2019 || anio > 2024);

        String comando = "SELECT " +
            "DATE_FORMAT(t.fechaCreacion, \"%d/%m/%Y\") AS fecha, " +
            "COUNT(t.numero) AS cantidad " +
            "FROM tarjetamemb AS t " +
            "WHERE MONTH(fechaCreacion) = ? AND YEAR(fechaCreacion) = ? "+
            "GROUP BY t.fechaCreacion "+
            "ORDER BY fechaCreacion ASC";

        Connection con = conexion.conectar();

        PreparedStatement ps = con.prepareStatement(comando);
        ps.setInt(1, mes);
        ps.setInt(2, anio);
        ResultSet rs = ps.executeQuery();
        boolean bandera = false;

        while (rs.next()) {
            bandera = true;
            break; // Salir del bucle si hay al menos un registro
        }

        if (bandera) {
            System.out.println(colores.CIAN + "\n\n   //////// VENTAS DE MEMBRESÍAS POR DÍA ////////\n" + colores.RESET);
            System.out.println(colores.CIAN + "+------------------------+------------------------+" + colores.RESET);
            System.out.println(colores.CIAN + "|" + colores.RESET + "         Fecha          " + colores.CIAN + "|" + colores.RESET + "    Cantidad de Ventas  " + colores.CIAN + "|" + colores.RESET);
            System.out.println(colores.CIAN + "+------------------------+------------------------+" + colores.RESET);

            rs.beforeFirst(); // Volver al principio del ResultSet

            while (rs.next()) {
                System.out.printf(colores.CIAN + "|" + colores.RESET + " %-22s " + colores.CIAN + "|" + colores.RESET + " %-22s " + colores.CIAN + "|\n",
                        rs.getString("fecha"),
                        rs.getString("cantidad"));
                System.out.println(colores.CIAN + "+------------------------+------------------------+" + colores.RESET);
            }

        } else {
            System.out.println(colores.ROJO+"\nNo hay registros para mostrar"+colores.RESET);
        }
        
        //Clean console
        try {
            Thread.sleep(2000); // 2000 milisegundos = 2 segundos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println(colores.CIAN+"\n  >>Pulse enter para continuar<<"+colores.RESET);
        r.nextLine();
        r.nextLine();
        limpiarConsola();
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