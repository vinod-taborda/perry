package gov.ca.cwds.service.scripts;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.script.ScriptException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by dmitry.rudenko on 10/3/2017.
 */
public class ScriptTest {
  private static final String SCRIPT_FILE = "test.groovy";

  @BeforeClass
  public static void before() throws IOException {
    Path file = Files.createFile(Paths.get(SCRIPT_FILE));
    Files.write(file, "\"$input\"".getBytes());
  }

  @Test
  public void test() throws IOException, ScriptException {
    Script script = new Script(SCRIPT_FILE, "input");
    String result = script.eval("test").toString();
    assert result.equals("test");
  }

  @AfterClass
  public static void after() throws IOException {
    Files.delete(Paths.get(SCRIPT_FILE));
  }
}
