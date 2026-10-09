# Pruebas TDD - ValidadorMision

Este documento detalla el ciclo estricto de Desarrollo Guiado por Pruebas (TDD) implementado para las validaciones de negocio en `ValidadorMision`.

## 1. Fase RED (Escribir la prueba que falla)
Primero se escribieron las pruebas unitarias utilizando JUnit 5 en el archivo `test/ValidadorMisionTest.java`. En este punto, los métodos a probar en `ValidadorMision` aún no existían o devolvían valores por defecto, por lo que las pruebas lógicamente fallaban (estado RED).

Se cubrieron los siguientes escenarios (Happy path y Casos negativos/Edge):
1. **Batería suficiente:** Drone con batería `>= 35%` (Verifica `true`).
2. **Batería crítica:** Drone con batería `< 35%` (Verifica `false`).
3. **Punto de llegada nulo:** Verifica que arroje `IllegalArgumentException`.
4. **Punto de llegada vacío:** Verifica que arroje `IllegalArgumentException`.
5. **Drone no disponible:** Drone con estado `disponible=false`. Verifica que arroje `IllegalStateException`.
6. **Zona de destino inválida (Edge Case):** Se restringe explícitamente el envío a "Zona Restringida", esperando un `IllegalArgumentException`.

*Se realizó un commit (`test(ValidadorMision): pruebas unitarias RED phase`) con el código de prueba antes de tocar el código de producción, cumpliendo con la filosofía TDD.*

## 2. Fase GREEN (Implementación del código mínimo)
Se procedió a implementar el código de producción en la clase `src/ValidadorMision.java`. Se escribieron exactamente las líneas mínimas requeridas para hacer que todas las afirmaciones (asserts) del archivo de prueba pasaran a verde.
* Se implementó `tieneBateriaSuficiente(DroneAcuatico drone)`.
* Se implementó `validarPuntoLlegada(String puntoLlegada)`.
* Se implementó `validarDisponibilidad(DroneAcuatico drone)`.

## 3. Fase REFACTOR
Al contar con la red de seguridad de las pruebas en verde, refactorizamos la estructura de `ValidadorMision`.
* Se agruparon las validaciones atómicas para ser reutilizadas dentro del método orquestador principal `esValida(Mision mision)`.
* Se aseguraron nombres de variables declarativos y limpios.
* Las pruebas continuaron ejecutándose en verde garantizando que no se rompiera la funcionalidad existente durante la reestructuración.

*Se realizó un segundo commit (`feat(ValidadorMision): implementacion GREEN y REFACTOR de validaciones`) para inmortalizar el comportamiento de producción terminado.*
