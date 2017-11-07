package kr.co.redbrush.bdd.test.ws;

import io.socket.client.IO;

import java.net.URISyntaxException;

public interface SocketIOClientFactory {
    SocketIOClient createInstance() throws URISyntaxException;
    SocketIOClient createInstance(IO.Options options) throws URISyntaxException;;
    SocketIOClient createInstance(String serverHost, IO.Options options) throws URISyntaxException;;
}
