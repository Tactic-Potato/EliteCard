package clases;
public class colores {
    public static final String RESET = "\u001B[0m";
    public static final String ROJO = "\u001B[31m";
    public static final String VERDE = "\u001B[32m";
    public static final String AMARILLO = "\u001B[33m";
    public static final String AZUL = "\u001B[34m";
    public static final String PURPURA = "\u001B[35m";
    public static final String NARANJA = "\033[38;5;214m";
    public static final String CIAN = "\u001B[36m";
    public static final String BLANCO = "\u001B[37m";
    public static final String BRONCE = "\033[38;5;166m";
    public static final String PLATA = "\033[38;5;146m";
    public static final String ORO = "\033[38;5;220m";
    public static final String INVISIBLE = "\033[08m";

    /*
        EJEMPLO DE USO, LOS CODIGOS SE PUEDEN UTILIZAR EN CUALQUIER LUGAR DEL PROGRAMA SIN NECESIDAD DE INSTANCIAR
        UN EJEMPLO DE ESTA CLASE 

        System.out.println(colores.ROJO + "ERROOOR" + Colores.RESET);

        En este caso el mensaje de error se imprimira de color rojo, debido a que se concatena la variable
        colores.ROJO

        ///IMPORTANTE
        Despues de utilizar una variable de color se debe concatenar colores.RESET para delimitar hasta donde
        se aplicara el cambio de color.
        
        Dicho de otra forma colores.RESET sirve como un delimitador y de no usarlo todas las impresiones se veran de
        un solo color

    */
}