public class Exhibicion implements Programable {

    private String titulo;
    private Programacion programacion;

    public Exhibicion(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título de la exhibición no puede estar vacío.");
        }
        this.titulo = titulo.trim();
        this.programacion = null;
    }

    @Override
    public void programar(Programacion programacion) {
        if (programacion == null) {
            throw new IllegalArgumentException("La programación no puede estar vacía.");
        }
        this.programacion = programacion;
    }

    @Override
    public boolean estaProgramado() {
        return programacion != null;
    }

    @Override
    public boolean admiteProgramacion() {
        return true;
    }

    @Override
    public boolean participa(Equipo equipo) {
        return false;
    }

    @Override
    public boolean comparteEquipoCon(Programable otro) {
        return false;
    }

    @Override
    public String getDescripcion() {
        String texto = "Exhibición: " + titulo;
        if (programacion != null) {
            texto = texto + " | " + programacion;
        } else {
            texto = texto + " | Sin programar";
        }
        return texto;
    }

    @Override
    public Programacion getProgramacion() {
        return programacion;
    }
}
