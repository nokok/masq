package net.nokok.masq.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProgressBarTest {

  private ByteArrayOutputStream output;
  private ProgressBar progressBar;

  @BeforeEach
  public void beforeEach() throws Exception {
    output = new ByteArrayOutputStream();
    progressBar = new ProgressBar(100, new PrintStream(output));
  }


  @Test
  public void testInitWithZero() {
    assertThrows(IllegalArgumentException.class, () -> new ProgressBar(0));
  }

  @Test
  public void test0Percent() {
    progressBar.printProgress();
    assertEquals("[                                                  ] 0.0% 0/100", output.toString());
  }

  @Test
  public void test50Percent() {
    progressBar.increment(50);
    progressBar.printProgress();
    assertEquals("[#########################                         ] 50.0% 50/100", output.toString());
  }

  @Test
  public void testComplete1() {
    progressBar.complete();
    progressBar.printProgress();
    assertEquals("[##################################################] 100.0% 100/100", output.toString());
  }

  @Test
  public void testComplete2() {
    progressBar.increment(100);
    progressBar.printProgress();
    assertEquals("[##################################################] 100.0% 100/100", output.toString());
  }

  @Test
  public void testComplete3() {
    progressBar.increment(101);
    progressBar.printProgress();
    assertEquals("[##################################################] 100.0% 100/100", output.toString());
  }

  @Test
  public void testIncrement() {
    progressBar.increment(25);
    progressBar.increment(25);
    progressBar.printProgress();
    assertEquals("[#########################                         ] 50.0% 50/100", output.toString());
  }

}
