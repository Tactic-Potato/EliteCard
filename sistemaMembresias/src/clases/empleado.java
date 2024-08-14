package clases;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern; 

public class empleado extends persona {
    Scanner s = new Scanner(System.in);
    private String contrasenia;
    private String puesto;
    boolean exception = false;
    boolean estado;
    boolean validEmail;
    // conexión a la BD
    private conexion conexion;

    public empleado(conexion conexion) {
        this.conexion = conexion;
    }

    public void verRegistros() throws SQLException { // Método para ver todos los registros de empleados
        Connection con = conexion.conectar();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM empleado");
        System.out.println(colores.CIAN + "\n\t//////////// DATOS DE LOS EMPLEADOS ////////////\n" + colores.RESET);
        while (rs.next()) {
            System.out.println("---------------------------------------------------------------");
            System.out.println("Número del empleado: " + rs.getInt("numero"));
            System.out.println("Puesto del empleado: " + rs.getString("puesto"));
            System.out.println(colores.AMARILLO + " - - - DATOS PERSONALES - - - " + colores.RESET);
            System.out.println("Correo: " + rs.getString("correo") + " ");
            System.out.println("Contraseña: " + rs.getString("contrasenia") + " ");
            System.out.println("Nombre: " + rs.getString("nombre") + " " + rs.getString("primerApellido") + " " + rs.getString("segundoApellido"));
            System.out.println("Estatus: "+ (rs.getBoolean("estado") ? "ACTIVO":"INACTIVO"));
            System.out.println("---------------------------------------------------------------\n\n");
        }
    }

