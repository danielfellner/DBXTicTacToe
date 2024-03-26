package hu.dbx.homework;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** Tic-tac-toe end game analyser util class */
public class TicTacEndGameAnalyser {

  public static final byte DEFAULT_MARKER_TO_WIN = 3;
  private static final byte DEFAULT_MINIMAL_TABLE_SIZE = 3; // 3x3
  private static final Character PLAYER_ONE_TABLE_FIELD_MARKER_CHARACTER = 'X';
  private static final Character PLAYER_TWO_TABLE_FIELD_MARKER_CHARACTER = '0';
  private static final Character EMPTY_TABLE_FIELD_MARKER_CHARACTER = ' ';
  private static final String REGEXP_CHECK_PATTERN_TABLE_FIELD_MARKERS =
      "["
          + PLAYER_ONE_TABLE_FIELD_MARKER_CHARACTER
          + PLAYER_TWO_TABLE_FIELD_MARKER_CHARACTER
          + EMPTY_TABLE_FIELD_MARKER_CHARACTER
          + "]+";

  private int tableSize = 0;
  private int marksToWin = 0;
  private String[][] gameTable = null;
  private List<String> preGeneratedErrorMessages = new ArrayList<>();

  /**
   * Constructor
   * @param rawTableData - String param contains game table data
   * @param marksToWin - contains how many markers can be set to the right position to win the game
   */
  public TicTacEndGameAnalyser(String rawTableData, int marksToWin) {
    this.marksToWin = marksToWin;

    if (calculateTableSizeFromData(rawTableData)) {
      checkMarkersToWin(this.marksToWin);
      fullFillTableModelFromData(rawTableData);
    }
  }

  /**
   * Check the given markersToWin parameter is valid for the calculated table size and a minimal
   * value
   *
   * @param markersToWin - contains how many markers can be set to the right position to win the
   *     game
   */
  private void checkMarkersToWin(int markersToWin) {
    if (markersToWin < DEFAULT_MARKER_TO_WIN) {
      preGeneratedErrorMessages.add(
          "Markers to win less than "
              .concat("" + DEFAULT_MARKER_TO_WIN)
              .concat(" The value is :".concat("" + markersToWin)));
    }

    if (markersToWin > tableSize) {
      preGeneratedErrorMessages.add(
          "Markers count to win is bigger than game table size "
              .concat("" + tableSize)
              .concat(" The marker count is :".concat("" + markersToWin)));
    }
  }

  /**
   * Calculate the game table size from the given table data.
   *
   * @param rawData - contains game play characters only.
   * @return true if the table size decidable from the data
   */
  private boolean calculateTableSizeFromData(String rawData) {
    boolean result = false;
    if (rawData != null && rawData.length() >= getDefaultMinimalTableSizeDataLength()) {
      if (rawData.matches(REGEXP_CHECK_PATTERN_TABLE_FIELD_MARKERS)) {
        double dimension = Math.sqrt(rawData.length());
        if (Math.sqrt(rawData.length()) % 1 == 0) {
          tableSize = (int) dimension;
          result = true;
        } else {
          preGeneratedErrorMessages.add("Cannot calculate game table size from rawData!");
        }
      } else {
        preGeneratedErrorMessages.add(
            "There are some not enabled characters in the raw table data");
      }
    } else {
      if (rawData == null) {
        preGeneratedErrorMessages.add("There is no information about game table data.");
      } else {
        preGeneratedErrorMessages.add(
            "Checked data length ("+rawData.length()+") less than a minimal game table ("
                .concat("" + DEFAULT_MINIMAL_TABLE_SIZE)
                .concat("X")
                .concat("" + DEFAULT_MINIMAL_TABLE_SIZE)
                .concat(" ) data length : ")
                .concat("" + getDefaultMinimalTableSizeDataLength()));
      }
    }

    return result;
  }

  /**
   * Helper method to decide the default minimal table size data length count
   *
   * @return default minimal table size data length count
   */
  private static byte getDefaultMinimalTableSizeDataLength() {
    return DEFAULT_MINIMAL_TABLE_SIZE * DEFAULT_MINIMAL_TABLE_SIZE;
  }

