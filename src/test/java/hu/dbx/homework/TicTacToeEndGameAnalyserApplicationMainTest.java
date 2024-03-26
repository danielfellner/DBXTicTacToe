package hu.dbx.homework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;

@ExtendWith(MockitoExtension.class)
public class TicTacToeEndGameAnalyserApplicationMainTest {
  @Test
  void testNoArguments() {
    ByteArrayInputStream testIn = new ByteArrayInputStream("X0X0X0X0X".getBytes());
    System.setIn(testIn);
    String[] args = null;
    TicTacToeEndGameAnalyserApplicationMain.main(args);
  }

  @Test
  void testMarkerToWinArgShortNameOnly() {
    ByteArrayInputStream testIn = new ByteArrayInputStream("X0X0X0X0X".getBytes());
    System.setIn(testIn);
    String[] args = {"-m", "2"};
    TicTacToeEndGameAnalyserApplicationMain.main(args);
  }

  @Test
  void testMarkerToWinArgLongNameOnly() {
    ByteArrayInputStream testIn = new ByteArrayInputStream("X0X0X0X0X".getBytes());
    System.setIn(testIn);
    String[] args = {"-marker", "2"};
    TicTacToeEndGameAnalyserApplicationMain.main(args);
  }

  @Test
  void testFileLongNameArg() {
    ByteArrayInputStream testIn = new ByteArrayInputStream("".getBytes());
    System.setIn(testIn);
    String[] args = {"-filename", "./target/test-classes/runtime_testfile.txt"};
    TicTacToeEndGameAnalyserApplicationMain.main(args);
  }

  @Test
  void testFileShortNameArg() {
    ByteArrayInputStream testIn = new ByteArrayInputStream("".getBytes());
    System.setIn(testIn);
    String[] args = {"-f", "./target/test-classes/runtime_testfile.txt"};
    TicTacToeEndGameAnalyserApplicationMain.main(args);
  }

  @Test
  void testFileNotExistsArg() {
    ByteArrayInputStream testIn = new ByteArrayInputStream("".getBytes());
    System.setIn(testIn);
    String[] args = {"-f", "./target/test-classes/testfileNotExists.txt"};
    TicTacToeEndGameAnalyserApplicationMain.main(args);
  }
}