    public void verUnRegistros() throws SQLException { // Método para ver un registro de empleado
        Connection con = conexion.conectar();
        Statement st = con.createStatement();

        System.out.println(colores.AZUL+"\n- - - - VER DATOS DE EMPLEADO - - - - - "+colores.RESET);
        do {
            try {
                System.out.println("\nIngrese el número del empleado del cual desea ver su información (o 'r' para cancelar): ");
                String input = s.next();
                if (input.equalsIgnoreCase("r")) {
                    limpiarConsola();
                    System.out.println("\nOperación cancelada.");
                    s.nextLine();
                    return;
                } else if (input.matches("\\d+")) { // Verificar si es un número
                    setNumero(Integer.parseInt(input));
                    exception = true;
                    s.nextLine();
                } else {
                    System.out.println(colores.ROJO + "\nFavor de ingresar un número o 'r'" + colores.RESET);
                    exception = false;
                    s.nextLine();
                }
            } catch (Exception e) {
                System.out.println("Error inesperado. Inténtalo de nuevo.");
                exception = false;
            }
        } while (!exception);

        ResultSet rs = st.executeQuery("SELECT * FROM empleado WHERE numero = '" + getNumero() + "' ");
        if (!rs.next()) {
            System.out.println(colores.ROJO + "\nNO SE ENCONTRÓ UN EMPLEADO CON ESE NÚMERO\n" + colores.RESET);
        } else {
            do {
                System.out.println(colores.CIAN + "\n+----------------------------------------------+");
                System.out.println("|                DATOS ACTUALES                |");
                System.out.println("+----------------------------------------------+" + colores.RESET);
                String puesto = rs.getString("puesto");
                String nombrePuesto;
                if (puesto.equals("PT1")) {
                    nombrePuesto = "Gerente";
                } else if (puesto.equals("PT2")) {
                    nombrePuesto = "Vendedor";
                } else if (puesto.equals("PT3")) {
                    nombrePuesto = "Cajero";
                } else if (puesto.equals("PT4")) {
                    nombrePuesto = "Administrador";
                } else {
                    nombrePuesto = "Unknown";
                }
                System.out.printf(colores.CIAN + "|" + colores.RESET + " %-44s " + colores.CIAN + "|\n", "Puesto:      " + nombrePuesto);
                System.out.println(colores.CIAN + "+----------------------------------------------+");
                System.out.printf(colores.CIAN + "|" + colores.RESET + " %-44s " + colores.CIAN + "|\n", "Correo:      " + rs.getString("correo"));
                System.out.println(colores.CIAN + "+----------------------------------------------+");
                System.out.printf(colores.CIAN + "|" + colores.RESET + " %-44s " + colores.CIAN + "|\n", "Contraseña:  " + rs.getString("contrasenia"));
                System.out.println(colores.CIAN + "+----------------------------------------------+");
                System.out.printf(colores.CIAN + "|" + colores.RESET + " %-44s " + colores.CIAN + "|\n", "Nombre:      " + rs.getString("nombre") + " " + rs.getString("primerApellido") + " " + rs.getString("segundoApellido"));
                System.out.println(colores.CIAN + "+----------------------------------------------+");
                System.out.printf(colores.CIAN + "|" + colores.RESET + " %-44s " + colores.CIAN + "|\n", "Estatus:     "+ (rs.getBoolean("estado")? "ACTIVO":"INACTIVO"));
                System.out.println(colores.CIAN + "+----------------------------------------------+");
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
        limpiarConsola();
    }

    public void agregarEmpld() throws SQLException {
        Connection con = conexion.conectar();
        Statement st = con.createStatement();
    
        System.out.println(colores.CIAN + "- - - - AÑADIR EMPLEADO - - - - - " + colores.RESET);
    
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
    
        boolean APvalido = true;
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
        // Validar correo
        
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
    
        // Validar contraseña
        do {
            System.out.println("Ingrese la contraseña: ");
            contrasenia = s.nextLine();
            if (contrasenia.equalsIgnoreCase("r")) {
                System.out.println("Operación cancelada.");
                return;
            } else if (contrasenia.isEmpty()) {
                System.out.println(colores.ROJO + "Error: La contraseña no puede estar vacía."+ colores.RESET) ;
            } else if (contrasenia.length() < 5) {
                System.out.println(colores.ROJO + "Error: La contraseña debe tener un mínimo de 5 caracteres."+ colores.RESET);
            }
        } while (contrasenia.isEmpty() || contrasenia.length() < 5);
    
        puesto = seleccionarPuesto();
        if (puesto.equalsIgnoreCase("CANCEL") || puesto.equalsIgnoreCase("r")) {
            System.out.println("Operación cancelada.");
            return;
        }
    
        String comando = "INSERT INTO empleado(correo, contrasenia, nombre, primerApellido, segundoApellido, puesto) " +
                         "values ('" + getCorreo() + "','" + contrasenia + "','" + getNombre() + "','" + getPrimer_AP() + "','" + getSegundo_AP() + "','" + puesto + "')";
        st.executeUpdate(comando);
        limpiarConsola();
        comando = "SELECT LAST_INSERT_ID() AS numero";
        ResultSet rs = st.executeQuery(comando);
        int numMiembro;
        if (rs.next()) {
            System.out.println("-------------------------------------------------");
            System.out.println(colores.VERDE + " - - - EMPLEADO REGISTRADO DE FORMA EXITOSA - - - " + colores.RESET);
            System.out.println("-------------------------------------------------");
            numMiembro = rs.getInt("numero");
            System.out.println("Número de empleado asignado: " + numMiembro);
            System.out.println("-------------------------------------------------");
    
            rs = st.executeQuery("SELECT " +
                                 "CONCAT(nombre, ' ', primerApellido, ' ', IFNULL(segundoApellido, '')) as Empleado " +
                                 "FROM empleado " +
                                 "WHERE numero = " + numMiembro);
    
            while (rs.next()) {
                System.out.println("Nombre: \n" + rs.getString("Empleado"));
                System.out.println("-------------------------------------------------\n");
            }
        }
    }

    public void updateEmpld() throws SQLException {
        Connection con = conexion.conectar();
        Statement st = con.createStatement();
        System.out.println(colores.AZUL+"\n\t- - - - MODIFICAR EMPLEADO - - - - - "+colores.RESET);
        do {
            try {
                System.out.println("\nIngrese el numero del empleado a modificar (o 'r' para cancelar): ");
                
                String input = s.nextLine();
                if (input.equalsIgnoreCase("r")) {
                    System.out.println("Operación cancelada.");
                    return;
                } else if (input.matches("\\d+")) { // Verificar si es un número
                    setNumero(Integer.parseInt(input));
                    exception = true;
                } else {
                    System.out.println(colores.ROJO +"FAVOR DE INGRESAR UN NUMERO \n"+ colores.RESET);
                    exception = false;
                }
            } catch (Exception e) {
                System.out.println("Error inesperado. Inténtalo de nuevo.");
                exception = false;
            }
        } while (!exception);


        ResultSet rs = st.executeQuery("SELECT * FROM empleado WHERE numero = '" + getNumero() + "' ");
        if (!rs.next()) {
            System.out.println("\nNO SE ENCONTRO UN EMPLEADO CON ESE NUMERO\n");
        } else {
            do {
                System.out.println(colores.CIAN + "\n+----------------------------------------------+");
                System.out.println("|                DATOS ACTUALES                |");
                System.out.println("+----------------------------------------------+" + colores.RESET);
                String puesto = rs.getString("puesto");
                String nombrePuesto;
                if (puesto.equals("PT1")) {
                    nombrePuesto = "Gerente";
                } else if (puesto.equals("PT2")) {
                    nombrePuesto = "Vendedor";
                } else if (puesto.equals("PT3")) {
                    nombrePuesto = "Cajero";
                } else if (puesto.equals("PT4")) {
                    nombrePuesto = "Administrador";
                } else {
                    nombrePuesto = "Unknown";
                }
                System.out.printf(colores.CIAN + "|" + colores.RESET + " %-44s " + colores.CIAN + "|\n", "Puesto:      " + nombrePuesto);
                System.out.println(colores.CIAN + "+----------------------------------------------+");
                System.out.printf(colores.CIAN + "|" + colores.RESET + " %-44s " + colores.CIAN + "|\n", "Correo:      " + rs.getString("correo"));
                System.out.println(colores.CIAN + "+----------------------------------------------+");
                System.out.printf(colores.CIAN + "|" + colores.RESET + " %-44s " + colores.CIAN + "|\n", "Contraseña:  " + rs.getString("contrasenia"));
                System.out.println(colores.CIAN + "+----------------------------------------------+");
                System.out.printf(colores.CIAN + "|" + colores.RESET + " %-44s " + colores.CIAN + "|\n", "Nombre:      " + rs.getString("nombre") + " " + rs.getString("primerApellido") + " " + rs.getString("segundoApellido"));
                System.out.println(colores.CIAN + "+----------------------------------------------+");
                System.out.printf(colores.CIAN + "|" + colores.RESET + " %-44s " + colores.CIAN + "|\n", "Estatus:     "+ (rs.getBoolean("estado")? "ACTIVO":"INACTIVO"));
                System.out.println(colores.CIAN + "+----------------------------------------------+");
            } while (rs.next());

            boolean banderaOP = true;
            String op;
            do {
                do {
                    try {
                        System.out.println(colores.AZUL+"\nSeleccione el campo que desea modificar:"+colores.RESET);
                        System.out.println("1. Correo electrónico");
                        System.out.println("2. Contraseña");
                        System.out.println("3. Puesto");
                        System.out.println("4. Estado");
                        System.out.println("5. Cancelar operación");
                        int opcion = s.nextInt();
                        s.nextLine(); // Limpiar el buffer del Scanner
                        if (opcion == 5) { // Agregué una opción 5 para cancelar la operación
                            System.out.println("Operación cancelada.");
                            limpiarConsola();
                            return; // Salir del método
                        }

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
                                comando = "UPDATE empleado SET " + "correo = '" + getCorreo() + "' " +
                                        "WHERE numero = " + getNumero();
                                st.executeUpdate(comando);
                                System.out.println("\n - - - CORREO ACTUALIZADO - - - \n");
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
                                    System.out.println("Ingrese la nueva contraseña: ");
                                    contrasenia = s.nextLine();
                                    if (contrasenia.equalsIgnoreCase("r")) {
                                        System.out.println("Operación cancelada.");
                                        return;
                                    } else if (contrasenia.isEmpty()) {
                                        System.out.println(colores.ROJO + "Error: La contraseña no puede estar vacía." + colores.RESET);
                                    } else if (contrasenia.length() < 5) {
                                        System.out.println(colores.ROJO + "Error: La contraseña debe tener un mínimo de 5 caracteres." + colores.RESET);
                                    }
                                } while (contrasenia.isEmpty() || contrasenia.length() < 5);
                                    comando = "UPDATE empleado SET " + "contrasenia = '" + contrasenia + "' " +
                                        "WHERE numero = " + getNumero();
                                    st.executeUpdate(comando);
                                    System.out.println("\n - - - CONTRASEÑA ACTUALIZADA - - - \n");
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
                                puesto = seleccionarPuesto();
                                if (puesto.equals("CANCEL")){
                                    System.out.println("\nSe cancelo la operacion");
                                } else {
                                    comando = "UPDATE empleado SET " + "puesto = '" + puesto.toUpperCase() + "' " +
                                        "WHERE numero = " + getNumero();
                                        st.executeUpdate(comando);
                                System.out.println("\n - - - EMPLEADO ACTUALIZADO- - - \n");
                                System.out.println("\n¿Desea modificar otro parametro? (a. Si \nOtra tecla. No)");
                                s.nextLine();
                                op = s.nextLine();
                                if (op.toLowerCase().equals("a")) {
                                    banderaOP = true;
                                }else{
                                    banderaOP = false;
                                }
                                }
                                limpiarConsola();
                                break;
                            case 4:
                                do {
                                    System.out.println("Ingrese el estado del empleado (a.Activo - b.Inactivo): ");
                                    op = s.nextLine();
                                    switch (op.toLowerCase()) {
                                        case "a":
                                            comando = "UPDATE empleado SET estado = 1 WHERE numero = '"+getNumero()+"'";
                                            st.executeUpdate(comando);
                                            exception = false;
                                            break;
                                        case "b":
                                            comando = "UPDATE empleado SET estado = 0 WHERE numero = '"+getNumero()+"'";
                                            st.executeUpdate(comando);
                                            exception = false;
                                            break;
                                    
                                        default:
                                            System.out.println(colores.ROJO + "OPCION INVALIDA" + colores.RESET);
                                            exception = true;
                                            break;
                                    }
                                } while (exception);
                                System.out.println("\n - - - EMPLEADO ACTUALIZADO- - - \n");
                                System.out.println("\n¿Desea modificar otro parametro? (a. Si \nOtra tecla. No)");
                                op = s.nextLine();
                                if (op.toLowerCase().equals("a")) {
                                    banderaOP = true;
                                }else{
                                    banderaOP = false;
                                }
                                break;
                            default:
                                System.out.println("Opción inválida. Por favor seleccione una opción válida.");
                                banderaOP = true;
                                break;
                            }
                        exception = true;
                        } catch (InputMismatchException e) {
                            System.out.println("FAVOR DE INGRESAR UN NUMERO");
                            s.nextLine();
                            banderaOP = true;
                        }
                    } while (!exception);
                } while (banderaOP);
        }
    }

