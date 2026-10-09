import urllib.request
import os

puml = """@startuml
left to right direction
skinparam packageStyle rectangle

actor "Usuario Base" as userBase
actor "Operador Hídrico" as operador
actor "Técnico de Mantenimiento" as tecnico
actor "Centro de Control" as centroControl
actor "Administrador ECI" as admin

userBase <|-- operador
userBase <|-- admin

rectangle "AquaPort v2 (Prinplup)" {
  usecase "Consultar flota" as UC_Consultar
  usecase "Asignar misión automáticamente" as UC_Asignar
  usecase "Validar condiciones hídricas" as UC_ValidarCondiciones
  usecase "Notificar fallo al técnico" as UC_NotificarFallo
  usecase "Reparar Drone" as UC_Reparar
}

userBase --> UC_Consultar

operador --> UC_Asignar

UC_Asignar ..> UC_ValidarCondiciones : <<include>>
UC_NotificarFallo ..> UC_Asignar : <<extend>>\\ncondition: {Drone entra en FALLO}

UC_NotificarFallo --> tecnico
tecnico --> UC_Reparar

UC_ValidarCondiciones --> centroControl
@enduml
"""

os.makedirs('docs', exist_ok=True)
url = "https://kroki.io/plantuml/png"
req = urllib.request.Request(url, data=puml.encode('utf-8'), method='POST')
req.add_header('User-Agent', 'Mozilla/5.0')
req.add_header('Content-Type', 'text/plain')

with urllib.request.urlopen(req) as response:
    with open('docs/diagrama-cu-prinplup.png', 'wb') as f:
        f.write(response.read())

print("Generado docs/diagrama-cu-prinplup.png")
