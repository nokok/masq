package net.nokok.masq.impl.mysql.plainsql;

import net.nokok.masq.impl.SupportedFileTypes;
import net.nokok.masq.io.InputFile;
import net.nokok.masq.impl.ParseResult;
import net.sf.jsqlparser.statement.Statement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParseMySQLPlainSQLTest {
  @Test
  public void testSelectStatement() {
    ParseMySQLPlainSQL parse = new ParseMySQLPlainSQL();
    ParseResult<Statement> result = parse.parsePerFile(InputFile.ofVirtualFile("""
      SELECT * FROM USERS WHERE USER_NAME = 'Hoge'
      """, SupportedFileTypes.PlainSQL));

    assertTrue(result.isSucceeded());
  }

  @Test
  public void testUpdateStatement() {
    ParseMySQLPlainSQL parse = new ParseMySQLPlainSQL();
    ParseResult<Statement> result = parse.parsePerFile(InputFile.ofVirtualFile("""
      UPDATE USERS SET USER_NAME = 'FUGA' WHERE USER_ID = 1
      """, SupportedFileTypes.PlainSQL));

    assertTrue(result.isSucceeded());
  }
}
