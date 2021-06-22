package net.nokok.masq.impl.mysql.fullprocesslist;

import net.nokok.masq.impl.SupportedFileTypes;
import net.nokok.masq.io.InputFile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SingleLineTransformerTest {
  @Test
  public void testCase1() {
    SingleLineTransformer singleLine = new SingleLineTransformer(InputFile.ofVirtualFile("""
      Info: Test1
             Test2
      """.stripIndent()
      , SupportedFileTypes.MySQLProcessList));
    assertEquals("Info: Test1 Test2", singleLine.transform());
  }

  @Test
  public void testCase2() {
    SingleLineTransformer singleLine = new SingleLineTransformer(InputFile.ofVirtualFile("""
      State: Has read all relay log; waiting for the slave
              I/O thread to update it
      """.stripIndent(), SupportedFileTypes.MySQLProcessList));
    assertEquals("State: Has read all relay log; waiting for the slave I/O thread to update it", singleLine.transform());
  }

  @Test
  public void testCase3() {
    SingleLineTransformer singleLine = new SingleLineTransformer(InputFile.ofVirtualFile("""
      State: Test1
              Test2
       Info: TestValue
      """.stripIndent(), SupportedFileTypes.MySQLProcessList));
    assertEquals("""
      State: Test1 Test2
       Info: TestValue""".stripIndent(), singleLine.transform());
  }

  @Test
  public void testManyLines() {
    SingleLineTransformer singleLine = new SingleLineTransformer(InputFile.ofVirtualFile("""
      Info: A
             B
                C
               D
             E
             F
              G
               H
      """.stripIndent(), SupportedFileTypes.MySQLProcessList));
    assertEquals("Info: A B C D E F G H", singleLine.transform());
  }

  @Test
  public void testColonCharInData() {
    SingleLineTransformer singleLine = new SingleLineTransformer(InputFile.ofVirtualFile("""
       Test: Test
      State: Fuga
       Info: Hoge
              Fuga: Piyo
         00:00:00
      """.stripIndent(), SupportedFileTypes.MySQLProcessList));
    assertEquals("""
       Test: Test
      State: Fuga
       Info: Hoge Fuga: Piyo 00:00:00""".stripIndent(), singleLine.transform());
  }

  @Test
  public void testNoSpaces() {
    SingleLineTransformer singleLine = new SingleLineTransformer(InputFile.ofVirtualFile("""
      Info: Hoge
      Fuga
      """.stripIndent(), SupportedFileTypes.MySQLProcessList));
    assertEquals("Info: Hoge Fuga", singleLine.transform());
  }

  @Test
  public void testSampleData1() {
    SingleLineTransformer singleLine = new SingleLineTransformer(InputFile.ofVirtualFile("""
      *************************** 1. row ***************************
           Id: 1
         User: test
         Host: localhost
           db: NULL
      Command: Sleep
         Time: 1
        State: cleaned up
         Info: NULL
      """.stripIndent(), SupportedFileTypes.MySQLProcessList));
    assertEquals("""
      *************************** 1. row ***************************
           Id: 1
         User: test
         Host: localhost
           db: NULL
      Command: Sleep
         Time: 1
        State: cleaned up
         Info: NULL""".stripIndent(), singleLine.transform());
  }

  @Test
  public void testSampleData2() {
    SingleLineTransformer singleLine = new SingleLineTransformer(InputFile.ofVirtualFile("""
      *************************** 456. row ***************************
           Id: 111
         Test: user
               test
      *************************** 457. row ***************************
           Id: 111
         Test: user
               test
      """.stripIndent(), SupportedFileTypes.MySQLProcessList));
    assertEquals("""
      *************************** 456. row ***************************
           Id: 111
         Test: user test
      *************************** 457. row ***************************
           Id: 111
         Test: user test""".stripIndent(), singleLine.transform());
  }

  @Test
  public void testSQL() {
    SingleLineTransformer singleLine = new SingleLineTransformer(InputFile.ofVirtualFile("""
      Info: SELECT
      *
      FROM users
      """.stripIndent(), SupportedFileTypes.MySQLProcessList));
    assertEquals("Info: SELECT * FROM users", singleLine.transform());
  }

  @Test
  public void testEmptyData() {
    SingleLineTransformer singleLine = new SingleLineTransformer(InputFile.ofVirtualFile("""
      User: system user
      Host:
      Test: Hoge""".stripIndent(), SupportedFileTypes.MySQLProcessList));
    assertEquals("""
      User: system user
      Host:
      Test: Hoge""".stripIndent(), singleLine.transform());

  }
}
