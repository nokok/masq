package net.nokok.masq.cli;

import java.io.PrintStream;
import java.util.Objects;

public class ProgressBar {
  private final int max;
  private final PrintStream console;
  private int current = 0;
  private int lastProgressBarLength = 0;

  public ProgressBar(int max) {
    this(max, System.err);
  }

  public ProgressBar(int max, PrintStream console) {
    if (max <= 0) {
      throw new IllegalArgumentException("Negative value: max=" + max);
    }
    this.max = max;
    this.console = Objects.requireNonNull(console);
  }

  public void increment(int current) {
    if (this.current + current > max) {
      complete();
      return;
    }
    this.current += current;
  }

  public void complete() {
    this.current = max;
  }

  public void printProgress() {
    double percent = ((double) current / max) * 50;
    console.print("\b".repeat(lastProgressBarLength));
    String progressbar = "[%-50s] %.1f%% %s/%s".formatted("#".repeat((int) percent), percent * 2, current, max);
    lastProgressBarLength = progressbar.length();
    console.print(progressbar);
  }
}
