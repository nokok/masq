package net.nokok.masq.impl.mask.jsqlparser;

import net.nokok.masq.cli.Flag;
import net.nokok.masq.cli.Flags;
import net.nokok.masq.cli.context.Context;
import net.nokok.masq.impl.mysql.fullprocesslist.file.ParsedSQLInfo;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitorAdapter;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class JsqlStatementReplacer {
  private final String replaceString;
  private final boolean collapseSelectColumns;
  private final boolean collapseInExpressions;

  public JsqlStatementReplacer(Context context) {
    this(context.replaceString(), context.flags());
  }

  public JsqlStatementReplacer(String replaceString) {
    this(replaceString, new Flags(Collections.emptySet()));
  }

  public JsqlStatementReplacer(String replaceString, Flags flags) {
    this.replaceString = Objects.requireNonNull(replaceString);
    this.collapseSelectColumns = flags.has(Flag.COLLAPSE_LONG_SELECT_COLUMNS);
    this.collapseInExpressions = flags.has(Flag.COLLAPSE_LONG_IN_EXPRESSIONS);
  }

  public void execute(Statement statement) {
    statement.accept(new ReplaceStatement());
  }

  public void execute(ParsedSQLInfo sqlInfo) {
    sqlInfo.accept(new ReplaceStatement());
  }

  static class CollapseExpressions extends ItemsListVisitorAdapter {
    @Override
    public void visit(ExpressionList expressionList) {
      expressionList.setExpressions(List.of(new StringValue("<" + expressionList.getExpressions().size() + " items>")));
    }
  }

  class ReplaceSelect extends SelectVisitorAdapter {
    @Override
    public void visit(PlainSelect plainSelect) {
      if (collapseSelectColumns) {
        plainSelect.setSelectItems(List.of(new AllColumns()));
      }
      if (plainSelect.getWhere() != null) {
        plainSelect.getWhere().accept(new ReplaceExpression());
      }

      FromItem fromItem = plainSelect.getFromItem();
      if (fromItem != null) {
        fromItem.accept(new FromItemVisitorAdapter() {
          @Override
          public void visit(LateralSubSelect lateralSubSelect) {
            lateralSubSelect.getSubSelect().getSelectBody().accept(new ReplaceSelect());
          }

          @Override
          public void visit(ParenthesisFromItem aThis) {
            aThis.accept(this);
            aThis.getFromItem().accept(this);
          }

          @Override
          public void visit(SubJoin subjoin) {
            if (subjoin.getLeft() != null) {
              subjoin.getLeft().accept(this);
            }
            List<Join> joins = new ArrayList<>();
            for (Join join : subjoin.getJoinList()) {
              if (join.getRightItem() != null) {
                join.getRightItem().accept(this);
              }
              if (join.getOnExpression() != null) {
                join.getOnExpression().accept(new ReplaceExpression());
              }
              joins.add(join);
            }
            subjoin.setJoinList(joins);
          }

          @Override
          public void visit(SubSelect subSelect) {
            subSelect.getSelectBody().accept(new ReplaceSelect());
          }
        });
      }

      if (plainSelect.getJoins() != null) {
        List<Join> joins = new ArrayList<>();
        for (Join join : plainSelect.getJoins()) {
          if (join.getOnExpression() != null) {
            join.getOnExpression().accept(new ReplaceExpression());
          }
          joins.add(join);
        }
        plainSelect.setJoins(joins);
      }
    }

    // union
    @Override
    public void visit(SetOperationList setOpList) {
      List<SelectBody> selects = new ArrayList<>();
      for (SelectBody selectBody : setOpList.getSelects()) {
        selectBody.accept(new ReplaceSelect());
        selects.add(selectBody);
      }
      setOpList.setSelects(selects);
    }
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
      update.getWhere().accept(new ReplaceExpression());
    }

    @Override
    public void visit(Delete delete) {
      delete.getWhere().accept(new ReplaceExpression());
    }

    @Override
    public void visit(Select select) {
      if (collapseSelectColumns) {
        select.getSelectBody().accept(new SelectVisitorAdapter() {
          @Override
          public void visit(PlainSelect plainSelect) {
            plainSelect.setSelectItems(List.of(new AllColumns()));
          }
        });
      }
      select.getSelectBody().accept(new ReplaceSelect());
    }

    @Override
    public void visit(Insert insert) {
      insert.getItemsList().accept(new ItemsListVisitorAdapter() {
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
      value.setValue(replaceString);
    }

    @Override
    public void visit(SubSelect subSelect) {
      subSelect.getSelectBody().accept(new ReplaceSelect());
    }

    @Override
    public void visit(LongValue value) {
      value.setValue(-1);
    }

    @Override
    public void visit(InExpression expr) {
      if (expr.getLeftItemsList() != null) {
        expr.getLeftItemsList().accept(new ReplaceExpression());
      }
      if (expr.getRightItemsList() != null) {
        expr.getRightItemsList().accept(new ReplaceExpression());
      }

      if (collapseInExpressions) {
        if (expr.getLeftItemsList() != null) {
          expr.getLeftItemsList().accept(new CollapseExpressions());
        }
        if (expr.getRightItemsList() != null) {
          expr.getRightItemsList().accept(new CollapseExpressions());
        }
      }
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
  }
}
