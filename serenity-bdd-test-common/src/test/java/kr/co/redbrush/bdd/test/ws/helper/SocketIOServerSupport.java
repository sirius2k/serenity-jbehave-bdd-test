package kr.co.redbrush.bdd.test.ws.helper;

import io.socket.client.IO;
import io.socket.client.Socket;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
public abstract class SocketIOServerSupport {
    protected final static int TIMEOUT = 7000;
    protected final static int PORT = 3001;

    private Process serverProcess;
    private ExecutorService serverService;
    private Future serverOutput;
    private Future serverError;

    @Before
    public void startServer() throws IOException, InterruptedException {
        /*
            When you get error message "Cannot find module 'socket.io'" after starting server,
            you need to run following command
            $ npm install -g socket.io
            $ npm link socket.io
         */
        LOGGER.debug("Starting server ...");

        final CountDownLatch latch = new CountDownLatch(1);
        serverProcess = Runtime.getRuntime().exec(String.format("node src/test/resources/server/server.js %s", nsp()), createEnv());
        serverService = Executors.newCachedThreadPool();
        serverOutput = serverService.submit(new Runnable() {
            @Override
            public void run() {
                BufferedReader reader = new BufferedReader(new InputStreamReader(serverProcess.getInputStream()));
                String line = null;

                try {
                    line = reader.readLine();
                    latch.countDown();
                    do {
                        LOGGER.debug("SERVER OUT: " + line);
                    } while ((line = reader.readLine()) != null);
                } catch (IOException e) {
                    LOGGER.warn(e.getMessage());
                }
            }
        });
        serverError = serverService.submit(new Runnable() {
            @Override
            public void run() {
                BufferedReader reader = new BufferedReader(new InputStreamReader(serverProcess.getErrorStream()));
                String line = null;

                try {
                    while ((line = reader.readLine()) != null) {
                        LOGGER.debug("SERVER ERR: " + line);
                    }
                } catch (IOException e) {
                    LOGGER.warn(e.getMessage());
                }
            }
        });
        latch.await(3000, TimeUnit.MILLISECONDS);
    }

    @After
    public void stopServer() throws InterruptedException {
        LOGGER.debug("Stopping server ...");
        serverProcess.destroy();
        serverOutput.cancel(false);
        serverError.cancel(false);
        serverService.shutdown();
        serverService.awaitTermination(3000, TimeUnit.MILLISECONDS);
    }

    protected Socket client() throws URISyntaxException {
        return client(createOptions());
    }

    protected Socket client(String path) throws URISyntaxException {
        return client(path, createOptions());
    }

    protected Socket client(IO.Options opts) throws URISyntaxException {
        return client(nsp(), opts);
    }

    protected Socket client(String path, IO.Options opts) throws URISyntaxException {
        return IO.socket(uri() + path, opts);
    }

    String uri() {
        return "http://localhost:" + PORT;
    }

    String nsp() {
        return "/";
    }

    IO.Options createOptions() {
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        return opts;
    }

    String[] createEnv() {
        Map<String, String> env = new HashMap<String, String>(System.getenv());
        env.put("DEBUG", "socket.io:*");
        env.put("PORT", String.valueOf(PORT));
        String[] environments = new String[env.size()];

        int i = 0;
        for (String key : env.keySet()) {
            environments[i] = key + "=" + env.get(key);
            i++;
        }

        return environments;
    }
}