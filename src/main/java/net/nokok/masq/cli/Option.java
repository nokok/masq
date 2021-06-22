package net.nokok.masq.cli;

import java.util.Optional;

public enum Option {
  REPLACE_STRING("option.replace_string", String.class, "--replace", "-r"),
  INPUT_FORMAT("option.input_format", String.class, "--input-type", "-t"),
  LIMIT_QUERY_SIZE("option.query_size_limit", int.class, "--limit-query-size", "-l"),
  HELP("option.help", boolean.class, "--help", "-h"),
  ;

  private final String descriptionKey;
  private final Class<?> type;
  private final String longFormat;
  private final String shortFormat;

  <T> Option(String descriptionKey, Class<T> type, String longFormat, String shortFormat) {
    this.descriptionKey = descriptionKey;
    this.type = type;
    this.longFormat = longFormat;
    this.shortFormat = shortFormat;
  }

  public String longFormat() {
    return this.longFormat;
  }

  public Optional<String> shortFormat() {
    return Optional.ofNullable(this.shortFormat);
  }

  public String descriptionKey() {
    return this.descriptionKey;
  }

  public static Optional<Option> fromOptionString(String s) {
    boolean isLongFormat = s.startsWith("--");
    boolean isShortFormat = !isLongFormat && s.startsWith("-") && s.length() >= 2;
    for (Option option : Option.values()) {
      if (isLongFormat && option.longFormat().equals(s)) {
        return Optional.of(option);
      }
      if (isShortFormat && option.shortFormat().filter(o -> o.equals(s)).isPresent()) {
        return Optional.of(option);
      }
    }
    return Optional.empty();
  }
}
