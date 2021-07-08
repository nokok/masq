package net.nokok.masq.impl.mysql.plainsql;

import net.nokok.masq.cli.context.Context;
import net.nokok.masq.impl.mask.jsqlparser.JsqlStatementReplacer;
import net.sf.jsqlparser.statement.Statement;

public class MaskingParameters {

  private final JsqlStatementReplacer mask;
  private final Statement statement;

  public MaskingParameters(Context context, Statement statement) {
    this.mask = new JsqlStatementReplacer(context);
    this.statement = statement;
  }

  public Statement execute() {
    mask.execute(statement);
    return this.statement;
  }
}
