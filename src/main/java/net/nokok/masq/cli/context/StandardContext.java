package net.nokok.masq.cli.context;

import net.nokok.masq.cli.Flag;
import net.nokok.masq.cli.Flags;
import net.nokok.masq.impl.SupportedFileTypes;
import net.nokok.masq.io.InputFiles;

import java.io.PrintStream;
import java.util.Objects;

public class StandardContext implements Context {

  private final PrintStream console;
  private final InputFiles files;
  private final Flags flags;
  private final String replaceString;

  public StandardContext(PrintStream console, InputFiles files, Flags flags, String replaceString) {
    this.console = Objects.requireNonNullElse(console, System.out);
    this.files = Objects.requireNonNull(files);
    this.flags = Objects.requireNonNull(flags);
    this.replaceString = Objects.requireNonNull(replaceString);
  }

  @Override
  public PrintStream console() {
    return this.console;
  }

  @Override
  public SupportedFileTypes fileType() {
    return this.files.fileTypes();
  }

  @Override
  public InputFiles files() {
    return this.files;
  }

  @Override
  public Flags flags() {
    return this.flags;
  }

  @Override
  public String replaceString() {
    return this.replaceString;
  }

  @Override
  public boolean hasFlag(Flag flag) {
    return this.flags.has(flag);
  }

  @Override
  public boolean hasError() {
    return false;
  }

  @Override
  public String toString() {
    return String.format(
      "StandardContext (console=%s, files=%s, flags=%s, replaceString=%s)", this.console, this.files, this.flags, this.replaceString);
  }
}
