package clases;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner
;
public class establecimiento{
    Scanner s = new Scanner(System.in);
    private String nombre;
    private String codigo;
    private String calle;
    private String numero;
    private String colonia;
    private boolean ciclo;
    private conexion conexion;

    
    public establecimiento(conexion conexion){
    this.conexion=conexion;
    }

    public void verEstablecimiento() throws SQLException{
        Connection con = conexion.conectar();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM establecimiento");

        System.out.println(colores.CIAN+"---------[DATOS DEL ESTABLECIMIENTO]--------"+colores.RESET);
        while (rs.next()) {
            System.out.println("Establecimiento: "+rs.getString("nombre"));
            System.out.println(colores.CIAN+"----------[DATOS DE LA DIRECCION]-----------"+colores.RESET);
            System.out.println("Ciudad:          " +rs.getString("ciudad"));
            System.out.println("Calle:           "+rs.getString("calle"));
            System.out.println("Numero:          "+rs.getString("numero"));
            System.out.println("Colonia:         "+rs.getString("colonia"));
            System.out.println("Codigo postal:   "+rs.getString("codigoPostal"));
            System.out.println("--------------------------------------------\n\n");
        }
    }

public void updateEstablecimiento() throws SQLException{
    
    Connection con = conexion.conectar();
        do{
            do {
                System.out.println(colores.AZUL + "\n- - - - GESTIONAR ESTABLECIMIENTO - - - - - \n"+colores.RESET);
                System.out.println("Seleccione lo que desea realizar: ");
                System.out.println("\t1. Editar nombre del establecimiento");
                System.out.println("\t2. Editar dirección del establecimiento");
                System.out.println("\t3. Ver datos del establecimiento");
                System.out.println("\t4. Cancelar");
                String comando;

                try {
                    int editarEstablecimiento = s.nextInt();
                    s.nextLine();
                    ciclo=false;
                    switch (editarEstablecimiento) {
                        case 1:
                        System.out.println("Ingresa el nuevo nombre del establecimiento");
                        nombre=s.nextLine();
                        comando= "UPDATE establecimiento SET nombre = ? WHERE codigo= 'EST' ";
                        PreparedStatement pstmt = con.prepareStatement(comando);
                        pstmt.setString(1, nombre);
                        pstmt.executeUpdate();

                        System.out.println("\nEl nombre del establecimiento se ha actualizado correctamente.");
                        ciclo=true;
                            break;
                        
                        case 2:
                        do {
                            System.out.println("Elija la opcion a actualizar.");
                            System.out.println("\t1. Calle");
                            System.out.println("\t2. Numero");
                            System.out.println("\t3. Colonia");
                            System.out.println("\tSeleccione cualquier otra tecla para regresar");
                            int editarDireccion = s.nextInt();

                            switch (editarDireccion) {
                                case 1:
                                System.out.println("\tIngresa el nuevo nombre de la calle");
                                    s.nextLine();
                                    calle=s.nextLine();
                                    comando= "UPDATE establecimiento SET calle = ? WHERE codigo= 'EST' ";
                                    PreparedStatement ene = con.prepareStatement(comando);
                                    ene.setString(1, calle);
                                    ene.executeUpdate();
            
                                    System.out.println("\tEl nombre de la calle se ha actualizado correctamente.");
                                    ciclo = true;
                                    
                                break;

                                case 2:
                                    System.out.println("\tIngresa el nuevo numero de direccion");
                                    s.nextLine();
                                    numero=s.nextLine();
                                    comando= "UPDATE establecimiento SET numero = ? WHERE codigo= 'EST' ";
                                    PreparedStatement num = con.prepareStatement(comando);
                                    num.setString(1, numero);
                                    num.executeUpdate();
                
                                    System.out.println("\tEl numero de la direccion se ha actualizado correctamente.");
                                    ciclo = true;

                                break;

                                case 3:
                                    System.out.println("\n\tIngresa el nuevo nombre de la colonia");
                                    s.nextLine();
                                    colonia=s.nextLine();
                                    comando= "UPDATE establecimiento SET colonia = ? WHERE codigo= 'EST' ";
                                    PreparedStatement coloniaPreparedStatement = con.prepareStatement(comando);
                                    coloniaPreparedStatement.setString(1, colonia);
                                    coloniaPreparedStatement.executeUpdate();
                
                                    System.out.println("\nEl nombre de la colonia se ha actualizado correctamente.");
                                    ciclo = true;
                                break;

                                default:
                                System.out.println("\nOperacion cancelada");
                                ciclo=false;
                                    break;
                            }
                        } while (ciclo==true);
                        break;

                        case 3:
                        verEstablecimiento();
                        ciclo=true;
                        break;
                        default:
                        ciclo=false;
                            break;
                    }
                    
                } catch (InputMismatchException e) {
                    s.next();
                    ciclo = true;
                }
            } while (ciclo==true);

            
            

        }while (ciclo==true);
    }
            
        public String getNombre(){
            return nombre;
        }

        public void setNombre(String nombre){
            this.nombre=nombre;
        }

        public String getCodigo(){
            return codigo; 
        }

        public void setCalle(String calle){
            this.calle = calle;
        }

        public String getCalle(){
            return calle;
        }

        public void setNumero(String numero){
            this.numero = numero;
        }

        public String getNumero(){
            return numero;
        }

        public void setColonia(String colonia){
            this.colonia = colonia;
        }

        public String getColonia(){
            return colonia;
        }


}