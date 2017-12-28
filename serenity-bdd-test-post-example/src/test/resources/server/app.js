var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var fs = require('fs');

app.get('/', function(req, res) {
    res.send('Hello node test server');
});

app.get('/posts', function(req, res) {
    res.json(readPosts());
});

app.post('/posts', function(req, res) {
    var json = readPost();
    json.id = 101;

    res.json(json);
});

app.get('/posts/:id', function(req, res) {
    var json = readPost();
    json.id = req.params.id;

    res.json(json);
});

io.on('connection', function(socket) {
    socket.on('getPost', getPostViaSocketIO);

    socket.on('getPosts', getPostsViaSocketIO);
});

function getPostViaSocketIO(params) {
    console.log('getPost called. request = ' + JSON.stringify(params));

    var post;
    if (params.id == 1) {
        post = readPost();
    }

    io.emit('getPost', post);
}

function getPostsViaSocketIO(params) {
    console.log('getPosts called. request = ' + JSON.stringify(params));

    var posts = readPosts();

    io.emit('getPosts', posts);
}

function readPost() {
    return readJsonFile('post.json');
}

function readPosts() {
    return readJsonFile('posts.json');
}

function readJsonFile(filename) {
    return JSON.parse(fs.readFileSync(__dirname + '/' + filename, 'utf8'));
}

http.listen(3000, function() {
    console.log('listening on *:3000');
});