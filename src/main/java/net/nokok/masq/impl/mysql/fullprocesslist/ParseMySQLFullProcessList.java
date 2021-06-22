package net.nokok.masq.impl.mysql.fullprocesslist;

import net.nokok.masq.cli.ProgressBar;
import net.nokok.masq.io.InputFile;
import net.nokok.masq.impl.ParseResult;
import net.nokok.masq.impl.Parser;
import net.nokok.masq.impl.mysql.fullprocesslist.file.*;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseMySQLFullProcessList implements Parser<MySQLFullProcessList> {

  @Override
  public ParseResult<MySQLFullProcessList> parsePerFile(InputFile file) {
    Pattern number = Pattern.compile("[0-9]+");

    try {
      System.err.println("Normalizing...");
      String body = new SingleLineTransformer(file).transform();
      BufferedReader bufferedReader = new BufferedReader(new StringReader(body));
      RowData.Builder builder = new RowData.Builder();
      List<RowData> rows = new ArrayList<>();
      String line;
      LocalDateTime localDateTime = LocalDateTime.now();
      ProgressBar progressBar = new ProgressBar(body.length());
      Runnable printProgressBar = progressBar::printProgress;
      ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
      scheduledExecutorService.scheduleAtFixedRate(printProgressBar, 0, 500, TimeUnit.MILLISECONDS);
      while ((line = bufferedReader.readLine()) != null) {
        progressBar.increment(line.length());
        if (line.isEmpty()) {
          continue;
        }
        int i = line.indexOf(":");
        if (line.startsWith("DATE: ")) {
          // time stamp row
          // DATE: 2021-06-23-00:00:01
          localDateTime = LocalDateTime.parse(line.replaceAll("DATE: ", ""), DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss"));
        } else if (i == -1 && line.startsWith("****")) {
          // header row
          Matcher matcher = number.matcher(line);
          if (!matcher.find()) {
            throw new IllegalStateException();
          }
          String rowNumber = matcher.group();
          builder.setRowNumber(new RowNumber(Integer.parseInt(rowNumber)));
        } else {
          String dataHeader = line.substring(0, i).strip();
          String data = line.substring(i + 1).strip();
          Optional<RowData> d = switch (dataHeader) {
            case "Id" -> {
              builder.setId(new Id(Integer.parseInt(data)));
              yield Optional.empty();
            }
            case "User" -> {
              builder.setUser(new User(data));
              yield Optional.empty();
            }
            case "Host" -> {
              builder.setHost(new Host(data));
              yield Optional.empty();
            }
            case "db" -> {
              builder.setDb(new Db(data));
              yield Optional.empty();
            }
            case "Command" -> {
              builder.setCommand(new Command(data));
              yield Optional.empty();
            }
            case "Time" -> {
              builder.setTime(new Time(data));
              yield Optional.empty();
            }
            case "State" -> {
              builder.setState(new State(data));
              yield Optional.empty();
            }
            case "Info" -> {
              try {
                Statement statement = CCJSqlParserUtil.parse(data);
                builder.setInfo(new ParsedSQLInfo(data, statement));
              } catch (JSQLParserException e) {
                builder.setInfo(new SimpleTextInfo(data));
              }
              RowData rowData = builder.build();
              if (rowData.info().value().equalsIgnoreCase("NULL") ||
                rowData.info().value().equalsIgnoreCase("show full processlist") ||
                rowData.info().value().equalsIgnoreCase("SET autocommit = 1")) {
                builder = new RowData.Builder();
                yield Optional.empty();
              } else {
                builder = new RowData.Builder();
                yield Optional.of(rowData);
              }
            }
            default -> Optional.empty();
          };
          d.ifPresent(rows::add);
        }
      }
      progressBar.setMax();
      progressBar.printProgress();
      scheduledExecutorService.shutdown();
      LogPerDate l = new LogPerDate(localDateTime, rows);
      return ParseResult.succeeded(new MySQLFullProcessList(
        List.of(l)
      ));
    } catch (IOException e) {
      return ParseResult.failed();
    }
  }
}
