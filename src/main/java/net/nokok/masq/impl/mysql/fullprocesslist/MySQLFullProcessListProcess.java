package net.nokok.masq.impl.mysql.fullprocesslist;

import net.nokok.masq.cli.context.Context;
import net.nokok.masq.impl.MasqProcess;
import net.nokok.masq.impl.ParseResults;
import net.nokok.masq.impl.Parser;
import net.nokok.masq.impl.mask.jsqlparser.JsqlStatement;
import net.nokok.masq.impl.mysql.fullprocesslist.file.LogPerDate;
import net.nokok.masq.impl.mysql.fullprocesslist.file.MySQLFullProcessList;
import net.nokok.masq.impl.mysql.fullprocesslist.file.RowData;

import java.io.PrintStream;
import java.util.List;

public class MySQLFullProcessListProcess implements MasqProcess {
  private final Context context;

  public MySQLFullProcessListProcess(Context context) {
    this.context = context;
  }

  @Override
  public void execute() {
    Parser<MySQLFullProcessList> parser = new ParseMySQLFullProcessList();
    System.err.println("Loading...");
    ParseResults<MySQLFullProcessList> parseResults = parser.parse(context.files());
    System.err.println("Complete");
    List<MySQLFullProcessList> maskedFullProcessLists = parseResults.map(r -> new MaskingParameters(this.context, r)).map(MaskingParameters::execute).toList();
    System.err.println("Mask...");

    StringBuilder sb = new StringBuilder();
    for (MySQLFullProcessList maskedFullProcessList : maskedFullProcessLists) {
      for (LogPerDate log : maskedFullProcessList.logs()) {
        for (RowData rowData : log.body()) {
          sb.append("DateTime=").append(log.dateTime()).append(",");
          sb.append("Row=").append(rowData.rowNumber().value()).append(",");
          sb.append("Id=").append(rowData.id().value()).append(",");
          sb.append("User=").append(rowData.user().name()).append(",");
          sb.append("Host=").append(rowData.host().name()).append(",");
          sb.append("Db=").append(rowData.db().name()).append(",");
          sb.append("Command=").append(rowData.command().value()).append(",");
          sb.append("Time=").append(rowData.time().value()).append(",");
          sb.append("State=").append(rowData.state().value()).append(",");
          /* Infoだけテキストの場合とSQLの場合があるので、出力はtoStringに任せる */
          sb.append("Info=\"\"\"").append(rowData.info()).append("\"\"\"");
          sb.append(System.lineSeparator());
        }
      }
    }
    PrintStream console = context.console();
    console.print(sb);
  }
}
