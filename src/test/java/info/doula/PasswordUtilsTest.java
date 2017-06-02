package info.doula;

import info.doula.utils.PasswordUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PasswordUtilsTest {
    @Test
    @Repeat(100)
    public void testRandomlyGeneratePasswordIsdoulatCompliant() throws IOException {
        String password = PasswordUtils.generateRandomPassword();
        boolean isCompliant = PasswordUtils.passwordCompliantWithPolicy(password);

        assertThat(isCompliant, is(true));
    }
}
