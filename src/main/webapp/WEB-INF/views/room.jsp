<!DOCTYPE html>
<html>
<head>
    <title>WebSocket Chat</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <input type="text" id="message" placeholder="Enter message">
    <button onclick="sendMessage()">Send</button>
    <div id="chatArea"></div>

    <script>
        var socket = new WebSocket("ws://localhost:8080/chat");

        socket.onmessage = function(event) {
            var message = event.data;
            $("#chatArea").append("<p>" + message + "</p>");
        };

        function sendMessage() {
            var message = $("#message").val();
            socket.send(message);
            $("#message").val("");
        }
    </script>
</body>
</html>
