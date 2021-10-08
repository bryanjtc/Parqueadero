package parqueadero;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Sistema {
    protected String placa;
    protected char respuesta;
    protected int opcion;

    public void fijarPlaca(String placa) {
        this.placa = placa;
    }

    public void fijarRespuesta(char respuesta) {
        this.respuesta = respuesta;
    }

    public void fijarOpcion(int opcion) {
        this.opcion = opcion;
    }

    public String leer(String lectura, Scanner input) {// Lee una cadena
        do {
            lectura = input.nextLine();
            if (lectura == null || lectura.isEmpty())
                System.out.println("No ingreso nada, intente nuevamente");
        } while (lectura == null || lectura.isEmpty());
        return lectura;
    }

    public double leer(double lectura, Scanner input) {// Lee un real
        try {
            do {
                lectura = Double.parseDouble(input.nextLine());
                if (lectura < 0)
                    System.out.println("Ingreso un numero menor a 0, intente nuevamente");
            } while (lectura < 0);
        } catch (InputMismatchException ex) {
            System.out.println("No ingreso un numero, intente nuevamente");
            lectura = leer(lectura, input);
        }
        return lectura;
    }

    public int leer(int lectura, Scanner input) {// Lee un entero
        try {
            do {
                lectura = Integer.parseInt(input.nextLine());
                if (lectura < 1)
                    System.out.println("Ingreso un numero menor a 1, intente nuevamente");
            } while (lectura < 0);
        } catch (InputMismatchException ex) {
            System.out.println("No ingreso un numero, intente nuevamente");
            lectura = leer(lectura, input);
        }
        return lectura;
    }

    public void obtenerPlaca(Scanner input) {// Obtiene la placa
        System.out.println("Ingrese la placa del cliente");
        placa = leer(placa, input);
        fijarPlaca(placa);
    }

    public String obtenerFecha() {// Obtiene la fecha y hora exacta
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM-dd-yy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        return formattedDate;
    }

    public void obtenerRespuesta(Scanner input, String mensaje) {// Obtiene la respuesta del usuario
        String lectura = "";
        char respuesta = 0;
        System.out.println("Ingrese Y si desea continuar " + mensaje);
        lectura = input.nextLine();
        respuesta = lectura.charAt(0);
        fijarRespuesta(respuesta);
    }

    public void obtenerOpcion(Scanner input) {// Obtiene la opcion del usuario
        int opcion = 0;
        opcion = leer(opcion, input);
        fijarOpcion(opcion);
    }

    public boolean existenciaDeArchivo(String nombreDeDocumento) {// Revisa si existe archivo
        File archivo = new File(nombreDeDocumento);
        boolean exists = archivo.exists();
        return exists;
    }

    public void crearArchivo(String nombreDeDocumento) {// Crea un archivo
        try {
            File archivo = new File(nombreDeDocumento);
            if (archivo.createNewFile())
                System.out.println("Archivo creado: " + nombreDeDocumento.replace(".txt", ""));
        } catch (IOException error) {
        }
    }

    public void verArchivo(String nombreDeDocumento) {// Muestra un archivo
        try (FileReader fileReader = new FileReader(nombreDeDocumento)) {
            Scanner reader = new Scanner(fileReader);
            String line, arregloDeLinea[];
            System.out.println("\r\n" + nombreDeDocumento.replace(".txt", ""));
            while ((line = reader.nextLine()) != null) {
                arregloDeLinea = line.split("\n");
                for (int posicion = 0; posicion < arregloDeLinea.length; posicion++)
                    arregloDeLinea[posicion] = arregloDeLinea[posicion].replace(",", " ");
                System.out.println(arregloDeLinea[0]);
            }
            fileReader.close();
            reader.close();
        } catch (Exception error) {
        }
    }

    public boolean buscarEnArchivo(String busqueda, String nombreDeDocumento) {
        // Revisa si existe la busqueda en el archivo
        boolean isEncontrado = false;
        try (FileReader fileReader = new FileReader(nombreDeDocumento)) {
            Scanner reader = new Scanner(fileReader);
            String line;
            while ((line = reader.nextLine()) != null) {
                if (line.contains(busqueda)) {
                    isEncontrado = true;
                }
            }
            fileReader.close();
            reader.close();
        } catch (Exception error) {
        }
        return isEncontrado;
    }

    public void escribirEnArchivo(String entrada, String nombreDeDocumento, boolean isAgregar) {// Escribe en un archivo
        try {
            FileWriter escritor = new FileWriter(nombreDeDocumento, isAgregar);
            BufferedWriter bufferedWriter = new BufferedWriter(escritor);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);
            printWriter.println(entrada);
            printWriter.flush();
            printWriter.close();
        } catch (Exception error) {
        }
    }
}