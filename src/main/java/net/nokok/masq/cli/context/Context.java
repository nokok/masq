package net.nokok.masq.cli.context;

import net.nokok.masq.cli.Flag;
import net.nokok.masq.cli.Flags;
import net.nokok.masq.impl.SupportedFileTypes;
import net.nokok.masq.io.InputFiles;

import java.io.PrintStream;

public interface Context {
  PrintStream console();

  SupportedFileTypes fileType();

  InputFiles files();

  Flags flags();

  String replaceString();

  boolean hasFlag(Flag flag);

  boolean hasError();
}
