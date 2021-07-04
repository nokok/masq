package net.nokok.masq.impl.mask.jsqlparser;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

class JsqlStatementTest {
  private static final String SENSITIVE_DATA_STRING = "Sensitive Data";
  private static final int SENSITIVE_DATA_NUMBER = 42;

  @Test
  public void testSql() throws IOException {
    JsqlStatement s = new JsqlStatement("<masked>");
    List<Path> testCaseFilePaths = Files.list(Paths.get("src/test/resources/mask/sql")).toList();
    for (Path testCaseFilePath : testCaseFilePaths) {
      try {
        Statement parsedStatement = parse(testCaseFilePath);
        String testCaseFileAbsolutePath = testCaseFilePath.toAbsolutePath().toString();
        String before = parsedStatement.toString();
        if (!before.contains(SENSITIVE_DATA_STRING) && !before.contains(String.valueOf(SENSITIVE_DATA_NUMBER))) {
          fail("The %s or \"%s\" is required in the test data : %s".formatted(SENSITIVE_DATA_NUMBER, SENSITIVE_DATA_STRING, testCaseFileAbsolutePath));
        }
        s.execute(parsedStatement);
        String after = parsedStatement.toString();
        assertFalse(after.contains(SENSITIVE_DATA_STRING), "Mask test failed in Path=%s, Data=%s".formatted(testCaseFileAbsolutePath, after));
        assertFalse(after.contains(String.valueOf(SENSITIVE_DATA_NUMBER)), "Mask test failed in Path=%s, Data=%s".formatted(testCaseFileAbsolutePath, after));
      } catch (IOException | JSQLParserException e) {
        fail(e.getMessage());
      }
    }
  }

  private Statement parse(Path path) throws IOException, JSQLParserException {
    return CCJSqlParserUtil.parse(Files.newInputStream(path));
  }
}
