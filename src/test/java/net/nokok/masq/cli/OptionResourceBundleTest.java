package net.nokok.masq.cli;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class OptionResourceBundleTest {

  @Test
  public void testSupportedLocale_ja_JP() {
    OptionResourceBundle resourceBundle = new OptionResourceBundle(Locale.JAPAN);
    assertTrue(resourceBundle.isSupportedLocale());
  }

  @Test
  public void testSupportedLocale_en_US() {
    OptionResourceBundle resourceBundle = new OptionResourceBundle(Locale.US);
    assertTrue(resourceBundle.isSupportedLocale());
  }

  @Test
  public void testResourceBundleKeyDefinition() {
    List<Option> keys = Arrays.stream(Option.values()).toList();
    List<OptionResourceBundle> resourceBundles = OptionResourceBundle.availableLocales().stream().map(OptionResourceBundle::new).toList();
    for (Option key : keys) {
      for (OptionResourceBundle resourceBundle : resourceBundles) {

        assertDoesNotThrow(() -> assertNotNull(resourceBundle.getMessage(key)));
      }
    }
  }

}