  /**
   * Fill the inside table model from rawData
   *
   * @param rawData - contains game play characters
   */
  private void fullFillTableModelFromData(String rawData) {

    gameTable = new String[tableSize][tableSize];

    int index = 0;
    int player1Count = 0;
    int player2Count = 0;

    for (int columnIndex = 0; columnIndex < tableSize; columnIndex++) {
      for (int rowIndex = 0; rowIndex < tableSize; rowIndex++) {
        Character character = rawData.charAt(index++);
        if (PLAYER_ONE_TABLE_FIELD_MARKER_CHARACTER.equals(character)) {
          player1Count++;
        }
        if (PLAYER_TWO_TABLE_FIELD_MARKER_CHARACTER.equals(character)) {
          player2Count++;
        }
        gameTable[columnIndex][rowIndex] = character.toString();
      }
    }

    int playerCountDiff = Math.abs(player1Count - player2Count);
    if (playerCountDiff > 1) {
      preGeneratedErrorMessages.add(
          "Wrong game state! Cannot differ player's marker count than 1. "
              + PLAYER_ONE_TABLE_FIELD_MARKER_CHARACTER
              + " marker count: "
              + player1Count
              + " "
              + PLAYER_TWO_TABLE_FIELD_MARKER_CHARACTER
              + " marker count: "
              + player2Count);
    }
  }

  /**
   * Calculate the end game result.
   *
   * @return AnalyserResult
   */
  public AnalyserResult calculateEndGameResult() {

    if (preGeneratedErrorMessages.isEmpty()) {
      return parseGameTable();
    } else {
      return new AnalyserResult(
          AnalyserResult.GameStatusEnum.CANNOT_DECIDED_DATA_PARAMETER_ERROR,
          preGeneratedErrorMessages.stream().collect(Collectors.joining("\n")));
    }
  }

  /**
   * Parse the validated game table model.
   *
   * @return AnalyserResult
   */
  private AnalyserResult parseGameTable() {

    int playerOneWinsCount = 0;
    int playerTwoWinsCount = 0;

    for (int columnIndex = 0; columnIndex < tableSize; columnIndex++) {
      for (int rowIndex = 0; rowIndex < tableSize; rowIndex++) {
        String marker = gameTable[columnIndex][rowIndex];
        if (PLAYER_ONE_TABLE_FIELD_MARKER_CHARACTER.toString().equals(marker)
            || PLAYER_TWO_TABLE_FIELD_MARKER_CHARACTER.toString().equals(marker)) {
          int winCount = checkEndConditionsCount(columnIndex, rowIndex, marker);
          if (PLAYER_ONE_TABLE_FIELD_MARKER_CHARACTER.toString().equals(marker)) {
            playerOneWinsCount += winCount;
          } else {
            playerTwoWinsCount += winCount;
          }
        }
      }
    }

    return generateResultByWonCounts(playerOneWinsCount, playerTwoWinsCount);
  }

  /**
   * Decide the analyzed result from the calculated won counts.
   *
   * @param playerOneWins - how many times won player 1 the game
   * @param playerTwoWins - how many times won player 2 the game
   * @return AnalyserResult
   */
  private AnalyserResult generateResultByWonCounts(int playerOneWins, int playerTwoWins) {
    AnalyserResult result = null;
    if (playerOneWins == 0 && playerTwoWins == 0) {
      result =
          new AnalyserResult(
              AnalyserResult.GameStatusEnum.DECIDED_ENDGAME, "The game is in a draw");
      result.setDraw(true);
    } else if (playerOneWins > 0 && playerTwoWins == 0) {
      result =
          new AnalyserResult(
              AnalyserResult.GameStatusEnum.DECIDED_ENDGAME,
              "" + PLAYER_ONE_TABLE_FIELD_MARKER_CHARACTER + " player won the game!");
      result.setPlayer1Won(true);
    } else if (playerOneWins == 0 && playerTwoWins > 0) {
      result =
          new AnalyserResult(
              AnalyserResult.GameStatusEnum.DECIDED_ENDGAME,
              "" + PLAYER_TWO_TABLE_FIELD_MARKER_CHARACTER + " player won the game!");
      result.setPlayer2Won(true);
    } else {
      result =
          new AnalyserResult(
              AnalyserResult.GameStatusEnum.CANNOT_DECIDED_GAME_PLAY_ERROR,
              "Both players have the final move that would lead to victory, but it should only be available to one of them.");
    }

    return result;
  }

