
const indexedDB = window.indexedDB || window.mozIndexedDB || window.webkitIndexedDB || window.msIndexedDB


const dataBase = indexedDB.open("opencuesta", 1);


dataBase.onupgradeneeded = function (e) {
    console.log("Creando la estructura de la tabla");


    active = dataBase.result;


    const formulario = active.createObjectStore("formularios", {keyPath: 'id', autoIncrement: true});

    formulario.createIndex('por_indice', 'indice', {unique: true});

};

dataBase.onsuccess = function (e) {
    listarDatos();
    console.log('Proceso ejecutado de forma correctamente');
};

dataBase.onerror = function (e) {
    console.error('Error en el proceso: ' + e.target.errorCode);
};


var lati = "";
var longi = "";
let Thepicture = "NONE";


function GEOPosicion() {
    function success(pos) {
        var crd = pos.coords;
        lati = crd.latitude;
        longi = crd.longitude;
    }

    navigator.geolocation.getCurrentPosition(success);
}

function addForm() {
    var nombre = "";
    var sector = "";
    var nivelEscolar = "";

    nombre = $("#nombre").val();
    sector = $("#sector").val();
    nivelEscolar = $("#nivelEscolar").val()
    console.log("Nivel Escolar", nivelEscolar);
    //validando campos..

    if (nombre != "" && sector != "" && nivelEscolar != "Nivel Academico") {

        var dbActiva = dataBase.result; //Nos retorna una referencia al IDBDatabase.

        // Para realizar una operación de agreagr, actualización o borrar.
        // Es necesario abrir una transacción e indicar un modo: readonly, readwrite y versionchange
        var transaccion = dbActiva.transaction(["form"], "readwrite");

        //Manejando los errores.
        transaccion.onerror = function (e) {
            alert(request.error.name + '\n\n' + request.error.message);
        };

        transaccion.oncomplete = function (e) {
            document.querySelector("#nombre").value = '';
            alert('Formulario agregado correctamente!');
            listarDatos();
            ///window.location.reload();

        };

        //Abriendo la colección de datos que estaremos usando.
        var formulario = transaccion.objectStore("form");


        //Para agregar se puede usar add o put; el add requiere que no exista el objeto.
        var request = formulario.put({
            //id: document.querySelector("#id").value,
            nombre: document.querySelector("#nombre").value,
            sector: document.querySelector("#sector").value,
            nivelEscolar: document.querySelector("#nivelEscolar").value,
            latitude: lati,
            longitude: longi,
            user: document.querySelector("#idUsuario").value,
            //usuario: document.querySelector("#idUsuario").value,
        });

        request.onerror = function (e) {
            var mensaje = "Error: " + e.target.errorCode;
            console.error(mensaje);
            alert(mensaje)
        };

        request.onsuccess = function (e) {
            console.log("Datos Procesado con exito");
            document.querySelector("#nombre").value = "";
            document.querySelector("#sector").value = "";
            document.querySelector("#nivelEscolar").value = "Nivel Academico";
            console.log(formulario)
        };


    } else {
        validate();
    }
}

function listarDatos() {
    var data = dataBase.result.transaction(["form"]);
    var formulario = data.objectStore("form");
    var contador = 0;
    var formulario_recuperados = [];

    //Abriendo el cursor.
    formulario.openCursor().onsuccess = function (e) {
        //Recuperando la posicion del cursor
        var cursor = e.target.result;
        if (cursor) {
            contador++;
            //recuperando el objeto.
            formulario_recuperados.push(cursor.value);

            //Función que permite seguir recorriendo el cursor.
            cursor.continue();
        } else {
            console.log("La cantidad de registros recuperados es: " + contador);
        }
    };

    data.oncomplete = function () {
        imprimirTabla(formulario_recuperados);
    }
}

