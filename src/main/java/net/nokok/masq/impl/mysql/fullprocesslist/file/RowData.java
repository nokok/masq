package net.nokok.masq.impl.mysql.fullprocesslist.file;

import java.util.Objects;

public record RowData(
  RowNumber rowNumber,
  Id id,
  User user,
  Host host,
  Db db,
  Command command,
  Time time,
  State state,
  Info info
) {
  public static class Builder {
    RowNumber rowNumber;
    private Id id;
    private User user;
    private Host host;
    private Db db;
    private Command command;
    private Time time;
    private State state;
    private Info info;

    public void setRowNumber(RowNumber rowNumber) {
      if (this.rowNumber != null) {
        throw new IllegalStateException();
      }
      this.rowNumber = rowNumber;
    }

    public void setId(Id id) {
      if (this.id != null) {
        throw new IllegalStateException();
      }
      this.id = id;
    }

    public void setUser(User user) {
      if (this.user != null) {
        throw new IllegalStateException();
      }
      this.user = user;
    }

    public void setHost(Host host) {
      if (this.host != null) {
        throw new IllegalStateException();
      }
      this.host = host;
    }

    public void setDb(Db db) {
      if (this.db != null) {
        throw new IllegalStateException();
      }
      this.db = db;
    }

    public void setCommand(Command command) {
      if (this.command != null) {
        throw new IllegalStateException();
      }
      this.command = command;
    }

    public void setTime(Time time) {
      if (this.time != null) {
        throw new IllegalStateException();
      }
      this.time = time;
    }

    public void setState(State state) {
      if (this.state != null) {
        throw new IllegalStateException();
      }
      this.state = state;
    }

    public void setInfo(Info info) {
      if (this.info != null) {
        throw new IllegalStateException();
      }
      this.info = info;
    }

    public RowData build() {
      Objects.requireNonNull(this.rowNumber);
      Objects.requireNonNull(this.id);
      Objects.requireNonNull(this.user);
      Objects.requireNonNull(this.host);
      Objects.requireNonNull(this.db);
      Objects.requireNonNull(this.command);
      Objects.requireNonNull(this.time);
      Objects.requireNonNull(this.state);
      Objects.requireNonNull(this.info);

      return new RowData(
        this.rowNumber,
        this.id,
        this.user,
        this.host,
        this.db,
        this.command,
        this.time,
        this.state,
        this.info
      );
    }
  }

  @Override
  public String toString() {
    return """
          RowData(
            rowNumber : %s
                   id : %s
                 user : %s
                 host : %s
                   db : %s
              command : %s
                 time : %s
                state : %s
                 info : %s
          )
        """.strip().formatted(
      this.rowNumber,
      this.id,
      this.user,
      this.host,
      this.db,
      this.command,
      this.time,
      this.state,
      this.info
    );
  }
}