  /**
   * Check one table field marker to solve the marker to win condition to all ways from the field
   * position
   *
   * @param columnIndex - marker column position
   * @param rowIndex - marker row position
   * @param marker - marker that use a player in tic-tac-toe
   * @return how many times to win a player in this position
   */
  private int checkEndConditionsCount(int columnIndex, int rowIndex, String marker) {
    int result = 0;
    if (checkEndConditionRight(columnIndex, rowIndex, marker)) {
      result++;
    }

    if (checkEndConditionRightUp(columnIndex, rowIndex, marker)) {
      result++;
    }

    if (checkEndConditionRightDown(columnIndex, rowIndex, marker)) {
      result++;
    }

    if (checkEndConditionLeft(columnIndex, rowIndex, marker)) {
      result++;
    }

    if (checkEndConditionLeftUp(columnIndex, rowIndex, marker)) {
      result++;
    }

    if (checkEndConditionLeftDown(columnIndex, rowIndex, marker)) {
      result++;
    }

    if (checkEndConditionUp(columnIndex, rowIndex, marker)) {
      result++;
    }

    if (checkEndConditionDown(columnIndex, rowIndex, marker)) {
      result++;
    }

    return result;
  }

  /**
   * Check one table field marker to solve the marker to win condition in right way from the field
   * position
   *
   * @param columnIndex - marker column position
   * @param rowIndex - marker row position
   * @param marker - marker that use a player in tic-tac-toe
   * @return can be win a player in the right way?
   */
  private boolean checkEndConditionRight(int columnIndex, int rowIndex, String marker) {

    int nextColumnIndex = columnIndex + 1;
    int markerCount = 1;
    boolean markerEqual = true;

    while (nextColumnIndex < tableSize && markerEqual) {
      String nextMarker = gameTable[nextColumnIndex][rowIndex];
      if (nextMarker.equals(marker)) {
        markerCount++;
      } else {
        markerEqual = false;
      }

      nextColumnIndex++;
    }

    return markerCount >= marksToWin;
  }

  /**
   * Check one table field marker to solve the marker to win condition in right up way from the
   * field position
   *
   * @param columnIndex - marker column position
   * @param rowIndex - marker row position
   * @param marker - marker that use a player in tic-tac-toe
   * @return can be win a player in the right up way?
   */
  private boolean checkEndConditionRightUp(int columnIndex, int rowIndex, String marker) {
    int nextColumnIndex = columnIndex + 1;
    int nextRowIndex = rowIndex - 1;
    int markerCount = 1;
    boolean markerEqual = true;

    while (nextColumnIndex < tableSize && nextRowIndex >= 0 && markerEqual) {
      String nextMarker = gameTable[nextColumnIndex][nextRowIndex];
      if (nextMarker.equals(marker)) {
        markerCount++;
      } else {
        markerEqual = false;
      }

      nextColumnIndex++;
      nextRowIndex--;
    }

    return markerCount >= marksToWin;
  }

  /**
   * Check one table field marker to solve the marker to win condition in right down way from the
   * field position
   *
   * @param columnIndex - marker column position
   * @param rowIndex - marker row position
   * @param marker - marker that use a player in tic-tac-toe
   * @return can be win a player in the right down way?
   */
  private boolean checkEndConditionRightDown(int columnIndex, int rowIndex, String marker) {
    int nextColumnIndex = columnIndex + 1;
    int nextRowIndex = rowIndex + 1;
    int markerCount = 1;
    boolean markerEqual = true;

    while (nextColumnIndex < tableSize && nextRowIndex < tableSize && markerEqual) {
      String nextMarker = gameTable[nextColumnIndex][nextRowIndex];
      if (nextMarker.equals(marker)) {
        markerCount++;
      } else {
        markerEqual = false;
      }

      nextColumnIndex++;
      nextRowIndex++;
    }

    return markerCount >= marksToWin;
  }

