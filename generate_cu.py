import base64
import zlib
import urllib.request
import os

plantuml_code = """
@startuml
left to right direction
skinparam packageStyle rectangle

actor "Operador Hídrico" as op
actor "Sistema Meteorológico" as sysClima
actor "Técnico Mantenimiento" as tec

rectangle "AquaPort Enterprise (Sistema Core)" {
  usecase "Gestionar Misión Multi-etapa" as UC_Principal
  
  usecase "Reasignar Drone Automáticamente" as UC_Fallo
  usecase "Pausar Misión por Mal Clima" as UC_Clima
  usecase "Cancelar Despliegue" as UC_Inactiva
  usecase "Disparar Alerta de Seguridad" as UC_Custodia
  usecase "Forzar Retorno a Base" as UC_Bateria
  
  op --> UC_Principal
  
  UC_Fallo .> UC_Principal : <<extend>>\\n{Condición: Fallo de drone en waypoint}
  UC_Clima .> UC_Principal : <<extend>>\\n{Condición: Condición hídrica adversa en tramo}
  UC_Inactiva .> UC_Principal : <<extend>>\\n{Condición: Zona destino inactiva}
  UC_Custodia .> UC_Principal : <<extend>>\\n{Condición: Cadena de custodia interrumpida}
  UC_Bateria .> UC_Principal : <<extend>>\\n{Condición: Drone con batería crítica mitad de ruta}
  
  sysClima --> UC_Clima
  UC_Fallo --> tec
  UC_Custodia --> op
}
@enduml
"""

compressed = zlib.compress(plantuml_code.encode('utf-8'))
b64 = base64.urlsafe_b64encode(compressed).decode('ascii')
url = f"https://kroki.io/plantuml/png/{b64}"

req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0'})
with urllib.request.urlopen(req) as response, open('docs/diagrama-cu-empoleon.png', 'wb') as out_file:
    out_file.write(response.read())
print("Diagrama de Casos de Uso generado con exito")
