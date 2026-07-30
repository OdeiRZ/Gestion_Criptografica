# Gestión Criptográfica

Aplicación de escritorio en Java (Swing) para cifrar y descifrar ficheros de texto usando el algoritmo Rijndael (AES).

## Características

- Autenticación simple: el "usuario" es el nombre de un fichero `.txt` en `src/recursos/` y la "contraseña" es su contenido (por ejemplo `lorem`/`ipsum` o `odei`/`riveiro`).
- Generación de una clave privada (`SecretKey`) a partir del usuario y contraseña autenticados, usando `KeyGenerator` con el algoritmo Rijndael y una semilla derivada de esas credenciales.
- Cifrado de un fichero `.txt` elegido por el usuario, generando una copia `nombre.cifrado.txt` en la misma carpeta.
- Descifrado de un fichero previamente cifrado, generando una copia `nombre.descifrado.txt`; si la clave no coincide con la usada para cifrar, informa del error en lugar de generar un fichero corrupto.
- Procesamiento de los ficheros por bloques de 1 KB mediante `Cipher.update`, cerrando el cifrado con `doFinal`.
- Interfaz gráfica con mensajes de resultado (éxito/error) mediante `JOptionPane`.

## Tecnologías

- Java (Swing / AWT para la interfaz gráfica)
- `javax.crypto` (Cipher, KeyGenerator, SecretKey) y `java.security` (SecureRandom) para el cifrado Rijndael/ECB
- Apache Ant + NetBeans (`build.xml`, estructura de proyecto NetBeans)

## Instalación / Cómo ejecutarlo

**Opción rápida (ejecutable ya compilado):**
```
java -jar dist/Gestion_Criptografica.jar
```

**Compilando desde el código fuente:**
1. Abre el proyecto con NetBeans (o cualquier IDE compatible con Ant), o compílalo con `ant` desde la raíz del proyecto usando el `build.xml` incluido.
2. Ejecuta la clase `Controles` (contiene el `main`).
3. Autentícate usando como usuario el nombre de uno de los ficheros de `src/recursos` (p. ej. `lorem`) y como contraseña su contenido (`ipsum`).
4. Indica la ruta de un fichero `.txt` para cifrarlo o descifrarlo.

Requiere Java 7 o superior.

Ejercicio académico que practica el uso de la API criptográfica de Java (`javax.crypto`) para cifrar y descifrar ficheros con una clave simétrica.

## Seguridad

Sustituido el cifrado Rijndael/ECB (ya no disponible en JDKs modernos, y débil por naturaleza) por AES/CBC con un IV aleatorio por fichero. La clave ya no se deriva directamente de usuario/contraseña en texto plano, sino mediante PBKDF2WithHmacSHA256, y las contraseñas almacenadas se comparan por hash SHA-256 en lugar de en texto plano.

## Licencia

GPL versión 3 (ver archivo [LICENSE](LICENSE)).
