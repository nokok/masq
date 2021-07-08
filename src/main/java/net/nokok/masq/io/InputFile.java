package net.nokok.masq.io;

import net.nokok.masq.impl.SupportedFileTypes;

import java.io.IOException;
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
    return new RealFile(path, fileType);
  }

  static InputFile ofVirtualFile(String fileBody, SupportedFileTypes fileType) {
    return new VirtualFile(Objects.requireNonNull(fileBody), fileType);
  }
}
