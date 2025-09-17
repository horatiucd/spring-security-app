let stompClient = null;
let reconnectAttempts = 0;
const maxReconnectAttempts = 1;

$(document).ready(function () {
    connect();

    $('#send-private').click(function () {
        stompClient.send('/ws/user-messages', {},
            JSON.stringify({'content': 'New - front to back - message'}));
    });
});

function connect() {
    let socket = new SockJS('/app/session-websocket');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        reconnectAttempts = 0;

        stompClient.subscribe('/user/topic/user-messages', function (message) {
            let content = JSON.parse(message.body).content;
            console.log("Received message " + content);
            window.location.reload();
        });
    });

    socket.onclose = function(event) {
        console.log('WebSocket connection closed');
        onSocketClose();
    };
}

function onSocketClose() {
    if (reconnectAttempts < maxReconnectAttempts) {
        reconnectAttempts++;
        setTimeout(connect, 2000);
    } else {
        window.location.reload();
    }
}
