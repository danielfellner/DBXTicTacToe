package hu.dbx.homework;

public class AnalyserResult {

  private GameStatusEnum status = null;
  private String resultMessage = null;
  private boolean player1Won = false;
  private boolean player2Won = false;
  private boolean draw = false;

  public enum GameStatusEnum {
    CANNOT_DECIDED_DATA_PARAMETER_ERROR(true),
    CANNOT_DECIDED_GAME_PLAY_ERROR(true),
    DECIDED_ENDGAME(false);
    private boolean errorState;

    private GameStatusEnum(boolean errorState) {
      this.errorState = errorState;
    }

    public boolean isErrorState() {
      return errorState;
    }
  }

  public AnalyserResult(GameStatusEnum status, String resultMessages) {
    this.status = status;
    this.resultMessage = resultMessages;
  }

  public GameStatusEnum getStatus() {
    return status;
  }

  public String getResultMessage() {
    return resultMessage;
  }

  public void setPlayer1Won(boolean won) {
    this.player1Won = won;
  }

  public boolean isPlayer1Won() {
    return this.player1Won;
  }

  public void setPlayer2Won(boolean won) {
    this.player2Won = won;
  }

  public boolean isPlayer2Won() {
    return this.player2Won;
  }

  public void setDraw(boolean draw) {
    this.draw = draw;
  }

  public boolean isDraw() {
    return this.draw;
  }
}
