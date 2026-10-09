import urllib.request
import os

puml = """@startuml
left to right direction
skinparam packageStyle rectangle

actor "Operador Hídrico" as op
actor "Solicitante" as sol
actor "Administrador ECI" as admin

rectangle "AquaPort MVP" {
  usecase "Ver flota de drones" as UC_VerFlota
  usecase "Registrar misión" as UC_Registrar
  usecase "Cancelar misión" as UC_Cancelar
  usecase "Exportar reporte" as UC_Exportar
  usecase "Validar disponibilidad del drone" as UC_Validar
  usecase "Alertar batería crítica" as UC_Alertar

  op --> UC_VerFlota
  op --> UC_Registrar
  op --> UC_Cancelar
  
  sol --> UC_Registrar : "Solicita misión"
  admin --> UC_Exportar
  
  UC_Registrar ..> UC_Validar : <<include>>
  UC_Alertar .> UC_Registrar : <<extend>>\\n(Condición: batería < 40%)
}
@enduml
"""

os.makedirs('docs', exist_ok=True)
url = "https://kroki.io/plantuml/png"
req = urllib.request.Request(url, data=puml.encode('utf-8'), method='POST')
req.add_header('User-Agent', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36')
req.add_header('Content-Type', 'text/plain')

with urllib.request.urlopen(req) as response:
    with open('docs/diagrama-cu-piplup.png', 'wb') as f:
        f.write(response.read())
