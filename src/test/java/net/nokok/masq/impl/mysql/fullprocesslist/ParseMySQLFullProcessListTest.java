package net.nokok.masq.impl.mysql.fullprocesslist;

import net.nokok.masq.impl.SupportedFileTypes;
import net.nokok.masq.impl.mysql.fullprocesslist.file.MySQLFullProcessList;
import net.nokok.masq.io.InputFile;
import net.nokok.masq.io.InputFiles;
import net.nokok.masq.impl.ParseResult;
import net.nokok.masq.impl.ParseResults;
import net.nokok.masq.impl.mysql.fullprocesslist.file.LogPerDate;
import net.nokok.masq.impl.mysql.fullprocesslist.file.RowData;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParseMySQLFullProcessListTest {
  @Test
  public void test1Row() {
    ParseMySQLFullProcessList parse = new ParseMySQLFullProcessList();
    ParseResult<MySQLFullProcessList> fullProcessListParseResult = parse.parsePerFile(InputFile.ofVirtualFile("""
      *************************** 1. row ***************************
             Id: 42
           User: testuser
           Host: localhost
             db: NULL
        Command: Sleep
           Time: 1
          State: cleaned up
           Info: SELECT 1 FROM DUAL
      """.stripIndent(), SupportedFileTypes.MySQLProcessList));
    assertTrue(fullProcessListParseResult.isSucceeded());
    fullProcessListParseResult.forEach(r -> {
      assertEquals(1, r.logs().size());
      LogPerDate rd = r.logs().get(0);
      assertEquals(1, rd.body().size());
      RowData result = rd.body().get(0);
      assertEquals(42, result.id().value());
      assertEquals("testuser", result.user().name());
      assertEquals("localhost", result.host().name());
      assertEquals("NULL", result.db().name());
      assertEquals("Sleep", result.command().value());
      assertEquals("1", result.time().value());
      assertEquals("cleaned up", result.state().value());
      assertEquals("SELECT 1 FROM DUAL", result.info().value());
    });
  }

  @Test
  public void test2Rows() {
    ParseMySQLFullProcessList parse = new ParseMySQLFullProcessList();
    ParseResult<MySQLFullProcessList> fullProcessListParseResult = parse.parsePerFile(InputFile.ofVirtualFile("""
      *************************** 1. row ***************************
             Id: 42
           User: testuser
           Host: localhost
             db: NULL
        Command: Sleep
           Time: 1
          State: cleaned up
           Info: SELECT 1
      *************************** 2. row ***************************
             Id: 43
           User: testuser2
           Host: localhost
             db: db1
        Command: Sleep
           Time: 1
          State: cleaned up
           Info: SELECT 2
      """.stripIndent(), SupportedFileTypes.MySQLProcessList));
    assertTrue(fullProcessListParseResult.isSucceeded());
    fullProcessListParseResult.forEach(r -> {
      assertEquals(1, r.logs().size());
      LogPerDate rd = r.logs().get(0);
      assertEquals(2, rd.body().size());
    });
  }

  @Test
  public void testResources() throws IOException {
    ParseMySQLFullProcessList parse = new ParseMySQLFullProcessList();
    List<InputFile> inputFiles = Files.list(Paths.get("src/test/resources/mysql-showfullprocesslist/pos")).map(f -> InputFile.of(f, SupportedFileTypes.MySQLProcessList)).toList();
    ParseResults<MySQLFullProcessList> result = parse.parse(new InputFiles(inputFiles));
    assertFalse(result.hasFailedResult());
  }

}
