package parqueadero;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Historial extends Sistema {
    private double totalRecaudado, dias[] = new double[7], meses[] = new double[12], regular[] = new double[2];

    public LocalDate crearFechaLocal(String fecha) {
        LocalDate localdate = LocalDate.now();
        try {
            String oldformat = "MM-dd-yy";
            String newformat = "yyyy-MM-dd";
            SimpleDateFormat formateo = new SimpleDateFormat(oldformat);
            Date fechaDeEntrada = formateo.parse(fecha);
            formateo.applyPattern(newformat);
            String newdate = formateo.format(fechaDeEntrada);
            localdate = LocalDate.parse(newdate);
        } catch (Exception ex) {
        }
        return localdate;
    }

    public int calcularDiaDeSemana(String fecha) {
        int diaDeSemana = 0;
        try {
            LocalDate localdate = crearFechaLocal(fecha);
            DayOfWeek dayOfWeek = localdate.getDayOfWeek();
            diaDeSemana = dayOfWeek.getValue();

        } catch (Exception e) {
        }
        return diaDeSemana;
    }

    public boolean buscarUsoEnSemana(int weekOfYear, int year) {
        boolean isUsadoCincoVeces = false;
        int vecesUsado = 0;
        try (FileReader fileReader = new FileReader("historial.txt")) {
            Scanner reader = new Scanner(fileReader);
            String line, celdas[], celdasDeEntrada[];
            int semana, yearOfWeek;
            while ((line = reader.nextLine()) != null) {
                if (line.contains(placa)) {
                    celdas = line.split(",");
                    if (celdas.length > 4) {
                        celdasDeEntrada = celdas[3].split("\\s+");
                        semana = crearFechaLocal(celdasDeEntrada[0]).get(ChronoField.ALIGNED_WEEK_OF_YEAR);
                        yearOfWeek = crearFechaLocal(celdasDeEntrada[0]).getMonthValue();
                        if (semana == weekOfYear && yearOfWeek == year)
                            vecesUsado += 1;
                    }
                }
            }
            fileReader.close();
            reader.close();
        } catch (Exception ex) {
        }
        if (vecesUsado >= 5)
            isUsadoCincoVeces = true;
        return isUsadoCincoVeces;
    }

    public int calcularMes(Date startDate, Date endDate) {
        int workingDays = 0;
        try {
            Calendar start = Calendar.getInstance();
            start.setTime(startDate);
            Calendar end = Calendar.getInstance();
            end.setTime(endDate);
            while (!start.after(end)) {
                int day = start.get(Calendar.DAY_OF_WEEK);
                day = day + 2;
                if (day > 7) {
                    day = day - 7;
                }
                if ((day != Calendar.SATURDAY) && (day != Calendar.SUNDAY))
                    workingDays++;
                start.add(Calendar.DATE, 1);
            }
            System.out.println("Este es su dia: " + workingDays);
        } catch (Exception e) {
        }
        return workingDays;
    }

    public void calcularRecaudaciones() {
        try (FileReader fileReader = new FileReader("historial.txt")) {
            Scanner reader = new Scanner(fileReader);
            String line, celdas[], celdasDeEntrada[];
            int mes, diaDeSemana;
            double monto;
            while ((line = reader.nextLine()) != null) {
                if (line.matches(".*\\d.*")) {
                    celdas = line.split(",");
                    monto = Double.valueOf(celdas[7]);
                    if (celdas.length > 4) {
                        celdasDeEntrada = celdas[3].split("\\s+");
                        if (line.contains("vip")) {
                            if (monto > 100) {
                                regular[1] += (monto - 100);
                                monto = 100;
                            }
                            LocalDate localDate = crearFechaLocal(celdasDeEntrada[0]);
                            mes = localDate.getMonthValue();
                            switch (mes) {
                                case 1:
                                    meses[0] += monto;
                                    break;
                                case 2:
                                    meses[1] += monto;
                                    break;
                                case 3:
                                    meses[2] += monto;
                                    break;
                                case 4:
                                    meses[3] += monto;
                                    break;
                                case 5:
                                    meses[4] += monto;
                                    break;
                                case 6:
                                    meses[5] += monto;
                                    break;
                                case 7:
                                    meses[6] += monto;
                                    break;
                                case 8:
                                    meses[7] += monto;
                                    break;
                                case 9:
                                    meses[8] += monto;
                                    break;
                                case 10:
                                    meses[9] += monto;
                                    break;
                                case 11:
                                    meses[10] += monto;
                                    break;
                                case 12:
                                    meses[11] += monto;
                                    break;
                            }
                        } else if (line.contains("premium")) {
                            if (monto > 6.50) {
                                regular[1] += (monto - 6.50);
                                monto = 6.50;
                            }
                            diaDeSemana = calcularDiaDeSemana(celdasDeEntrada[0]);
                            switch (diaDeSemana) {
                                case 1:
                                    dias[0] += monto;
                                    break;
                                case 2:
                                    dias[1] += monto;
                                    break;
                                case 3:
                                    dias[2] += monto;
                                    break;
                                case 4:
                                    dias[3] += monto;
                                    break;
                                case 5:
                                    dias[4] += monto;
                                    break;
                                case 6:
                                    dias[5] += monto;
                                    break;
                                case 7:
                                    dias[6] += monto;
                                    break;
                            }
                        } else {
                            if (celdas[6].contains("0.04"))
                                regular[0] += monto;
                            else
                                regular[1] += monto;
                        }
                    }
                }
            }
            fileReader.close();
            reader.close();
        } catch (Exception ex) {
        }
    }

    public void cobrarEstacionamiento(Scanner input) {
        final String nombreDeDocumento = "historial.txt", nombreDeDocumento2 = "estacionamientos.txt",
                format = "MM-dd-yy hh:mm:ss";
        boolean isEncontrado;
        int duracion = 0, semana = 0, year = 0, posicion, dias = 0;
        double tarifa = 0.0, monto = 0.0;
        String estacionamiento = "", horaDeSalida, mensaje, line, celdas[], celdasDeEntrada[], celdasDeSalida[],
                estacionamientos[];
        ArrayList<String> entradas = new ArrayList<String>();
        ArrayList<String> filasDeEstacionamientos = new ArrayList<String>();
        isEncontrado = buscarEnArchivo("ocupado", nombreDeDocumento2);
        if (isEncontrado) {
            System.out.println("Se recibio el tiquete");
            do {
                do {
                    obtenerPlaca(input);
                    isEncontrado = buscarEnArchivo(String.valueOf(placa), nombreDeDocumento);
                    if (!isEncontrado)
                        System.out.println("Ingreso una placa que no esta en el parqueadero, intente nuevamente");
                } while (!isEncontrado);
                horaDeSalida = obtenerFecha();
                try (FileReader fileReader = new FileReader(nombreDeDocumento)) {
                    Scanner reader = new Scanner(fileReader);
                    while ((line = reader.nextLine()) != null) {
                        if (line.contains(placa)) {
                            celdas = line.split(",");
                            celdasDeEntrada = celdas[3].split("\\s+");
                            celdasDeSalida = horaDeSalida.split("\\s+");
                            if (celdas.length > 4) {
                                if (line.contains("vip-true")) {
                                    estacionamiento = celdas[2];
                                    line = line.replace(celdas[4], horaDeSalida);
                                    try {
                                        SimpleDateFormat sdf = new SimpleDateFormat(format);
                                        Date fechaDeEntrada = sdf.parse(celdasDeEntrada[0] + " " + celdasDeEntrada[1]);
                                        Date fechaDeSalida = sdf.parse(celdasDeSalida[0] + " " + celdasDeSalida[1]);
                                        duracion = (int) ((fechaDeSalida.getTime() - fechaDeEntrada.getTime())
                                                / (60 * 1000));
                                        dias = calcularMes(fechaDeEntrada, fechaDeSalida);
                                    } catch (Exception e) {
                                    }
                                    line = line.replace(celdas[5], String.valueOf(duracion));
                                    if (dias >= 20)
                                        line = line.replace(celdas[1], "vip-false");
                                    if (dias > 20) {
                                        try {
                                            SimpleDateFormat sdf = new SimpleDateFormat(format);
                                            Date fechaDeEntrada = sdf.parse(celdasDeSalida[0] + " " + "00:00:00");
                                            Date fechaDeSalida = sdf.parse(celdasDeSalida[0] + " " + celdasDeSalida[1]);
                                            duracion = (int) ((fechaDeSalida.getTime() - fechaDeEntrada.getTime())
                                                    / (60 * 1000));
                                        } catch (Exception e) {
                                        }
                                        if (dias > 21)
                                            duracion += ((dias - 21) * 1440);
                                        line = line.replace(celdas[5], String.valueOf(duracion + 28800));
                                        line = line.replace(celdas[1], "vip-false");
                                        tarifa = 0.05;
                                        System.out.println(
                                                "Excedio los 20 dias, debera pagar el resto de tiempo utilizado con la tarifa regular de 0.05 x minuto");
                                        monto = duracion * tarifa;
                                        totalRecaudado += monto;
                                        line = line.replace("100.00,100.00",
                                                "100.00," + String.format("%.2f", (monto + 100)));
                                        System.out.println("El monto cobrado es:" + String.format("%.2f", monto));
                                    }
                                }
                            } else {
                                estacionamiento = celdas[2];
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat(format);
                                    Date fechaDeEntrada = sdf.parse(celdasDeEntrada[0] + " " + celdasDeEntrada[1]);
                                    Date fechaDeSalida = sdf.parse(celdasDeSalida[0] + " " + celdasDeSalida[1]);
                                    duracion = (int) ((fechaDeSalida.getTime() - fechaDeEntrada.getTime())
                                            / (60 * 1000));
                                    duracion = Math.abs(duracion);
                                    dias = calcularMes(fechaDeEntrada, fechaDeSalida);
                                } catch (Exception ex) {
                                }
                                if (line.contains("vip-true")) {
                                    tarifa = 100.00;
                                    monto = tarifa;
                                    totalRecaudado += monto;
                                    if (dias >= 20)
                                        line = line.replace(celdas[1], "vip-false");
                                    if (dias > 20) {
                                        try {
                                            SimpleDateFormat sdf = new SimpleDateFormat(format);
                                            Date fechaDeEntrada = sdf.parse(celdasDeSalida[0] + " " + "00:00:00");
                                            Date fechaDeSalida = sdf.parse(celdasDeSalida[0] + " " + celdasDeSalida[1]);
                                            duracion = (int) ((fechaDeSalida.getTime() - fechaDeEntrada.getTime())
                                                    / (60 * 1000));
                                        } catch (Exception e) {
                                        }
                                        if (dias > 21)
                                            duracion += ((dias - 21) * 1440);
                                        line = line.replace(celdas[1], "vip-false");
                                        tarifa = 0.05;
                                        System.out.println(
                                                "Excedio los 20 dias, debera pagar el resto de tiempo utilizado con la tarifa regular de 0.05 x minuto");
                                        monto += duracion * tarifa;
                                        totalRecaudado += monto;
                                        duracion += 28800;
                                        tarifa = 100.00;
                                    }
                                } else if (line.contains("premium")) {
                                    tarifa = 6.50;
                                    monto = tarifa;
                                    totalRecaudado += monto;
                                    if (duracion > 1440) {
                                        System.out.println(
                                                "Excedio el dia, debera pagar el resto de tiempo utilizado con la tarifa regular de 0.05 x minuto");
                                        monto = (duracion - 1440) * tarifa;
                                        totalRecaudado += monto;
                                    }
                                } else {
                                    try {
                                        semana = crearFechaLocal(celdasDeEntrada[0])
                                                .get(ChronoField.ALIGNED_WEEK_OF_YEAR);
                                        year = crearFechaLocal(celdasDeEntrada[0]).getMonthValue();
                                    } catch (Exception ex) {
                                    }
                                    if (buscarUsoEnSemana(semana, year)) {
                                        tarifa = 0.04;
                                        monto = tarifa * duracion;
                                        totalRecaudado += monto;
                                    } else {
                                        tarifa = 0.05;
                                        monto = tarifa * duracion;
                                        totalRecaudado += monto;
                                    }
                                }
                                System.out.println("La tarifa es:" + tarifa);
                                System.out.println("Estuvo " + duracion + " minutos en el estacionamiento");
                                System.out.println("El monto cobrado es:" + String.format("%.2f", monto));
                                line += "," + horaDeSalida + "," + duracion + "," + String.format("%.2f", tarifa) + ","
                                        + String.format("%.2f", monto);
                            }
                        }
                        entradas.add(line);
                    }
                    fileReader.close();
                    reader.close();
                } catch (Exception ex) {
                }
                escribirEnArchivo(entradas.get(0), nombreDeDocumento, false);
                for (posicion = 1; posicion < entradas.size(); posicion++) {
                    escribirEnArchivo(entradas.get(posicion), nombreDeDocumento, true);
                }
                entradas.clear();
                if (estacionamiento == null || estacionamiento.isEmpty())
                    System.out.println("El auto con esa placa no se encuentra en el parqueadero");
                else {
                    try (FileReader fileReader = new FileReader("estacionamientos.txt")) {
                        Scanner reader = new Scanner(fileReader);
                        while ((line = reader.nextLine()) != null) {
                            estacionamientos = line.split(",");
                            for (posicion = 0; posicion < estacionamientos.length; posicion++) {
                                if (estacionamientos[posicion].contains(estacionamiento)) {
                                    line = line.replace(estacionamientos[posicion],
                                            estacionamientos[posicion].replace("-ocupado", ""));
                                }
                            }
                            filasDeEstacionamientos.add(line);
                        }
                        System.out.println("Se ha liberado el estacionamiento:" + estacionamiento);
                        fileReader.close();
                        reader.close();
                    } catch (Exception ex) {
                    }
                    escribirEnArchivo(filasDeEstacionamientos.get(0), nombreDeDocumento2, false);
                    for (posicion = 1; posicion < filasDeEstacionamientos.size(); posicion++) {
                        escribirEnArchivo(filasDeEstacionamientos.get(posicion), nombreDeDocumento2, true);
                    }
                    filasDeEstacionamientos.clear();
                }
                mensaje = "cobrando estacionamientos";
                obtenerRespuesta(input, mensaje);
                if (!buscarEnArchivo("ocupado", nombreDeDocumento2)) {
                    System.out.println("Ya no hay mas estacionamientos ocupados para cobrar");
                    respuesta = 'n';
                }
            } while (respuesta == 'y' || respuesta == 'Y');
            System.out.println("Ha terminando de cobrar estacionamientos");
        } else
            System.out.println("El parqueadero tienen los estacionamientos libres, intente mas tarde.");
    }

    public void cerrarCaja() {
        if (totalRecaudado != 0)
            System.out.println("El total recaudado en el dia es: $" + totalRecaudado);
        else
            System.out.println(
                    "No se ha recaudado nada durante el dia, intente despues de haber cobrado un estacionamiento");
    }

    public void montoRecaudado(Scanner input) {
        calcularRecaudaciones();
        int i = 0;
        final String nombreDeDocumento = "historial.txt", nombreDeDocumento2 = "registro.txt",
                nomb_mes[] = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
                        "Octubre", "Noviembre", "Diciembre" },
                nomb_dias[] = { "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo" },
                nomb_tarifa[] = { "0.04", "0.05" };
        String line1, celdas1[], line2, celdas2[];
        do {
            System.out.println("Bienvenido a Parkings S.A.\r\n"
                    + "Ingrese el numero de la categoria de la cual desea ver el recaudo:\r\n" + "1. VIP\r\n"
                    + "2. PREMIUM\r\n" + "3. REGULAR\r\n" + "4. Todas las categorias\r\n" + "5. Salir\r\n");
            obtenerOpcion(input);
            switch (opcion) {
                case 1:
                    System.out.println("\nRecaudo de la categoria VIP por mes:");
                    for (i = 0; i < 12; i++) {
                        System.out.println(" ~ " + nomb_mes[i] + " : $" + String.format("%.2f", meses[i]));
                    }
                    break;
                case 2:
                    System.out.println("\nRecaudo de la categoria PREMIUM por dia:");
                    for (i = 0; i < 7; i++) {
                        System.out.println(" ~ " + nomb_dias[i] + " : $" + String.format("%.2f", dias[i]));
                    }
                    break;
                case 3:
                    System.out.println("\nRecaudo de la categoria REGULAR por minuto:");
                    for (i = 0; i < 2; i++) {
                        System.out
                                .println(" ~ Tarifa de " + nomb_tarifa[i] + " : $" + String.format("%.2f", regular[i]));
                    }
                    try (FileReader fr = new FileReader(nombreDeDocumento)) {
                        Scanner reader = new Scanner(fr);
                        System.out.println("CLIENTES FRECUENTES");
                        while ((line1 = reader.nextLine()) != null) {
                            if (line1.contains("0.04")) {
                                celdas1 = line1.split(",");
                                try (FileReader fr2 = new FileReader(nombreDeDocumento2)) {
                                    Scanner reader2 = new Scanner(fr2);
                                    while ((line2 = reader2.nextLine()) != null) {
                                        if (line2.contains(celdas1[0])) {
                                            celdas2 = line2.split(",");
                                            System.out.print(celdas2[4] + " " + celdas2[1] + " " + celdas2[0]);
                                        }
                                    }
                                    fr2.close();
                                    reader2.close();
                                } catch (Exception error) {
                                }
                            }
                        }
                        fr.close();
                        reader.close();
                    } catch (Exception error) {
                    }
                    break;
                case 4:
                    System.out.println("RECAUDADO EN TODAS LAS CATEGORIAS");
                    System.out.println(" - Recaudo de la categoria VIP por mes:");
                    for (i = 0; i < 12; i++) {
                        System.out.println("    ~ " + nomb_mes[i] + " : $" + String.format("%.2f", meses[i]));
                    }
                    System.out.println(" - Recaudo de la categoria PREMIUM por dia:");
                    for (i = 0; i < 7; i++) {
                        System.out.println("    ~ " + nomb_dias[i] + " : $" + String.format("%.2f", dias[i]));
                    }
                    System.out.println(" - Recaudo de la categoria REGULAR por minuto:");
                    for (i = 0; i < 2; i++) {
                        System.out.println(
                                "    ~ Tarifa de " + nomb_tarifa[i] + " : $" + String.format("%.2f", regular[i]));
                    }
                    try (FileReader fr = new FileReader(nombreDeDocumento)) {
                        Scanner reader = new Scanner(fr);
                        System.out.println("CLIENTES FRECUENTES");
                        while ((line1 = reader.nextLine()) != null) {
                            if (line1.contains("0.04")) {
                                celdas1 = line1.split(",");
                                try (FileReader fr2 = new FileReader(nombreDeDocumento2)) {
                                    Scanner reader2 = new Scanner(fr2);
                                    while ((line2 = reader2.nextLine()) != null) {
                                        if (line2.contains(celdas1[0])) {
                                            celdas2 = line2.split(",");
                                            System.out.print(celdas2[4] + " " + celdas2[1] + " " + celdas2[0]);
                                        }
                                    }
                                    fr2.close();
                                    reader2.close();
                                } catch (Exception error) {
                                }
                            }
                        }
                        fr.close();
                        reader.close();
                    } catch (Exception error) {
                    }
                    break;
                case 5:
                    System.out.println("Ha salido del menu");
                    break;
                default:
                    System.out.println("Ingreso una opcion que no existe, intente nuevamente\r\n");
                    break;
            }
        } while (opcion != 5);
    }

    public void mostrarHistorial(Scanner input) {
        final String nombreDeDocumento = "historial.txt";
        String line, celdas[];
        boolean existe = false;
        try (FileReader fr = new FileReader(nombreDeDocumento)) {
            Scanner reader = new Scanner(fr);
            obtenerPlaca(input);
            existe = buscarEnArchivo(String.valueOf(placa), nombreDeDocumento);
            if (existe == true) {
                while ((line = reader.nextLine()) != null) {
                    if (line.contains(placa)) {
                        celdas = line.split(",");
                        if (line.contains("vip")) {
                            System.out.println("\nEstacionamiento VIP");
                            System.out.println("Inicio de mes : " + celdas[3] + "\r\n" + "Fin de mes : " + celdas[4]
                                    + "\r\n" + "Monto pagado : $" + celdas[7] + "\r\n");
                        } else if (line.contains("premium")) {
                            System.out.println("\nEstacionamiento PREMIUM");
                            System.out
                                    .println("Fecha : " + celdas[3] + "\r\n" + "Monto pagado : $" + celdas[7] + "\n\n");
                        } else {
                            System.out.println("\nEstacionamiento Estandar");
                            System.out.println("Fecha : " + celdas[3] + "\r\n" + "Duracion : " + celdas[5]
                                    + " minutos\r\n" + "Monto pagado : $" + celdas[7] + "\n\n");
                        }
                    }
                }
                fr.close();
                reader.close();
            } else {
                System.out.println("La placa no se encuentra en el historial");
            }
        } catch (Exception error) {
        }

    }
}