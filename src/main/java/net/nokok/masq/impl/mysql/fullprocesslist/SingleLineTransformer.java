package net.nokok.masq.impl.mysql.fullprocesslist;

import net.nokok.masq.io.InputFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.Objects;

public class SingleLineTransformer {
  private final InputFile inputFile;

  public SingleLineTransformer(InputFile inputFile) {
    this.inputFile = Objects.requireNonNull(inputFile);
  }

  public String transform() {
    try {
      int indentSize = new IndentSizeDetector(this.inputFile).detect();
      BufferedReader bufferedReader = new BufferedReader(Files.newBufferedReader(this.inputFile.path()));
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        if (line.startsWith("*****")) {
          // *** 1. row *** という文字列の場合は継続しない
          if (sb.length() != 0 && sb.charAt(sb.length() - 1) != '\n') {
            sb.append(System.lineSeparator());
          }
          sb.append(line).append(System.lineSeparator());
          continue;
        }
        int colonPosition = line.indexOf(":");

        boolean hasStandardIndent = line.startsWith(" ".repeat(indentSize));
        boolean hasColon = line.contains(":");
        boolean hasColonAfterColumn = colonPosition > indentSize;
        boolean hasColonBeforeColumn = hasColon && colonPosition < indentSize;
        boolean hasColonInSamePosition = hasColon && colonPosition == indentSize;

        boolean concatenatePreviousLine = false;

        // 行が通常のインデントがされている(検出したインデントサイズ分の空白がある)場合は直前の行に繋げる
        // Stateとかで複数行含む場合はこのパターンになる
        concatenatePreviousLine |= hasStandardIndent;

        // 行が通常のインデントされているかに関わらずコロンを含まない場合は直前の行に繋げる
        // 時刻(コロンを含むデータの例)などを含まないSELECT文はこれにあたる
        concatenatePreviousLine |= !hasColon;

        // コロンの位置が検出したインデントサイズよりも右にある場合は直前の行に繋げる
        concatenatePreviousLine |= hasColon && hasColonAfterColumn;

        // コロンの位置が検出したインデントサイズよりも左にあって、それがヘッダでなさそうなら直前の行に繋げる
        concatenatePreviousLine |= hasColonBeforeColumn && !line.endsWith(":");

        // コロンの位置が検出したインデントと同じ位置にあり、": " を含まない場合はデータなので直前の行に繋げる
        // ただし、: でデータが終わるときは繋げない(ので、:で終わらない行を繋げる
        concatenatePreviousLine |= hasColonInSamePosition && !line.contains(": ") && !line.endsWith(":");

        if (concatenatePreviousLine) {
          if (sb.length() > 0) {
            sb.append(" ");
          }
          sb.append(line.strip());
        } else {
          if (sb.length() != 0 && sb.charAt(sb.length() - 1) != '\n') {
            sb.append(System.lineSeparator());
          }
          sb.append(line);
        }
      }
      return sb.toString();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
