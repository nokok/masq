package net.nokok.masq.cli;

import net.nokok.masq.cli.context.Context;
import net.nokok.masq.impl.SupportedFileTypes;
import net.nokok.masq.io.InputFile;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ArgsParserTest {
  @Test
  public void testEmpty() {
    ArgsParser parser = new ArgsParser();
    Context ctx = parser.parse();

    assertTrue(ctx.files().isEmpty());
    assertTrue(ctx.flags().has(Flag.SHOW_HELP));
  }

  @Test
  public void testShortHelp() {
    ArgsParser parser = new ArgsParser();
    Context ctx = parser.parse("-h");

    assertTrue(ctx.files().isEmpty());
    assertTrue(ctx.flags().has(Flag.SHOW_HELP));
  }

  @Test
  public void testLongHelp() {
    ArgsParser parser = new ArgsParser();
    Context ctx = parser.parse("--help");

    assertTrue(ctx.files().isEmpty());
    assertTrue(ctx.flags().has(Flag.SHOW_HELP));
  }

  @Test
  public void testPlainSQLFormat() {
    ArgsParser parser = new ArgsParser();
    String testFile = testFile();
    Context ctx = parser.parse("-t", "sql", testFile);

    assertFalse(ctx.hasError());
    assertTrue(ctx.flags().isEmpty());
    assertEquals(Optional.of(testFile), ctx.files().get(0).map(InputFile::path).map(Path::toString));
    assertEquals(SupportedFileTypes.PlainSQL, ctx.fileType());
  }

  @Test
  public void testMySQLFullProcessListFormat() {
    ArgsParser parser = new ArgsParser();
    String testFile = testFile();
    Context ctx = parser.parse("-t", "mysql/process-list", testFile);

    assertFalse(ctx.hasError());
    assertTrue(ctx.flags().isEmpty());
    assertEquals(Optional.of(testFile), ctx.files().get(0).map(InputFile::path).map(Path::toString));
    assertEquals(SupportedFileTypes.MySQLProcessList, ctx.fileType());
  }

  private String testFile() {
    try {
      return Files.createTempFile("masq", "testfile").toAbsolutePath().toString();
    } catch (IOException e) {
      fail();
      throw new IllegalStateException();
    }
  }
}
