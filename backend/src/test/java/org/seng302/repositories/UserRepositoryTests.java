package org.seng302.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.seng302.TestApplication;
import org.seng302.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.NoSuchAlgorithmException;

/**
 * Integration tests of the user repository.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestApplication.class)
@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;


    private User testUser;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        assertThat(userRepository).isNotNull();
        testUser = new User("Wileen", "YEP", "Tilsley","Diverse", "hybrid orchestration","wtilsley0@rakuten.co.jp","1921-10-08","+86 815 603 3959","32 Little Fleur Trail", "zWkb3AeLn3lc");
        User anotherUser = new User("Gannon", "YEP", "Tynemouth", "Exclusive", "6th generation intranet", "gtynemouth1@indiatimes.com","1996-03-31","+62 140 282 1784","2860 Clyde Gallagher Alley","HGD0nAJNjSD");
        userRepository.save(testUser);
        userRepository.save(anotherUser);
    }

    @Test
    public void findUsers() {
        User found = userRepository.findUserByEmail("wtilsley0@rakuten.co.jp");
        assertThat(found.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(found.getPassword()).isEqualTo(testUser.getPassword());
        assertThat(found.getId()).isEqualTo(1);

        User notFound = userRepository.findUserByEmail("nonexistingemail@aol.com");
        assertThat(notFound).isNull();

    }


}
