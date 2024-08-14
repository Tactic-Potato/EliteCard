package clases;
import java.sql.*;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class miembro extends persona{
    Scanner s = new Scanner(System.in);
    private String numTel;
    private int empleado;
    boolean exception = false;
    boolean bandera;
    boolean rsnull = false;
    boolean validEmail;
    boolean APvalido = true;
    LocalDate fecha;
    //conexion a la BD
    private conexion conexion;

    tarjetaMembresia adminTarj = new tarjetaMembresia(conexion);

    public miembro(conexion conexion) {
        this.conexion = conexion;
    }
    public void verRegistros() throws SQLException{
        Connection con = conexion.conectar();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM miembro");
        System.out.println("\n\t//////////// DATOS DE LOS MIEMBROS ////////////\n");
        while (rs.next()) {
            System.out.println("---------------------------------------------------------------");
            System.out.println("Número de miembro: "+rs.getInt("numero"));
            System.out.println("Fecha de registro: "+rs.getDate("fechaRegistro"));
            System.out.println("Número de telefono: "+rs.getString("numTel")+" ");
            System.out.println("Nombre: "+rs.getString("nombre")+ " "+rs.getString("primerApellido")+ " "+rs.getString("segundoApellido"));
            String correo = rs.getString("correo");
            if (correo!= null) {
                System.out.println("Correo: "+correo+" ");
            }
            System.out.println("Empleado que lo registró: "+rs.getInt("empleado"));
            System.out.println("Estatus: "+ (rs.getBoolean("estado")? "ACTIVO":"INACTIVO"));
            System.out.println("---------------------------------------------------------------\n\n");
        }
    }

    public void verUnRegistros() throws SQLException{ 
        Connection con = conexion.conectar();
        Statement st = con.createStatement();
        System.out.println(colores.AZUL+"\n- - - - MOSTRAR INFORMACION DE UN MIEMBRO - - - - - "+colores.RESET);
        do {
            try {
                System.out.println("\nIngrese el número del miembro del cual desea ver su información (o 'r' para cancelar): ");
                String input = s.next();
                if (input.equalsIgnoreCase("r")) {
                    System.out.println("Operación cancelada.");
                    return; // Salir del método
                }
                setNumero(Integer.parseInt(input));
                exception = true;
            } catch (NumberFormatException e) {
                System.out.println("\n - - INGRESE UN NÚMERO - - \n");
                exception = false;
            }
        } while (exception == false);
        ResultSet rs = st.executeQuery("SELECT * FROM miembro WHERE numero = '"+getNumero()+"' ");
        if (!rs.next()) {
            System.out.println("\nNO SE ENCONTRÓ UN MIEMBRO CON ESE NÚMERO\n");
        } else {
            do {
                System.out.println(colores.CIAN + "\n+----------------------------------------------------+");
                System.out.println("|                   DATOS ACTUALES                   |");
                System.out.println("+----------------------------------------------------+" + colores.RESET);
                System.out.printf(colores.CIAN + "|" + colores.RESET + " %-50s " + colores.CIAN + "|\n", "Nombre:             " + rs.getString("nombre") + " " + rs.getString("primerApellido") + " " + rs.getString("segundoApellido"));
                System.out.println(colores.CIAN + "+----------------------------------------------------+");
                System.out.printf(colores.CIAN + "|" + colores.RESET + " %-50s " + colores.CIAN + "|\n", "Numero:             " + rs.getInt("numero"));
                System.out.println(colores.CIAN + "+----------------------------------------------------+");
                System.out.printf(colores.CIAN + "|" + colores.RESET + " %-50s " + colores.CIAN + "|\n", "Fecha de registro:  " + rs.getDate("fechaRegistro"));
                System.out.println(colores.CIAN + "+----------------------------------------------------+");
                System.out.printf(colores.CIAN + "|" + colores.RESET + " %-50s " + colores.CIAN + "|\n", "Numero de telefono: " + rs.getString("numTel"));
                String correo = rs.getString("correo");
                if (correo != null) {
                    System.out.printf(colores.CIAN + "|" + colores.RESET + " %-50s " + colores.CIAN + "|\n", "Correo:             " + correo);
                }
                System.out.println(colores.CIAN + "+----------------------------------------------------+");
                System.out.printf(colores.CIAN + "|" + colores.RESET + " %-50s " + colores.CIAN + "|\n", "Estatus:            " + (rs.getBoolean("estado") ? "ACTIVO" : "INACTIVO"));
                System.out.println(colores.CIAN + "+----------------------------------------------------+");
            } while (rs.next());
        }

        //Clean console
        try {
            Thread.sleep(2000); // 2000 milisegundos = 2 segundos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\n  >>Pulse enter para continuar<<");
        s.nextLine();
        s.nextLine();
        limpiarConsola();
    }
    
    public void agregarMiemb(int numEmpld) throws SQLException {
        Connection con = conexion.conectar();
        Statement st = con.createStatement();
        this.empleado = numEmpld;
        System.out.println(colores.AZUL+"\n- - - - AÑADIR MIEMBRO - - - - - "+colores.RESET);
        do {
            System.out.println("Ingrese el nombre de pila: ");
            System.out.println(" * Ingrese 'r' en cualquier momento para cancelar *");
            setNombre(s.nextLine());
            if (getNombre().equalsIgnoreCase("r")) {
                System.out.println("Operación cancelada.");
                return;
            } else if (getNombre().isEmpty()) {
                System.out.println(colores.ROJO + "Error: El nombre no puede estar vacío." + colores.RESET);
            } else if (!super.noContieneNum(getNombre())) {
                System.out.println(colores.ROJO + "Error: El nombre no debe contener números." + colores.RESET);
                setNombre("");
            } else if (!super.longitud(getNombre())) {
                System.out.println(colores.ROJO + "Error: El nombre debe tener más de 3 letras." + colores.RESET);
                setNombre(""); // Reiniciar el nombre para repetir la solicitud
            }
        } while (getNombre().isEmpty());

        do {
            System.out.println("Ingrese el primer apellido: ");
            setPrimer_AP(s.nextLine());
            if (getPrimer_AP().equalsIgnoreCase("r")) {
                System.out.println("Operación cancelada.");
                return;
            } else if (getPrimer_AP().isEmpty()) {
                System.out.println(colores.ROJO + "Error: El primer apellido no puede estar vacío."  + colores.RESET);
            } else if (!super.noContieneNum(getPrimer_AP())) {
                System.out.println(colores.ROJO + "Error: El primer apellido no debe contener números." + colores.RESET);
                setPrimer_AP("");
            }else if (!super.longitud(getPrimer_AP())) {
                System.out.println(colores.ROJO + "Error: El apellido debe tener más de 3 letras." + colores.RESET);
                setPrimer_AP(""); // Reiniciar el nombre para repetir la solicitud
            }
        } while (getPrimer_AP().isEmpty());
    
        do {
            System.out.println("Ingrese el segundo apellido (puede estar vacio): ");
            setSegundo_AP(s.nextLine());
            if (getSegundo_AP().equalsIgnoreCase("r")) {
                System.out.println("Operación cancelada.");
                return;
            } else if (!super.noContieneNum(getSegundo_AP())) {
                System.out.println(colores.ROJO + "Error: El segundo apellido no debe contener números."  + colores.RESET);
                setSegundo_AP("");
                APvalido = false;
            } else if (getSegundo_AP().isEmpty()) {
                APvalido = true;
            }
        } while (!APvalido);

        do{
            do {
                System.out.println("Ingrese el correo: ");
                setCorreo(s.nextLine());
                if (getCorreo().equalsIgnoreCase("r")) {
                    System.out.println("Operación cancelada.");
                    return;
                } else if (getCorreo().isEmpty()) {
                    System.out.println(colores.ROJO + "Error: El correo no puede estar vacío."  + colores.RESET);
                }
            } while (getCorreo().isEmpty());

            if (super.validarEmail(getCorreo())) {
                validEmail = true;
            }else{
                System.err.println(colores.ROJO + "ERROR correo invalido" + colores.RESET);
                validEmail= false;
            }
        }while(validEmail == false);

        do {
            try {
                System.out.println("\nIngrese el número de teléfono: ");
                String numTel = s.next();  
                s.nextLine(); // Limpiar el buffer del Scanner
                setNumTel(numTel);
                
                if (numTel.equalsIgnoreCase("r")) {
                    System.out.println("\nOperación cancelada.");
                    return;
                }
                // Validar que la longitud del número de teléfono sea de al menos 10 dígitos
                if (numTel.length()!= 10) {
                    System.out.println(colores.ROJO+"\nEl número de teléfono debe tener 10 dígitos. Inténtalo de nuevo."+colores.RESET);
                    exception = false;
                } else {
                    // Validar que todos los caracteres sean dígitos
                    if (numTel.matches("\\d+")) {
                        exception = true;
                    } else {
                        System.out.println(colores.ROJO+"\nEl número de teléfono solo debe contener dígitos. Inténtalo de nuevo."+colores.RESET);
                        exception = false;
                    }
                }
            } catch (Exception e) {
                System.out.println(colores.ROJO + "\n - - INGRESE UN NÚMERO VÁLIDO - - \n" + colores.RESET);
                s.nextLine(); // Limpiar el buffer del Scanner
                exception = false;
            }

        } while (!exception);

        LocalDate dateNow = LocalDate.now();
        Date fechaRegistro = Date.valueOf(dateNow);
        String comando = "INSERT INTO miembro(fechaRegistro, numTel, nombre, primerApellido, segundoApellido, correo, empleado) values ('" +fechaRegistro+ "','" + getNumTel() + "','"+getNombre()+"','"+getPrimer_AP()+"','"+getSegundo_AP()+"','"+getCorreo()+"', '"+empleado+"')";
        st.executeUpdate(comando);
        limpiarConsola();
        System.out.println(colores.VERDE+"\n - - - MIEMBRO REGISTRADO DE FORMA EXITOSA - - - \n"+colores.RESET);
        comando = "SELECT LAST_INSERT_ID() AS numero";
                ResultSet rs = st.executeQuery(comando);
                int numMiembro;
                if (rs.next()) {
                    System.out.println("-------------------------------------------------");
                    System.out.println("      COMPROBANTE DE REGISTRO DEL MIEMBRO        ");
                    System.out.println("-------------------------------------------------");
                    numMiembro = rs.getInt("numero");
                    System.out.println("Número de miembro asignado: " + numMiembro);
                    System.out.println("-------------------------------------------------");

                    rs= st.executeQuery("SELECT " +
                                        "fechaRegistro as fecha, " +
                                        "CONCAT (m.nombre,' ', m.primerApellido, ' ', IFNULL(CONCAT(m.segundoApellido, ' '),' ')) as Miembro, " +
                                        "CONCAT (e.nombre,' ', e.primerApellido, ' ', IFNULL(CONCAT(e.segundoApellido, ' '),' ')) as Empleado " +
                                        "FROM miembro as m " +
                                        "INNER JOIN empleado as e ON m.empleado = e.numero " +
                                        "WHERE m.numero = " + numMiembro);
                    
                    while (rs.next()) {
                        System.out.println("Nombre del cliente: " + rs.getString("Miembro"));
                        System.out.println("\nFecha de registro del cliente: "+rs.getDate("fecha"));
                        System.out.println("-------------------------------------------------");
                        System.out.println("Empleado que lo registró: \n" + rs.getString("Empleado"));
                        System.out.println("-------------------------------------------------\n");
                    }

                    System.out.println("Desea adquirir una tarjeta de membresia para el miembro registrado?");
                    System.out.println("a. Si");
                    System.out.println("b. No");

                    String respuesta = s.nextLine().trim().toLowerCase();
                    if (respuesta.equals("a")){
                        adminTarj.asignarTarjeta(con, empleado, numMiembro);
                    }
                }
    }

    public void updateMiembro() throws SQLException {
        Connection con = conexion.conectar();
        Statement st = con.createStatement();
        System.out.println("\n- - - - MODIFICAR MIEMBRO - - - - - ");
        do {
            try {
                System.out.println("Ingrese el numero del miembro a modificar: ");
                System.out.println(" * Ingrese 'r' para cancelar en cualquier momento");
                String input = s.next();
                if (input.equalsIgnoreCase("r")) {
                    System.out.println("Operación cancelada.");
                    return; // Salir del método
                } else if (input.matches("\\d+")) {
                    setNumero(Integer.parseInt(input));
                    exception = true;
                } else {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.println(colores.ROJO + "\nDebe ingresar un valor numérico o 'r'. Por favor, ingréselo de nuevo" + colores.RESET);
                s.nextLine();
            }
        } while (!exception);
        ResultSet rs = st.executeQuery("SELECT * FROM miembro WHERE numero = '" + getNumero() + "' ");
        if (!rs.next()) {
            System.out.println("\nNO SE ENCONTRO UN MIEMBRO CON ESE NUMERO\n");
        } else {
            System.out.println("\n\t//////////// DATOS ACTUALES ////////////\n");  
            do {
                System.out.println("-------------------------------------------------------------");
                System.out.println("Numero: "+rs.getInt("numero"));
                System.out.println("Fecha de registro: "+rs.getDate("fechaRegistro"));
                System.out.println("Numero de telefono: "+rs.getString("numTel")+" ");
                System.out.println("Nombre: "+rs.getString("nombre")+ " "+rs.getString("primerApellido")+ " "+rs.getString("segundoApellido"));
                String correo = rs.getString("correo");
                if (correo!= null) {
                    System.out.println("Correo: "+correo+" ");
                }
                System.out.println("Empleado que lo registro: "+rs.getInt("empleado"));
                System.out.println("Estatus: "+ (rs.getBoolean("estado") ? "ACTIVO":"INACTIVO"));
                System.out.println("-------------------------------------------------------------\n");
            } while (rs.next()); 
            boolean banderaOP = true;
            String op;
                do {
                    do {
                        try {
                        System.out.println("Seleccione el campo que desea modificar:");
                        System.out.println("1. Correo electrónico");
                        System.out.println("2. Número de teléfono");
                        System.out.println("3. Estatus");
                        int opcion = s.nextInt();
                        s.nextLine(); // Limpiar el buffer del Scanner
                        String comando;
                        switch (opcion) {
                            case 1:
                                do{
                                    do {
                                        System.out.println("Ingrese el correo: ");
                                        setCorreo(s.nextLine());
                                        if (getCorreo().equalsIgnoreCase("r")) {
                                            System.out.println("Operación cancelada.");
                                            return;
                                        } else if (getCorreo().isEmpty()) {
                                            System.out.println(colores.ROJO + "Error: El correo no puede estar vacío."  + colores.RESET);
                                        }
                                    } while (getCorreo().isEmpty());
                        
                                    if (super.validarEmail(getCorreo())) {
                                        validEmail = true;
                                    }else{
                                        System.err.println(colores.ROJO + "ERROR correo invalido" + colores.RESET);
                                        validEmail= false;
                                    }
                                }while(validEmail == false);
                                
                                
                                comando = "UPDATE miembro SET " + "correo = '" + getCorreo() + "' " +
                                        "WHERE numero = " + getNumero();
                                st.executeUpdate(comando);
                                System.out.println(colores.VERDE + "\n - - - CORREO ACTUALIZADO - - - \n" + colores.RESET);
                                System.out.println("\n¿Desea modificar otro parametro? (a. Si \nOtra tecla. No)");
                                op = s.nextLine();
                                if (op.toLowerCase().equals("a")) {
                                    banderaOP = true;
                                }else{
                                    banderaOP = false;
                                }
                                limpiarConsola();
                                break;
                            case 2:
                                do {
                                    try {
                                        System.out.println("Ingrese el nuevo número de teléfono: ");
                                        String numTel = s.next();
                                        s.nextLine(); // Limpiar el buffer del Scanner
                                        if (numTel.equalsIgnoreCase("r")) {
                                            System.out.println("Operación cancelada.");
                                            return; // Salir del método
                                        }
                                        if (numTel.length() < 10) {
                                            System.out.println(colores.ROJO + "El número de teléfono debe tener al menos 10 dígitos. Inténtalo de nuevo." + colores.RESET);
                                            exception = false;
                                        } else {
                                            if (numTel.matches("\\d+")) {
                                                exception = true;
                                            } else {
                                                System.out.println(colores.ROJO + "El número de teléfono solo debe contener dígitos. Inténtalo de nuevo."+ colores.RESET);
                                                exception = false;
                                            }
                                        }
                                    } catch (Exception e) {
                                        System.out.println(colores.ROJO + "\n - - INGRESE UN NÚMERO VÁLIDO - - \n" + colores.RESET);
                                        s.nextLine(); // Limpiar el buffer del Scanner
                                        exception = false;
                                    }
                                } while (!exception);
                                comando = "UPDATE miembro SET " + "numTel = '" + numTel + "' " +
                                        "WHERE numero = " + getNumero();
                                        st.executeUpdate(comando);
                                System.out.println(colores.VERDE + "\n - - - NUMERO DE TELEFONO ACTUALIZADO - - - \n" + colores.RESET);
                                System.out.println("\n¿Desea modificar otro parametro? (a. Si \nOtra tecla. No)");
                                op = s.nextLine();
                                if (op.toLowerCase().equals("a")) {
                                    banderaOP = true;
                                }else{
                                    banderaOP = false;
                                }
                                limpiarConsola();
                                break;
                            case 3:
                                do {
                                    System.out.println("Ingrese el estado del miembro (a.Activo - b.Despedido): ");
                                    op = s.nextLine();
                                    if (op.equalsIgnoreCase("r")) {
                                        System.out.println("Operación cancelada.");
                                        return; // Salir del método
                                    }
                                    switch (op.toLowerCase()) {
                                        case "a":
                                            comando = "UPDATE miembro SET estado = 1 WHERE numero = '"+getNumero()+"'";
                                            st.executeUpdate(comando);
                                            exception = false;
                                            break;
                                        case "b":
                                            comando = "UPDATE miembro SET estado = 0 WHERE numero = '"+getNumero()+"'";
                                            st.executeUpdate(comando);
                                            exception = false;
                                            break;
                                    
                                        default:
                                            System.out.println(colores.ROJO + "OPCION INVALIDA" + colores.RESET);
                                            exception = true;
                                            break;
                                    }
                                } while (exception);
                                System.out.println("\n - - - MIEMBRO MODIFICADO- - - \n");
                                System.out.println("\n¿Desea modificar otro parametro? (a. Si \nOtra tecla. No)");
                                op = s.nextLine();
                                if (op.toLowerCase().equals("a")) {
                                    banderaOP = true;
                                }else{
                                    banderaOP = false;
                                }
                                s.nextLine();
                                limpiarConsola();
                                break;
                            default:
                                System.out.println(colores.ROJO + "Opción inválida. Por favor seleccione una opción válida." + colores.RESET);
                                banderaOP = true;
                                break;
                            }
                        exception = true;
                        } catch (InputMismatchException e) {
                            System.out.println(colores.ROJO + "FAVOR DE INGRESAR UN NUMERO" + colores.RESET);
                            s.nextLine();
                            banderaOP = true;
                        }
                    } while (!exception);
                } while (banderaOP);
                limpiarConsola();
        }
    }

    public void deleteMiemb() throws SQLException{
        Connection con = conexion.conectar();
        Statement st = con.createStatement();
        System.out.println(colores.ROJO + "- - - -  Dar de baja a un miembro - - - - - " + colores.AZUL);
        do {
            try {
                System.out.println("Ingrese el número del miembro a dar de baja (o 'r' para cancelar la operacion): ");
                String input = s.next();
                if (input.equalsIgnoreCase("r")) {
                    return;
                }
                setNumero(Integer.parseInt(input));
                exception = true;
            } catch (InputMismatchException e) {
                System.out.println(colores.ROJO + "Favor de ingresar un número o 'r'" + colores.RESET);
                s.next();
                exception = false;
            }
        } while (!exception);
        ResultSet rs = st.executeQuery("SELECT * FROM miembro WHERE numero = '" + getNumero() + "' ");
        if (!rs.next()) {
            System.out.println(colores.ROJO + "\nNUMERO DE MIEMBRO INEXISTENTE\n" + colores.RESET);
            s.nextLine();
        } else {
            System.out.println(colores.ROJO + "Miembro a dar de baja: ");
            System.out.println("Nombre: "+rs.getString("nombre")+ " "+rs.getString("primerApellido")+ " "+rs.getString("segundoApellido") + colores.RESET);
            System.out.println(colores.AMARILLO + "¿Desea continuar? (a.Si - Cualquier otra tecla (CANCELAR))" + colores.RESET);
            s.nextLine();
            String op = s.nextLine();
            if (op.toLowerCase().equals("a")) {
                String comando = "UPDATE miembro SET estado = 0 WHERE numero = '"+getNumero()+"'";
                st.executeUpdate(comando);
                System.out.println(colores.VERDE + " - - - MIEMBRO DADO DE BAJA EXITOSAMENTE - - - " + colores.RESET);
            }
        }
    }

    // CONSULTA #1
    public void mostrarMembCliente() throws SQLException {
        // Comando para conectar a la BD
        Connection con = conexion.conectar();
        Statement st = con.createStatement();
    
        System.out.println("\n- - - - MOSTRAR TARJETAS DE MEMBRESIAS - - - - - ");
        System.out.println("\nIngresa el numero del miembro a consultar: ");
        do {
            do {
                try {
                    bandera = false;
                    setNumero(s.nextInt());
                } catch (Exception e) {
                    System.out.println("Debe ingresar un valor numerico. Por favor, ingreselo de nuevo");
                    s.next();
                    bandera = true;
                }
            } while (bandera);
            bandera = verificarMiembro(getNumero());
        } while (bandera == false);
    
        // Comando para realizar CONSULTAS
        ResultSet rs = st.executeQuery("SELECT m.numero AS NumeroMiembro, "
                + "CONCAT(m.nombre, ' ', m.primerApellido, ' ', IFNULL(CONCAT(m.segundoApellido, ' '), ' ')) AS Miembro "
                + "FROM miembro AS m "
                + "where m.numero=" + getNumero() + "  ");
    
        if (rs.next()) {
            System.out.println("\nNúmero del cliente: " + rs.getInt("NumeroMiembro"));
            System.out.println("Nombre del cliente: " + rs.getString("Miembro"));
            System.out.println();
    
            // Consulta para obtener las membresías asociadas
            rs = st.executeQuery("SELECT t.numero AS NumeroMemb, "
                    + "DATE_FORMAT(t.fechaCreacion, \"%d/%m/%Y\") AS fechaInicio, "
                    + "DATE_FORMAT(t.fechaExpiracion, \"%d/%m/%Y\") AS fechaFinal, "
                    + "n.descripcion AS descripcion "
                    + "FROM miembro AS m "
                    + "INNER JOIN tarjetamemb AS t ON t.miembro = m.numero "
                    + "INNER JOIN niveltarjeta AS n ON t.niveltarjeta = n.codigo "
                    + "where m.numero=" + getNumero() + "  ");
    
            if (rs.next()) {
                System.out.println("\n                           //////////// TARJETAS ASOCIADAS ////////////\n");
                System.out.println("+-----------------------+-----------------------+-----------------------+-----------------------+");
                System.out.println("|  Número de membresía  |    Nivel de tarjeta   |    Fecha de inicio    |  Fecha de expiracion  |");
                System.out.println("+-----------------------+-----------------------+-----------------------+-----------------------+");
    
                do {
                    System.out.printf("| %-21s | %-21s | %-21s | %-21s |\n", rs.getString("NumeroMemb"), rs.getString("descripcion"), rs.getString("fechaInicio"), rs.getString("fechaFinal"));
                    System.out.println("+-----------------------+-----------------------+-----------------------+-----------------------+");
                } while (rs.next());

                System.out.println();
            } else {
                System.out.println(colores.ROJO+"\nNo hay membresías asociadas a este cliente\n"+colores.RESET);
            }
        } else {
            System.out.println("\nNo se encontraron resultados para el cliente\n " /*+ numeroCliente*/);
        }

        //Clean console
        try {
            Thread.sleep(2000); // 2000 milisegundos = 2 segundos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\n  >>Pulse enter para continuar<<");
        s.nextLine();
        s.nextLine();
        limpiarConsola();
    
        rs.close();
        st.close();
        con.close();
    }

    //Codigo para verificar que el miembro ingresado exista. Se utiliza dentro del metodo registrarCompra
    public boolean verificarMiembro(int miembro){
        try {
            Connection con = conexion.conectar();
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM miembro WHERE numero =?");
            pstmt.setInt(1, miembro);
            ResultSet resultado = pstmt.executeQuery();

            if (resultado.next()) {
                System.out.println("Cliente encontrado!");
                return true;
            } else {
                System.out.println("Error: El número de miembro no existe, ingreselo de nuevo");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar la base de datos: " + e.getMessage());
            return false;
        }
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public String getNumTel() {
        return numTel;
    }
}