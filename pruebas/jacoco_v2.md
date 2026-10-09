# Auditoría de Pruebas Unitarias - JaCoCo v2

Hemos configurado un *Quality Gate* en Maven (`pom.xml`) que obliga a todas las clases de dominio de AquaPort v2 a superar el umbral del **80% de Line Coverage**. Si una PR de la v2 tiene menos del 80%, el build arrojará un `BUILD FAILURE`.

En nuestro reporte más reciente, el `AsignadorAutomatico` alcanzó un asombroso nivel de cobertura, confirmando la solidez de la lógica de negocio.

## Documentación de Ramas de Decisión (Ramas Amarillas y Verdes)

Durante la implementación de la v2 usando TDD, identificamos varias ramas de código (condicionales lógicos `if / while`) cruciales para el negocio. JaCoCo las marca de color "amarillo" cuando solo se evalúa una condición (ej: `if (A || B)` pero la prueba solo evalúa `A`). 

A continuación detallo las ramas de decisión que cubrimos y por qué fueron vitales:

### 1. Rama: Batería inferior a 35% pero drone Disponible
**El Código:** `if (!drone.disponible() || drone.getBateria() < 35)`
- **Prueba añadida:** `droneConBateriaBaja_esDescartado()`
- **¿Por qué probar esta rama?** Si un drone no tiene el umbral mínimo (35%), el algoritmo debe descartarlo de forma limpia, pidiendo otro al Strategy sin lanzar falsas alarmas de "FALLO" a la red. Si el desarrollador invirtiera el OR por AND, misiones fracasarían en altamar porque se mandarían drones descargados.

### 2. Rama: Drone Ocupado (`EN_MISION`) pero no en `FALLO`
**El Código:** `if (!drone.disponible())` seguido de la validación `if (drone.getEstado() == EstadoDrone.FALLO)`
- **Prueba añadida:** `droneOcupadoNoNotificaFallo()`
- **¿Por qué probar esta rama?** Evita el ruido en la central. Si el Strategy devuelve un drone que está simplemente "En Misión" (no disponible), el sistema debe buscar el siguiente, **sin notificar al técnico de mantenimiento**, porque no es una avería técnica sino un tema logístico.

### 3. Rama: Agotamiento de Lista en Misiones NORMALES
**El Código:** `if (mision.getPrioridad() == Prioridad.CRITICA)` al salir del ciclo `while` sin candidatos.
- **Prueba añadida:** `misionNormalSinDrones_noNotifica()`
- **¿Por qué probar esta rama?** El negocio dice que las misiones CRÍTICAS disparan alarmas a los observadores cuando fallan, pero las NORMALES pueden simplemente dejarse en `PENDIENTE` o rechazar la creación en silencio. Si no probamos esta rama, el Centro de Control podría colapsar de alertas de prioridad baja.

### 4. Rama: El Umbral Exacto (Edge Case)
**El Código:** `drone.getBateria() < 35`
- **Prueba añadida:** `bateriaExactamenteUmbral_CasoEdge()`
- **¿Por qué probar esta rama?** El error de Off-By-One (`<` vs `<=`) es el bug más clásico. Probar exactamente el `35` garantiza que el dron puede operar con la batería justa sin ser descartado injustamente.
