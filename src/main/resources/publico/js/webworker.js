var webSocket;


this.addEventListener('message',function (e){
    var data = e.data
    var lista = JSON.parse(data)
    var result = []

    for(var i =0;i < lista.length;i++){
        var temp = [];
        temp.push(lista[i].nombre + "," + lista[i].n_grado + "," + lista[i].sector + "," + lista[i].latitud + "," + lista[i].longitud);
        result.push(temp);
    }
    conectar(result);
},false);

function recibirInformacionServidor(mensaje){
    console.log("Recibiendo del servidor: "+mensaje.data)
    webSocket.onclose();
    this.postMessage("LISTO :D");
}

function conectar(data) {
    webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/ws/registrar");

    webSocket.onmessage = function(data){recibirInformacionServidor(data);};
    webSocket.onopen  = function(e){ console.log("Conectado - status "+this.readyState);webSocket.send(data)};
    webSocket.onclose = function(e){
        console.log("Desconectado - status "+this.readyState);
    };
}