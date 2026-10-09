# Requerimientos de AquaPort MVP

## Requerimientos Funcionales (RF)
1. **RF1 (Registrar misión):** El **Operador Hídrico** debe poder **registrar una nueva misión** (especificando drone, punto de partida y llegada), para obtener en pantalla la confirmación del **código único de misión generado**.
2. **RF2 (Consultar disponibilidad):** El **Operador Hídrico** debe poder **consultar la lista de drones**, obteniendo en pantalla un **listado filtrado de los drones cuyo estado actual sea "disponible"**.
3. **RF3 (Exportar reporte):** El **Administrador ECI** debe poder **exportar un reporte de las misiones diarias**, descargando un **archivo de texto (.txt) con el registro histórico de las asignaciones**.

## Requerimientos No Funcionales (RNF)
1. **RNF1 (Rendimiento):** La consulta de drones disponibles en el `RegistradorMisiones` debe retornar resultados en **menos de 200ms** para una flota en memoria de hasta 10 drones, comprobado mediante `assertTimeout` en JUnit 5.
2. **RNF2 (Integridad/Confiabilidad):** El constructor de misiones (`Builder`) debe rechazar y lanzar `IllegalStateException` en el **100% de los intentos de creación** donde el dron asignado tenga su estado `disponible == false` o la batería sea menor al 20%.
3. **RNF3 (Portabilidad):** El sistema MVP (ejecutable desde consola) debe compilar exitosamente y ejecutarse sin errores de sintaxis en cualquier entorno que posea **Java 17 o superior**, sin requerir configuraciones adicionales de variables de entorno complejas.

## Priorización MoSCoW

| Requerimiento | Prioridad | Justificación |
|---|---|---|
| **RF1** (Registrar Misión) | **Must Have** | Es el corazón de la operativa; sin la capacidad de crear misiones el sistema no tiene razón de ser. |
| **RF2** (Consultar Drones) | **Must Have** | Es un paso previo estrictamente obligatorio y necesario para poder asignar un drone manualmente a una misión. |
| **RF3** (Exportar Reporte) | **Could Have** | Es deseable para la gerencia, pero el proceso operativo de asignar drones no se detiene si esta función se retrasa. |
| **RNF1** (Rendimiento <200ms) | **Must Have** | Una respuesta lenta al consultar drones frustraría al operador y entorpecería la fluidez esencial del MVP. |
| **RNF2** (Integridad Builder) | **Must Have** | Construir misiones con drones descargados o no disponibles corrompería todo el estado lógico de la aplicación. |
| **RNF3** (Portabilidad Java 17) | **Should Have** | Es altamente recomendable para estandarizar el despliegue, aunque el código podría funcionar en versiones similares con mínimos ajustes. |

## Plantilla DOSW: Caso de Uso

| Campo | Detalle |
|---|---|
| **Código** | AP-01 |
| **Nombre** | Registrar misión de transporte de muestra |
| **Actor** | Operador Hídrico |
| **Precondiciones** | - Debe haber al menos 1 drone disponible con batería ≥ 35%. |
| **Datos de Entrada** | - `drone`: Objeto `DroneAcuatico(id:String, bateria:int, disponible:boolean, zona:String)`<br>- `puntoPartida`: `String`<br>- `puntoLlegada`: `String`<br>- `tipoCarga`: `Enum(MUESTRA_AGUA, SUMINISTROS, EQUIPO_MEDICION)` |
| **Datos de Salida** | - `codigoMision`: `String` (ID de la misión generada exitosamente) |

### Flujo Básico
1. El Operador Hídrico selecciona un drone disponible del listado proporcionado por el sistema.
2. El Operador ingresa el `puntoPartida`, `puntoLlegada` y el `tipoCarga` a transportar.
3. El sistema valida los datos ingresados utilizando el `ValidadorMision` para asegurar el cumplimiento de las reglas de negocio.
4. El sistema guarda la nueva misión en el `RepositorioMisiones` y actualiza el estado inicial a `PENDIENTE`.
5. El sistema notifica al Operador Hídrico el éxito de la operación mostrando el `codigoMision` generado.

