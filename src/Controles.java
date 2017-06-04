import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.*;
import javax.crypto.*;
import javax.swing.JOptionPane;

/**
 * Clase Controles. 
 * Se encarga de generar los Procedimientos que usaremos para implementar 
 * los métodos Criptográficos usados en la aplicación.
 *
 * @author Odei
 * @version 30.04.2016
 */
public class Controles {
    /**
     * Variable SecretKey usada para almacenar la clave utilizada para 
     * encriptar y desencriptar los Ficheros gestionados.
     */
    protected static SecretKey clave;
    
    /**
     * Variable de tipo cadena usada para almacenar la Ruta del equipo 
     * donde estarán almacenados los ficheros utilizados.
     */
    protected static final String ruta = "src/recursos/";
    
    /**
     * Constructor de la Clase Controles.
     * Genera e inicializa los elementos utilizados para gestionar la
     * ejecución de los Controles Criptográficos usados durante el Programa.
     */
    public Controles() {
        Interfaz interfaz = new Interfaz();                                     // Lanzamos una Instancia de la Interfaz Gráfica para el Usuario
    }
    
    /**
     * Método usado para comprobar si el Usuario enviado es correcto.
     * Devuelve una cadena tras comprobar si el nombre de usuario está entre
     * los ficheros del Servidor y su contenido es igual a la Contraseña enviada.
     *
     * @param user String: cadena con el Usuario a identificar
     * @param pass String: cadena con el Password a identificar
     * @return aux String: resultado obtenido tras verificar o no al Usuario
     */
    protected static String checkUser(String user, String pass) {
        String aux = "error";
        try {
            if (new File(ruta + user + ".txt").isFile()) {                      // Comprobamos si existe un fichero con el nombre del usuario
                BufferedReader br = 
                    new BufferedReader(new FileReader(ruta + user + ".txt"));   // Obtenemos los datos contenidos dentro del fichero (donde se almancena la contraseña) y leemos su contenido
                aux = br.readLine();
                if (aux.equals(pass)) {                                         // Si el password recibido es igual al leido                                    
                    aux = "ok";                                                 // marcamos como ok la conexión
                    clave = genKey(user, pass);                                 // e inicializamos la clave probada usada para des/encriptar los ficheros
                }
            }
        } catch (IOException ex) { }
        return aux;                                                             // y devolvemos en forma de cadena el resultado
    }
      
