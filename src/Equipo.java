public class Equipo implements Comparable<Equipo> {

    private String codigo;
    private String nombre;
    private String empresa;
    private int puntosTabla;

    public Equipo(String codigo, String nombre, String empresa) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del equipo no puede estar vacío.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del equipo no puede estar vacío.");
        }
        if (empresa == null || empresa.isBlank()) {
            throw new IllegalArgumentException("La empresa del equipo no puede estar vacía.");
        }
        this.codigo = codigo.trim();
        this.nombre = nombre.trim();
        this.empresa = empresa.trim();
        this.puntosTabla = 0;
    }

    public void sumarPuntos(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException("Los puntos de tabla a sumar no pueden ser negativos.");
        }
        puntosTabla = puntosTabla + puntos;
    }

    @Override
    public int compareTo(Equipo otro) {
        int resultado = Integer.compare(otro.puntosTabla, puntosTabla);
        if (resultado == 0) {
            resultado = nombre.compareToIgnoreCase(otro.nombre);
        }
        if (resultado == 0) {
            resultado = codigo.compareToIgnoreCase(otro.codigo);
        }
        return resultado;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (otro == null || getClass() != otro.getClass()) {
            return false;
        }
        Equipo equipo = (Equipo) otro;
        return codigo.equalsIgnoreCase(equipo.codigo);
    }

    @Override
    public int hashCode() {
        return codigo.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (" + empresa + ")";
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmpresa() {
        return empresa;
    }

    public int getPuntosTabla() {
        return puntosTabla;
    }
}
