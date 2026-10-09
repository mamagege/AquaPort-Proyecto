# Requerimientos AquaPort v2 (Prinplup)

## Requisitos Funcionales (RF)

| ID | Requisito Funcional | MoSCoW | Descripción |
| :--- | :--- | :---: | :--- |
| **AP-05** | **Asignación Automática** | **Must** | El sistema debe asignar el drone disponible más óptimo de la flota sin intervención humana, apoyándose en la evaluación algorítmica de la zona y tipo de carga. |
| **AP-06** | **Gestión de Alertas de Fallo** | **Must** | El sistema debe emitir una notificación push estructurada al *Sistema de Alertas* y al *Técnico de Mantenimiento* en el instante en que un drone reporte el estado `FALLO`. |
| **AP-07** | **Consulta de Condiciones Hídricas** | **Should** | El sistema debe consumir la *API de Condiciones Hídricas* para validar agitación y temperatura antes de instanciar la autorización de ruta con el Centro de Control. |
| **AP-08** | **Confirmación de Técnico** | **Could** | El sistema debe permitir al *Técnico de Mantenimiento* ingresar un informe de corrección y cambiar el estado del drone de `FALLO` a `DISPONIBLE`. |


## Requisitos No Funcionales (RNF)

| ID | Requisito No Funcional | MoSCoW | Métrica de Aceptación (SMART) |
| :--- | :--- | :---: | :--- |
| **RNF-04** | **Eficiencia Algorítmica** | **Must** | El algoritmo de asignación (`AsignadorAutomatico`) debe seleccionar el drone óptimo en menos de **400ms** para una flota de hasta **30 drones**, medido con `assertTimeout` de JUnit 5. |
| **RNF-05** | **Concurrencia de Notificaciones** | **Should** | El patrón *Observer* debe ser capaz de procesar hasta **50 notificaciones simultáneas de FALLO** sin bloquear el hilo principal de la aplicación por más de **1 segundo**. |
| **RNF-06** | **Tolerancia a Fallos API** | **Must** | Si la *API de Condiciones Hídricas* no responde tras **3 intentos con un timeout de 2000ms**, el sistema debe aplicar parámetros seguros por defecto para autorizar la ruta. |
| **RNF-07** | **Calidad de Código y Deuda** | **Must** | Todo código nuevo de la v2 introducido debe mantener **0 Bugs, 0 Vulnerabilidades** y tener un mínimo del **85% de Line Coverage** verificado en SonarQube/JaCoCo. |


---

## Análisis de Tensión Operativa: AP-07 vs AP-08 (MVP vs v2)

Durante la recolección de requisitos se detectó la siguiente tensión técnica en el dominio del negocio:

> **AP-07:** "El sistema asigna automáticamente el drone de mayor batería disponible para cualquier misión hídrica."
> **AP-08:** "Las misiones CRÍTICAS tienen prioridad absoluta: deben recibir el drone técnicamente más apto para la zona, independientemente de la batería."

### El Problema (La Tensión)
Existe un conflicto natural entre optimizar la duración de la flota global (cuidando que todos se descarguen parejo, enviando al más cargado) frente a asegurar el éxito de una misión específica que podría salvar una vida humana o equipo muy valioso (enviando al más fuerte/robusto, aunque tenga apenas batería suficiente para el viaje redondo).

### Solución Arquitectónica Recomendada
Para resolver esta tensión sin romper el OCP, se ha implementado el **Patrón Strategy**. La resolución funciona así:

1. El sistema primero evalúa el enum `Prioridad` de la Misión a registrar.
2. Si la prioridad es `NORMAL` o `BAJA`, el contexto instancia la `MayorBateriaStrategy`. Esta estrategia asume el riesgo normal y privilegia **AP-07**, eligiendo el drone con batería más alta.
3. Si la prioridad es `CRITICA` o `ALTA`, el contexto dinámicamente cambia a `PrioridadCriticaStrategy`. Este algoritmo ignora la optimización de flota (rompe AP-07 a propósito) para privilegiar el **AP-08**, evaluando la `capacidadCargaMax` y robustez de la zona por encima de todo, siempre que el nivel de batería sea `> 35%` (mínimo operativo estricto validado por `ValidadorMision`).