    /**
     * Método usado para generar la clave privada a utilizar.
     * Inicializa la clave privada a usar para des/encriptar ficheros a partir 
     * del nombre y contraseña del Usuario autenticado desde la Interfaz.
     * 
     * @param user String: cadena con el Usuario usado para generar la semilla
     * @param pass String: cadena con el Password usado para generar la semilla
     * @return SecretKey: clave privada utilizada durante la des/encriptación
     */
    public static SecretKey genKey(String user, String pass) {
        SecretKey claveAux = null;             
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("Rijndael");         // Creamos un objeto para generar la clave usando el algoritmo Rijndael
            SecureRandom sr = new SecureRandom();
            sr.setSeed((user + pass).getBytes());                               // Generamos semilla con los datos del Usuario
            keyGen.init(128, sr);                                               // Indicamos el tamaño de la clave y la semilla a usar
            claveAux = keyGen.generateKey();                                    // Genera la clave privada
        } catch (NoSuchAlgorithmException ex) { }
        return claveAux;                                                        // Devolvemos la clave privada
    }
    
    /**
     * Método usado para comprobar la existencia del Fichero enviado.
     * Comprueba si el fichero enviado existe y redirecciona al método oportuno
     * a partir de la variable entera recibida como parámetro.
     * 
     * @param user String: cadena con el Usuario a identificar
     * @param pass String: cadena con el Password a identificar
     * @param fich String: cadena con la URL del Fichero a encriptar
     * @param t int: entero que representa la función a realizar
     * @return aux String: resultado obtenido tras comprobar el Fichero
     */
    public static String checkFile(String user, String pass, String fich, int t){
        String aux = "error_1";
        try {
            if (new File(fich).isFile() && fich.endsWith(".txt")) {             // Comprobamos si existe el fichero enviado y si es un .txt
                String msj;
                int tipo = 1;
                if (t == 1) {                                                   // en cuyo caso procesamos la llamada para encriptar el fichero enviado
                    aux = "ok_1";
                    cifrarFichero(user, pass, fich);
                    fich = fich.substring(0,fich.lastIndexOf("\\"));            // acortamos ruta recibida para mostrar solo la url hasta el fichero
                    msj = "Fichero Cifrado en:\n" + fich + "\n\n";
                } else {                                                        // si el entero recibido no es 1 llamamos al método que desencripta el fichero
                    aux = descifrarFichero(fich);
                    if (!aux.equals("error_1")) {
                        fich = fich.substring(0,fich.lastIndexOf("\\"));        // acortamos ruta recibida para mostrar solo la url hasta el fichero
                        msj = "Fichero Descifrado en:\n" + fich + "\n\n";
                    } else {
                        tipo = 0;
                        msj = "El Fichero no ha sido Encriptado previamente y/o"
                        +"\nla Clave Privada no es correcta para este Usuario\n\n";
                    }
                }
                JOptionPane.showMessageDialog(null, msj, " Información", tipo); // Mostramos mensaje con el resultado en una ventana de diálogo
            }  
        } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException |
            IllegalBlockSizeException | BadPaddingException | InvalidKeyException e){}
        return aux;                                                             // Devolvemos la respuesta producida
    }   

    /**
     * Método usado para encriptar el Fichero recibido como parámetro.
     * Encripta un fichero usando la clave previamente inicializada 
     * haciendo uso del algoritmo Rijndael.
     * 
     * @param user String: cadena con el Usuario a identificar
     * @param pass String: cadena con el Password a identificar
     * @param fich String: cadena con la URL del Fichero a encriptar
     */
    private static void cifrarFichero(String user, String pass, String fic)
            throws IOException,NoSuchPaddingException,IllegalBlockSizeException, 
            NoSuchAlgorithmException,FileNotFoundException,InvalidKeyException,
                                                           BadPaddingException {
        FileInputStream fe;                                                     // Fichero de entrada
        FileOutputStream fs;                                                    // Fichero de salida
        Cipher cifrador = Cipher.getInstance("Rijndael/ECB/PKCS5Padding");      // Se Crea el objeto Cipher para cifrar, utilizando el algoritmo Rijndael
        cifrador.init(Cipher.ENCRYPT_MODE, clave);                              // Se inicializa el cifrador en modo CIFRADO o ENCRIPTACIÓN  
        byte[] buffer = new byte[1000];
        byte[] bufferCifrado;
        fe = new FileInputStream(fic);                                          // Objeto que contiene el fichero de entrada
        fic = fic.substring(0, fic.length()-4);                                 // eliminamos la extensión del fichero
        fs = new FileOutputStream(fic + ".cifrado.txt");                        // y se la agregamos manualmente para evitar errores
        int bytesLeidos = fe.read(buffer, 0, 1000);                             // Leemos el fichero de 1k en 1k y pasamos los fragmentos leidos al cifrador
        while (bytesLeidos != -1) {                                             // Mientras no se llegue al final del fichero
            bufferCifrado = cifrador.update(buffer, 0, bytesLeidos);            // se pasa eñ texto claro al cifrador y lo cifra, asignándolo a bufferCifrado
            fs.write(bufferCifrado);                                            // Grabamos el texto cifrado en fichero
            bytesLeidos = fe.read(buffer, 0, 1000);
        }
        bufferCifrado = cifrador.doFinal();                                     // Completamos el cifrado
        fs.write(bufferCifrado);                                                // Grabamos el final del texto cifrado si existe
        fe.close(); 
        fs.close();                                                             // Cerramos ficheros
    }
    
    /**
     * Método usado para desencriptar el Fichero recibido como parámetro.
     * Desencripta un fichero usando la clave previamente inicializada haciendo
     * uso del algoritmo Rijndael y creando un fichero en el mismo directorio.
     * 
     * @param fic String: cadena con la URL del Fichero a encriptar
     * @return res String: resultado obtenido tras descifrar el Fichero
     */
    private static String descifrarFichero(String fic) 
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            FileNotFoundException, InvalidKeyException, IOException{
        String res = "ok_1";
        FileInputStream fe;                                                     // Fichero de entrada
        FileOutputStream fs;                                                    // Fichero de salida
        Cipher cifrador = Cipher.getInstance("Rijndael/ECB/PKCS5Padding");
        cifrador.init(Cipher.DECRYPT_MODE, clave);                              // Ponemos cifrador en modo DESCIFRADO o DESENCRIPTACIÓN
        fe = new FileInputStream(fic);
        fs = new FileOutputStream(fic.substring(0,fic.length()-4)+".descifrado.txt");
        byte[] bufferClaro;
        byte[] buffer = new byte[1000];
        int bytesLeidos = fe.read(buffer, 0, 1000);                             // Leemos el fichero de 1k en 1k y pasamos los fragmentos leidos al cifrador
        while (bytesLeidos != -1) {                                             // mientras no se llegue al final del fichero
            bufferClaro = cifrador.update(buffer, 0, bytesLeidos);              // pasamos el texto cifrado al cifrador y lo desciframos, asignándolo a bufferClaro
            fs.write(bufferClaro);                                              // Grabamos el texto claro en fichero
            bytesLeidos = fe.read(buffer, 0, 1000);
        }
        try {
            bufferClaro = cifrador.doFinal();                                   // Completamos el descifrado
            fs.write(bufferClaro);                                              // Grabamos el final del texto claro si existe
        } catch(IllegalBlockSizeException | BadPaddingException | IOException e){
            fs.close();                                                         // Cerramos fichero de salida si hay algún error
            res = "error_1";                                                    // y lo borramos asignando el error correspondiente
            new File(fic.substring(0,fic.length()-4)+".descifrado.txt").delete();
        }
        fe.close();
        fs.close();                                                             // Cerramos archivos
        return res;                                                             // Devolvemos la respuesta producida
    }
    
    /**
     * Método Principal de la Clase Controles.
     * Lanza una Instancia del Programa llamando al Constructor.
     * 
     * @param args String[]: argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        Controles app = new Controles();                                        // Creamos una Instancia del Programa
    }
}