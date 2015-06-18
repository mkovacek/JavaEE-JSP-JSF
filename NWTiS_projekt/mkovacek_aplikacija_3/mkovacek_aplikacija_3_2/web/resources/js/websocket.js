var aplikacija = "/" + document.location.pathname.split("/")[1];
var wsUri = "ws://" + document.location.host + aplikacija + "/adreseEndpoint";
var websocket = new WebSocket(wsUri);

websocket.onopen = function (evt) {
};
websocket.onmessage = function (evt) {
    document.getElementById("websocketMsg").innerHTML = "Obavijest: "+evt.data;
    setTimeout(function(){
        window.location.reload(true);        
    },1000)
    /*if (!alert(evt.data)) {
        window.location.reload(true);
    }*/
};
