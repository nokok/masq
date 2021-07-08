package net.nokok.masq.io;

import net.nokok.masq.impl.SupportedFileTypes;

import java.nio.file.Path;
import java.util.Objects;

class RealFile implements InputFile {

  private final Path path;
  private final SupportedFileTypes type;

  RealFile(Path path, SupportedFileTypes type) {
    this.path = Objects.requireNonNull(path);
    this.type = Objects.requireNonNull(type);
  }

  @Override
  public String fileName() {
    return this.path.getFileName().toString();
  }

  @Override
  public Path path() {
    return this.path;
  }

  @Override
  public SupportedFileTypes type() {
    return this.type;
  }

  @Override
  public String toString() {
    return String.format(
      "File (path=%s, type=%s)", this.path.toAbsolutePath(), this.type);
  }
}
