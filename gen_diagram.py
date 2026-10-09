import urllib.request
import os

puml = """@startuml
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Context.puml

title Diagrama de Contexto C4 - AquaPort MVP

Person(operador, "Operador Hidrico", "Registra solicitudes y asigna drones manualmente")
Person(solicitante, "Solicitante", "Pide el transporte de muestras o sensores")
Person(admin, "Administrador ECI", "Gestiona la flota y consulta reportes")

System(aquaport, "AquaPort MVP", "Sistema central de gestion de drones para la calidad del agua")

Rel(solicitante, aquaport, "Solicitud de transporte de muestra")
Rel(aquaport, solicitante, "Codigo de mision generado")

Rel(operador, aquaport, "Solicitud de mision, asignacion de drone")
Rel(aquaport, operador, "Confirmacion de asignacion, estado de mision")

Rel(admin, aquaport, "Gestion de flota, consulta de reportes")
@enduml
"""

os.makedirs('docs', exist_ok=True)
url = "https://kroki.io/plantuml/png"
req = urllib.request.Request(url, data=puml.encode('utf-8'), method='POST')
req.add_header('User-Agent', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36')
req.add_header('Content-Type', 'text/plain')

with urllib.request.urlopen(req) as response:
    with open('docs/c4-contexto-piplup.png', 'wb') as f:
        f.write(response.read())
