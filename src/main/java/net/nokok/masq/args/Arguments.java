package net.nokok.masq.args;

import net.nokok.masq.cli.Option;
import net.nokok.masq.cli.SupportOptionSet;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class Arguments implements Iterator<String> {
  private final SupportOptionSet supportOptionSet;
  private final List<String> args;
  private int cursor = -1;

  public Arguments(String... args) {
    this(SupportOptionSet.DEFAULT, args);
  }

  public Arguments(SupportOptionSet supportOptionSet, String... args) {
    this.supportOptionSet = supportOptionSet;
    this.args = new ArrayList<>(Arrays.asList(args));
  }

  @Override
  public boolean hasNext() {
    return this.args.size() > cursor + 1;
  }

  @Override
  public String next() {
    this.cursor++;
    return this.args.get(this.cursor);
  }

  public String current() {
    return this.args.get(this.cursor);
  }

  public boolean wasOption() {
    return Option.fromOptionString(this.current()).filter(supportOptionSet::isSupportedOption).isPresent();
  }

  public Option asOption() throws UnknownOptionException {
    return Option.fromOptionString(this.current()).filter(supportOptionSet::isSupportedOption).orElseThrow(() -> new UnknownOptionException(this.current()));
  }

  public boolean wasFile() {
    return Files.exists(Paths.get(this.current()));
  }

  public Path asPath() {
    return Paths.get(this.current());
  }

  public String nextOptionString(Supplier<String> errorMessage) throws NoArgumentException {
    if (this.hasNext()) {
      return next();
    } else {
      throw new NoArgumentException(errorMessage.get());
    }
  }

  public <T> T nextOptionString(Function<String, Optional<T>> map, Function<String, String> errorMessage) throws NoArgumentException {
    String s = nextOptionString(() -> errorMessage.apply("???"));
    return map.apply(s).orElseThrow(() -> new NoArgumentException(errorMessage.apply(s)));
  }
}