    public void deleteEmpld() throws SQLException {
        Connection con = conexion.conectar();
        Statement st = con.createStatement();
        do {
            System.out.println("\n - - - DESPEDIR EMPLEADO - - - \n");
            try {
                System.out.println("Ingrese el número del empleado (o 'r' para cancelar): ");
                String input = s.next();
                if (input.equalsIgnoreCase("r")) {
                    System.out.println("\nOperación cancelada.");
                    return; // Salir del método
                } else if (input.matches("\\d+")) { // Verificar si es un número
                    setNumero(Integer.parseInt(input));
                    exception = true;
                } else {
                    System.out.println(colores.ROJO + "\nFavor de ingresar un número o 'r'" + colores.RESET);
                    exception = false;
                }
            } catch (Exception e) {
                System.out.println("Error inesperado. Inténtalo de nuevo.");
                exception = false;
            }
        } while (!exception);
        ResultSet rs = st.executeQuery("SELECT * FROM empleado WHERE numero = '" + getNumero() + "' ");
        if (!rs.next()) {
            System.out.println(colores.ROJO + "\nNO SE ENCONTRÓ UN EMPLEADO CON ESE NÚMERO\n" + colores.RESET);
            s.nextLine();
        } else {
            System.out.println(colores.ROJO + "Empleado a despedir: ");
            System.out.println("Nombre: "+rs.getString("nombre")+ " "+rs.getString("primerApellido")+ " "+rs.getString("segundoApellido") + colores.RESET);
            System.out.println(colores.AMARILLO + "¿Desea continuar? (a.Si - Cualquier otra tecla (CANCELAR))" + colores.RESET);
            s.nextLine();
            String op = s.nextLine();
            if (op.toLowerCase().equals("a")) {
                String comando = "UPDATE empleado SET estado = 0 WHERE numero = '"+getNumero()+"'";
                st.executeUpdate(comando);
                System.out.println(colores.VERDE + " - - - EMPLEADO DESPEDIDO - - - " + colores.RESET);
            }
        }
    }

