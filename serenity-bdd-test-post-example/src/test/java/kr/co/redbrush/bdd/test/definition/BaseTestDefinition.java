package kr.co.redbrush.bdd.test.definition;

import kr.co.redbrush.bdd.test.service.TestContextService;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.core.Serenity;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.ScenarioType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Map;

/**
 * Created by kwpark on 02/04/2017.
 */
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class})
@ContextConfiguration(locations = "/spring/config.xml")
@Slf4j
public class BaseTestDefinition {
    @Autowired
    protected TestContextService testContextService;
}
