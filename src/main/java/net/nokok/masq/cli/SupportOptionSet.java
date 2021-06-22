package net.nokok.masq.cli;

import java.util.EnumSet;
import java.util.List;

public class SupportOptionSet {
  public static SupportOptionSet DEFAULT = new SupportOptionSet(
    EnumSet.allOf(Option.class)
  );

  private final EnumSet<Option> supportedOptions;

  private SupportOptionSet(EnumSet<Option> supportedOptions) {
    this.supportedOptions = supportedOptions;
  }

  public boolean isSupportedOption(Option option) {
    return this.supportedOptions.contains(option);
  }

  public List<Option> list() {
    return this.supportedOptions.stream().toList();
  }
}
