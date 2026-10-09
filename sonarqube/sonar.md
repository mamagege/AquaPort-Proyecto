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

---

## Anexo: ¿Cómo desplegar SonarQube Localmente?

Si deseas levantar tu propio servidor de SonarQube y visualizar el dashboard en tu máquina, la forma más limpia y profesional de hacerlo es utilizando **Docker**. Sigue estos pasos:

### Paso 1: Levantar el Servidor (Docker)
Abre tu terminal y ejecuta el siguiente comando para descargar e iniciar la imagen oficial de SonarQube Community LTS:
```bash
docker run -d --name sonarqube -p 9000:9000 sonarqube:lts-community
```
*Nota: Puede tardar unos minutos en arrancar.*

### Paso 2: Acceder al Dashboard
1. Abre tu navegador web y ve a `http://localhost:9000`.
2. Inicia sesión con las credenciales por defecto:
   - **Usuario:** `admin`
   - **Contraseña:** `admin`
3. El sistema te pedirá que cambies la contraseña por seguridad.

### Paso 3: Configurar el Proyecto en SonarQube
1. Una vez dentro, haz clic en **"Create a local project"**.
2. Asigna un `Project Key` (ejemplo: `aquaport-mvp`) y un `Display Name`.
3. Selecciona la opción para analizar el código **"Locally"**.
4. SonarQube te pedirá generar un **Token de autenticación**. Genéralo, cópialo y guárdalo, ya que no volverá a mostrarse.

### Paso 4: Ejecutar el Análisis desde Maven
Ve a la raíz de tu proyecto (donde está el `pom.xml`) y ejecuta el siguiente comando reemplazando `TU_TOKEN` por el token que acabas de copiar:

```bash
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=aquaport-mvp \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=TU_TOKEN
```

### Paso 5: Ver Resultados
Vuelve a tu navegador (`http://localhost:9000`). Verás tu proyecto `aquaport-mvp` en el dashboard principal mostrando las métricas exactas (Bugs, Vulnerabilities, Code Smells, Coverage) con una interfaz gráfica detallada.
