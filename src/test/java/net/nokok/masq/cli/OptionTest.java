package net.nokok.masq.cli;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OptionTest {
  @Test
  public void testFromOptionString() {
    assertEquals(Optional.of(Option.REPLACE_STRING), Option.fromOptionString("--replace"));
    assertEquals(Optional.of(Option.REPLACE_STRING), Option.fromOptionString("-r"));
    assertEquals(Optional.of(Option.INPUT_FORMAT), Option.fromOptionString("--input-type"));
    assertEquals(Optional.of(Option.INPUT_FORMAT), Option.fromOptionString("-t"));
    assertEquals(Optional.of(Option.HELP), Option.fromOptionString("--help"));
    assertEquals(Optional.of(Option.HELP), Option.fromOptionString("-h"));
  }

  @Test
  public void testLongOptionAreUnique() {
    List<Option> options = Arrays.asList(Option.values());
    List<String> duplicates = options.stream().map(Option::longFormat).filter(o -> Collections.frequency(options, o) > 1).toList();
    assertEquals(Collections.emptyList(), duplicates);
  }

  @Test
  public void testShortOptionAreUnique() {
    List<Option> options = Arrays.asList(Option.values());
    List<String> duplicates = options.stream().map(Option::shortFormat).filter(Optional::isPresent).map(Optional::get).filter(o -> Collections.frequency(options, o) > 1).toList();
    assertEquals(Collections.emptyList(), duplicates);
  }

}
