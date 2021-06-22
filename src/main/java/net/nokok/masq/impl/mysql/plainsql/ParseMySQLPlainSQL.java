package net.nokok.masq.impl.mysql.plainsql;

import net.nokok.masq.io.InputFile;
import net.nokok.masq.impl.ParseResult;
import net.nokok.masq.impl.Parser;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

import java.io.IOException;

public class ParseMySQLPlainSQL implements Parser<Statement> {
  @Override
  public ParseResult<Statement> parsePerFile(InputFile file) {
    try {
      Statement statement = CCJSqlParserUtil.parse(new String(file.readAllBytes()));
      return ParseResult.succeeded(statement);
    } catch (IOException | JSQLParserException e) {
      return ParseResult.failed();
    }
  }
}