  /**
   * Check one table field marker to solve the marker to win condition in left way from the field
   * position
   *
   * @param columnIndex - marker column position
   * @param rowIndex - marker row position
   * @param marker - marker that use a player in tic-tac-toe
   * @return can be win a player in the left way?
   */
  private boolean checkEndConditionLeft(int columnIndex, int rowIndex, String marker) {
    int nextColumnIndex = columnIndex - 1;
    int markerCount = 1;
    boolean markerEqual = true;

    while (nextColumnIndex >= 0 && markerEqual) {
      String nextMarker = gameTable[nextColumnIndex][rowIndex];
      if (nextMarker.equals(marker)) {
        markerCount++;
      } else {
        markerEqual = false;
      }

      nextColumnIndex--;
    }

    return markerCount >= marksToWin;
  }

  /**
   * Check one table field marker to solve the marker to win condition in left up way from the field
   * position
   *
   * @param columnIndex - marker column position
   * @param rowIndex - marker row position
   * @param marker - marker that use a player in tic-tac-toe
   * @return can be win a player in the left up way?
   */
  private boolean checkEndConditionLeftUp(int columnIndex, int rowIndex, String marker) {
    int nextColumnIndex = columnIndex - 1;
    int nextRowIndex = rowIndex - 1;
    int markerCount = 1;
    boolean markerEqual = true;

    while (nextColumnIndex >= 0 && nextRowIndex >= 0 && markerEqual) {
      String nextMarker = gameTable[nextColumnIndex][nextRowIndex];
      if (nextMarker.equals(marker)) {
        markerCount++;
      } else {
        markerEqual = false;
      }

      nextColumnIndex--;
      nextRowIndex--;
    }

    return markerCount >= marksToWin;
  }

  /**
   * Check one table field marker to solve the marker to win condition in left down way from the
   * field position
   *
   * @param columnIndex - marker column position
   * @param rowIndex - marker row position
   * @param marker - marker that use a player in tic-tac-toe
   * @return can be win a player in the left down way?
   */
  private boolean checkEndConditionLeftDown(int columnIndex, int rowIndex, String marker) {
    int nextColumnIndex = columnIndex - 1;
    int nextRowIndex = rowIndex + 1;
    int markerCount = 1;
    boolean markerEqual = true;

    while (nextColumnIndex >= 0 && nextRowIndex < tableSize && markerEqual) {
      String nextMarker = gameTable[nextColumnIndex][nextRowIndex];
      if (nextMarker.equals(marker)) {
        markerCount++;
      } else {
        markerEqual = false;
      }

      nextColumnIndex--;
      nextRowIndex++;
    }

    return markerCount >= marksToWin;
  }

  /**
   * Check one table field marker to solve the marker to win condition in up way from the field
   * position
   *
   * @param columnIndex - marker column position
   * @param rowIndex - marker row position
   * @param marker - marker that use a player in tic-tac-toe
   * @return can be win a player in the up way?
   */
  private boolean checkEndConditionUp(int columnIndex, int rowIndex, String marker) {
    int nextRowIndex = rowIndex - 1;
    int markerCount = 1;
    boolean markerEqual = true;

    while (nextRowIndex >= 0 && markerEqual) {
      String nextMarker = gameTable[columnIndex][nextRowIndex];
      if (nextMarker.equals(marker)) {
        markerCount++;
      } else {
        markerEqual = false;
      }

      nextRowIndex--;
    }

    return markerCount >= marksToWin;
  }

  /**
   * Check one table field marker to solve the marker to win condition in down way from the field
   * position
   *
   * @param columnIndex - marker column position
   * @param rowIndex - marker row position
   * @param marker - marker that use a player in tic-tac-toe
   * @return can be win a player in the down way?
   */
  private boolean checkEndConditionDown(int columnIndex, int rowIndex, String marker) {
    int nextRowIndex = rowIndex + 1;
    int markerCount = 1;
    boolean markerEqual = true;

    while (nextRowIndex < tableSize && markerEqual) {
      String nextMarker = gameTable[columnIndex][nextRowIndex];
      if (nextMarker.equals(marker)) {
        markerCount++;
      } else {
        markerEqual = false;
      }

      nextRowIndex++;
    }

    return markerCount >= marksToWin;
  }
}
