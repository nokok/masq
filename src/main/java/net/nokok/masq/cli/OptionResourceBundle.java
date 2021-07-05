package net.nokok.masq.cli;

import java.util.*;

public class OptionResourceBundle {
  private final Locale locale;
  private final ResourceBundle resourceBundle;

  public OptionResourceBundle(Locale locale) {
    Objects.requireNonNull(locale);
    if (availableLocales().contains(locale)) {
      this.resourceBundle = ResourceBundle.getBundle("net.nokok.masq.cli.help.messages", locale);
    } else {
      this.resourceBundle = ResourceBundle.getBundle("net.nokok.masq.cli.help.messages", Locale.US);
    }
    this.locale = locale;
  }

  public static Set<Locale> availableLocales() {
    return Set.of(Locale.JAPAN, Locale.US);
  }

  public boolean isSupportedLocale() {
    return availableLocales().contains(locale);
  }

  public String getMessage(Option option) {
    return this.resourceBundle.getString(option.descriptionKey());
  }

}
