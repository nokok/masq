package net.nokok.masq.impl.mysql.fullprocesslist.file;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;

public class ParsedSQLInfo implements Info {
  private final String text;
  private final Statement statement;

  public ParsedSQLInfo(String text, Statement statement) {
    this.text = text;
    this.statement = statement;
  }

  @Override
  public String value() {
    return this.text;
  }

  public void accept(StatementVisitor visitor) {
    this.statement.accept(visitor);
  }

  @Override
  public String toString() {
    return String.format("SQL: %s", this.statement);
  }
}
