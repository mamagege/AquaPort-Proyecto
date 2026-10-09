# AquaPort

Sistema integral de gestión y monitoreo de la flota de drones acuáticos.

## Diagrama de Ramas (GitFlow)

AquaPort utiliza GitFlow para organizar el ciclo de vida del desarrollo. A continuación se documenta el flujo utilizado para la versión v2:

```mermaid
gitGraph
    commit id: "v1.0" tag: "v1.0.0"
    branch develop
    checkout develop
    commit id: "iniciar v2"
    
    branch feature/asignacion-automatica
    checkout feature/asignacion-automatica
    commit id: "feat: asignacion automatica"
    checkout develop
    merge feature/asignacion-automatica
    
    branch feature/alertas-centro-control
    checkout feature/alertas-centro-control
    commit id: "feat: alertas centro de control"
    checkout develop
    merge feature/alertas-centro-control
    
    branch feature/factory-drones
    checkout feature/factory-drones
    commit id: "feat: factory drones"
    checkout develop
    merge feature/factory-drones
    
    branch release/v2.0
    checkout release/v2.0
    commit id: "fix: bugfixes for v2.0 release"
    checkout main
    merge release/v2.0 tag: "v2.0.0"
    checkout develop
    merge release/v2.0
    
    checkout main
    branch hotfix/fix-bateria-critica
    checkout hotfix/fix-bateria-critica
    commit id: "hotfix: correccion urgente bateria critica"
    checkout main
    merge hotfix/fix-bateria-critica
    checkout develop
    merge hotfix/fix-bateria-critica
```
