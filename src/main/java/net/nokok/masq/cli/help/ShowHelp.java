package net.nokok.masq.cli.help;

import net.nokok.masq.cli.Option;
import net.nokok.masq.cli.SupportOptionSet;
import net.nokok.masq.impl.SupportedFileTypes;

import java.io.PrintStream;
import java.util.*;

public class ShowHelp {
  private final PrintStream console;

  public ShowHelp() {
    this.console = System.out;
  }

  public ShowHelp(PrintStream out) {
    this.console = Objects.requireNonNull(out);
  }

  public void printHelp() {
    ResourceBundle message = ResourceBundle.getBundle("net.nokok.masq.cli.help.messages", Locale.getDefault());
    console.println(message.getString("usage.header") + ": masq [input_file] [options...]");
    List<Option> supportedOptions = SupportOptionSet.DEFAULT.list();
    OptionalInt max = supportedOptions.stream().map(r -> r.shortFormat().orElse("") + ", " + r.longFormat()).mapToInt(String::length).max();
    int optionLength = max.orElseThrow(IllegalStateException::new);
    console.println();
    console.println("[supported file types]");
    for (SupportedFileTypes s : SupportedFileTypes.values()) {
      if (s.equals(SupportedFileTypes.Unknown)) {
        continue;
      }
      System.out.println("  " + s.shortName());
    }
    console.println("[options]");
    for (Option option : supportedOptions) {
      console.printf(" %" + optionLength + "s  %s%n", option.shortFormat().map(o -> o + ", ").orElse("") + option.longFormat(), message.getString(option.descriptionKey()));
    }
  }
}
