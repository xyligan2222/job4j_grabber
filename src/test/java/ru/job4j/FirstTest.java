package ru.job4j;



import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

    public class FirstTest {
        @Test
        public void checkOutHelloJob4j() {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            ru.job4j.grabber.Test.main(null);
            assertThat(out.toString(), is("Hello, Job4j!" + System.lineSeparator()));
        }

    }

