import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Copa implements Iterable<Programable> {

    private ArrayList<Equipo> equipos;
    private ArrayList<Programable> eventos;
    private ArrayList<Puntuable> encuentros;

    public Copa() {
        this.equipos = new ArrayList<>();
        this.eventos = new ArrayList<>();
        this.encuentros = new ArrayList<>();
    }

    public void registrarEquipo(String codigo, String nombre, String empresa) {
        validarCodigoDisponible(codigo);
        equipos.add(new Equipo(codigo, nombre, empresa));
    }

    public void validarCodigoDisponible(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del equipo no puede estar vacío.");
        }
        if (buscarEquipo(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un equipo con el código " + codigo.trim() + ".");
        }
    }

    public Equipo buscarEquipo(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (Equipo equipo : equipos) {
            if (equipo.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return equipo;
            }
        }
        return null;
    }

    public void agregarEncuentro(Encuentro encuentro) {
        eventos.add(encuentro);
        encuentros.add(encuentro);
    }

    public void agregarExhibicion(Exhibicion exhibicion) {
        eventos.add(exhibicion);
    }

    public void programarEvento(Programable evento, Programacion programacion) {
        if (evento == null || programacion == null) {
            throw new IllegalArgumentException("Faltan datos para programar el evento.");
        }
        if (!evento.admiteProgramacion()) {
            throw new IllegalStateException("El evento ya tiene resultado y no puede programarse ni reprogramarse.");
        }
        validarConflictos(evento, programacion);
        evento.programar(programacion);
    }

    private void validarConflictos(Programable evento, Programacion programacion) {
        for (Programable otro : eventos) {
            if (otro != evento && otro.estaProgramado() && otro.getProgramacion().mismoHorario(programacion)) {
                if (otro.getProgramacion().mismaSede(programacion)) {
                    throw new IllegalStateException("La sede " + programacion.getSede() + " ya está ocupada a esa fecha y hora por: " + otro.getDescripcion());
                }
                if (evento.comparteEquipoCon(otro)) {
                    throw new IllegalStateException("Uno de los equipos ya tiene otro encuentro a esa fecha y hora: " + otro.getDescripcion());
                }
            }
        }
    }

    public ArrayList<Equipo> obtenerTablaPosiciones() {
        ArrayList<Equipo> tabla = new ArrayList<>(equipos);
        Collections.sort(tabla);
        return tabla;
    }

    @Override
    public Iterator<Programable> iterator() {
        ArrayList<Programable> programados = new ArrayList<>();
        for (Programable evento : eventos) {
            if (evento.estaProgramado()) {
                programados.add(evento);
            }
        }
        Collections.sort(programados);
        return Collections.unmodifiableList(programados).iterator();
    }

    public int cantidadEventos() {
        return eventos.size();
    }

    public Programable getEvento(int indice) {
        return eventos.get(indice);
    }

    public int cantidadEncuentros() {
        return encuentros.size();
    }

    public Puntuable getEncuentro(int indice) {
        return encuentros.get(indice);
    }
}
