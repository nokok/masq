package net.nokok.masq.impl.mysql.fullprocesslist;

import net.nokok.masq.io.InputFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IndentSizeDetector {
  private final String body;

  public IndentSizeDetector(String body) {
    this.body = Objects.requireNonNull(body);
  }

  public IndentSizeDetector(InputFile inputFile) {
    try {
      this.body = new String(inputFile.readAllBytes());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public int detect() {
    int[] ints = this.body.lines().limit(20).mapToInt(l -> l.indexOf(":")).filter(i -> i != -1).toArray();
    return new Mode(ints).value();
  }

  class Mode {
    private final int[] values;

    public Mode(int[] values) {
      this.values = values;
    }

    int value() {
      Map<Integer, Integer> table = new HashMap<>();
      for (int i : values) {
        Integer value = table.getOrDefault(i, 0);
        table.put(i, value + 1);
      }
      int modeIndentSize = -1;
      int maxCount = 0;
      for (Map.Entry<Integer, Integer> integerIntegerEntry : table.entrySet()) {
        Integer key = integerIntegerEntry.getKey();
        Integer value = integerIntegerEntry.getValue();
        if (value > maxCount) {
          maxCount = value;
          modeIndentSize = key;
        }
      }
      return modeIndentSize;
    }
  }
}
