import java.time.LocalDate;
import java.util.NoSuchElementException;

public class ControladorArena {

    private Copa copa;
    private VistaArena vista;

    public ControladorArena() {
        this.copa = new Copa();
        this.vista = new VistaArena();
        cargarDatosIniciales();
    }

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            vista.mostrarMenu();
            try {
                String texto = vista.leerTexto("Opción: ");
                if (texto.isEmpty()) {
                    continue;
                }
                int opcion = vista.convertirEntero(texto);
                switch (opcion) {
                    case 1:
                        registrarEquipo();
                        break;
                    case 2:
                        crearEncuentro();
                        break;
                    case 3:
                        crearExhibicion();
                        break;
                    case 4:
                        programarEvento();
                        break;
                    case 5:
                        registrarResultado();
                        break;
                    case 6:
                        mostrarTabla();
                        break;
                    case 7:
                        mostrarCalendario();
                        break;
                    case 0:
                        salir = true;
                        vista.mostrarMensaje("Programa finalizado.");
                        break;
                    default:
                        vista.mostrarError("La opción " + opcion + " no existe en el menú.");
                }
            } catch (OperacionCancelada e) {
                vista.mostrarMensaje(e.getMessage());
            } catch (IllegalArgumentException | IllegalStateException e) {
                vista.mostrarError(e.getMessage());
            } catch (NoSuchElementException e) {
                salir = true;
            }
        }
    }

    private void cargarDatosIniciales() {
        copa.registrarEquipo("FAR", "Faro", "Banco Quetzal");
        copa.registrarEquipo("SOL", "Sol", "Cafés del Valle");
        copa.registrarEquipo("LUN", "Luna", "Banco Quetzal");
        copa.registrarEquipo("ROC", "Roca", "Cafés del Valle");

        Equipo faro = copa.buscarEquipo("FAR");
        Equipo sol = copa.buscarEquipo("SOL");
        Equipo luna = copa.buscarEquipo("LUN");
        Equipo roca = copa.buscarEquipo("ROC");

        Encuentro encuentro1 = new FutbolSala(faro, sol);
        Encuentro encuentro2 = new Baloncesto(sol, faro);
        Encuentro encuentro3 = new FutbolSala(luna, roca);
        Encuentro encuentro4 = new FutbolSala(luna, sol);
        Encuentro encuentro5 = new Baloncesto(roca, luna);
        Encuentro encuentro6 = new FutbolSala(faro, roca);
        Exhibicion exhibicion1 = new Exhibicion("Clínica de baloncesto");
        Exhibicion exhibicion2 = new Exhibicion("Demostración de fútbol sala");

        copa.agregarEncuentro(encuentro1);
        copa.agregarEncuentro(encuentro2);
        copa.agregarEncuentro(encuentro3);
        copa.agregarEncuentro(encuentro4);
        copa.agregarEncuentro(encuentro5);
        copa.agregarEncuentro(encuentro6);
        copa.agregarExhibicion(exhibicion1);
        copa.agregarExhibicion(exhibicion2);

        copa.programarEvento(encuentro1, new Programacion(LocalDate.of(2026, 11, 12), 9, "Cancha Norte"));
        copa.programarEvento(encuentro2, new Programacion(LocalDate.of(2026, 11, 12), 14, "Gimnasio Central"));
        copa.programarEvento(encuentro3, new Programacion(LocalDate.of(2026, 11, 13), 9, "Cancha Norte"));
        copa.programarEvento(encuentro4, new Programacion(LocalDate.of(2026, 11, 13), 14, "Cancha Norte"));
        copa.programarEvento(encuentro5, new Programacion(LocalDate.of(2026, 11, 14), 10, "Gimnasio Central"));
        copa.programarEvento(exhibicion1, new Programacion(LocalDate.of(2026, 11, 12), 9, "Gimnasio Central"));

        encuentro1.registrarResultado(3, 1);
        encuentro2.registrarResultado(78, 74);
        encuentro3.registrarResultado(2, 2);
        encuentro4.registrarResultado(1, 0);
    }

    private void registrarEquipo() {
        String codigo = vista.pedirDato("Código del equipo: ", texto -> {
            copa.validarCodigoDisponible(texto);
            return texto;
        });
        String nombre = vista.pedirTexto("Nombre del equipo: ");
        String empresa = vista.pedirTexto("Empresa que representa: ");
        copa.registrarEquipo(codigo, nombre, empresa);
        vista.mostrarMensaje("Equipo registrado correctamente.");
    }

    private void crearEncuentro() {
        vista.mostrarMensaje("Deportes disponibles:");
        vista.mostrarMensaje("1. Fútbol sala");
        vista.mostrarMensaje("2. Baloncesto");
        int deporte = vista.pedirDato("Deporte: ", texto -> {
            int numero = vista.convertirEntero(texto);
            if (numero != 1 && numero != 2) {
                throw new IllegalArgumentException("El deporte " + numero + " no existe.");
            }
            return numero;
        });
        Equipo equipo1 = vista.pedirDato("Código del equipo 1: ", this::buscarEquipoExistente);
        Equipo equipo2 = vista.pedirDato("Código del equipo 2: ", texto -> {
            Equipo equipo = buscarEquipoExistente(texto);
            if (equipo.equals(equipo1)) {
                throw new IllegalArgumentException("Un equipo no puede jugar contra sí mismo.");
            }
            return equipo;
        });
        Encuentro encuentro;
        if (deporte == 1) {
            encuentro = new FutbolSala(equipo1, equipo2);
        } else {
            encuentro = new Baloncesto(equipo1, equipo2);
        }
        copa.agregarEncuentro(encuentro);
        vista.mostrarMensaje("Encuentro creado: " + encuentro.getDescripcion());
    }

    private void crearExhibicion() {
        String titulo = vista.pedirTexto("Título de la exhibición: ");
        Exhibicion exhibicion = new Exhibicion(titulo);
        copa.agregarExhibicion(exhibicion);
        vista.mostrarMensaje("Exhibición creada: " + exhibicion.getDescripcion());
    }

    private void programarEvento() {
        Programable evento = elegirEvento();
        // Los conflictos de sede u horario dependen de los tres datos, por eso se vuelven a pedir juntos.
        while (true) {
            LocalDate fecha = vista.pedirFecha("Fecha (dd/MM/yyyy): ");
            int hora = vista.pedirDato("Hora de inicio (0 a 23): ", texto -> {
                int numero = vista.convertirEntero(texto);
                Programacion.validarHora(numero);
                return numero;
            });
            String sede = vista.pedirTexto("Sede: ");
            try {
                copa.programarEvento(evento, new Programacion(fecha, hora, sede));
                break;
            } catch (IllegalArgumentException | IllegalStateException e) {
                vista.mostrarErrorReintento(e.getMessage());
            }
        }
        vista.mostrarMensaje("Evento programado: " + evento.getDescripcion());
    }

    private void registrarResultado() {
        Puntuable encuentro = elegirEncuentro();
        // Reglas como "no hay empates en baloncesto" dependen de ambos marcadores, por eso se vuelven a pedir juntos.
        while (true) {
            int marcador1 = pedirMarcador("Marcador del equipo 1: ");
            int marcador2 = pedirMarcador("Marcador del equipo 2: ");
            try {
                encuentro.registrarResultado(marcador1, marcador2);
                break;
            } catch (IllegalArgumentException | IllegalStateException e) {
                vista.mostrarErrorReintento(e.getMessage());
            }
        }
        vista.mostrarMensaje("Resultado registrado: " + encuentro.getDescripcion());
    }

    private int pedirMarcador(String mensaje) {
        return vista.pedirDato(mensaje, texto -> {
            int marcador = vista.convertirEntero(texto);
            Encuentro.validarMarcadorNoNegativo(marcador);
            return marcador;
        });
    }

    private void mostrarTabla() {
        vista.mostrarTabla(copa.obtenerTablaPosiciones());
    }

    private void mostrarCalendario() {
        vista.mostrarCalendario(copa);
    }

    private Equipo buscarEquipoExistente(String codigo) {
        Equipo equipo = copa.buscarEquipo(codigo);
        if (equipo == null) {
            throw new IllegalArgumentException("No existe un equipo con el código " + codigo + ".");
        }
        return equipo;
    }

    private Programable elegirEvento() {
        if (copa.cantidadEventos() == 0) {
            throw new IllegalStateException("No hay eventos creados.");
        }
        vista.mostrarMensaje("Eventos:");
        for (int i = 0; i < copa.cantidadEventos(); i++) {
            vista.mostrarMensaje((i + 1) + ". " + copa.getEvento(i).getDescripcion());
        }
        return vista.pedirDato("Número del evento: ", texto -> {
            int numero = vista.convertirEntero(texto);
            if (numero < 1 || numero > copa.cantidadEventos()) {
                throw new IllegalArgumentException("El evento número " + numero + " no existe.");
            }
            Programable evento = copa.getEvento(numero - 1);
            if (!evento.admiteProgramacion()) {
                throw new IllegalStateException("El evento ya tiene resultado y no puede programarse ni reprogramarse.");
            }
            return evento;
        });
    }

    private Puntuable elegirEncuentro() {
        if (copa.cantidadEncuentros() == 0) {
            throw new IllegalStateException("No hay encuentros creados.");
        }
        vista.mostrarMensaje("Encuentros:");
        for (int i = 0; i < copa.cantidadEncuentros(); i++) {
            vista.mostrarMensaje((i + 1) + ". " + copa.getEncuentro(i).getDescripcion());
        }
        return vista.pedirDato("Número del encuentro: ", texto -> {
            int numero = vista.convertirEntero(texto);
            if (numero < 1 || numero > copa.cantidadEncuentros()) {
                throw new IllegalArgumentException("El encuentro número " + numero + " no existe.");
            }
            Puntuable encuentro = copa.getEncuentro(numero - 1);
            encuentro.validarAdmiteResultado();
            return encuentro;
        });
    }
}
