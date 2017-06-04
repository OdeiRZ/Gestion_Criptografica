import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 * Clase Interfaz que extiende de JFrame e implementa ActionListener. 
 * Se encarga de generar la Interfaz que usaremos para implementar los métodos
 * de control criptográficos usados en la aplicación.
 *
 * @author Odei
 * @version 30.04.2016
 */
public class Interfaz extends JFrame implements ActionListener {
    /**
     * Variable usada para almacenar el Frame de la Interfaz de Usuario.
     */
    protected static JFrame frame;
    
    /**
     * Variable usada para almacenar el Nombre de Usuario con el que operar.
     */
    protected JTextField tfUser;
    
    /**
     * Variable usada para almacenar el Password de Usuario con el que operar.
     */
    protected JPasswordField tfPass;
       
    /**
     * Variable usada para almacenar la ruta del Fichero con el que operar.
     */
    protected JTextField tfFichero;
    
    /**
     * Variable usada para mostrar el Resultado de la Validación del Usuario.
     */
    protected JLabel tfResUsuario;
        
    /**
     * Variable usada para mostrar el Resultado de la Validación del Fichero.
     */
    protected JLabel tfResFichero;
    
    /**
     * Variable usada para lanzar los eventos con los que interactúa el Usuario.
     */
    protected JButton btn;
            
