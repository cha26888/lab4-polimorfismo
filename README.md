# Lab 4 - Polimorfismo: ArenaGT Lite

**Autores:** Henry Emanuel Vicente Alvarado 26275 y Joel Alejandro Chávez Pérez 26888

Aplicación de consola en Java que simula la gestión de una copa deportiva ("ArenaGT Lite"), con registro de equipos, encuentros de distintos deportes, exhibiciones, programación de eventos (sede/fecha/hora) y una tabla de posiciones. El laboratorio está enfocado en aplicar **polimorfismo** mediante herencia, interfaces y sobrescritura de métodos.

## Objetivo del laboratorio

Modelar un sistema donde distintos tipos de eventos (encuentros deportivos y exhibiciones) se comportan de forma polimórfica a través de interfaces comunes, y donde distintos deportes (`Baloncesto`, `Fútbol sala`) especializan el comportamiento de una clase abstracta base (`Encuentro`) mediante **sobrescritura de métodos** (validación de marcador, cálculo de puntos, nombre del deporte).

## Estructura del proyecto

```
lab4-polimorfismo/
├── src/                     # Código fuente (.java)
├── bin/                     # Clases compiladas (.class)
└── docs/                    # Documentación adicional
```

## Clases e interfaces principales

### Modelo de dominio

- **`Equipo`**: representa un equipo participante (código, nombre, empresa que representa, puntos en tabla). Implementa `Comparable<Equipo>` para ordenar la tabla de posiciones (por puntos, luego nombre, luego código).
- **`Encuentro`** *(abstracta)*: representa un partido entre dos equipos. Implementa las interfaces `Programable` y `Puntuable`. Define el flujo común (programar, validar y registrar resultado) y delega en subclases los detalles propios de cada deporte mediante los métodos abstractos:
  - `validarMarcador(marcador1, marcador2)`: reglas específicas del deporte (p. ej. sin empates).
  - `calcularPuntos(propio, rival)`: puntos de tabla que otorga el resultado.
  - `getDeporte()`: nombre del deporte para mostrarlo en pantalla.
- **`Baloncesto`** *(extends `Encuentro`)*: no admite empates; otorga 2 puntos al ganador y 1 al perdedor.
- **`FutbolSala`** *(extends `Encuentro`)*: admite empates; otorga 3 puntos al ganador, 1 a cada equipo en empate y 0 al perdedor.
- **`Exhibicion`** *(implements `Programable`)*: evento programable que no afecta la tabla de posiciones ni tiene marcador (p. ej. clínicas o demostraciones).
- **`Copa`** *(implements `Iterable<Programable>`)*: contenedor del torneo. Registra equipos, encuentros y exhibiciones, valida conflictos de sede/horario al programar eventos, calcula la tabla de posiciones y permite iterar el calendario de eventos ya programados (ordenados por fecha, hora y sede).
- **`Programacion`**: value object con fecha, hora y sede de un evento; incluye utilidades para comparar horarios y sedes.

### Interfaces (contratos polimórficos)

- **`Programable`**: contrato común para todo evento que se puede programar en el calendario (`Encuentro` y `Exhibicion`). Extiende `Comparable<Programable>` con una implementación por defecto que ordena por fecha, hora y sede.
- **`Puntuable`**: contrato común para todo evento que admite un resultado y otorga puntos de tabla (lo implementan los encuentros, no las exhibiciones).

Gracias a estas interfaces, `Copa` y `VistaArena` pueden tratar encuentros y exhibiciones de forma uniforme (como `Programable`) sin conocer el tipo concreto, y tratar únicamente los encuentros como `Puntuable` cuando se trata de registrar resultados.

### Soporte

- **`EstadoEncuentro`**: enum con los estados de un encuentro (`SIN_PROGRAMAR`, `PROGRAMADO`, `FINALIZADO`).
- **`OperacionCancelada`**: excepción de control usada por la vista para cancelar una operación en curso cuando el usuario deja una entrada en blanco.
- **`ControladorArena`**: orquesta el flujo de la aplicación (patrón MVC simplificado); conecta `Copa` (modelo) con `VistaArena` (vista) y carga datos de ejemplo al iniciar.
- **`VistaArena`**: maneja toda la entrada/salida por consola (menú, lectura y validación de datos, impresión de tabla y calendario).
- **`Main`**: punto de entrada del programa.

## Menú de la aplicación

Al ejecutarse, el programa carga datos de ejemplo (4 equipos, 6 encuentros, 2 exhibiciones, algunos ya programados y con resultado) y muestra un menú interactivo:

1. Registrar equipo
2. Crear encuentro
3. Crear exhibición
4. Programar o reprogramar evento
5. Registrar resultado de un encuentro
6. Mostrar tabla de posiciones
7. Mostrar calendario
0. Salir

En cualquier solicitud de dato, dejar la entrada en blanco cancela la operación y regresa al menú principal.

## Compilación y ejecución

Desde la raíz del proyecto:

```bash
javac -d bin src/*.java
java -cp bin Main
```

## Conceptos de POO aplicados

- **Herencia y clases abstractas**: `Encuentro` define el algoritmo común de registrar resultado (plantilla) y delega los pasos variables a sus subclases `Baloncesto` y `FutbolSala`.
- **Polimorfismo**: una misma referencia `Encuentro`, `Programable` o `Puntuable` puede apuntar a distintos objetos concretos, y las llamadas a métodos como `calcularPuntos`, `validarMarcador` o `getDescripcion` ejecutan el comportamiento específico de cada clase en tiempo de ejecución.
- **Interfaces y contratos**: `Programable` y `Puntuable` separan responsabilidades (programación vs. puntuación) y permiten que `Exhibicion` participe del calendario sin tener marcador ni puntos.
- **Encapsulamiento y validación**: cada clase valida sus propios invariantes (equipos no vacíos, marcadores no negativos, sedes/horarios sin conflicto, etc.) lanzando excepciones específicas (`IllegalArgumentException`, `IllegalStateException`).
