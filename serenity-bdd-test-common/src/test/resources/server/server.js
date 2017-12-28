var fs = require('fs');

var server;
if (process.env.SSL) {
    server = require('https').createServer({
        key: fs.readFileSync(__dirname + '/key.pem'),
        cert: fs.readFileSync(__dirname + '/cert.pem')
    });
} else {
    server = require('http').createServer();
}

var io = require('socket.io')(server, {
    pingInterval: 2000,
    wsEngine: 'ws'
});
var port = process.env.PORT || 3001;
var nsp = process.argv[2] || '/';
var slice = Array.prototype.slice;

io.of(nsp).on('connection', function(socket) {
    socket.on("echo", function() {
        var args = slice.call(arguments);

        socket.emit.apply(socket, ['echoBack'].concat(args));
    });

    socket.on('getPost', getPost);

    socket.on('getBookStore', getBookStore);

    socket.on('getBook', getBook);
});

function before(context, name, fn) {
    var method = context[name];
    context[name] = function() {
        fn.apply(this, arguments);
        return method.apply(this, arguments);
    };
}

before(io.engine, 'handleRequest', function(req, res) {
    // echo a header value
    var value = req.headers['x-socketio'];
    if (!value) return;
    res.setHeader('X-SocketIO', value);
});

before(io.engine, 'handleUpgrade', function(req, socket, head) {
    // echo a header value for websocket handshake
    var value = req.headers['x-socketio'];
    if (!value) return;
    this.ws.once('headers', function(headers) {
        headers.push('X-SocketIO: ' + value);
    });
});

function getPost(request) {
    console.log('getPost called. request = ' + JSON.stringify(request));

    var post = {
        id : request.id,
        title : 'title',
        body : 'test body',
        userId : 1
    };

    io.emit('getPost', post);
}

function getBookStore() {
    console.log('getBookStore called.');

    var bookStore = JSON.parse(fs.readFileSync(__dirname + '/bookstore.json'));

    io.emit('getBookStore', bookStore);
}

function getBook() {
    console.log('getBook called.');

    var bookStore = JSON.parse(fs.readFileSync(__dirname + '/book.json'));

    io.emit('getBook', bookStore);
}

server.listen(port, function() {
    console.log('Socket.IO server listening on port', port);
});