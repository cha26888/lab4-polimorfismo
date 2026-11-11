import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Programacion {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private LocalDate fecha;
    private int hora;
    private String sede;

    public Programacion(LocalDate fecha, int hora, String sede) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede estar vacía.");
        }
        validarHora(hora);
        if (sede == null || sede.isBlank()) {
            throw new IllegalArgumentException("La sede no puede estar vacía.");
        }
        this.fecha = fecha;
        this.hora = hora;
        this.sede = sede.trim();
    }

    public static void validarHora(int hora) {
        if (hora < 0 || hora > 23) {
            throw new IllegalArgumentException("La hora debe estar entre 0 y 23.");
        }
    }

    public boolean mismoHorario(Programacion otra) {
        return fecha.equals(otra.fecha) && hora == otra.hora;
    }

    public boolean mismaSede(Programacion otra) {
        return sede.equalsIgnoreCase(otra.sede);
    }

    @Override
    public String toString() {
        return fecha.format(FORMATO) + " " + String.format("%02d", hora) + ":00 - " + sede;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getHora() {
        return hora;
    }

    public String getSede() {
        return sede;
    }
}
