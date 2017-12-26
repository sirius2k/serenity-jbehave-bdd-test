package kr.co.redbrush.bdd.test.definition;

import kr.co.redbrush.bdd.test.service.TestContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * Created by kwpark on 02/04/2017.
 */
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class})
@ContextConfiguration(locations = "/spring/config.xml")
public class BaseTestDefinition {
    @Autowired
    protected TestContextService testContextService;
}
