package models;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;

import java.io.IOException;
import java.util.ArrayList;

public class Cliente {

    private Document recurso;
    private Connection.Response respuesta;


    public Cliente(String url) {
        try {
            this.recurso = Jsoup.connect(url).get();
            this.respuesta = Jsoup.connect(url).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Document getRecurso() {
        return recurso;
    }

    public void setRecurso(Document recurso) {
        this.recurso = recurso;
    }

    public Connection.Response getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(Connection.Response respuesta) {
        this.respuesta = respuesta;
    }

    //a) Indicar la cantidad de lineas del recurso retornado.

    public int obternerLineas(){
        return respuesta.body().split("\n").length;
    }


    //b) Indicar la cantidad de párrafos (p) que contiene el documento HTML.

    public int obtenerParrafos(){

        return recurso.getElementsByTag("p").size();

    }

    //c) Indicar la cantidad de imágenes (img) dentro de los párrafos que contiene el archivo HTML.
    public int obtenerimagenes(){

        return recurso.select("p img").size();

    }

    //d) indicar la cantidad de formularios (form) que contiene el HTML por
    // categorizando por el método implementado POST o GET.
    public int obtenerFormularios(String metodo){

        return recurso.select("form[method='" + metodo + "']").size();

    }

    //e) Para cada formulario mostrar los campos del tipo input y su respectivo tipo que contiene en el documento HTML.

    public ArrayList<String> obtenerCampos(){
        int campo = 1;
        int idforms = 1;
        ArrayList<String> lista = new ArrayList<>();

        for(FormElement misforms : recurso.getElementsByTag("form").forms()){
            for(Element camposInput : misforms.getElementsByTag("input")){
                         lista.add(" -> Formulario no. " + idforms + " input no. " + campo + " -tipo " + camposInput.attr("type") + "- \n");
                         campo++;
            }
                idforms++;

        }
        return lista;
    }
    //f) Para cada formulario parseado, identificar que el método de envío del formulario sea POST y enviar una petición al servidor con el
    //parámetro llamado asignatura y valor practica1 y un header llamado matricula con el valor correspondiente a matrícula asignada. Debe
    //mostrar la respuesta por la salida estándar.

    public void obtenerSalidaPost(){
        String url;
        Document midocumento;

        for(Element miform: this.recurso.select("form[method='post']")){

            url = miform.absUrl("action");

            try {
                midocumento = Jsoup.connect(url).data("asignatura","practica1").header("matricula", "2018-1669").post();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Resultado: \n" + midocumento.body().toString());


        }



    }









}
