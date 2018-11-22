package kr.co.redbrush.bdd.test.ws;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.google.inject.matcher.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
@RunWith(PowerMockRunner.class)
public class DefaultListenerTest {
    @InjectMocks
    private DefaultEmitterListener defaultListener;

    @Mock
    private SocketIOClient socketIOClient;

    @Before
    public void before() {
        defaultListener = new DefaultEmitterListener(socketIOClient);
    }

    @Test
    public void testCall() {
        String message = "1234";

        Object[] obj = new Object[1];
        obj[0] = message;

        defaultListener.call(obj);

        verify(socketIOClient).addMessage(obj[0]);
    }

    @Test
    public void testCallWithoutMessage() {
        defaultListener.call();

        verify(socketIOClient, times(0)).addMessage(any());
    }
}
