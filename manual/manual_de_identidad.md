# Manual de Identidad - AquaPort MVP

## 1. Paleta de Colores
- **Color Primario (Fondo Técnico):** Azul profundo / Dark Navy (`#0B3D91`). Transmite calma, inmersión acuática profunda y precisión técnica. Evita la fatiga visual del operador.
- **Color Secundario (Acentos):** Cian técnico (`#00E5FF`). Brinda un contraste alto y legible contra el fondo oscuro, representando tecnología de punta.
- **Fondo de Interfaz (Dark Mode):** Gris muy oscuro (`#121212`). Ideal para entornos de monitoreo prolongado en cuartos de control.

## 2. Colores de Estado (Semántica Crítica)
- **Disponible:** Verde (`#00E676`) — Indica que el drone está listo para recibir órdenes.
- **En misión:** Azul (`#2979FF`) — Indica actividad operativa normal.
- **Recargando:** Amarillo / Ámbar (`#FFC107`) — Indica pausa temporal y estado de alerta moderada.
- **Mantenimiento:** Gris (`#9E9E9E`) — Indica inactividad programada (no interactuable).
- **Fallo:** Rojo (`#FF1744`) — Requiere atención crítica e inmediata del operador.

## 3. Tipografía
- **Interfaz General:** `Inter` o `Roboto` (Sans-serif). Moderna, limpia y muy legible en tamaños pequeños en pantallas oscuras.
- **Datos Críticos (IDs, Códigos):** `Fira Code` o similares (Monoespaciada). Evita confusiones entre caracteres (como "O" y "0"), lo cual es vital para identificar drones (Ej: `AQ-01`).

## 4. Tono de Voz
- **Técnico, preciso y sin ambigüedades.** Mensajes directos para un entorno operativo. (Ej: "Batería insuficiente: 18%. Mínimo: 35%", en lugar de "¡Ups! El drone está descargado").

---

## Panel de Monitoreo: Cumplimiento de Principios de Nielsen

La pantalla generada de monitoreo aplica exitosamente las heurísticas de Nielsen para garantizar usabilidad crítica:

1. **#1 Visibilidad del estado del sistema:** Cada drone posee su propio componente en donde su estado (Disponible, En misión, Recargando, Fallo) es visible a primera vista, apoyado por codificación de colores exactos del manual (Verde, Azul, Ámbar, Rojo).
2. **#2 Coincidencia entre el sistema y el mundo real:** El diseño usa íconos estandarizados en el mundo físico (ícono de batería llenándose, pin de geolocalización) y vocabulario específico del dominio del Operador ("Zona", "Misión", "Fallo").
3. **#4 Consistencia y estándares:** El layout de las 4 tarjetas de los drones es idéntico. La ubicación del ID, la métrica de la batería y la zona se encuentran en el mismo eje X/Y en todas las tarjetas, eliminando el esfuerzo de buscar información.
4. **#5 Prevención de errores:** La tarjeta del drone "Recargando" o en "Fallo" previene clics accidentales al advertir mediante sus insignias de estado (badges) de color ambar/rojo que dichos vehículos no son seleccionables en el flujo de asignación.
5. **#8 Diseño estético y minimalista:** La interfaz es sumamente limpia (relación señal/ruido altísima). Muestra estrictamente lo vital que el operador necesita para decidir: ID del dispositivo, estado actual, métrica visual de batería y la ubicación. No hay menús ni textos innecesarios que distraigan la monitorización.
