package net.nokok.masq.impl.mysql.plainsql;

import net.nokok.masq.cli.context.Context;
import net.nokok.masq.impl.MasqProcess;
import net.nokok.masq.impl.ParseResults;
import net.nokok.masq.impl.Parser;
import net.sf.jsqlparser.statement.Statement;

public class PlainSQLProcess implements MasqProcess {
  private final Context context;

  public PlainSQLProcess(Context context) {
    this.context = context;
  }

  @Override
  public void execute() {
    Parser<Statement> parser = new ParseMySQLPlainSQL();
    ParseResults<Statement> parseResults = parser.parse(context.files());
    parseResults.map(r -> new MaskingParameters(this.context, r)).map(MaskingParameters::execute).forEach(System.out::println);
  }
}
