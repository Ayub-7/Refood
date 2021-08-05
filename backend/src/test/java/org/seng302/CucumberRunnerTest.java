package org.seng302;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"}, // How to format test report, "pretty" is good for human eyes
        glue = {"org.seng302.steps"}, // Where to look for your tests' steps
        features = {"src/test/resources/team900/features/"}, // Where to look for your features
        publish = true
)
public class CucumberRunnerTest {
}