    public boolean iniciarsesion() throws SQLException {
        Connection con = conexion.conectar();
        boolean rsnull;
        do {
            do {
                try {
                    System.out.println("\nIngrese su número de empleado (o 'r' para cancelar): ");
                    String input = s.next();
                    if (input.equalsIgnoreCase("r")) {
                        System.out.println(colores.ROJO + "Operación cancelada" + colores.RESET);
                        return false;
                    }
                    setNumero(Integer.parseInt(input));
                    exception = true;
                } catch (NumberFormatException e) {
                    System.out.println(colores.ROJO + "\nDebe ingresar un valor numérico o 'r'. Por favor, ingréselo de nuevo" + colores.RESET);
                    s.nextLine();
                    exception = false;
                }
            } while (!exception);
            String query = "SELECT * FROM empleado WHERE numero = ?";
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setInt(1, getNumero());
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println(colores.ROJO + "\nNo existe un empleado con ese numero \nFavor de ingresar un número válido\n" + colores.RESET);
                        rsnull = true;
                    } else {
                        rsnull = false;
                    }
                }
            } catch (SQLException e) {
                System.out.println(colores.ROJO + "Error al consultar la base de datos: " + e.getMessage() + colores.RESET);
                rsnull = true; 
            }
        } while (rsnull);
        String query = "SELECT * FROM empleado WHERE numero = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, getNumero());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    estado = rs.getBoolean("estado");
                    if (estado) {
                        boolean passwrd = false;
                        s.nextLine();
                        do {
                            System.out.println("\nIngrese su contraseña (o 'r' para cancelar): "+colores.INVISIBLE);
                            String input = s.nextLine();
                            System.out.println(colores.RESET);
                            if (input.equalsIgnoreCase("r")) {
                                System.out.println(colores.ROJO + "Operación cancelada" + colores.RESET);
                                return false;
                            }
                            setContrasenia(input);
                            // Verificar la contraseña
                            String passwTemp = rs.getString("contrasenia");
                            if (passwTemp.equals(getContrasenia())) {
                                passwrd = true;
                                setPuesto(rs.getString("puesto"));
                                
                            } else {
                                
                            }
                        } while (!passwrd);
                        return true;
                    } else {
                        System.out.println(colores.ROJO + "El empleado esta inactivo " + colores.RESET);
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(colores.ROJO + "Error al consultar la base de datos: " + e.getMessage() + colores.RESET);
            return false;
        }
        return false;
    }
    
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getPuesto() {
        return puesto;
    }

    public String retornarNombre(int numSession) throws SQLException {
        Connection con = conexion.conectar();
        PreparedStatement ps = con.prepareStatement("SELECT \n" +
                "  CONCAT(e.nombre, ' ', e.primerApellido, ' ', IFNULL(CONCAT(e.segundoApellido, ' '), '')) AS empleado \n" +
                "FROM empleado AS e \n" +
                "WHERE e.numero = ?");
        ps.setInt(1, numSession); // Asigna el valor de numSession al parámetro ?
        ResultSet rs = ps.executeQuery();
        String emp = null;
        while (rs.next()) {
            emp = rs.getString("empleado");
        }
        return emp;
    }

    public String seleccionarPuesto(){
        Boolean bandera;
        System.out.println("\nSeleccione un puesto de trabajador:");
        System.out.println("\ta. Gerente \n\tb. Vendedor \n\tc. Cajero \n\td. Administrador");
        System.out.println(" * Ingrese 'r' para cancelar la operacion *");
        do { //Switch que verifica que se selecciona uno de los 3 niveles de la tarjeta.
            char cond = s.next().charAt(0);
            switch (cond) {
                case 'a':
                    bandera = true;
                    puesto = "PT1";
                    break;
                    
                case 'b':
                    bandera = true;
                    puesto = "PT2";
                    break;
                
                case 'c':
                    bandera = true;
                    puesto = "PT3";
                    break;
            
                case 'd':
                    bandera = true;
                    puesto = "PT4";
                    break;

                case 'r':
                    bandera = true;
                    puesto = "CANCEL";
                    break;
                    
                default:
                    bandera = false;
                    System.out.println("El valor ingresado es incorrecto. Volver a ingresar.");
                    break;
            }
        } while (bandera == false);
        return puesto;
    }

    public static boolean validarEmail (String correo){
        String patron = "^[\\w._%+-]+@(gmail\\.com|hotmail\\.com)$";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();
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