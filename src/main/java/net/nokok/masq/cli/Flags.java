package net.nokok.masq.cli;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public class Flags {
  private final Set<Flag> flags;

  public Flags(Set<Flag> flags) {
    this.flags = Objects.requireNonNull(flags);
  }

  public boolean has(Flag flag) {
    return this.flags.contains(flag);
  }

  @Override
  public String toString() {
    return String.format(
      "Flags (flags=%s)", this.flags);
  }
}
