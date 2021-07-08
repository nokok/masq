package net.nokok.masq.io;

import net.nokok.masq.impl.SupportedFileTypes;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

class VirtualFile implements InputFile {
  private final Path filePath;
  private final SupportedFileTypes type;

  VirtualFile(String fileBody, SupportedFileTypes type) {
    try {
      this.filePath = Files.createTempFile("virtualfile", "");
      Files.writeString(this.filePath, fileBody);
      this.type = Objects.requireNonNull(type);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public String fileName() {
    return "<unknown>";
  }

  @Override
  public Path path() {
    return filePath;
  }

  @Override
  public SupportedFileTypes type() {
    return this.type;
  }

  @Override
  public String toString() {
    return String.format(
      "VirtualFile (filePath=%s, type=%s)", "<virtual>", this.type);
  }
}