function imprimirTabla(lista_formulario) {
    // creando la tabla...
    var fila = ""
    for (var key in lista_formulario) {
        console.log("indice: ", lista_formulario[key])
        fila += "<tr>"
        fila += "<td>" + lista_formulario[key].id + "</td>"
        fila += "<td>" + lista_formulario[key].nombre + "</td>"
        fila += "<td>" + lista_formulario[key].sector + "</td>"
        fila += "<td>" + lista_formulario[key].nivelEscolar + "</td>"
        fila += "<td>"
        fila += "<a id='editar' href='#editModal' class='edit' data-toggle='modal'>" + "<i class='fas fa-edit' data-toggle='tooltip' title='Editar'>" + "</i>" + "</a>"
        fila += "<div type='hidden'>" + "<div>"
        fila += "<a id='borrar' href='#deleteModal' class='delete' data-toggle='modal'>" + "<i class='fas fa-trash' data-toggle='tooltip' title='Eliminar'>" + "</i>" + "</a>"
        fila += "</td>"
        fila += "</tr>"

    }
    document.getElementById("formList").innerHTML = fila;
}

function deleteForm() {

    const id = $("#idBorrar").val();
    console.log("ID digitadO: " + id);

    const data = dataBase.result.transaction(["form"], "readwrite");
    const formulario = data.objectStore("form");

    formulario.delete(parseInt(id)).onsuccess = function (e) {
        console.log("form eliminado...");
        listarDatos();

    };


}

function updateForm() {


    var id = $("#idEdit").val();
    console.log("ID digitadO:  " + id);

    var nombre = $("#nombreEdit").val();
    console.log("el nombre digitada: " + nombre);

    var sector = $("#sectorEdit").val();
    console.log("el sector digitado: " + sector);

    var nivelEscolar = $("#nivelEscolarEdit").val();
    console.log("el nivel digitado: " + nivelEscolar);


    var data = dataBase.result.transaction(["form"], "readwrite");
    var formulario = data.objectStore("form");


    formulario.get(parseInt(id)).onsuccess = function (e) {

        var resultado = e.target.result;
        console.log("los datos: " + JSON.stringify(resultado));

        if (resultado !== undefined) {

            resultado.nombre = nombre;
            resultado.sector = sector;
            resultado.nivelEscolar = nivelEscolar;

            var solicitudUpdate = formulario.put(resultado);


            solicitudUpdate.onsuccess = function (e) {
                console.log("Datos Actualizados....");
                listarDatos();
            }
            solicitudUpdate.onerror = function (e) {
                console.error("Error Datos Actualizados....");
            }
        } else {
            console.log("form no encontrado...");
        }
    };
}


var webSocket;
var tiempoReconectar = 5000;

$(document).ready(function () {
    conectar();


    $("#enviarForm").click(function () {
        console.log("Enviando formulario al servidor")
        enviarDatoServidor();
    });
});

function recibirInfServidor(mensaje) {
    console.log("Recibiendo del servidor: " + mensaje.data)
    $("#mensajeServidor").append(mensaje.data);
}

function conectar() {
    webSocket = new WebSocket("wss://" + location.hostname + ":" + location.port + "/conectarServidor");
    var req = new XMLHttpRequest();
    req.timeout = 5000;
    req.open('GET', "https://" + location.hostname + ":" + location.port + "/formularios", true);
    req.send();
    //indicando los eventos:
    webSocket.onmessage = function(data){recibirInformacionServidor(data);};
    webSocket.onopen  = function(e){
        var req = new XMLHttpRequest();
        req.timeout = 5000;
        req.open('GET', "https://" + location.hostname + ":" + location.port + "/formularios", true);
        req.send();
        console.log("Conectado - status "+this.readyState); };
    webSocket.onclose = function(e){
        console.log("Desconectado - status "+this.readyState);
        var req = new XMLHttpRequest();
        req.timeout = 5000;
        req.open('GET', "https://" + location.hostname + ":" + location.port + "/formularios", true);
        req.send();
    };
}





function enviarDatoServidor() {
    var data = dataBase.result.transaction(["form"]);
    var formularios = data.objectStore("form");
    var listaFormulario = formularios.getAll();

    listaFormulario.onsuccess = function () {
        if(document.querySelectorAll("#dataTable tr").length <=1){
            $('#infoRegister').modal('show')
        }
        else{
            //datos obtenido de forma correcta
            webSocket.send(JSON.stringify(listaFormulario.result));
            alert("form enviado de forma exitosa!")
            limpiarDB();
            listarDatos();

        }
    };

}

function limpiarDB() {
    const data = dataBase.result.transaction(["form"], "readwrite");
    const objecStore = data.objectStore("form");
    const repuesta = objecStore.clear();
    repuesta.onsuccess = function () {
        //Se han borrados todos los formulario de la BD
        console.log("Se han borrados todos los formulario de la BD");
    };
}