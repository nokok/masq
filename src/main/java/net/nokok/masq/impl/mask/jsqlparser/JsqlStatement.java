package net.nokok.masq.impl.mask.jsqlparser;

import net.nokok.masq.cli.context.Context;
import net.nokok.masq.impl.mysql.fullprocesslist.file.ParsedSQLInfo;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitorAdapter;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectVisitorAdapter;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.update.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JsqlStatement {
  private final Context context;

  public JsqlStatement(Context context) {
    this.context = Objects.requireNonNull(context);
  }

  public void execute(Statement statement) {
    statement.accept(new ReplaceStatement());
  }

  public void execute(ParsedSQLInfo sqlInfo) {
    sqlInfo.accept(new ReplaceStatement());
  }

  class ReplaceStatement extends StatementVisitorAdapter {
    @Override
    public void visit(Update update) {
      List<Expression> replacedExpressions = new ArrayList<>();
      for (Expression expression : update.getExpressions()) {
        expression.accept(new ReplaceExpression());
        replacedExpressions.add(expression);
      }
      update.setExpressions(replacedExpressions);
    }

    @Override
    public void visit(Delete delete) {
      delete.getWhere().accept(new ReplaceExpression());
    }

    @Override
    public void visit(Select select) {
      select.getSelectBody().accept(new SelectVisitorAdapter() {
        @Override
        public void visit(PlainSelect plainSelect) {
          if (plainSelect.getWhere() != null) {
            plainSelect.getWhere().accept(new ReplaceExpression());
          }
        }
      });
    }

    @Override
    public void visit(Insert insert) {
      insert.getItemsList().accept(new ItemsListVisitorAdapter(){
        @Override
        public void visit(ExpressionList expressionList) {
          List<Expression> replacedExpressions = new ArrayList<>();
          for (Expression expression : expressionList.getExpressions()) {
            expression.accept(new ReplaceExpression());
            replacedExpressions.add(expression);
          }
          expressionList.setExpressions(replacedExpressions);
        }
      });
    }
  }

  class ReplaceExpression extends ExpressionVisitorAdapter {
    @Override
    public void visit(StringValue value) {
      value.setValue(context.replaceString());
    }

    @Override
    public void visit(LongValue value) {
      value.setStringValue(context.replaceString());
    }

    @Override
    public void visit(ValueListExpression valueListExpression) {
      List<Expression> expressions = new ArrayList<>();
      for (Expression expression : valueListExpression.getExpressionList().getExpressions()) {
        expression.accept(this);
        expressions.add(expression);
      }
      valueListExpression.getExpressionList().setExpressions(expressions);
    }

    @Override
    public void visit(EqualsTo expr) {
      expr.accept(new ExpressionVisitorAdapter() {
        @Override
        public void visit(StringValue value) {
          value.setValue(context.replaceString());
        }
      });
    }
  }
}
