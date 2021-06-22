package net.nokok.masq.impl;

import java.util.Objects;
import java.util.Optional;

import static net.nokok.masq.impl.SupportedFileTypes.ShortNames.MYSQL_PROCESS_LIST;
import static net.nokok.masq.impl.SupportedFileTypes.ShortNames.PLAIN_SQL;

public enum SupportedFileTypes {
  Unknown(""),
  PlainSQL(PLAIN_SQL),
  MySQLProcessList(MYSQL_PROCESS_LIST),
  ;

  public static class ShortNames {
    public static final String PLAIN_SQL = "sql";
    public static final String MYSQL_PROCESS_LIST = "mysql/process-list";
  }

  public static SupportedFileTypes DEFAULT = PlainSQL;
  private final String shortName;

  SupportedFileTypes(String shortName) {
    this.shortName = shortName;
  }

  public String shortName() {
    return this.shortName;
  }

  public static Optional<SupportedFileTypes> fromOptionString(String option) {
    Objects.requireNonNull(option);

    return switch (option) {
      case PLAIN_SQL -> Optional.of(PlainSQL);
      case MYSQL_PROCESS_LIST -> Optional.of(MySQLProcessList);
      default -> {
        System.err.println("Unknown filetypes : " + option);
        yield Optional.empty();
      }
    };
  }
}
