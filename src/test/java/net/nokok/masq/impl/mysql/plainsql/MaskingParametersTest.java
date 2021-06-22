package net.nokok.masq.impl.mysql.plainsql;

import net.nokok.masq.cli.Flag;
import net.nokok.masq.cli.Flags;
import net.nokok.masq.cli.context.Context;
import net.nokok.masq.impl.SupportedFileTypes;
import net.nokok.masq.io.InputFile;
import net.nokok.masq.io.InputFiles;
import net.sf.jsqlparser.statement.Statement;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaskingParametersTest {
  class DummyContext implements Context {

    @Override
    public PrintStream console() {
      return null;
    }

    @Override
    public SupportedFileTypes fileType() {
      return null;
    }

    @Override
    public InputFiles files() {
      return null;
    }

    @Override
    public Flags flags() {
      return null;
    }

    @Override
    public String replaceString() {
      return "<masked>";
    }

    @Override
    public boolean hasFlag(Flag flag) {
      return false;
    }

    @Override
    public boolean hasError() {
      return false;
    }
  }

  private Context dummyContext = new DummyContext();

  @Test
  public void maskSimpleSelect() {
    ParseMySQLPlainSQL parse = new ParseMySQLPlainSQL();

    Statement statement = parse.parsePerFile(InputFile.ofVirtualFile(
      "select * from users where user_name = 'Hoge'"
      , SupportedFileTypes.PlainSQL)).result().get();
    MaskingParameters maskParameters = new MaskingParameters(this.dummyContext, statement);
    maskParameters.execute();
    assertEquals("SELECT * FROM users WHERE user_name = '<masked>'", statement.toString());
  }

  @Test
  public void testSimpleUpdate() {
    ParseMySQLPlainSQL parse = new ParseMySQLPlainSQL();

    Statement statement = parse.parsePerFile(InputFile.ofVirtualFile(
      "update users set user_name = 'Fuga' where user_id = 1", SupportedFileTypes.PlainSQL
    )).result().get();
    MaskingParameters maskParameters = new MaskingParameters(this.dummyContext, statement);
    maskParameters.execute();
    assertEquals("UPDATE users SET user_name = '<masked>' WHERE user_id = 1", statement.toString());
  }

  @Test
  public void testSimpleDelete() {
    ParseMySQLPlainSQL parse = new ParseMySQLPlainSQL();

    Statement statement = parse.parsePerFile(InputFile.ofVirtualFile(
      "DELETE FROM users where user_name = 'Hoge'", SupportedFileTypes.PlainSQL
    )).result().get();
    MaskingParameters maskParameters = new MaskingParameters(this.dummyContext, statement);
    maskParameters.execute();
    assertEquals("DELETE FROM users WHERE user_name = '<masked>'", statement.toString());
  }
}
