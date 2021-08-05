package org.seng302.steps;
import io.cucumber.spring.CucumberContextConfiguration;
import org.seng302.CucumberRunnerTest;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = CucumberRunnerTest.class)
public class CucumberSpringConfiguration {

}
