package net.nokok.masq.args;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentsTest {
  @Test
  public void testFirst() {
    Arguments arguments = new Arguments("1");
    assertEquals("1", arguments.next());
    assertFalse(arguments.hasNext());
  }

  @Test
  public void testOption() {
    Arguments arguments = new Arguments("-t", "sql");
    assertEquals("-t", arguments.next());
    assertTrue(arguments.wasOption());
  }

}
