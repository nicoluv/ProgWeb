# practica9-SOAP
practica 9 de la materia de Programacion Web

Diego Benitez 20181603
Nicol Ureña 20181669

1. Se tienen que instalar las librerias que se encuentran en el requirements
2. El javalin-demo no pose una metodo de eliminar estudiante en su Web Services (EstudianteWebServices), por lo que tendremos que crearlo:
@WebMethod
public void eliminarEstudiante(int matricula){
    fakeServices.eliminandoEstudiante(matricula);
}
3. Se tiene que tener en ejecucion el proyecto javalin-demo

NOTA: Para instalar requirements por terminal
pip install -r requirements.txt

