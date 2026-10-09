# AquaPort Enterprise 🌊🚁

**AquaPort** es el sistema centralizado de gestión, monitoreo y logística hidrológica para la flota de drones acuáticos de la Escuela Colombiana de Ingeniería (ECI). Ha sido diseñado con estándares arquitectónicos *Enterprise* para soportar misiones de recolección de muestras de agua, inspección de turbidez, y entrega de suministros a lo largo de 4 zonas hídricas.

---

## 🚀 Evolución del Proyecto

El desarrollo de AquaPort se ejecutó bajo una metodología ágil iterativa e incremental, escalando a través de 3 fases de madurez (inspiradas en las etapas evolutivas de un pingüino de agua):

1. **Fase v1 (Piplup - MVP):** Establecimiento del dominio base. Modelado de los drones y misiones. Pruebas unitarias iniciales (JUnit 5).
2. **Fase v2 (Prinplup - Optimización):** Implementación de Java Streams para búsqueda eficiente de drones. Introducción de TDD (Test-Driven Development) estricto. Creación del manual de identidad visual (UI/UX) y Mockups fotorrealistas. Primer escaneo con SonarQube y JaCoCo.
3. **Fase v3 (Empoleon - Enterprise):** Consolidación de **Clean Architecture**, inyección de Patrones de Diseño (GoF), Rutas Multi-etapa dinámicas, y validación exhaustiva de Calidad (`Zero-Bug Policy`).

---

## 🏛️ Arquitectura de Software

### Clean Architecture & SOLID
AquaPort está construido sobre una estricta **Arquitectura por Capas** (*Clean Architecture*). El núcleo lógico de la aplicación (el Dominio) está completamente aislado de frameworks externos.
- **Regla de la Dependencia:** Ninguna de las clases del dominio en la carpeta `src/` importa paquetes externos (0 imports de `org.mockito.*`, `com.fasterxml.jackson.*`, ni clientes HTTP). Las dependencias están compuestas única y exclusivamente por la API estándar de Java (`java.util.*`).
- **Principios SOLID:** Cumplimiento total. Ejemplos incluyen *Open/Closed Principle* mediante el uso de la jerarquía polimórfica `DroneAcuatico`, y *Dependency Inversion Principle* inyectando proveedores externos mediante interfaces como `ProveedorCondiciones`.

### Patrones de Diseño (GoF) Implementados
Para orquestar la complejidad de la versión Enterprise, se integraron patrones clave:
- **Chain of Responsibility:** Empleado en `ValidadorCadena` para la validación secuencial y aborto temprano de misiones (Validar Batería ➔ Capacidad de Carga ➔ Zona Activa ➔ Clima).
- **Decorator:** Implementado (`DroneConMonitoreo`, `DroneConCifrado`) para envolver cualquier instancia de `DroneAcuatico` agregándole capacidades de telemetría y seguridad AES-256 en tiempo real sin alterar la clase base.
- **Adapter:** Utilizado en `AdaptadorAPIHidrica` para traducir los JSON y contratos externos de sistemas meteorológicos en inglés, al modelo de dominio interno (`CondicionesHidricas`).
- **Strategy:** Usado en `EstrategiaSeleccion` para permitir la inyección dinámica de algoritmos de selección de drones (ej. `MayorBateriaStrategy`, `ZonaCercanaStrategy`).

### Modelado C4 & Casos de Uso
La arquitectura de integración está respaldada visualmente en el repositorio:
- [Diagrama de Contenedores C4 (Nivel 2)](docs/diagramas_empoleon.md)
- [Diagrama de Casos de Uso con Flujos Alternos (Fallo de dron, Clima adverso, Custodia rota)](docs/diagramas_empoleon.md)

---

## 🛡️ Calidad de Código y Testing (QA)

### Cobertura con JaCoCo & TDD
El núcleo de enrutamiento y asignación (`PlanificadorRuta`) fue desarrollado bajo TDD. Las pruebas abarcan 3 capas (Unitarias, Mocks con Mockito, e Integración).
- **Line Coverage:** 88.4% (Umbral requerido: ≥85%)
- **Branch Coverage:** 78.1% (Umbral requerido: ≥75%)

### Quality Gate de SonarQube
El código es auditado automáticamente. Nuestro Quality Gate en producción ostenta un resultado de **6/6 en Verde**:
- **0 Bugs**
- **0 Vulnerabilidades**
- **0 Min. Deuda Técnica** (Eliminación estricta de *code smells* como `System.out.println` reemplazados por `java.util.logging`).
- **0.8% Duplicación de Código** (Umbral requerido: <3%).

---

## 📈 Agilismo y Proceso

- **Gestión (Jira):** El Roadmap fue fragmentado en épicas y sprints con *Sprint Goals* definidos, *Definition of Done (DoD) Enterprise* y un manejo de capacidad mediante Puntos de Historia. [Ver Roadmap y Retrospectiva](agilismo/roadmap_empoleon.md).
- **GitFlow y Conventional Commits:** Todo aporte al repositorio debe superar el `pre-commit hook` configurado que aborta cualquier commit que no respete el estándar (`feat:`, `fix:`, `refactor:`, etc.).
- **Ramas Protegidas:** Inserciones a `main` y `develop` están bloqueadas, exigiendo flujos por *Pull Requests*.

---

## 🎨 UX & Design System

El componente visual de AquaPort Enterprise no se quedó atrás. El sistema cuenta con un marco estético de calidad de producción:
- **Design System:** Filosofía de *Glassmorphism en Dark Mode* (Slate 900) con acentos de Cyan Neón para evocar tecnología marítima.
- **Accesibilidad:** Aprobación del nivel **WCAG AA** con ratios de contraste superiores a 4.5:1 para estados críticos (Warning, Critical, Success).
- **Leyes de UX Aplicadas:** Se estructuró el flujo cognitivo mitigando la carga del operador a través de las leyes de *Fitts*, *Hick*, *Miller*, y el principio de *Salience* para las alertas rojas de fallos de reasignación. 
- [Ver documentación completa de UX y Flujos](docs/ux/design_system_empoleon.md).

---
*AquaPort: Inteligencia, resiliencia y precisión para la conservación del ecosistema hídrico. Construido con ingeniería de clase mundial.*
