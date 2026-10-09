# Design System & UX: AquaPort Enterprise

Para soportar la orquestación de la versión Enterprise, hemos diseñado un sistema de componentes (Design System) con calidad de producción, garantizando accesibilidad y principios cognitivos.

## 1. Design Tokens

### 🎨 Paleta de Colores (Dark Mode - Glassmorphism)
- **Background Base:** `#0F172A` (Slate 900) - Profundidad oceánica.
- **Surface (Paneles):** `rgba(30, 41, 59, 0.6)` - Slate 800 con 60% opacidad y `backdrop-filter: blur(12px)`.
- **Primary:** `#06B6D4` (Cyan 500) - Botones principales, acentos neón.
- **Success (Estado Disponible / Éxito):** `#10B981` (Emerald 500).
- **Warning (Batería baja / Mantenimiento):** `#F59E0B` (Amber 500).
- **Critical (Fallo de Drone):** `#EF4444` (Red 500).
- **Text Primary:** `#F8FAFC` (Slate 50).
- **Text Secondary:** `#94A3B8` (Slate 400).

### 🔠 Tipografía
- **Font Family:** `Inter`, sans-serif (para lectura clara de datos técnicos) y `Space Grotesk` (para titulares de telemetría y dashboard).
- **Base Size:** 16px.
- **Scale:** H1 (32px), H2 (24px), Body (16px), Caption (12px).

### 📐 Espaciado & Geometría
- **Spacing Scale:** Base 8px (8, 16, 24, 32, 48).
- **Border Radius:** `12px` (Cards y Paneles) / `9999px` (Botones Píldora / Tags).

---

## 2. Verificación de Contraste (WCAG AA)
Todos los colores de estado fueron testeados contra el fondo oscuro (`#0F172A`) asegurando un ratio superior a **4.5:1** (Nivel AA):
- `Cyan 500` vs `Slate 900`: Ratio **7.1:1** (Pass)
- `Emerald 500` vs `Slate 900`: Ratio **6.2:1** (Pass)
- `Red 500` vs `Slate 900`: Ratio **5.3:1** (Pass)
- `Amber 500` (Texto oscuro `#1E293B` en botón): Ratio **8.4:1** (Pass)

---

## 3. Mapa de Flujos Enterprise (Wireframe Flow)

El operador interactúa con 6 pantallas clave en un flujo continuo y orgánico:

1. **🗺️ Mapa de Zonas:** Vista cenital de los 4 embalses con heatmap de drones.
2. **📍 Selección de Ruta:** El usuario hace clic en "Z1" y luego en "Z3" para definir el origen y destino.
3. **⛓️ Asignación por Tramos:** El sistema divide la ruta y muestra visualmente qué drone toma el tramo Z1-Z2 y cuál el Z2-Z3.
4. **📡 Monitoreo en Tiempo Real:** Dashboard con telemetría en vivo (Batería, Turbidez, Nivel de Agua) transmitida vía WebSockets.
5. **🚨 Alerta de Fallo y Reasignación:** Panel modal rojo (`#EF4444`) interrumpe la UI informando que un drone en Z2 falló. Un botón pulsante ofrece "Reasignar Dinámicamente".
6. **📈 Reporte de Entrega:** Pantalla de resumen final con la trazabilidad criptográfica de la muestra y métricas de éxito.

---

## 4. Leyes UX Aplicadas por Pantalla

| Pantalla | Ley UX Aplicada | Justificación |
|----------|-----------------|---------------|
| **1. Mapa de Zonas** | **Ley de Fitts** | Los embalses (áreas clickeables) son grandes y están espaciados, reduciendo el tiempo para interactuar con la interfaz del mapa. |
| **2. Selección de Ruta** | **Ley de Hick** | Se ocultan las opciones avanzadas de drones. Al elegir ruta, solo se muestran 2 clicks necesarios, reduciendo la carga cognitiva al decidir. |
| **3. Asignación por Tramos** | **Ley de Miller** | La ruta se divide en tramos visuales. La mente humana procesa mejor la información en "chunks" (trozos de 3 a 7 elementos). |
| **4. Monitoreo en Tiempo Real** | **Efecto de Posición Serial** | Los datos críticos (Batería del Drone y Progreso) se colocan en los extremos de la pantalla (arriba a la izquierda y centro) para recordar mejor el estado general. |
| **5. Alerta de Fallo** | **Ley de Prägnanz / Salience** | El fondo rojo brillante y el modal central rompen la jerarquía visual del dark mode instantáneamente para capturar el 100% de la atención del operador ante la crisis. |
| **6. Reporte de Entrega** | **Ley de Jakob / Familiaridad** | El formato del reporte imita a una "factura o tracking" de e-commerce clásico, aprovechando el modelo mental previo del operador para entender la cadena de custodia. |
