
import models.Cliente;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String url;
        System.out.println("\n\t\t¡Bienvenido!");
        System.out.println("Introduce la url: ");
        Scanner scanner = new Scanner(System.in);
        url = scanner.next();
        Cliente micliente = new Cliente(url);


        //a) Indicar la cantidad de lineas del recurso retornado.
        System.out.println("\n");
        System.out.println("a- Cantidad de lineas del recurso ");
        System.out.println(" El total de lineas es de " + micliente.obternerLineas());
        System.out.println("\n");


        //b) Indicar la cantidad de párrafos (p) que contiene el documento HTML.

        System.out.println("b- Cantidad de parrafos");
        System.out.println(" El total de parrafos (p) es de " + micliente.obtenerParrafos());
        System.out.println("\n");

        //c) Indicar la cantidad de imágenes (img) dentro de los párrafos que contiene el archivo HTML.

        System.out.println("c- Cantidad de imagenes");
        System.out.println(" El total de imagenes (img) es de " + micliente.obtenerimagenes());
        System.out.println("\n");

        //d) indicar la cantidad de formularios (form) que contiene el HTML por
        // categorizando por el método implementado POST o GET.
        System.out.println("d- Cantidad de formularios");
        System.out.println(" El total de formularios con metodo GET es de " + micliente.obtenerFormularios("get"));
        System.out.println(" El total de formularios con metodo POST es de " + micliente.obtenerFormularios("post"));
        System.out.println("\n");

        //e) Para cada formulario mostrar los campos del tipo input y su respectivo tipo que contiene en el documento HTML.
        System.out.println("e- Campos input y su tipo");
        for (String formulario : micliente.obtenerCampos()){
            System.out.println(formulario);
        }

        //f) Para cada formulario parseado , identificar que el método de envío del formulario sea POST y enviar una petición al servidor con el
        //parámetro llamado asignatura y valor practica1 y un header llamado matricula con el valor correspondiente a matrícula asignada. Debe
        //mostrar la respuesta por la salida estándar.

        System.out.println("f- Resultado de peticion");
        micliente.obtenerSalidaPost();
        System.out.println("\n");

    }
}