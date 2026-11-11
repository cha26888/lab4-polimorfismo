public class OperacionCancelada extends RuntimeException {

    public OperacionCancelada() {
        super("Operación cancelada. Regresando al menú principal.");
    }
}
