package hu.dbx.homework;

import org.apache.commons.cli.Options;

public class CmdOptions {

  private static Options cmdOptionsField = new Options();

  static {
    for (ProgramArgumentsEnum arg : ProgramArgumentsEnum.values()) {
      cmdOptionsField.addOption(arg.getShortName(), arg.getLongName(), true, arg.getDescription());
    }
  }

  public static Options getCmdOptionsField() {
    return cmdOptionsField;
  }

  public enum ProgramArgumentsEnum {
    FILENAME("f", "filename", "Tic-tac-toe game state input file"),
    MARKER_TO_WIN("m", "markers", "Specify how many marker needs to win");

    private final String shortName;
    private final String longName;
    private final String description;

    ProgramArgumentsEnum(String shortName, String longName, String description) {
      this.shortName = shortName;
      this.longName = longName;
      this.description = description;
    }

    public String getShortName() {
      return this.shortName;
    }

    public String getLongName() {
      return this.longName;
    }

    public String getDescription() {
      return this.description;
    }
  }
}
