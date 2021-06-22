package net.nokok.masq.impl;


import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public interface ParseResult<R> {
  static <R> ParseResult<R> succeeded(R file) {
    return new Succeeded<>(file);
  }

  static <R> ParseResult<R> failed() {
    return new Failed<>();
  }

  class Succeeded<F> implements ParseResult<F> {
    private final F result;

    public Succeeded(F result) {
      this.result = Objects.requireNonNull(result);
    }

    @Override
    public Optional<F> result() {
      return Optional.of(result);
    }

    @Override
    public boolean isSucceeded() {
      return true;
    }
  }

  class Failed<F> implements ParseResult<F> {

    @Override
    public Optional<F> result() {
      return Optional.empty();
    }

    @Override
    public boolean isSucceeded() {
      return false;
    }
  }

  Optional<R> result();

  boolean isSucceeded();

  default <M> ParseResult<M> map(Function<R, M> f) {
    Optional<M> m = this.result().map(f);
    return m.map(ParseResult::succeeded).orElseGet(ParseResult::failed);
  }

  default void forEach(Consumer<R> c) {
    this.result().ifPresent(c);
  }
}

