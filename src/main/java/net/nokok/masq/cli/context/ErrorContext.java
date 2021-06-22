package net.nokok.masq.cli.context;

import net.nokok.masq.cli.Flag;
import net.nokok.masq.cli.Flags;
import net.nokok.masq.impl.SupportedFileTypes;
import net.nokok.masq.io.InputFiles;

import java.io.PrintStream;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class ErrorContext implements Context {

  private final String message;

  public ErrorContext() {
    this.message = "";
  }

  public ErrorContext(String message) {
    this.message = Objects.requireNonNull(message);
  }

  public String message() {
    return this.message;
  }

  @Override
  public PrintStream console() {
    return System.err;
  }

  @Override
  public SupportedFileTypes fileType() {
    return SupportedFileTypes.Unknown;
  }

  @Override
  public InputFiles files() {
    return new InputFiles(Collections.emptyList());
  }

  @Override
  public Flags flags() {
    return new Flags(Set.of(Flag.SHOW_HELP));
  }

  @Override
  public String replaceString() {
    return null;
  }

  @Override
  public boolean hasFlag(Flag flag) {
    return flag.equals(Flag.SHOW_HELP);
  }

  @Override
  public boolean hasError() {
    return true;
  }

  @Override
  public String toString() {
    return String.format(
      "ErrorContext (message=%s)", this.message);
  }
}
