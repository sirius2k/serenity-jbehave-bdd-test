package kr.co.redbrush.bdd.test.steps;

import kr.co.redbrush.bdd.test.service.TestContextService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;


/**
 * Created by kwpark on 13/03/2017.
 */
@Data
@Slf4j
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class})
@ContextConfiguration(locations = "/spring/config.xml")
public class BaseSteps {
    @Autowired
    protected TestContextService testContextService;
}
