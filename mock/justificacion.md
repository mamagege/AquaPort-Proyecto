# Mocks y Justificación de la Interfaz Asistida por IA

El objetivo visual de AquaPort v2 es transmitir autonomía, inteligencia artificial y robustez operativa mediante una estética *dark mode* y *glassmorphism*.

## 1. Panel de Flota (15 Drones)
**Prompt utilizado:**
> `UI Design for a marine drone fleet dashboard, dark mode, glassmorphism. Shows exactly 15 drone cards grouped cleanly by 3 types (Surface, Semi-submerged, Diver). Highly premium technical aesthetic.`

**Justificación UX:**
La Ley de Hick dicta que menos opciones organizadas facilitan la decisión. Por eso, dividimos los 15 drones en 3 grandes familias. El operador escanea por columnas según el tipo requerido antes de mirar los drones individuales.
*(Ver imagen `mock_flota.jpg`)*

## 2. Detalle de Recomendación de IA
**Prompt utilizado:**
> `UI Design for a mission detail interface in a marine drone dashboard, dark mode. Displays the AI recommended drone, battery level, route map, and highlighted text explaining the algorithmic selection reason. Vibrant technical blue accents, glassmorphism.`

**Justificación UX:**
El usuario confía más en el sistema si entiende **por qué** la IA tomó la decisión (Explainable AI). Resaltamos el "Algorithmic Selection Reason", que transparenta que el Patrón Strategy eligió ese drone por la corriente, profundidad requerida y carga.
*(Ver imagen `mock_recomendacion.jpg`)*

## 3. Confirmación de Asignación
**Prompt utilizado:**
> `UI Design for a success confirmation screen in a marine drone dashboard, dark mode. A prominent glowing green checkmark, large text saying 'Assignment Successful', showing the updated fleet status softly blurred in the background. Glassmorphism.`

**Justificación UX:**
Brinda al usuario validación inmediata (Heurística de Visibilidad del Estado). El check verde gigante da un alivio cognitivo claro, y el fondo desenfocado permite no perder contexto de dónde estábamos parados en la aplicación.
*(Ver imagen `mock_confirmacion.jpg`)*

## 4. Alerta de Misión Crítica
**Prompt utilizado (Imagen no adjuntada por límite de generación de IA, pero estructurada así):**
> `UI Design for a critical alert modal in a marine drone dashboard, dark mode. Red glowing warning signs, large text saying 'CRITICAL MISSION FAILED: NO DRONES AVAILABLE'. High tech, urgent aesthetic, glassmorphism.`

**Justificación UX:**
Cuando se lanza el `IllegalStateException` porque ningún drone cumple los requisitos o están todos caídos en `FALLO`, el *modal* debe adueñarse de toda la pantalla y emitir colores rojos. Se le debe informar inmediatamente al usuario que el sistema detuvo la operación.
