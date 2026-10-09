# Reporte de Cobertura con JaCoCo

Este documento detalla el proceso de implementación, ejecución y resultados del análisis de cobertura de código para el `ValidadorMision` utilizando **JaCoCo**.

## 1. Configuración de JaCoCo
Para medir la efectividad de nuestras pruebas, integramos JaCoCo en nuestro ciclo de vida de Maven mediante el `pom.xml`. Se configuraron dos objetivos (goals) principales:
- `prepare-agent`: Se adjunta al proceso de inicialización para escuchar la ejecución de las pruebas.
- `report`: Genera el reporte HTML tras finalizar la fase de test (`mvn test`).

## 2. Análisis Inicial y Código Muerto (Líneas Rojas)
En la primera ejecución de `mvn test`, el archivo `jacoco.csv` arrojó una cobertura del **52.9%** para la clase `ValidadorMision`.
Al analizar las rutas, notamos que el método orquestador `esValida()` nunca estaba siendo llamado por las pruebas unitarias, dejando descubiertas ramas y posibles excepciones (`catch (Exception e)`).

* **Acción tomada:** Se procedió a escribir las pruebas faltantes para cubrir las "Líneas Rojas" (rutas no evaluadas).

## 3. Pruebas Adicionales Implementadas
Se escribieron tres pruebas específicas para cubrir el 100% de los caminos del método orquestador:
1. `misionValida_retornaTrue`: Evalúa el **Happy Path** (flujo exitoso).
2. `misionNula_retornaFalse`: Protege contra misiones sin inicializar (`null`).
3. `misionInvalida_retornaFalse`: Valida la captura correcta de excepciones (`IllegalStateException` provenientes de validaciones internas del builder o del estado del drone) y su conversión a un retorno booleano `false`.

## 4. Resultado Final
Tras la inclusión de las nuevas pruebas y la ejecución final de `mvn test`, JaCoCo reportó:
- **Cobertura alcanzada:** **100.0%** de Line Coverage en `ValidadorMision`.
- Todas las ramas, ifs y flujos de error han sido transformados en **Líneas Verdes** (■).

El reporte HTML completo y verificable se encuentra generado de manera local en:
👉 `target/site/jacoco/index.html`
