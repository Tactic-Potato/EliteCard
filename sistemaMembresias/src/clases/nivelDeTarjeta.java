package clases;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class nivelDeTarjeta {

    Scanner s = new Scanner(System.in);
    private boolean bandera;
    private String nivelTarjeta;
    private double costo;
    private double valorPunto;
    private double descuentoPermanente;
    private double gananciaPuntos;
    private int limitePuntos;

    // Se debe declarar un atributo de la clase conexion
    private conexion conexion;

    public nivelDeTarjeta(conexion conexion) {
        this.conexion = conexion;
    }

    public nivelDeTarjeta(){

    }

    // CONSULTA #2
    // Metodo para imprimir los diferentes niveles de tarjeta con sus beneficios. Los datos los recoge de la BD.
    public void imprimirBeneficios() throws SQLException {

        // Comando para conectar a la BD
        Connection con = conexion.conectar();
        Statement st = con.createStatement();

        System.out.println(colores.AZUL+ "\n- - - - BENEFICIOS DE UN NIVEL DE TARJETA - - - - - "+colores.RESET);

        // Comando para realizar CONSULTAS (en este caso es para consultar los datos de la tabla del nivel de la tarjeta)
        nivelTarjeta = seleccionarNivel();
        if (nivelTarjeta == "CANCEL") {
            return; // Salir del método
        }
        ResultSet rs = st.executeQuery("SELECT * FROM nivelTarjeta WHERE codigo = \"" +nivelTarjeta+"\"");

        String color = "";
        switch (nivelTarjeta) {
            case "NV1":
                color = colores.BRONCE;
                break;
            case "NV2":
                color = colores.PLATA;
                break;
            case "NV3":
                color = colores.ORO;
                break;
        }

        System.out.println(color + "\n\t//////////// DETALLES DEL NIVEL ////////////\n");
        while (rs.next()) {
            System.out.println(color + "---------------------------------------------------------------");
            System.out.println(color + "Nivel de tarjeta de membresia: "+rs.getString("nombre")+" ("+rs.getString("descripcion")+").");
            System.out.println(color + "Costo de $"+rs.getString("costo")+" pesos anuales.");
            System.out.println(color + "---------------------------------------------------------------");
            System.out.println(color + ">>Beneficios");
            System.out.println(color + "Descuento permanente del "+obtenerDescuentoPermanente(nivelTarjeta)+"%");
            System.out.println(color + "Ofrece una ganancia de puntos del "+obtenerGananciaPuntos(nivelTarjeta)+"% del total de cada compra.");//ganacia de puntos
            System.out.println(color + "Cada punto equivale a "+obtenerValorPunto(nivelTarjeta)+" centavos.");
            System.out.println(color + " *Limite de "+obtenerLimitePuntos(nivelTarjeta)+" puntos por tarjeta de membresia*");
            System.out.println(color + "---------------------------------------------------------------");
        }

        System.out.println(colores.RESET); // Resetear el color

        rs.close();
        st.close();
        con.close();
    }

    /* 
    // CONSULTA #6
    public void imprimirMismosBeneficios() throws SQLException{
        // Comando para conectar a la BD
        Connection con = conexion.conectar();
        
        
        try {
            ResultSet rs;
            PreparedStatement pstmt = con.prepareStatement("SELECT b.descripcion, n.nombre, b.cantidad " +
                     "FROM niveltarjeta n INNER JOIN beneficio b ON b.niveltarjeta = n.codigo " +
                     "ORDER BY b.descripcion, n.nombre"); 

            System.out.println(" Beneficios por Nivel ");
            System.out.println("---------------------");
            System.out.println("+----------+--------+--------+--------+");
            System.out.println("| Beneficio | Bronce | Plata  | Oro    |");
            System.out.println("+----------+--------+--------+--------+");

            String beneficioAnterior = "";
            while (pstmt.executeQuery().next()) {
                String beneficio = rs.getString("descripcion");
                String nivel = rs.getString("nombre");
                int cantidad = rs.getInt("cantidad");

                if (!beneficio.equals(beneficioAnterior)) {
                    System.out.print("| " + beneficio + " |");
                    beneficioAnterior = beneficio;
                }

                if (nivel.equals("Bronce")) {
                    System.out.printf(" %6d |", cantidad);
                } else if (nivel.equals("Plata")) {
                    System.out.printf(" %6d |", cantidad);
                } else if (nivel.equals("Oro")) {
                    System.out.printf(" %6d |", cantidad);
                }
            }
            System.out.println();
            System.out.println("+----------+--------+--------+--------+");
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
        }
    }
    */

    // CONSULTA #7
    public void imprimirPrecios() throws SQLException {
        // Comando para conectar a la BD
        Connection con = conexion.conectar();
        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery("SELECT * FROM nivelTarjeta ");

        System.out.println("\n\t//////////// NIVELES DE TARJETA ////////////\n");
        while (rs.next()) {
            System.out.println("---------------------------------------------------------------");
            System.out.println("Nivel de tarjeta de membresia: "+rs.getString("nombre")+" ("+rs.getString("descripcion")+").");
            System.out.println("Costo de $"+rs.getString("costo")+" pesos anuales. ");
            System.out.println("---------------------------------------------------------------\n"+colores.RESET);
        }

        //Clean console
        try {
            Thread.sleep(2000); // 2000 milisegundos = 2 segundos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println(colores.CIAN+"\n  >>Pulse enter para continuar<<"+colores.RESET);
        s.nextLine();
        limpiarConsola();

        rs.close();
        st.close();
        con.close();
        
    }

    // CONSULTA #8
    public void imprimirCantidad() throws SQLException{
        Connection con = conexion.conectar();
        Statement st = con.createStatement();
    
        ResultSet rs = st.executeQuery("SELECT nt.nombre, nt.descripcion, COUNT(tm.numero) AS total "
                                        + "FROM NivelTarjeta as nt "
                                        + "INNER JOIN TarjetaMemb as tm ON nt.codigo = tm.nivelTarjeta "
                                        + "GROUP BY nt.nombre, nt.descripcion ");
    
        System.out.println(colores.AZUL + "\n   //////////// NIVELES DE TARJETAS ////////////\n" + colores.RESET);
        System.out.println(colores.CIAN + "+------------------------+------------------------+" + colores.RESET);
        System.out.println(colores.CIAN + "|" + colores.RESET + "    Nivel de tarjeta    " + colores.CIAN + "|" + colores.RESET + "    Total Membresias    " + colores.CIAN + "|" + colores.RESET);
        System.out.println(colores.CIAN + "+------------------------+------------------------+" + colores.RESET);
        
        while (rs.next()) {
            System.out.printf(colores.CIAN + "|" + colores.RESET + " %-22s " + colores.CIAN + "|" + colores.RESET + " %-22s " + colores.CIAN + "|\n",
                    rs.getString("nombre"),
                    rs.getString("total"));
            System.out.println(colores.CIAN + "+------------------------+------------------------+" + colores.RESET);
        }

        //Clean console
        try {
            Thread.sleep(2000); // 2000 milisegundos = 2 segundos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println(colores.CIAN+"\n  >>Pulse enter para continuar<<"+colores.RESET);
        s.nextLine();
        limpiarConsola();
    
        rs.close();
        st.close();
        con.close();
    }

    // CONSULTA #9
    public void imprimirCantBeneficios() throws SQLException{
        Connection con = conexion.conectar();
        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery( "SELECT " +
                                        "nt.nombre AS nivel, " +
                                        "COUNT(CASE WHEN b.cantidad <> 0 THEN 1 ELSE NULL END) AS cantidad " +
                                        "FROM " +
                                        "NivelTarjeta AS nt " +
                                        "INNER JOIN Beneficio AS b ON nt.codigo = b.nivelTarjeta " +
                                        "GROUP BY " +
                                        "nt.nombre"
        );

        System.out.println("\n\t//////////// NIVELES DE TARJETAS ////////////\n");
        while (rs.next()) {
            System.out.println("---------------------------------------------------------------");
            System.out.println("Nivel de tarjeta de membresia: "+rs.getString("nivel")+".");
            int cantidad = rs.getInt("cantidad");
            System.out.println("Total de beneficios del nivel: " + cantidad);
        }
        System.out.println(" ");

        rs.close();
        st.close();
        con.close();

    }

    public String seleccionarNivel(){
        System.out.println("\nSeleccione el nivel de la tarjeta de membresía: ");
        System.out.println(colores.BRONCE+"\ta) Nivel básico");
        System.out.println(colores.PLATA+"\tb) Nivel intermedio");
        System.out.println(colores.ORO+"\tc) Nivel premium");
        System.out.println(colores.RESET+"\tr) Cancelar");
        do { 
            String cond = s.nextLine().toLowerCase();
            if (cond.equals("r")) {
                System.out.println("Operación cancelada.");
                return "CANCEL"; // o cualquier valor que indique que se canceló la selección
            }
            switch (cond) {
                case "a":
                    bandera = true;
                    nivelTarjeta = "NV1";
                    break;
                    
                case "b":
                    bandera = true;
                    nivelTarjeta = "NV2";
                    break;
                
                case "c":
                    bandera = true;
                    nivelTarjeta = "NV3";
                    break;
                
                default:
                    bandera = false;
                    System.out.println("El valor ingresado es incorrecto. Volver a ingresar.");
                    break;
            }
        } while (bandera == false);
        return nivelTarjeta;
    }

    //Metodo para actualizar el costo de la membresia
    public void actualizarCosto() throws SQLException {

        nivelTarjeta = seleccionarNivel();

        if (nivelTarjeta == "CANCEL") {
            return; // Salir del método
        }

        System.out.println("Ingrese el nuevo valor para el costo de la tarjeta de membresia seleccionada: ");
        do {
            try{
                bandera = false;
                costo = s.nextDouble();
            } catch (InputMismatchException ex) {
                  System.out.println("Error. Debe ingresar obligatoriamente un valor numérico");
                  s.next();
                  bandera=true;
                }
        } while (bandera);
        

        //Cada metodo requiere su propia conexion y statement
        Connection con = conexion.conectar();
        Statement st = con.createStatement();

        //OJO, para realizar una actualizacion de datos se usa el comando UPDATE 
        String comando = "UPDATE nivelTarjeta SET costo = " + costo + " WHERE codigo = \"" +nivelTarjeta+"\"";

        //OJO, el metodo statement para la ACTUALIZACION correcta es EXECUTE UPDATE
        st.executeUpdate(comando);

        System.out.println(" Se actualizo correctamente");

        st.close();
        con.close();
    }

    //Metodo para actualizar el limite de puntos del nivel de la tarjeta
    public void actualizarLimitePuntos() throws SQLException {

        nivelTarjeta = seleccionarNivel();

        if (nivelTarjeta == "CANCEL") {
            return; // Salir del método
        }


        System.out.println("Ingrese el nuevo valor para el limite de puntos de la tarjeta de membresia seleccionada: ");

        do {
            try{
                bandera = false;
                limitePuntos = s.nextInt();
            } catch (InputMismatchException ex) {
                  System.out.println("Error. Debe ingresar obligatoriamente un valor numérico");
                  s.next();
                  bandera=true;
                }
        } while (bandera);

        Connection con = conexion.conectar();
        Statement st = con.createStatement();
        String comando = "UPDATE beneficio SET cantidad = " + limitePuntos + " WHERE codigo = 'LP" + nivelTarjeta + "'";

        st.executeUpdate(comando);

        System.out.println(" Se actualizo correctamente");

        st.close();
        con.close();
    }

    public void actualizarValorPunto() throws SQLException {

        nivelTarjeta = seleccionarNivel();

        if (nivelTarjeta == "CANCEL") {
            return; // Salir del método
        }

        System.out.println("Ingrese el nuevo valor para el valor de un punto de la tarjeta de membresia seleccionada: ");
        System.out.println("(Ojo, cada digito equivale a 1 centavo. P. ej: 50 = 50 centavos)");
        do {
            try{
                bandera = false;
                valorPunto = s.nextInt();
            } catch (InputMismatchException ex) {
                  System.out.println("Error. Debe ingresar obligatoriamente un valor numérico");
                  s.next();
                  bandera=true;
                }
        } while (bandera);

        Connection con = conexion.conectar();
        Statement st = con.createStatement();
        String comando = "UPDATE beneficio SET cantidad = " + valorPunto + " WHERE codigo = 'VP" + nivelTarjeta + "'";

        st.executeUpdate(comando);

        System.out.println(" Se actualizo correctamente");

        st.close();
        con.close();
    }

    public void actualizarDescPermanente() throws SQLException {

        nivelTarjeta = seleccionarNivel();

        if (nivelTarjeta == "CANCEL") {
            return; // Salir del método
        }

        System.out.println("Ingrese el nuevo valor para el descuento permanente del nivel de tarjeta de membresia seleccionada: ");
        System.out.println("(Ojo, cada digito equivale a 1 porciento. P. ej: 10 = 10% de descuento permanente)");
        do {
            try{
                bandera = false;
                descuentoPermanente = s.nextInt();
            } catch (InputMismatchException ex) {
                  System.out.println("Error. Debe ingresar obligatoriamente un valor numérico");
                  s.next();
                  bandera=true;
                }
        } while (bandera);
        
        Connection con = conexion.conectar();
        Statement st = con.createStatement();
        String comando = "UPDATE beneficio SET cantidad = " + descuentoPermanente + " WHERE codigo = 'DP" + nivelTarjeta + "'";

        st.executeUpdate(comando);

        System.out.println(" Se actualizo correctamente");

        st.close();
        con.close();
    }

    public double obtenerGananciaPuntos(String nivelTarjeta) throws SQLException{
        Connection con = conexion.conectar();
        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery("SELECT cantidad FROM beneficio WHERE codigo = 'GP" + nivelTarjeta + "'");
        if (rs.next()) {
            gananciaPuntos = rs.getDouble("cantidad");
        }
        return gananciaPuntos;
    }

    public int obtenerLimitePuntos(String nivelTarjeta) throws SQLException{
        Connection con = conexion.conectar();
        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery("SELECT cantidad FROM beneficio WHERE codigo = 'LP" + nivelTarjeta + "'");
        if (rs.next()) {
            limitePuntos = rs.getInt("cantidad");
        }

        return limitePuntos;
    }

    public double obtenerValorPunto(String nivelTarjeta) throws SQLException{
        Connection con = conexion.conectar();
        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery("SELECT cantidad FROM beneficio WHERE codigo = 'VP" + nivelTarjeta + "'");
        if (rs.next()) {
            valorPunto = rs.getDouble("cantidad");
        }

        return valorPunto;
    }

    public double obtenerDescuentoPermanente(String nivelTarjeta) throws SQLException{
        Connection con = conexion.conectar();
        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery("SELECT cantidad FROM beneficio WHERE codigo = 'DP" + nivelTarjeta + "'");
        if (rs.next()) {
            descuentoPermanente = rs.getDouble("cantidad");
        }

        return descuentoPermanente;
    }

    public void setLimitePuntos(int limitePuntos) {
        this.limitePuntos = limitePuntos;
    }

    public void setGananciaPuntos(double gananciaPuntos) {
        this.gananciaPuntos = gananciaPuntos;
    }

    public void setDescuentoPermanente(double descuentoPermanente) {
        this.descuentoPermanente = descuentoPermanente;
    }

    public void setValorPunto(double valorPunto) {
        this.valorPunto = valorPunto;
    }

    public void setConexion(conexion conexion) {
        this.conexion = conexion;
    }

    public int getLimitePuntos() {
        return limitePuntos;
    }

    public double getGananciaPuntos() {
        return gananciaPuntos;
    }

    public double getDescuentoPermanente() {
        return descuentoPermanente;
    }

    public double getValorPunto() {
        return valorPunto;
    }

    public conexion getConexion() {
        return conexion;
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

    public boolean verificarMembresia(int numeroMembresia) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verificarMembresia'");
    }

    
}