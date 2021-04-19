package org.seng302;

import io.cucumber.java.Before;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Sole purpose of this class is to exist to make JPA repository tests work.
 */
@SpringBootApplication
public class TestApplication {
    @Before
    public void setup_cucumber_spring_context(){
        // Dummy method so cucumber will recognize this class as glue
        // and use its context configuration.
    }
}
