import base64
import zlib
import urllib.request
import os

mermaid_code = """
@startuml
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml

Person(operador, "Operador Hídrico", "Gestiona y monitorea la flota.")
Person(tecnico, "Técnico Mantenimiento", "Recibe alertas de fallos.")
Person(sistemaExt, "Sistema Emergencias", "Solicita rescates.")

System_Boundary(aquaport_ent, "AquaPort Enterprise") {
    Container(app, "Aplicación Principal", "Java", "Lógica de dominio, asignación, streams y validación.")
    Container(telemetria, "Módulo de Telemetría", "Componente Java", "Aplica Decorator para registrar datos de vuelo cifrados.")
    Container(adapterApi, "Adaptador API", "Componente Java", "Adapter que traduce JSON a Condiciones Hídricas.")
    ContainerDb(db, "Base de Datos Misiones", "Relacional", "Guarda el historial de rutas y telemetría.")
}

System_Ext(apiClima, "API Hídrica Externa", "Datos de turbidez/nivel.")
System_Ext(drone1, "Drones Buceadores", "HW")
System_Ext(drone2, "Drones Superficiales", "HW")
System_Ext(drone3, "Drones Semisumergidos", "HW")

Rel(operador, app, "Monitorea Dashboard", "HTTPS / UI")
Rel(sistemaExt, app, "Solicita misión", "REST / JSON")
Rel(app, tecnico, "Alerta de Fallo", "WSS / JSON")

Rel(app, adapterApi, "Valida condiciones", "Invocación directa")
Rel(adapterApi, apiClima, "Consulta API", "HTTPS / JSON")
Rel(app, db, "Persiste estado y misiones", "JDBC / SQL")
Rel(app, telemetria, "Monitorea dron", "Invocación directa")

Rel(telemetria, drone1, "Comunica", "MQTT / TCP")
Rel(telemetria, drone2, "Comunica", "MQTT / TCP")
Rel(telemetria, drone3, "Comunica", "MQTT / TCP")
@enduml
"""

compressed = zlib.compress(mermaid_code.encode('utf-8'))
b64 = base64.urlsafe_b64encode(compressed).decode('ascii')
url = f"https://kroki.io/plantuml/png/{b64}"

req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0'})
with urllib.request.urlopen(req) as response, open('docs/c4-contenedores-empoleon.png', 'wb') as out_file:
    out_file.write(response.read())
print("Imagen generada con exito")
