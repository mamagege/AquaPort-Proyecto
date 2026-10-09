import urllib.request
import os

puml = """@startuml
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Context.puml

title Diagrama de Contexto C4 - AquaPort v2 (Prinplup)

Person(operador, "Operador Hídrico", "Supervisa la flota. Ya no asigna manualmente")
Person(solicitante, "Solicitante", "Pide el transporte de muestras o sensores")
Person(admin, "Administrador ECI", "Gestiona la flota y métricas operativas")
Person(tecnico, "Técnico Mantenimiento", "Repara drones en estado de FALLO")

System(aquaport, "AquaPort v2", "Sistema autónomo de gestión, asignación y monitoreo de drones")

System_Ext(apiCondiciones, "API Condiciones Hídricas", "Provee datos meteorológicos y del agua")
System_Ext(centroControl, "Centro de Control ECI", "Autoridad central que aprueba rutas en el campus")
System_Ext(sistemaAlertas, "Sistema de Alertas", "Plataforma de notificaciones de emergencia")

Rel(solicitante, aquaport, "Envía datos de solicitud (zona, carga)")
Rel(aquaport, solicitante, "Retorna ID de misión y estado")

Rel(operador, aquaport, "Envía parámetros de supervisión")
Rel(aquaport, operador, "Muestra dashboard de flota")

Rel(admin, aquaport, "Configura parámetros globales")
Rel(aquaport, admin, "Genera reportes operativos")

Rel(aquaport, tecnico, "Notifica orden de revisión (ID drone, ubicación)")
Rel(tecnico, aquaport, "Registra confirmación de reparación")

Rel(aquaport, apiCondiciones, "Consulta coordenadas/zona")
Rel(apiCondiciones, aquaport, "Retorna nivel agitación, profundidad, temperatura")

Rel(aquaport, centroControl, "Envía solicitud de ruta y misión")
Rel(centroControl, aquaport, "Retorna autorización de ruta acuática")

Rel(aquaport, sistemaAlertas, "Envía notificación de evento FALLO")

@enduml
"""

os.makedirs('docs', exist_ok=True)
url = "https://kroki.io/plantuml/png"
req = urllib.request.Request(url, data=puml.encode('utf-8'), method='POST')
req.add_header('User-Agent', 'Mozilla/5.0')
req.add_header('Content-Type', 'text/plain')

with urllib.request.urlopen(req) as response:
    with open('docs/c4-contexto-prinplup.png', 'wb') as f:
        f.write(response.read())

print("Generado docs/c4-contexto-prinplup.png")
