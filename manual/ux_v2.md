# Identidad Visual y UX - AquaPort v2

En AquaPort v2, la interfaz evoluciona para soportar una flota más grande y un nivel de autonomía avanzado. Hemos rediseñado la experiencia utilizando principios psicológicos de interacción humano-computadora para reducir la carga cognitiva del Operador Hídrico.

## 1. Diseño de Estados (Tarjeta de Drone y Batería)

La Tarjeta de Drone Acuático es el microcomponente más crítico del dashboard. Su semántica de colores (establecida en el MVP) se ha expandido para soportar los 6 estados del ciclo de vida v2:

*   🟢 **Disponible (Verde):** Listo para asignación. Hover suave para indicar interactividad.
*   🔵 **En Misión (Azul):** Operando. Muestra datos telemétricos.
*   🟡 **Recargando (Amarillo):** En estación base recuperando energía.
*   ⚪ **Mantenimiento (Gris):** Fuera de servicio programado. Opacidad reducida al 60%.
*   🔴 **Fallo (Rojo):** Estado crítico. Animación de pulso leve para llamar la atención del técnico.
*   🌑 **Sumergido (Azul Oscuro):** Solo aplicable a `DroneBuceador` o `DroneSemisumergido` en fase de inmersión perdiendo telemetría parcial.

**Indicador de Batería (Color Coding):**
*   **≥60%:** Verde (Óptimo)
*   **35% - 59%:** Amarillo (Precaución, asignable solo a misiones cortas)
*   **<35%:** Rojo (Bloqueado por validación de negocio RN-01, parpadeo sutil).

---

## 2. Flujo de Asignación Automática (UI Mockups)

A continuación se presenta el flujo completo de 3 pantallas interactuando con la nueva flota.

### Pantalla 1: Panel de Flota (Dashboard Principal)
![Panel de Flota v2](/C:/Users/USUARIO/.gemini/antigravity-ide/brain/ff8df70b-9af7-417b-b8a6-c617d3976d76/panel_flota_v2_1791575794658.jpg)

### Pantalla 2: Detalle de Misión (Evaluación de Asignación)
![Detalle de Mision v2](/C:/Users/USUARIO/.gemini/antigravity-ide/brain/ff8df70b-9af7-417b-b8a6-c617d3976d76/detalle_mision_v2_1791575803995.jpg)

### Pantalla 3: Confirmación de Despliegue
![Confirmacion v2](/C:/Users/USUARIO/.gemini/antigravity-ide/brain/ff8df70b-9af7-417b-b8a6-c617d3976d76/confirmacion_v2_1791575827065.jpg)

---

## 3. Aplicación de Leyes UX

### Ley de Hick (Agrupación de Drones)
*El tiempo que se tarda en tomar una decisión aumenta con el número y la complejidad de las opciones.*
Al pasar de unos pocos drones a una flota de 15, mostrar todas las tarjetas juntas paralizaría al operador. Para resolver esto aplicando la Ley de Hick, agrupamos categóricamente la información en **3 bloques visuales lógicos** correspondientes a nuestro *Factory Method*: Superficiales, Semisumergidos y Buceadores. Así, si hay un incidente submarino, el cerebro del operador ignora los dos primeros tercios de la pantalla y enfoca su toma de decisión en un máximo de 5 opciones a la vez.

### Ley de Fitts (Botón de Emergencia / Abortar)
*El tiempo necesario para alcanzar un objetivo es una función de la distancia al objetivo y del tamaño del mismo.*
En la v2 incluimos un botón de "ABORT ALL MISSIONS" (o Abortar). Siendo una acción de contingencia crítica en caso de huracán o accidente, aplicamos la Ley de Fitts posicionándolo en la **esquina inferior derecha**, con un **área de cliqueo masiva (tamaño gigante)** y márgenes generosos. Su cercanía a los bordes de la pantalla crea un "objetivo infinito" (si el usuario lanza el mouse hacia la esquina, el puntero se detiene exactamente sobre el botón), asegurando un tiempo de reacción de milisegundos en situaciones de pánico.
