package net.nokok.masq.cli;

public class ProgressBar {
  private final int max;
  private int current = 0;
  private int lastProgressBarLength = 0;

  public ProgressBar(int max) {
    this.max = max;
  }

  public void increment(int current) {
    this.current += current;
  }

  public void setMax() {
    this.current = max;
  }

  public void printProgress() {
    double percent = ((double)current / max) * 100;
    System.err.print("\b".repeat(lastProgressBarLength));
    String progressbar = "[%-100s] %.1f%% %s/%s".formatted("#".repeat((int) percent), percent, current, max);
    lastProgressBarLength = progressbar.length();
    System.err.print(progressbar);
  }
}
