# Requisitos Funcionales y No Funcionales (Nivel Enterprise)

En esta fase Empoleon, AquaPort gestiona una red multi-embalse de alta exigencia. A continuación se detallan los RF y RNF bajo el framework de priorización **MoSCoW** (Must, Should, Could, Won't).

## 1. Requisitos Funcionales (RF)

| ID | Requisito Funcional | Descripción | Prioridad (MoSCoW) |
|----|----------------------|-------------|--------------------|
| **RF-E1** | **Rutas Multi-etapa** | El sistema debe planificar rutas que involucren múltiples waypoints intermedios, dividiendo la misión en tramos lógicos. | **Must Have** |
| **RF-E2** | **Reasignación Automática en Waypoint** | Si un drone falla al llegar a un waypoint intermedio, el sistema debe reasignar dinámicamente el siguiente tramo a un drone disponible en esa misma zona. | **Must Have** |
| **RF-E3** | **Coordinación entre Zonas** | El sistema debe coordinar el traspaso físico de muestras o carga entre drones de distintas zonas (Ej: Embalse de Investigación a Red de Canales). | **Should Have** |
| **RF-E4** | **Trazabilidad de Cadena de Custodia** | Se debe registrar y firmar digitalmente (mediante criptografía) cada traspaso de una muestra de agua para mantener la cadena de custodia íntegra. | **Should Have** |
| **RF-E5** | **Reportes de Eficiencia por Zona** | El sistema debe generar resúmenes analíticos (vía Streams) mostrando el rendimiento, éxito de entregas y promedios de batería por zona hídrica. | **Could Have** |

---

## 2. Requisitos No Funcionales (RNF)

| ID | Requisito No Funcional | Métrica de Producción (SLA) | Prioridad (MoSCoW) |
|----|-------------------------|-----------------------------|--------------------|
| **RNF-E1** | **SLA de Disponibilidad** | El sistema de asignación y enrutamiento debe mantener un uptime del **99.99%** (menos de 52 minutos de caída al año). | **Must Have** |
| **RNF-E2** | **Tiempo Máximo de Reasignación** | El motor debe detectar el fallo de un drone y reasignar la misión a otro en un tiempo máximo de **200 milisegundos**. | **Must Have** |
| **RNF-E3** | **Throughput de Misiones Simultáneas** | El sistema debe soportar la gestión concurrente de **hasta 1,000 misiones simultáneas** sin degradación del servicio. | **Should Have** |
| **RNF-E4** | **Seguridad en Telemetría** | La transmisión de datos del drone al Módulo de Telemetría debe usar cifrado **AES-256** (vía patrón Decorator). | **Must Have** |
| **RNF-E5** | **Tolerancia a Desconexión** | Los drones deben poder navegar de forma autónoma al waypoint más cercano si pierden conexión con el servidor por más de **15 segundos**. | **Won't Have** *(Para esta iteración)* |

---

## 3. Matriz de Tensiones Arquitectónicas (Trade-offs)

Al elevar el nivel a Enterprise, surgen fricciones naturales entre los requisitos que obligan a tomar decisiones arquitectónicas:

### Tensión 1: Trazabilidad de Cadena (RF-E4) vs. Throughput Simultáneo (RNF-E3)
- **Conflicto:** Firmar digitalmente cada traspaso de muestra con criptografía asimétrica consume valiosos ciclos de CPU, lo que impacta negativamente en la capacidad de procesar 1,000 misiones concurrentes con baja latencia.
- **Resolución:** Implementaremos la firma digital asíncrona a través de una cola de mensajes (Message Broker), garantizando el Throughput mientras la traza se consolida en background.

### Tensión 2: Reasignación Automática (RF-E2) vs. Tiempo Máximo (RNF-E2)
- **Conflicto:** Buscar un drone disponible en una red multi-embalse extensa toma tiempo, especialmente si hay que calcular distancias para asegurar que llegue al waypoint a tiempo. Hacerlo en menos de 200ms es un reto enorme.
- **Resolución:** El motor de asignación mantendrá índices en memoria (caché espacial de drones por zona) precalculados en segundo plano, evitando consultas directas a la base de datos SQL durante la crisis de un fallo.

### Tensión 3: Coordinación de Zonas (RF-E3) vs. SLA de Disponibilidad (RNF-E1)
- **Conflicto:** Si la orquestación depende de que ambos drones (el de entrega y el de recepción) estén en línea simultáneamente de manera síncrona, un fallo en la red local de una zona detiene la operación de la otra, comprometiendo el 99.99%.
- **Resolución:** Se modelarán "Zonas de Intercambio (Buzones)" estáticas. El primer drone deposita la muestra y notifica al sistema. El segundo drone la recoge posteriormente de manera desacoplada.
