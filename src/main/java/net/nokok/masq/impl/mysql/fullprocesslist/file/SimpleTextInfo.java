package net.nokok.masq.impl.mysql.fullprocesslist.file;

public class SimpleTextInfo implements Info {
  private final String text;

  public SimpleTextInfo(String text) {
    this.text = text;
  }

  @Override
  public String value() {
    return this.text;
  }

  @Override
  public String toString() {
    return String.format("TXT: %s", this.text);
  }
}