    /**
     * Constructor de la Interfaz Gráfica implementada.
     * Genera e inicializa la Interfaz y los elementos utilizados
     * para visualizar de forma interactiva la ejecución de la Aplicación.
     */
    public Interfaz() {
        JPanel panel = new JPanel(null);                                        // Creamos un panel para dibujar la interfaz gráfica
        JMenuBar mbar = new JMenuBar();                                         // a su vez creamos un menú para operar con distintas opciones que habilitaremos
        ButtonGroup bgSeleccion = new ButtonGroup();
        JMenu menu = new JMenu("Opciones");                                     // Le ponemos nombre al menú
        String nomMI[] = {"Ayuda", "Salir"};                                    // y a los sub elementos del mismo
        JMenuItem[] mi = new JMenuItem[2];
        JLabel lbUser = new JLabel("Usuario:");                                 // Agregamos etiquetas, botones, y demás elementos a la Interfaz
        JLabel lbPassword = new JLabel("Contraseña:");
        JLabel lbOperacion = new JLabel("Operación:");
        JLabel lbFichero = new JLabel("Fichero:");
        tfUser = new JTextField("");
        tfPass = new JPasswordField("");
        tfFichero = new JTextField("");
        tfResUsuario = new JLabel("");
        tfResFichero = new JLabel("");
        btn = new JButton("Conectar");
        JRadioButton rbEncriptar = new JRadioButton("Encriptar");
        JRadioButton rbDesencriptar = new JRadioButton("Desencriptar");
        
        bgSeleccion.add(rbEncriptar);
        bgSeleccion.add(rbDesencriptar);                                        // Agregamos JRadioButton a su grupo correspondiente
        panel.add(lbUser).setBounds(35, 24, 80, 20);
        panel.add(lbPassword).setBounds(35, 55, 80, 20);
        panel.add(lbFichero).setBounds(35, 135, 80, 20);
        panel.add(lbOperacion).setBounds(35, 177, 80, 20);
        panel.add(tfUser).setBounds(140, 24, 140, 20);
        panel.add(tfPass).setBounds(140, 55, 140, 20);
        panel.add(tfResUsuario).setBounds(280, 25, 50, 50);
        panel.add(tfFichero).setBounds(140, 135, 140, 20);
        panel.add(tfResFichero).setBounds(280, 120, 50, 50);
        panel.add(btn).setBounds(140, 92, 140, 25);
        panel.add(rbEncriptar).setBounds(140, 165, 150, 20);
        panel.add(rbDesencriptar).setBounds(140, 195, 150, 20);
        
        for (int i = 0; i < nomMI.length; i++) {
            mi[i] = new JMenuItem(nomMI[i]);                                    // Agregamos los elementos del menú
            menu.add(mi[i]).setAccelerator(KeyStroke.getKeyStroke(              // y una tecla asociada a cada uno de ellos
                    nomMI[i].charAt(0), KeyEvent.CTRL_DOWN_MASK));
            mi[i].addActionListener((ActionListener) this);
        } mbar.add(menu);
        btn.addActionListener((ActionListener) this);
        rbEncriptar.addActionListener((ActionListener) this);
        rbDesencriptar.addActionListener((ActionListener) this);
        rbEncriptar.setSelected(true);
        
        frame = new JFrame("Criptografía de Ficheros");                         // Creamos JFrame y le ponemos título
        frame.add(panel);                                                       // agregando el panel previamente creado
        frame.setJMenuBar(mbar);                                                // y la barra del menú
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(Toolkit.getDefaultToolkit().createImage(
                Interfaz.class.getResource("recursos/client.png")));            // Le ponemos una imágen de icono a la ventana
        frame.setSize(325, 187);                                                // y le asignamos tamaño y demás parámetros
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
 
   /**
     * Creamos un escuchador de eventos para capturar las opciones
     * utilizadas durante la ejecución de la Interfaz.
     * 
     * @param evt ActionEvent: evento lanzado por el Jugador
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        switch (evt.getActionCommand()) {
            case "Conectar":                                                    // Si pulsamos sobre Conectar
                String aux = "vacio";                                           // comprobaremos si los campos del acceso del Usuario no están vacíos
                if (!tfUser.getText().equals("") && !tfPass.getText().equals("")){
                    if (Controles.checkUser(tfUser.getText(), 
                                            tfPass.getText()).equals("ok")) {   // si todo es correcto
                        aux = "ok";
                        generarAudio("ok");                                     // reproducimos un sonido determinado
                        frame.setSize(325, 290);                                // agrandamos el tamaño de la ventana
                        tfUser.setEditable(false);
                        tfPass.setEditable(false);                              // deshabilitamos la edición de los campos del Usuario que usaremos de semilla
                        btn.setText("Encriptar");                               // y cambiamos el texto al botón
                    } else {
                        aux = "error";
                        generarAudio("error");                                  // en caso contrario lanzamos otro sonido 
                    }
                }
                cambiarImagen(tfResUsuario, aux);                               // y mostramos una imagen con el resultado
            break;
            case "Ayuda":                                                       // Si pulsamos en la opción Ayuda del menú
                String msj="En primer lugar deberás autenticarte con tu Usuario"
                  + " y Contraseña,\ntras hacerlo podrás agregar la URL de un "
                  + "fichero de texto .txt\nlocal para Encriptarlo o Desencriptarlo"
                  + " según tus necesidades.\nEl fichero resultante estará"
                  + " alojado en la carpeta '" + Controles.ruta + "'.\n\n";
                JOptionPane.showMessageDialog(null,msj," Ventana de Ayuda",1);  // Mostraremos un mensaje en una ventana de diálogo
            break;
            case "Encriptar":                                                   // Si presionamos sobre Encriptar 
                btn.setText("Encriptar");
                if(evt.getSource() instanceof JButton) {                        // Comprobaremos si la llamada la realiza el botón
                    aux = "vacio_1";
                    if (!tfFichero.getText().equals("")) {                      // y si el campo del fichero no esta vacío
                        aux = Controles.checkFile(tfUser.getText(),
                                    tfPass.getText(), tfFichero.getText(),1);   // en cuyo caso procesaremos la petición
                    }
                    cambiarImagen(tfResFichero, aux);                           // mostrando una imagen con el resultado
                }
            break;
            case "Desencriptar":                                                // Si presionamos sobre Encriptar 
                btn.setText("Desencriptar");
                if(evt.getSource() instanceof JButton) {                        // Comprobaremos también si la llamada la realiza el botón
                    aux = "vacio_1";
                    if (!tfFichero.getText().equals("")) {
                        aux = Controles.checkFile(tfUser.getText(),
                                    tfPass.getText(), tfFichero.getText(),0);   // en ese caso procesaremos la petición
                    }
                    cambiarImagen(tfResFichero, aux);                           // mostrando una imagen con el resultado de nuevo
                }
            break;
            default:
                System.exit(0);                                                 // Nos salimos de la aplicación 
        }
    }
    
    /**
     * Método usado para cambiar las imágenes usadas para mostrar errores o
     * avisos al intentar realizar diferentes funciones.
     * 
     * @param lbl JLabel: variable que contiene el elemento a cambiar
     * @param nombre String: variable usada para almacenar el aviso a mostrar
     */
    protected static void cambiarImagen(JLabel lbl, String nombre) {
        lbl.setIcon(new ImageIcon(
            Interfaz.class.getResource("recursos/"+nombre+".png")));            // Asignamos una nueva imagen a un elemento label concreto
    }
    
    /**
     * Método usado para generar un Sonido que simula el acceso erróneo o no 
     * que se produce cada vez que un Usuario pulsa el botón Conectar.
     * 
     * @param nombre String: variable usada para almacenar el audio a ejecutar
     */
    protected static void generarAudio(String nombre) {
        try {
            Clip audio = AudioSystem.getClip();                                 // Creamos un objeto Clip para reproducir 
            audio.open(AudioSystem.getAudioInputStream(
                Interfaz.class.getResource("recursos/"+nombre+".wav")));        // un audio cada vez que se intenta realizar una Conexión
            audio.start();                                                      // y lo ejecutamos una vez
        }catch(LineUnavailableException | UnsupportedAudioFileException | IOException e){ }
    }
}