package kr.co.redbrush.bdd.test.ws;

import io.socket.client.IO;

import java.net.URISyntaxException;

public interface SocketIOClientFactory {
    public SocketIOClient createInstance() throws URISyntaxException;
    public SocketIOClient createInstance(IO.Options options) throws URISyntaxException;;
    public SocketIOClient createInstance(String serverHost, IO.Options options) throws URISyntaxException;;
}
