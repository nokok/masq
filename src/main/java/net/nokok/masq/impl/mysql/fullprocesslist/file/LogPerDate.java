package net.nokok.masq.impl.mysql.fullprocesslist.file;

import java.time.LocalDateTime;
import java.util.List;

public record LogPerDate(LocalDateTime dateTime, List<RowData> body) {
}
