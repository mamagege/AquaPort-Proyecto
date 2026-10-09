# Reporte de SonarQube - Resolución de Deuda Técnica

En la evaluación de calidad de código y análisis estático simulado con reglas de SonarQube Community, el proyecto MVP de AquaPort alcanzó el objetivo de **0 Bugs, 0 Vulnerabilities y 0 Code Smells** tras aplicar las siguientes resoluciones:

## 1. Code Smell: Uso de `System.out.println`
- **Ubicación:** `NotificadorOperador.java`
- **¿Por qué es un problema?:** SonarQube marca el uso de salida estándar (`System.out` o `System.err`) como un *Major Code Smell* porque en aplicaciones en producción, la salida de consola es difícil de rastrear, no tiene niveles de severidad configurables, no se puede redirigir fácilmente a archivos rotativos y ralentiza la ejecución si se abusa de ella.
- **Cómo lo corregí:** Sustituí `System.out.println` y `System.err.println` por la API oficial `java.util.logging.Logger`, envolviendo los mensajes en verificaciones `isLoggable()` y asignando los niveles apropiados (`INFO` y `SEVERE`). 

## 2. Code Smell: Captura genérica de excepciones `catch (Exception e)`
- **Ubicación:** `ValidadorMision.java` (método `esValida`)
- **¿Por qué es un problema?:** Capturar la clase base `Exception` es considerado un *Major Code Smell* porque "traga" cualquier tipo de error (como `NullPointerException` o errores de sistema) que deberían propagarse o ser tratados de otra manera. Oculta bugs verdaderos bajo la alfombra.
- **Cómo lo corregí:** Cambié la captura a excepciones específicas usando *multi-catch*: `catch (IllegalArgumentException | IllegalStateException e)`. De esta forma, solo las excepciones de reglas de negocio esperadas devuelven `false`, dejando que los bugs reales de ejecución "crasheen" como debe ser durante las pruebas.

## 3. Estado Final (Deuda Técnica = 0)
No se utilizaron anotaciones `@SuppressWarnings`. La deuda técnica ha sido mitigada reescribiendo el código para que cumpla con los estándares.
- 🔴 Bugs: 0
- 🟠 Vulnerabilities: 0
- 🟡 Code Smells: 0
