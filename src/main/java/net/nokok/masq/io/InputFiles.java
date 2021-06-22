package net.nokok.masq.io;

import net.nokok.masq.impl.SupportedFileTypes;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class InputFiles {
  private final List<InputFile> files;

  public InputFiles(List<InputFile> files) {
    this.files = Objects.requireNonNull(files);
    SupportedFileTypes fileTypes = this.files.stream().findFirst().map(InputFile::type).orElse(SupportedFileTypes.Unknown);
    if (this.files.stream().map(InputFile::type).anyMatch(t -> !t.equals(fileTypes))) {
      throw new IllegalArgumentException();
    }
  }

  public SupportedFileTypes fileTypes() {
    return this.files.stream().map(InputFile::type).findFirst().orElse(SupportedFileTypes.Unknown);
  }

  public <R> Stream<R> map(Function<InputFile, R> mapper) {
    return this.files.stream().map(mapper);
  }

  public boolean isEmpty() {
    return this.files.isEmpty();
  }

  public void forEach(Consumer<? super InputFile> action) {
    this.files.forEach(action);
  }

  @Override
  public String toString() {
    return String.format(
      "InputFiles (files=%s)", this.files);
  }
}
