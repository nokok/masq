package net.nokok.masq.impl;

import net.nokok.masq.cli.context.Context;
import net.nokok.masq.impl.mysql.fullprocesslist.MySQLFullProcessListProcess;
import net.nokok.masq.impl.mysql.plainsql.PlainSQLProcess;

public interface MasqProcess {
  void execute();

  static MasqProcess of(Context context) {
    return switch (context.fileType()) {
      case MySQLProcessList -> new MySQLFullProcessListProcess(context);
      case PlainSQL -> new PlainSQLProcess(context);
      default -> throw new UnsupportedOperationException(context.fileType().toString());
    };
  }
}
