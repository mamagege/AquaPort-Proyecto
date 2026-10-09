# Reto Empoleon: Estándares de GitFlow y Conventional Commits

Un equipo de escala Enterprise no puede darse el lujo de tener un historial de Git confuso. Por ello, hemos introducido las siguientes regulaciones al flujo de trabajo del repositorio de AquaPort.

## 1. Validación de Conventional Commits (Git Hook)

Para forzar a que cada desarrollador respete el estándar arquitectónico de los mensajes de commit, hemos configurado un **Hook local de Git**.
Se creó el archivo `.git/hooks/commit-msg` con el siguiente script de bash que valida una expresión regular sobre cada intento de commit. Si un commit no inicia con una etiqueta válida (ej. `feat:`, `fix:`, `docs:`, etc.), Git aborta la operación instantáneamente.

```bash
#!/bin/bash
commit_msg_file=$1
commit_msg=$(cat "$commit_msg_file")

# Expresión regular de Conventional Commits
regex="^(feat|fix|docs|style|refactor|test|chore)(\(.+\))?: .+"

if [[ ! $commit_msg =~ $regex ]]; then
    echo "ERROR: El mensaje de commit no sigue Conventional Commits."
    echo "Formato: <tipo>(<alcance opcional>): <descripción corta>"
    echo "Tipos permitidos: feat, fix, docs, style, refactor, test, chore"
    exit 1
fi
```

## 2. Protección de Ramas (`main` y `develop`)

Dado que trabajamos con el modelo **GitFlow**, las ramas base no deben mutar por impulsos directos.

### Pasos aplicados en GitHub (Branch Protection Rules):
1. **Settings > Branches > Add branch protection rule**.
2. Escribimos el nombre del patrón de rama: `main` (y otra para `develop`).
3. Marcamos: **Require a pull request before merging** (mínimo 1 o 2 aprobaciones (code reviews) de pares).
4. Marcamos: **Require status checks to pass before merging** (en este caso, validaciones de GitHub Actions que ejecuten `mvn test` y JaCoCo obligatoriamente para asegurar 0 bugs y >85% coverage).
5. **Bloqueo a force push**: Desactivamos el push directo (`Do not allow bypassing the above settings`). Cualquier intento de un `git push origin main` desde local será rechazado.

## 3. Generación del CHANGELOG Automático

Gracias al uso riguroso de Conventional Commits, ahora es trivial generar y auditar notas de versiones. Mediante el siguiente comando, Git recorre la historia, la formatea y expulsa un archivo Markdown sin intervención manual:

```bash
git log --pretty=format:"* %s (%h)" > CHANGELOG.md
```

## 📜 Log Enterprise (10 Commits Estrictos)

Este es el historial resplandeciente de los últimos cambios de la v3, en los cuales creamos clases nuevas y algoritmos funcionales sin contaminar el repositorio con "wips" o mensajes basura:

```text
d0ec35d docs(changelog): actualizar CHANGELOG.md automaticamente desde el historial de git
4d233a7 chore(config): preparar repositorio para rama Enterprise
2833274 refactor(docs): reestructurar docs eliminando pngs obsoletos
9d9552b docs(analytics): crear documentacion streams_empoleon.md
58c02b2 test(analytics): anadir pruebas unitarias para streams de Empoleon
464678a feat(analytics): agregar logica de streams para Empoleon v3
f72f444 feat(dominio): incorporar lista de Waypoints a Mision y su Builder
856e0a8 feat(dominio): agregar estado EN_TRANSITO a EstadoMision
699b13f feat(dominio): agregar propiedad entregasExitosas a DroneAcuatico
aefa0c8 feat(dominio): agregar clase Waypoint para rutas multi-etapa
```
