#!/usr/bin/env python
from zeep import Client

def listarEstudiantes():
    return client.service.getListaEstudiante()

def consultarEstudiante(matricula):
    return client.service.getEstudiante(matricula)

def crearEstudiante(matricula,nombre,carrera):
    estudiante = Estudiante(matricula=matricula, nombre=nombre, carrera=carrera)
    client.service.crearEstudiante(estudiante)

def eliminarEstudiante(matricula):
    client.service.eliminarEstudiante(matricula)

def mostrarResultado(texto,accion):
    print("\n" + accion + "\n")
    print(texto)

#Creamos el cliente SOAP y la clase Estudiante
url = "http://localhost:7000/ws/EstudianteWebServices?wsdl"
client = Client(url)
Estudiante = client.get_type('ns0:estudiante')

#Se prueba la funcion de listar estudiantes
mostrarResultado(listarEstudiantes(),"Listado de Estudiantes")


#Se prueba la funcion de consultar estudiante
mostrarResultado(consultarEstudiante("20011136"),"Consulta de Estudiante")

#Se prueba la funcion de crear estudiante

crearEstudiante(20181603,"Diego Benitez","ISC")
crearEstudiante(20181669,"Nicol Urena","ISC")
mostrarResultado(listarEstudiantes(),"Creando Estudiantes")

#Se debe agregar el siguiente metodo al javalin-demo en el archivo EstudianteWebServices

#@WebMethod
#public void eliminarEstudiante(int matricula){
#    fakeServices.eliminandoEstudiante(matricula);
#}

#Se prueba la funcion de eliminar estudiante

eliminarEstudiante(20181603)
mostrarResultado(listarEstudiantes(),"Eliminando Estudiante")
