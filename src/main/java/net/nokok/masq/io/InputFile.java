package net.nokok.masq.io;

import net.nokok.masq.impl.SupportedFileTypes;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public interface InputFile {
  String fileName();

  Path path();

  SupportedFileTypes type();

  default byte[] readAllBytes() throws IOException {
    return Files.readAllBytes(path());
  }

  static InputFile of(Path path, SupportedFileTypes fileType) {
    class File implements InputFile {

      private final Path path;
      private final SupportedFileTypes type;

      File(Path path, SupportedFileTypes type) {
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
    return new File(path, fileType);
  }

  static InputFile ofVirtualFile(String fileBody, SupportedFileTypes fileType) {
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
    return new VirtualFile(Objects.requireNonNull(fileBody), fileType);
  }
}
