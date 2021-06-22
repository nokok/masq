package net.nokok.masq.impl;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public record ParseResults<T>(List<ParseResult<T>> results) {
  public <M> Stream<M> map(Function<T, M> f) {
    return results.stream().flatMap(m -> m.result().map(f).stream());
  }

  public boolean hasFailedResult() {
    return this.results.stream().anyMatch(r -> !r.isSucceeded());
  }
}
