package kr.co.redbrush.bdd.test.ws;

import io.socket.client.Socket;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by kwpark on 18/04/2017.
 */
@Slf4j
public class SocketIOClientTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    public Socket socket;

    @Before
    public void before() throws Exception {
    }

    @Test
    public void testCreateInstance() throws Exception {
        /*Socket socket = factory.createSocket(serverHost);

        assertThat("Socket is null.", socket, notNullValue());*/
    }
}
