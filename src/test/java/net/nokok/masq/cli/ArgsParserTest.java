package net.nokok.masq.cli;

import net.nokok.masq.cli.context.Context;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArgsParserTest {
  @Test
  public void testEmpty() {
    ArgsParser parser = new ArgsParser(SupportOptionSet.DEFAULT);
    Context ctx = parser.parse();
    assertTrue(ctx.files().isEmpty());
    assertTrue(ctx.flags().has(Flag.SHOW_HELP));
  }
}
