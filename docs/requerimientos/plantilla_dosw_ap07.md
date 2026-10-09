# Plantilla DOSW: RF AP-07 Asignar automáticamente drone a misión hídrica

## 1. Información General
- **ID:** AP-07
- **Nombre:** Asignar automáticamente drone a misión hídrica
- **Actor Principal:** Solicitante / Operador Hídrico (Supervisión)
- **Precondiciones:** Existen drones registrados en la flota y la *API de Condiciones Hídricas* está operativa.
- **Postcondiciones:** Un drone con batería ≥35% y capacidad apta queda asignado a la misión. El sistema de observadores notifica la asignación o falla.

## 2. Diccionario de Datos (Modelo de IO)

| Nombre | Tipo de campo | Reglas | Oblig. |
| :--- | :--- | :--- | :---: |
| `carga` | Objeto | — | Sí |
| `carga.peso` | Integer | Entre 1 y 1500 gramos | Sí |
| `carga.tipo` | Enum | `MUESTRA_AGUA`, `SENSOR`, `PAQUETE_LIGERO`, `EQUIPO_MEDICION` | Sí |
| `carga.prioridad` | Enum | `CRITICA`, `ALTA`, `NORMAL`, `BAJA` | Sí |
| `zonaDestino` | Enum | `EMBALSE_NORTE`, `CANAL_CENTRAL`, `LAGUNA_SUR`, `RIBERA_ESTE`, `LAB_HIDRICO` | Sí |
| `droneAsignado` | Objeto | Calculado por el sistema según la estrategia activa | No (salida) |
| `droneAsignado.id` | String | Formato `AR-XX` | No (salida) |
| `droneAsignado.tipo` | String | `SUPERFICIAL` / `SEMISUMERGIDO` / `BUCEADOR` | No (salida) |

## 3. Flujo de Eventos

### Flujo Básico (Camino Feliz)
1. **Recibir solicitud:** El sistema recibe el payload con la `carga` y la `zonaDestino`.
2. **Consultar condiciones hídricas:** El sistema invoca la *API de Condiciones Hídricas* enviando la `zonaDestino` y obtiene agitación, profundidad y temperatura.
3. **Filtrar drones aptos:** El sistema filtra los drones que están `DISPONIBLES` y soportan la agitación/profundidad requerida.
4. **Aplicar estrategia:** El sistema aplica la estrategia de selección basada en la `carga.prioridad` (Ej. `MayorBateriaStrategy` para normal, `PrioridadCriticaStrategy` para críticas).
5. **Validar:** El `ValidadorMision` corrobora las reglas de negocio (ver sección 4).
6. **Asignar:** Se asocia el `droneAsignado` a la Misión y pasa a estado `EN_MISION`.
7. **Notificar observadores:** Se dispara un evento `onDroneAsignado` a los observadores suscritos (Centro de Control ECI).

### Flujos Alternos
- **FA-01: Sin drones disponibles:** Si tras el paso 3 o 4 el filtrado devuelve `Empty`, el sistema rechaza la solicitud notificando: *"No hay drones operativos en este momento"*. La misión queda `PENDIENTE`.
- **FA-02: Condiciones hídricas adversas:** Si en el paso 2 la API reporta nivel de agitación extremo, el sistema aborta la creación de la misión notificando: *"Ruta bloqueada por clima/condiciones extremas"*.
- **FA-03: Carga supera capacidad del tipo:** Si en el paso 5 la validación falla por exceso de peso (ej. el drone filtrado es un *DroneBuceador* pero la `carga.peso` es de 500g), el algoritmo descarta ese drone e intenta asignar al siguiente en la lista filtrada. Si no hay ninguno, invoca FA-01.
- **FA-04: Drone en estado de FALLO detectado:** Si durante la asignación el drone lanza un evento interno de `FALLO`, el Asignador interrumpe, notifica a `CentroControlObserver` y `TecnicoMantenimientoObserver`, y retoma el paso 3 descartando al drone averiado.

## 4. Reglas de Negocio
- **RN-01 Batería Mínima Operativa:** Ningún drone puede ser asignado si su batería restante es estrictamente menor al 35% al momento del registro.
- **RN-02 Límite Estructural de Buceo:** Un `DroneBuceador` jamás podrá ser asignado a misiones donde la `carga.peso` sea mayor a 300 gramos, sin importar su nivel de batería o prioridad.
- **RN-03 Límite Estructural Superficial:** Un `DroneSuperficial` jamás podrá superar cargas de 500 gramos.
- **RN-04 Límite Estructural Semisumergido:** Un `DroneSemisumergido` jamás podrá superar cargas de 1500 gramos.
