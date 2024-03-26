package hu.dbx.homework;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static hu.dbx.homework.CmdOptions.ProgramArgumentsEnum.FILENAME;
import static hu.dbx.homework.CmdOptions.ProgramArgumentsEnum.MARKER_TO_WIN;
import static hu.dbx.homework.TicTacEndGameAnalyser.DEFAULT_MARKER_TO_WIN;

public class TicTacToeEndGameAnalyserApplicationMain {

  private static Logger logger =
      LoggerFactory.getLogger(TicTacToeEndGameAnalyserApplicationMain.class);

  /**
   * Simple Tic-Tac-Toe game status analyzer application main method that parse the game data from
   * file or stdInput and set the marker to win count to the {@link TicTacEndGameAnalyser} util
   * class that can calculate the result.
   *
   * <p>It is log the error states to the standard error and log the valid states to the standard
   * input.
   */
  public static void main(String[] args) {

    CommandLine cmd = getCommandLine(CmdOptions.getCmdOptionsField(), args);
    if (cmd != null) {
      List<String> programArgumentErrorMessages = new ArrayList<>();

      int markerToWin = readMarkerToWin(cmd, programArgumentErrorMessages);
      String data = readTableMarkerInputData(cmd, programArgumentErrorMessages);

      if (programArgumentErrorMessages.isEmpty()) {
        TicTacEndGameAnalyser analyser = new TicTacEndGameAnalyser(data, markerToWin);
        AnalyserResult result = analyser.calculateEndGameResult();
        if (!result.getStatus().isErrorState()) {
          logger.info(result.getResultMessage());
          logger.info(data);
        } else {
          logger.error(result.getResultMessage());
        }
      } else {
        for (String error : programArgumentErrorMessages) {
          logger.error(error);
        }
      }
    }
  }

  /**
   * Parse command line options
   *
   * @param options - command line option
   * @param args - main program arguments
   * @return parsed
   */
  private static CommandLine getCommandLine(Options options, String[] args) {
    CommandLineParser parser = new DefaultParser();
    CommandLine cmd = null;
    try {
      cmd = parser.parse(options, args);
    } catch (ParseException e) {
      logger.error("Error: {}", e.getMessage());
    }

    return cmd;
  }

  /**
   * Read and parse the marker counts from the program arguments
   *
   * @param cmd - The command line should contains the marker count that decide marker count to won
   *     the game
   * @param programArgumentErrorMessages
   * @return marker to win counts
   */
  private static int readMarkerToWin(CommandLine cmd, List<String> programArgumentErrorMessages) {
    int markerToWin = DEFAULT_MARKER_TO_WIN;
    if (cmd.hasOption(MARKER_TO_WIN.getLongName())) {
      String mcString = cmd.getOptionValue(MARKER_TO_WIN.getLongName());
      try {
        int markerParam = Integer.parseInt(mcString);
        if (markerParam >= DEFAULT_MARKER_TO_WIN) {
          markerToWin = markerParam;
        } else {
          programArgumentErrorMessages.add(
              MARKER_TO_WIN.getLongName() + " option value is less than " + DEFAULT_MARKER_TO_WIN);
        }
      } catch (NumberFormatException nex) {
        programArgumentErrorMessages.add(
            MARKER_TO_WIN.getLongName()
                + " option value is not a number. Set value is: "
                + mcString);
      }
    }

    return markerToWin;
  }

  /**
   * Read the game table data from a regular file using command line option. If there is not file
   * argument set the method read it from the standard input.
   *
   * @param cmd - It should countains the file path argument value
   * @param programArgumentErrorMessages - collect the file or standard input error messages
   * @return rawData is a read data from file or standard input
   */
  private static String readTableMarkerInputData(
      CommandLine cmd, List<String> programArgumentErrorMessages) {
    String rawData = null;
    if (cmd.hasOption(FILENAME.getLongName())) {
      String mcString = cmd.getOptionValue(FILENAME.getLongName());
      Path path = Paths.get(mcString);
      if (Files.exists(path) && Files.isReadable(path)) {
        try (Stream<String> lines = Files.lines(path)) {
          rawData = lines.collect(Collectors.joining());
        } catch (IOException ioEx) {
          programArgumentErrorMessages.add("Cannot get input data from file :" + mcString + "\n");
        }
      } else {
        programArgumentErrorMessages.add(
            "Data file is not exists or readable on path: " + mcString);
      }
    } else if (programArgumentErrorMessages.isEmpty()) {
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
        rawData = reader.lines().collect(Collectors.joining());
      } catch (IOException ex) {
        programArgumentErrorMessages.add("Cannot get input data from system in.");
      }
    }
    return rawData;
  }
}
