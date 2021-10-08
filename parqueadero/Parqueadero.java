package parqueadero;

import java.util.Scanner;

public class Parqueadero {

    public void menu(Scanner input) {
        Sistema sistema = new Sistema();
        Estacionamientos estacionamientos = new Estacionamientos();
        Historial historial = new Historial();
        if (!sistema.existenciaDeArchivo("estacionamientos.txt")) {
            sistema.crearArchivo("estacionamientos.txt");
            sistema.escribirEnArchivo(
                    "A1,A2,A3,A4,A5,A6,A7\r\nB1,B2,B3,B4,B5,B6,B7\r\nC1,C2,C3,C4,C5,C6,C7\r\nD1,D2,D3,D4,D5,D6,D7\r\nE1,E2,E3,E4,E5,E6,E7",
                    "estacionamientos.txt", true);
        }
        if (!sistema.existenciaDeArchivo("historial.txt")) {
            sistema.crearArchivo("historial.txt");
            sistema.escribirEnArchivo("placa,tipo,estacionamiento,hora entrada,hora salida,duracion,tarifa,monto",
                    "historial.txt", true);
        }
        if (!sistema.existenciaDeArchivo("registro.txt")) {
            sistema.crearArchivo("registro.txt");
            sistema.escribirEnArchivo(
                    "placa,marca,modelo,año,nombre,numero,color,email,cedula,licencia,r.u.v,poliza", "registro.txt",
                    true);
        }
        do {
            System.out.println("Bienvenido a Parkings S.A.\r\n"
                    + "Ingrese el numero de la opcion que desea realizar:\r\n" + "1. Configurar estacionamientos\r\n"
                    + "2. Consultar disponibilidad\r\n" + "3. Ingresar auto al parqueadero\r\n"
                    + "4. Cobrar estacionamiento\r\n" + "5. Consultar monto recuadado\r\n" + "6. Cerrar caja\r\n"
                    + "7. Mostrar historial\r\n" + "8. Salir\r\n");
            sistema.obtenerOpcion(input);
            switch (sistema.opcion) {
                case 1:
                    estacionamientos.configurarEstacionamientos(input);
                    break;
                case 2:
                    estacionamientos.consultarDisponibilidad();
                    break;
                case 3:
                    estacionamientos.ingresarAutoAlParqueadero(input);
                    break;
                case 4:
                    historial.cobrarEstacionamiento(input);
                    break;
                case 5:
                    historial.montoRecaudado(input);
                    break;
                case 6:
                    historial.cerrarCaja();
                    break;
                case 7:
                    historial.mostrarHistorial(input);
                    break;
                case 8:
                    System.out.println("Ha salido del menu");
                    break;
                default:
                    System.out.println("Ingreso una opcion que no existe, intente nuevamente\r\n");
                    break;
            }
        } while (sistema.opcion != 8);
    }

    public static void main(String[] args) {
        Parqueadero parqueadero = new Parqueadero();
        Scanner input = new Scanner(System.in);
        parqueadero.menu(input);
    }
}