### Flujos Alternos
- **A1. Drone sin batería suficiente:** En el paso 3, si el drone seleccionado tiene una batería insuficiente para el trayecto (ej. < 35%), el sistema cancela el registro y muestra un mensaje de error solicitando seleccionar otro dispositivo.
- **A2. Zona de destino inválida:** En el paso 3, si el `puntoLlegada` especificado es desconocido o está fuera del alcance de la flota, el sistema alerta al operador de destino inválido y cancela el registro.

### Reglas de Negocio
1. **RN-01:** Un drone no puede tener más de 1 misión activa (estado `PENDIENTE` o `EN_CURSO`) de forma simultánea.
2. **RN-02:** El `puntoPartida` ingresado en la misión debe coincidir con la `zona` actual en la que está ubicado el drone asignado.

## Diseño UI: Panel de Monitoreo de la Flota

**Historia de Usuario Relacionada:** "Como operador hídrico, quiero ver qué drones están disponibles y su nivel de batería para asignar la misión al drone más adecuado según la zona." (Corresponde al RF: Ver flota de drones).

### Prompt Base Utilizado para IA
> "Actúa como diseñador UX/UI senior de sistemas de monitoreo ambiental. SISTEMA: AquaPort — Panel de control de flota de drones acuáticos ECI. PANTALLA: Panel de monitoreo de la flota. ESTILO: Paleta azul profundo y cian técnico. Fondo oscuro tipo dashboard técnico hídrico. ACTOR: Operador Hídrico. DATOS A MOSTRAR POR DRONE: ID (formato AQ-XX), batería en %, estado con colores (DISPONIBLE Verde, EN_MISION Azul, RECARGANDO Ámbar, MANTENIMIENTO Gris, FALLO Rojo), zona actual. ESTADO DE LA PANTALLA: [Variable según estado]. Diseño UI de alta calidad, Dribbble style, sin marcos de dispositivo."

### Estado 1: Normal
![Estado Normal](file:///c:/Users/USUARIO/Desktop/Universidad/DOWS/CORTE02/AquaPort-Proyecto/docs/requerimientos/img/panel_normal.jpg)
- **Justificación:** Muestra la flota operando con normalidad. Permite al operador ver rápidamente qué drones están "Disponible" (verde) listos para recibir misiones, cuáles están "En Misión" (azul) y cuáles están "Recargando" (ámbar).

### Estado 2: Alerta
![Estado Alerta](file:///c:/Users/USUARIO/Desktop/Universidad/DOWS/CORTE02/AquaPort-Proyecto/docs/requerimientos/img/panel_alerta.jpg)
- **Justificación:** Resalta un drone en estado FALLO. Se utiliza iluminación y componentes en color rojo intenso, atrayendo inmediatamente la atención periférica del operador para que tome acción urgente.

### Estado 3: Vacío (Empty State)
![Estado Vacío](file:///c:/Users/USUARIO/Desktop/Universidad/DOWS/CORTE02/AquaPort-Proyecto/docs/requerimientos/img/panel_vacio.jpg)
- **Justificación:** Cuando todos los drones están ocupados y no hay opciones disponibles, en lugar de mostrar una pantalla en blanco que parece un error, se presenta un gráfico técnico indicando claramente la razón ("Todos los drones se encuentran en misión").

### Cumplimiento de Heurísticas de Nielsen
1. **#1 Visibilidad del estado:** El color de los "badges" en las tarjetas (Verde, Azul, Ámbar, Rojo) comunica en un milisegundo qué está haciendo exactamente cada componente de la flota.
2. **#8 Diseño estético y minimalista:** La interfaz oscura y limpia elimina el "ruido visual". Solo se muestra la información crítica solicitada en el requerimiento: ID, Batería, Estado y Zona.
3. **#5 Prevención de errores:** Los drones en estado "Recargando" o "Fallo" usan colores cálidos/de alerta que disuaden visualmente de seleccionarlos. En el Estado Vacío (Empty State), directamente se elimina la lista interactiva, previniendo que el operador intente asignar misiones a la nada.
