package kr.co.redbrush.bdd.test.ws;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static com.google.inject.matcher.Matchers.any;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(PowerMockRunner.class)
public class DefaultListenerTest {
    @InjectMocks
    private DefaultListener defaultListener;

    @Mock
    private SocketIOClient socketIOClient;

    @Before
    public void before() {
        defaultListener = new DefaultListener(socketIOClient);
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