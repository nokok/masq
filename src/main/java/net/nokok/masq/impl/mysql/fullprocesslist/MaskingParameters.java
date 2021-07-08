package net.nokok.masq.impl.mysql.fullprocesslist;

import net.nokok.masq.cli.context.Context;
import net.nokok.masq.impl.mask.jsqlparser.JsqlStatementReplacer;
import net.nokok.masq.impl.mysql.fullprocesslist.file.*;

import java.util.Objects;

public class MaskingParameters {
  private final JsqlStatementReplacer mask;
  private final MySQLFullProcessList mySQLFullProcessList;

  public MaskingParameters(Context context, MySQLFullProcessList mySQLFullProcessList) {
    this.mask = new JsqlStatementReplacer(context);
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
