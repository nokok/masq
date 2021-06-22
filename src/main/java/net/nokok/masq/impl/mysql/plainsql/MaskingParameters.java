package net.nokok.masq.impl.mysql.plainsql;

import net.nokok.masq.cli.context.Context;
import net.nokok.masq.impl.mask.jsqlparser.JsqlStatement;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectVisitorAdapter;
import net.sf.jsqlparser.statement.update.Update;

import java.util.ArrayList;
import java.util.List;

public class MaskingParameters {

  private final JsqlStatement mask;
  private final Statement statement;

  public MaskingParameters(Context context, Statement statement) {
    this.mask = new JsqlStatement(context);
    this.statement = statement;
  }

  public Statement execute() {
    mask.execute(statement);
    return this.statement;
  }
}
