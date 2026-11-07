import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Function;

public class VistaArena {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private Scanner scanner;

    public VistaArena() {
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        System.out.println();
        System.out.println("===== ArenaGT Lite =====");
        System.out.println("1. Registrar equipo");
        System.out.println("2. Crear encuentro");
        System.out.println("3. Crear exhibición");
        System.out.println("4. Programar o reprogramar evento");
        System.out.println("5. Registrar resultado de un encuentro");
        System.out.println("6. Mostrar tabla de posiciones");
        System.out.println("7. Mostrar calendario");
        System.out.println("0. Salir");
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String mensaje) {
        System.out.println("Operación rechazada: " + mensaje);
    }

    public void mostrarErrorReintento(String mensaje) {
        System.out.println("Dato inválido: " + mensaje);
        System.out.println("Intente de nuevo o deje la entrada en blanco para regresar al menú.");
    }

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    // Pide un dato hasta que el conversor lo acepte. Una entrada vacía cancela la operación.
    public <T> T pedirDato(String mensaje, Function<String, T> conversor) {
        while (true) {
            String texto = leerTexto(mensaje);
            if (texto.isEmpty()) {
                throw new OperacionCancelada();
            }
            try {
                return conversor.apply(texto);
            } catch (IllegalArgumentException | IllegalStateException e) {
                mostrarErrorReintento(e.getMessage());
            }
        }
    }

    public String pedirTexto(String mensaje) {
        return pedirDato(mensaje, texto -> texto);
    }

    public int pedirEntero(String mensaje) {
        return pedirDato(mensaje, this::convertirEntero);
    }

    public LocalDate pedirFecha(String mensaje) {
        return pedirDato(mensaje, this::convertirFecha);
    }

    public int convertirEntero(String texto) {
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El valor \"" + texto + "\" no es un número entero válido.");
        }
    }

    public LocalDate convertirFecha(String texto) {
        try {
            return LocalDate.parse(texto, FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Fecha inválida. Use el formato dd/MM/yyyy, por ejemplo 12/11/2026.");
        }
    }

    public void mostrarTabla(ArrayList<Equipo> tabla) {
        System.out.println();
        System.out.println("----- Tabla de posiciones -----");
        if (tabla.isEmpty()) {
            System.out.println("No hay equipos registrados.");
            return;
        }
        System.out.println(String.format("%-5s%-14s%-22s%s", "Pos.", "Equipo", "Empresa", "Puntos"));
        int posicion = 1;
        for (Equipo equipo : tabla) {
            System.out.println(String.format("%-5d%-14s%-22s%d", posicion, equipo.getNombre(), equipo.getEmpresa(), equipo.getPuntosTabla()));
            posicion = posicion + 1;
        }
    }

    public void mostrarCalendario(Iterable<Programable> calendario) {
        System.out.println();
        System.out.println("----- Calendario -----");
        boolean hayEventos = false;
        for (Programable evento : calendario) {
            System.out.println(evento.getDescripcion());
            hayEventos = true;
        }
        if (!hayEventos) {
            System.out.println("No hay eventos programados.");
        }
    }
}
