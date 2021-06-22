package net.nokok.masq.impl.mysql.fullprocesslist;

import net.nokok.masq.cli.context.Context;
import net.nokok.masq.impl.mask.jsqlparser.JsqlStatement;
import net.nokok.masq.impl.mysql.fullprocesslist.file.*;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.select.Select;

import java.util.Objects;

public class MaskingParameters {
  private final JsqlStatement mask;
  private final MySQLFullProcessList mySQLFullProcessList;

  public MaskingParameters(Context context, MySQLFullProcessList mySQLFullProcessList) {
    this.mask = new JsqlStatement(context);
    this.mySQLFullProcessList = Objects.requireNonNull(mySQLFullProcessList);
  }

  public MySQLFullProcessList execute() {
    for (LogPerDate log : mySQLFullProcessList.logs()) {
      for (RowData rowData : log.body()) {
        Info info = rowData.info();
        if (info instanceof ParsedSQLInfo sqlInfo) {
          mask.execute(sqlInfo);
        }
      }
    }
    return mySQLFullProcessList;
  }
}
