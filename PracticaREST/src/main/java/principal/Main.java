package principal;

import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import modelos.Estudiante;


public class Main {

    public static void main(String[] args) {


        System.out.println(" PRACTICA REST ");

        //OBJETOS ESTUDIANTE
        Estudiante estudiante = new Estudiante(20181669, "Nicol Urena", "ISC");
        Estudiante estudiante2 = new Estudiante(20181663, "Diego Benitez", "ISC");



        //listando estudiantes
        listarEstudiantes();

        //Creando estudiantes
        System.out.println("-Estudiantes Creados- \n");
        crearEstudiante(estudiante);
        crearEstudiante(estudiante2);

        //listando estudiantes
        listarEstudiantes();

        //consultando estudiante
        System.out.println("-Consultando estudiante- \n");
        consultarEstudiante(estudiante.getMatricula());


        //eliminando estudiante
        System.out.println("-Eliminando estudiante- \n");
        eliminarEstudiante(estudiante2.getMatricula());

        //listando restantes

        listarEstudiantes();

    }

    public static void crearEstudiante(Estudiante estudiante){

        Unirest.post("http://localhost:7000/api/estudiante/")
                .header("Content-Type", "application/json")
                .body(new Gson().toJson(new Estudiante(estudiante.getMatricula(), estudiante.getNombre(), estudiante.getCarrera())))
                .asEmpty();

    }

    public static void listarEstudiantes(){

        HttpResponse<JsonNode> response = Unirest.get("http://localhost:7000/api/estudiante/")
                .header("accept", "application/json")
                .queryString("apiKey", "123")
                .asJson();

        JSONArray array = response.getBody().getArray();


        int cantidad = array.length();

        System.out.println("El total de estudiantes es de  "+cantidad);

        for(int i = 0;i<cantidad;i++){

            JSONObject object = array.getJSONObject(i);
            System.out.println("Matrícula: " + object.getInt("matricula"));
            System.out.println("Nombre: " + object.getString("nombre"));
            System.out.println("Carrera: " + object.getString("carrera"));
            System.out.println("\n");
        }

        System.out.println("Estatus: "+response.getStatus());

    }

    public static void consultarEstudiante(int matricula){

        HttpResponse<JsonNode> response = Unirest.get("http://localhost:7000/api/estudiante/")
                .header("accept", "application/json")
                .queryString("apiKey", "123")
                .asJson();

        JSONArray array = response.getBody().getArray();
        System.out.println("Estatus: "+response.getStatus());
        int cantidad = array.length();

        for(int i = 0;i<cantidad;i++){

            JSONObject object = array.getJSONObject(i);
            if(object.getInt("matricula") == matricula){
                System.out.println("Estudiante consultado: ");
                System.out.println("Matricula: " + object.getInt("matricula"));
                System.out.println("Nombre: " + object.getString("nombre"));
                System.out.println("Carrera: " + object.getString("carrera"));

            }

        }

    }

    public static void eliminarEstudiante(int mat){

        Unirest.delete("http://localhost:7000/api/estudiante/{matricula}")
                .routeParam("matricula", String.valueOf(mat))
                .asString();

    }



}
