package net.nokok.masq.cli;

import net.nokok.masq.args.Arguments;
import net.nokok.masq.args.NoArgumentException;
import net.nokok.masq.args.UnknownOptionException;
import net.nokok.masq.cli.context.Context;
import net.nokok.masq.cli.context.ErrorContext;
import net.nokok.masq.cli.context.StandardContext;
import net.nokok.masq.impl.SupportedFileTypes;
import net.nokok.masq.io.InputFile;
import net.nokok.masq.io.InputFiles;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class ArgsParser {
  private final SupportOptionSet supportOptionSet;

  public ArgsParser(SupportOptionSet supportOptionSet) {
    this.supportOptionSet = supportOptionSet;
  }

  public Context parse(String... rawArgs) {
    Objects.requireNonNull(rawArgs);
    if (rawArgs.length == 0) {
      return new ErrorContext();
    }
    Arguments arguments = new Arguments(supportOptionSet, rawArgs);
    List<Flag> flags = new ArrayList<>();
    List<Path> files = new ArrayList<>();
    SupportedFileTypes fileTypes = SupportedFileTypes.DEFAULT;
    String replaceString = "<mask>";
    while (arguments.hasNext()) {
      arguments.next();
      try {
        if (arguments.wasOption()) {
          Option option = arguments.asOption();
          switch (option) {
            case HELP -> flags.add(Flag.SHOW_HELP);
            case INPUT_FORMAT -> fileTypes = arguments.nextOptionString(SupportedFileTypes::fromOptionString, formatType -> "Invalid format type : " + formatType);
            case REPLACE_STRING -> replaceString = arguments.nextOptionString(() -> "No Replace String");
          }
        } else if (arguments.wasFile()) {
          files.add(arguments.asPath());
        }
      } catch (UnknownOptionException | NoArgumentException e) {
        return new ErrorContext(e.getMessage());
      }
    }
    if (flags.contains(Flag.SHOW_HELP)) {
      // show usage
      return new ErrorContext();
    }
    if (files.isEmpty()) {
      return new ErrorContext("No input files");
    }

    final SupportedFileTypes confirmedFileType = fileTypes;
    return new StandardContext(System.out, new InputFiles(files.stream().map(f -> InputFile.of(f, confirmedFileType)).toList()), new Flags(new HashSet<>(flags)), replaceString);

  }

}
