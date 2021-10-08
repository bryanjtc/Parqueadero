package parqueadero;

import java.io.FileReader;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Scanner;

public class Estacionamientos extends Sistema {
    private String nombreDeEstacionamiento, marca, modelo, nombreDeCliente, tipoDeAlquiler, color, email, cedula,
            polizaDeSeguro, licencia;
    private int year, numeroDeEmergencia, registroUnicoVehicular;

    public void fijarNombreDeEstacionamiento(String nombreDeEstacionamiento) {
        this.nombreDeEstacionamiento = nombreDeEstacionamiento;
    }

    public String getNombreDeEstacionamiento() {
        return nombreDeEstacionamiento;
    }

    public void fijarDatos1(String marca, String modelo, int year) {
        this.marca = marca;
        this.modelo = modelo;
        this.year = year;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getYear() {
        return year;
    }

    public void fijarDatos2(String nombreDeCliente, int numeroDeEmergencia) {
        this.nombreDeCliente = nombreDeCliente;
        this.numeroDeEmergencia = numeroDeEmergencia;
    }

    public String getNombreDeCliente() {
        return nombreDeCliente;
    }

    public int getNumeroDeEmergencia() {
        return numeroDeEmergencia;
    }

    public void fijarTipoDeAlquiler(String tipoDeAlquiler) {
        this.tipoDeAlquiler = tipoDeAlquiler;
    }

    public String getTipoDeAlquiler() {
        return tipoDeAlquiler;
    }

    public void fijarColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void fijarEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void fijarDatos3(String cedula, String licencia, int registroUnicoVehicular, String polizaDeSeguro) {
        this.cedula = cedula;
        this.licencia = licencia;
        this.registroUnicoVehicular = registroUnicoVehicular;
        this.polizaDeSeguro = polizaDeSeguro;
    }

    public String getCedula() {
        return cedula;
    }

    public String getLicencia() {
        return licencia;
    }

    public int getRegistroUnicoVehicular() {
        return registroUnicoVehicular;
    }

    public String getPolizaDeSeguro() {
        return polizaDeSeguro;
    }

    public void configurarEstacionamientos(Scanner input) {// Edita el Parqueadero
        boolean isEncontrado;
        final String nombreDeDocumento = "estacionamientos.txt";
        String nombreDeEstacionamiento = "", mensaje, line, celdas[];
        ArrayList<String> filasDeEstacionamientos = new ArrayList<String>();
        int posicion;
        do {
            do {
                System.out.println("Ingrese el nombre del estacionamiento");
                nombreDeEstacionamiento = leer(nombreDeEstacionamiento, input);
                fijarNombreDeEstacionamiento(nombreDeEstacionamiento);
                isEncontrado = buscarEnArchivo(getNombreDeEstacionamiento(), nombreDeDocumento);
                if (!isEncontrado)
                    System.out.println("No existe intente de nuevo");
            } while (!isEncontrado);
            try (FileReader fileReader = new FileReader(nombreDeDocumento)) {
                Scanner reader = new Scanner(fileReader);
                while ((line = reader.nextLine()) != null) {
                    celdas = line.split(",");
                    if (line.contains(getNombreDeEstacionamiento())) {
                        for (posicion = 0; posicion < celdas.length; posicion++) {
                            if (celdas[posicion].contains(getNombreDeEstacionamiento())) {
                                if (celdas[posicion].contains("-ocupado"))
                                    System.out.println(
                                            "Ese estacionamiento esta ocupado no puede cambiarlo hasta que salga el auto");
                                else {
                                    do {
                                        System.out.println(
                                                "\r\nIngrese el numero de lo que quiere hacer con el estacionamiento:\r\n"
                                                        + "1. Marcar como VIP\r\n" + "2. Marcar como Premium\r\n");
                                        obtenerOpcion(input);
                                    } while (opcion < 1 || opcion > 2);
                                    switch (opcion) {
                                        case 1:
                                            line = line.replace(celdas[posicion],
                                                    (getNombreDeEstacionamiento() + "-vip"));
                                            break;
                                        case 2:
                                            line = line.replace(celdas[posicion],
                                                    (getNombreDeEstacionamiento() + "-premium"));
                                            break;
                                        default:
                                            System.out.println(
                                                    "Ingreso una opcion que no existe, intente nuevamente\r\n");
                                            break;
                                    }
                                }
                            }
                        }
                    }
                    filasDeEstacionamientos.add(line);
                }
                fileReader.close();
                reader.close();
            } catch (Exception error) {
            }
            escribirEnArchivo(filasDeEstacionamientos.get(0), nombreDeDocumento, false);
            for (posicion = 1; posicion < filasDeEstacionamientos.size(); posicion++) {
                escribirEnArchivo(filasDeEstacionamientos.get(posicion), nombreDeDocumento, true);
            }
            filasDeEstacionamientos.clear();
            mensaje = "configurando estacionamientos";
            obtenerRespuesta(input, mensaje);
        } while (respuesta == 'y' || respuesta == 'Y');
        System.out.println("Ha terminado de configurar estacionamientos\r\n");
    }

    public String revisarDisponibilidad() {
        final String nombreDeDocumento = "estacionamientos.txt";
        boolean isAsignado = false;
        String estacionamiento = "", line, estacionamientos[];
        ArrayList<String> filasDeEstacionamientos = new ArrayList<String>();
        int posicion;
        try (FileReader fileReader = new FileReader(nombreDeDocumento)) {
            Scanner reader = new Scanner(fileReader);
            while (!isAsignado) {
                while ((line = reader.nextLine()) != null) {
                    estacionamientos = line.split(",");
                    switch (opcion) {
                        case 1:
                            if (!isAsignado) {
                                posicion = 0;
                                while (posicion < estacionamientos.length) {
                                    if (estacionamientos[posicion].contains("-vip")
                                            && !estacionamientos[posicion].contains("-ocupado")) {
                                        line = line.replace(estacionamientos[posicion],
                                                (estacionamientos[posicion] + "-ocupado"));
                                        estacionamiento = estacionamientos[posicion];
                                        isAsignado = true;
                                        posicion = estacionamientos.length;
                                    } else
                                        posicion++;
                                }
                            }
                            break;
                        case 2:
                            if (!isAsignado) {
                                posicion = 0;
                                while (posicion < estacionamientos.length) {
                                    if (estacionamientos[posicion].contains("-premium")
                                            && !estacionamientos[posicion].contains("-ocupado")) {
                                        line = line.replace(estacionamientos[posicion],
                                                (estacionamientos[posicion] + "-ocupado"));
                                        estacionamiento = estacionamientos[posicion];
                                        isAsignado = true;
                                        posicion = estacionamientos.length;
                                    } else
                                        posicion++;
                                }
                            }
                            break;
                        case 3:
                            if (!isAsignado) {
                                posicion = 0;
                                while (posicion < estacionamientos.length) {
                                    if (!estacionamientos[posicion].contains("-vip")
                                            && !estacionamientos[posicion].contains("-premium")
                                            && !estacionamientos[posicion].contains("-ocupado")) {
                                        line = line.replace(estacionamientos[posicion],
                                                (estacionamientos[posicion] + "-ocupado"));
                                        estacionamiento = estacionamientos[posicion];
                                        isAsignado = true;
                                        posicion = estacionamientos.length;
                                    } else
                                        posicion++;
                                }
                            }
                            break;
                    }
                    filasDeEstacionamientos.add(line);
                }
                if (!isAsignado && opcion == 3) {
                    System.out.println("Estacionamientos llenos, intente mas tarde");
                    break;
                }
            }
            fileReader.close();
            reader.close();
        } catch (Exception error) {
        }
        escribirEnArchivo(filasDeEstacionamientos.get(0), nombreDeDocumento, false);
        for (posicion = 1; posicion < filasDeEstacionamientos.size(); posicion++) {
            escribirEnArchivo(filasDeEstacionamientos.get(posicion), nombreDeDocumento, true);
        }
        filasDeEstacionamientos.clear();
        if (!isAsignado && opcion != 3) {
            try (FileReader fileReader = new FileReader(nombreDeDocumento)) {
                Scanner reader = new Scanner(fileReader);
                while (!isAsignado) {
                    while ((line = reader.nextLine()) != null) {
                        estacionamientos = line.split(",");
                        if (!isAsignado) {
                            posicion = 0;
                            while (posicion < estacionamientos.length) {
                                if ((!estacionamientos[posicion].contains("-vip")
                                        || !estacionamientos[posicion].contains("-premium"))
                                        && !estacionamientos[posicion].contains("-ocupado")) {
                                    if (opcion == 1)
                                        line = line.replace(estacionamientos[posicion],
                                                (estacionamientos[posicion] + "-vip-ocupado"));
                                    else
                                        line = line.replace(estacionamientos[posicion],
                                                (estacionamientos[posicion] + "-premium-ocupado"));
                                    estacionamiento = estacionamientos[posicion];
                                    isAsignado = true;
                                    posicion = estacionamientos.length;
                                }
                                posicion++;
                            }
                        }
                        filasDeEstacionamientos.add(line);
                    }
                    if (!isAsignado) {
                        System.out.println("Estacionamientos llenos, intente mas tarde");
                        break;
                    }
                }
                fileReader.close();
                reader.close();
            } catch (Exception e) {
            }
            escribirEnArchivo(filasDeEstacionamientos.get(0), nombreDeDocumento, false);
            for (posicion = 1; posicion < filasDeEstacionamientos.size(); posicion++) {
                escribirEnArchivo(filasDeEstacionamientos.get(posicion), nombreDeDocumento, true);
            }
            filasDeEstacionamientos.clear();
        }
        return estacionamiento;
    }

    public void ingresarAutoAlParqueadero(Scanner input) {
        boolean isEncontrado, isTarjeta, isEstacionado;
        final String nombreDeDocumento = "registro.txt", nombreDeDocumento2 = "historial.txt";
        String marca = "", modelo = "", nombreDeCliente = "", tipoDeAlquiler, estacionamiento = "", color = "",
                email = "", cedula = "", polizaDeSeguro = "", horaDeEntrada, entrada, mensaje, licencia = "", line,
                celdas[];
        int year = 0, numeroDeEmergencia = 0, registroUnicoVehicular = 0, posicion = 0, diaDeSemana = 0;
        ;
        ArrayList<String> entradas = new ArrayList<String>();
        do {
            do {
                isEstacionado = false;
                do {
                    obtenerPlaca(input);
                    if (!placa.matches("[a-zA-Z0-9]*"))
                        System.out.println("Ingrese una placa valida");
                } while (!placa.matches("[a-zA-Z0-9]*"));
                try (FileReader fileReader = new FileReader(nombreDeDocumento2)) {
                    Scanner reader = new Scanner(fileReader);
                    while ((line = reader.nextLine()) != null) {
                        if (line.contains(placa)) {
                            celdas = line.split(",");
                            try (FileReader fileReader2 = new FileReader("estacionamientos.txt")) {
                                String line2, celdas2[];
                                Scanner reader2 = new Scanner(fileReader2);
                                while ((line2 = reader2.nextLine()) != null) {
                                    if (line2.contains(celdas[2])) {
                                        celdas2 = line2.split(",");
                                        for (posicion = 0; posicion < celdas2.length; posicion++) {
                                            if (celdas2[posicion].contains(celdas[2])
                                                    && celdas2[posicion].contains("ocupado")) {
                                                System.out.println(celdas2[posicion]);
                                                isEstacionado = true;
                                            }
                                        }
                                    }
                                }
                                fileReader2.close();
                                reader2.close();
                            } catch (Exception ex) {
                            }
                        }
                    }
                    fileReader.close();
                    reader.close();
                } catch (Exception ex) {
                }
                if (isEstacionado)
                    System.out.println("Ingreso una placa de un auto que ya esta estacionado, intente de nuevo");
            } while (isEstacionado);
            isEncontrado = buscarEnArchivo(placa, nombreDeDocumento);
            isTarjeta = false;
            if (isEncontrado) {
                mensaje = "ingresando con una tarjeta";
                obtenerRespuesta(input, mensaje);
                if (respuesta == 'y' || respuesta == 'y') {
                    try {
                        LocalDate localdate = LocalDate.now();
                        DayOfWeek dayOfWeek = localdate.getDayOfWeek();
                        diaDeSemana = dayOfWeek.getValue();
                    } catch (Exception e) {
                    }
                    if (diaDeSemana != 6 || diaDeSemana != 7) {
                        isEncontrado = buscarEnArchivo((placa + ",vip-true"), nombreDeDocumento2);
                        if (isEncontrado) {
                            System.out.println("Su tarjeta es valida");
                            {
                                isTarjeta = true;
                                opcion = 1;
                            }
                        } else
                            System.out.println("Su tarjeta no es valida");
                    } else
                        System.out.println("Solo puede usar la tarjeta de Lunes a Viernes");
                }
                if (!isTarjeta) {
                    do {
                        System.out.println("Se procedera a verificar sus datos");
                        System.out.println("Ingrese su nombre");
                        nombreDeCliente = leer(nombreDeCliente, input);
                        System.out.println("Ingrese su numero de emergencia");
                        numeroDeEmergencia = leer(numeroDeEmergencia, input);
                        fijarDatos2(nombreDeCliente, numeroDeEmergencia);
                        isEncontrado = buscarEnArchivo(getNombreDeCliente(), nombreDeDocumento)
                                && buscarEnArchivo(String.valueOf(getNumeroDeEmergencia()), nombreDeDocumento);
                        if (!isEncontrado)
                            System.out.println("Intente de nuevo");
                    } while (!isEncontrado);
                    do {
                        System.out.println("\r\nIngrese el numero del tipo de alquiler:\r\n" + "1. VIP\r\n"
                                + "2. Premium\r\n" + "3. Regular\r\n");
                        obtenerOpcion(input);
                        switch (opcion) {
                            case 1:
                                tipoDeAlquiler = "vip";
                                fijarTipoDeAlquiler(tipoDeAlquiler);
                                break;
                            case 2:
                                tipoDeAlquiler = "premium";
                                fijarTipoDeAlquiler(tipoDeAlquiler);
                                break;
                            case 3:
                                tipoDeAlquiler = "regular";
                                fijarTipoDeAlquiler(tipoDeAlquiler);
                                break;
                            default:
                                System.out.println("Ingreso una opcion que no existe, intente nuevamente\r\n");
                                break;
                        }
                    } while (opcion < 1 || opcion > 3);
                }
            } else {
                System.out.println("No esta registrado, iniciara la registracion");
                System.out.println("Ingrese la marca");
                do {
                    marca = leer(marca, input);
                    if (!marca.matches("^[a-zA-Z]*$"))
                        System.out.println("Ingrese una marca valida");
                } while (!marca.matches("^[a-zA-Z]*$"));
                System.out.println("Ingrese el modelo");
                do {
                    modelo = leer(modelo, input);
                    if (!modelo.matches("[a-zA-Z0-9]*"))
                        System.out.println("Ingrese un modelo valido");
                } while (!modelo.matches("[a-zA-Z0-9]*"));

                System.out.println("Ingrese el año");
                do {
                    year = leer(year, input);
                } while (year < 1886 || year > 2021);
                fijarDatos1(marca, modelo, year);
                System.out.println("Ingrese su nombre");
                do {
                    nombreDeCliente = leer(nombreDeCliente, input);
                    if (!nombreDeCliente.matches("[a-zA-Z\\s\'\"]+"))
                        System.out.println("Ingrese su nombre completo y sin numeros");
                } while (!nombreDeCliente.matches("[a-zA-Z\\s\'\"]+"));
                System.out.println("Ingrese su numero de emergencia");
                numeroDeEmergencia = leer(numeroDeEmergencia, input);
                fijarDatos2(nombreDeCliente, numeroDeEmergencia);
                do {
                    System.out.println("\r\nIngrese el numero del tipo de alquiler:\r\n" + "1. VIP\r\n"
                            + "2. Premium\r\n" + "3. Regular\r\n");
                    obtenerOpcion(input);
                    switch (opcion) {
                        case 1:
                            tipoDeAlquiler = "vip-true";
                            fijarTipoDeAlquiler(tipoDeAlquiler);
                            System.out.println("Ingrese el email");
                            do {
                                email = leer(email, input);
                                if ((!email.contains("@") && !email.contains(".")) || email.contains(" "))
                                    System.out.println("Ingrese un email valido");
                            } while ((!email.contains("@") && !email.contains(".")) || email.contains(" "));
                            fijarEmail(email);
                            System.out.println("Ingrese la cedula");
                            do {
                                cedula = leer(cedula, input);
                                if (!cedula.matches("[a-zA-Z0-9]*"))
                                    System.out.println("Ingrese una cedula valida");
                            } while (!cedula.matches("[a-zA-Z0-9]*"));

                            System.out.println("Ingrese la licencia");
                            do {
                                licencia = leer(licencia, input);
                                if (!licencia.matches("[a-zA-Z0-9]*"))
                                    System.out.println("Ingrese una licencia valida");
                            } while (!licencia.matches("[a-zA-Z0-9]*"));

                            System.out.println("Ingrese el registro unico vehicular");
                            registroUnicoVehicular = leer(registroUnicoVehicular, input);
                            System.out.println("Ingrese la poliza de seguro");
                            polizaDeSeguro = leer(polizaDeSeguro, input);
                            fijarDatos3(cedula, licencia, registroUnicoVehicular, polizaDeSeguro);
                            break;
                        case 2:
                            tipoDeAlquiler = "premium";
                            fijarTipoDeAlquiler(tipoDeAlquiler);
                            System.out.println("Ingrese el email");
                            do {
                                email = leer(email, input);
                                if ((!email.contains("@") && !email.contains(".")) || email.contains(" "))
                                    System.out.println("Ingrese un email valido");
                            } while ((!email.contains("@") && !email.contains(".")) || email.contains(" "));
                            fijarEmail(email);
                            break;
                        case 3:
                            tipoDeAlquiler = "regular";
                            fijarTipoDeAlquiler(tipoDeAlquiler);
                            System.out.println("Ingrese el color");
                            do {
                                color = leer(color, input);
                                if (!color.matches("^[a-zA-Z]*$"))
                                    System.out.println("Ingrese un color valido");
                            } while (!color.matches("^[a-zA-Z]*$"));
                            fijarColor(color);
                            break;
                        default:
                            System.out.println("Ingreso una opcion que no existe, intente nuevamente\r\n");
                            break;
                    }
                } while (opcion < 1 || opcion > 3);
                entrada = placa + "," + marca + "," + modelo + "," + year + "," + nombreDeCliente + ","
                        + numeroDeEmergencia + "," + color + "," + email + "," + cedula + "," + licencia + ","
                        + registroUnicoVehicular + "," + polizaDeSeguro;
                escribirEnArchivo(entrada, nombreDeDocumento, true);
            }
            estacionamiento = revisarDisponibilidad();
            if (estacionamiento != null && !estacionamiento.isEmpty()) {
                horaDeEntrada = obtenerFecha();
                if (opcion == 1)
                    System.out.println("Tome su tarjeta\r\n" + "Placa:" + placa + "\r\n" + "Estacionamiento:"
                            + estacionamiento.substring(0, 2));
                else
                    System.out.println("Tome su tiquete\r\n" + "Hora de entrada:" + horaDeEntrada + "\r\n" + "Placa:"
                            + placa + "\r\n" + "Estacionamiento:" + estacionamiento.substring(0, 2));
                if (!isTarjeta) {
                    entrada = placa + "," + getTipoDeAlquiler() + "," + estacionamiento.substring(0, 2) + ","
                            + horaDeEntrada;
                    escribirEnArchivo(entrada, nombreDeDocumento2, true);
                } else {
                    try (FileReader fileReader = new FileReader(nombreDeDocumento2)) {
                        Scanner reader = new Scanner(fileReader);
                        while ((line = reader.nextLine()) != null) {
                            if (line.contains(placa + "," + "vip-true")) {
                                celdas = line.split(",");
                                line = line.replace(celdas[2], estacionamiento.substring(0, 2));
                            }
                            entradas.add(line);
                        }
                        fileReader.close();
                        reader.close();
                    } catch (Exception ex) {
                    }
                    escribirEnArchivo(entradas.get(0), nombreDeDocumento2, false);
                    for (posicion = 1; posicion < entradas.size(); posicion++) {
                        escribirEnArchivo(entradas.get(posicion), nombreDeDocumento2, true);
                    }
                    entradas.clear();
                }
            }
            mensaje = "ingresando autos al parqueadero";
            obtenerRespuesta(input, mensaje);
        } while (respuesta == 'y' || respuesta == 'Y');
        System.out.println("Ha terminado de ingrear autos al parqueadero\r\n");

    }

    public void consultarDisponibilidad() {
        final int width = 19;
        String line, borde, estacionamientos[];
        int posicion;
        try (FileReader fileReader = new FileReader("estacionamientos.txt")) {
            Scanner reader = new Scanner(fileReader);
            System.out.println("Estacionamientos:\n");
            while ((line = reader.nextLine()) != null) {
                estacionamientos = line.split(",");
                line = "";
                for (posicion = 0; posicion < estacionamientos.length; posicion++) {
                    estacionamientos[posicion] = String.format("%-" + width + "s",
                            String.format(
                                    "|%" + (estacionamientos[posicion].length()
                                            + (width - estacionamientos[posicion].length()) / 2) + "s",
                                    estacionamientos[posicion]));
                    line += estacionamientos[posicion];
                }
                borde = "―".repeat(line.length()) + "―";
                System.out.println(borde);
                System.out.println(line + "|");
                if (line.contains("E"))
                    System.out.println(borde);
            }
            fileReader.close();
            reader.close();
        } catch (Exception ex) {
        }
        System.out.println("Ha consultado la disponbilidad del parqueadero\r\n");
    }
}