package net.nokok.masq.args;

public class UnknownOptionException extends Exception {
  public UnknownOptionException(String unknownOption) {
    super("Unknown option : " + unknownOption);
  }
}
