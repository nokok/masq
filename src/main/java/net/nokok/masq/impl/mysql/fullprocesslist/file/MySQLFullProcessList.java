package net.nokok.masq.impl.mysql.fullprocesslist.file;

import net.nokok.masq.impl.mysql.fullprocesslist.file.LogPerDate;

import java.util.List;

public record MySQLFullProcessList(List<LogPerDate> logs) {
  public int logsCount() {
    return logs.size();
  }

  public boolean isEmpty() {
    return logs.isEmpty();
  }
}
