package net.nokok.masq.impl;

import net.nokok.masq.io.InputFile;
import net.nokok.masq.io.InputFiles;
import net.nokok.masq.impl.mysql.fullprocesslist.ParseMySQLFullProcessList;
import net.nokok.masq.impl.mysql.plainsql.ParseMySQLPlainSQL;

import java.util.List;
import java.util.stream.Collectors;

public interface Parser<T> {
  default ParseResults<T> parse(InputFiles files) {
    List<ParseResult<T>> results = files.map(this::parsePerFile).collect(Collectors.toList());
    return new ParseResults<>(results);
  }

  ParseResult<T> parsePerFile(InputFile file);

  @SuppressWarnings("unchecked")
  static <T> Parser<T> of(SupportedFileTypes types) {
    return switch (types) {
      case PlainSQL -> (Parser<T>) new ParseMySQLPlainSQL();
      case MySQLProcessList -> (Parser<T>) new ParseMySQLFullProcessList();
      default -> throw new UnsupportedOperationException();
    };
  }
}
