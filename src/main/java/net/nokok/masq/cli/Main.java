package net.nokok.masq.cli;

import net.nokok.masq.cli.context.Context;
import net.nokok.masq.cli.context.ErrorContext;
import net.nokok.masq.cli.help.ShowHelp;
import net.nokok.masq.impl.MasqProcess;

public class Main {
  public static void main(String[] args) {
    Thread.setDefaultUncaughtExceptionHandler((t, throwable) -> {
      throwable.printStackTrace();
      if (isFatal(throwable)) {
        return;
      }
      System.err.println("######################### BUG #########################");
      System.exit(-1);
    });

    ArgsParser argsParser = new ArgsParser(SupportOptionSet.DEFAULT);
    Context context = argsParser.parse(args);
    if (context.hasError()) {
      System.err.println(((ErrorContext) context).message());
    }
    if (context.hasError() || context.hasFlag(Flag.SHOW_HELP)) {
      new ShowHelp().printHelp();
      System.exit(0);
    }
    MasqProcess process = MasqProcess.of(context);
    process.execute();
  }

  private static boolean isFatal(Throwable e) {
    return e instanceof Error;
  }
}

