package net.nokok.masq.impl.mysql.fullprocesslist;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IndentSizeDetectorTest {
  @Test
  public void test() {
    IndentSizeDetector d = new IndentSizeDetector("""
      State: A
       Into: I
      """.stripIndent());

    assertEquals(5, d.detect());
  }

}
