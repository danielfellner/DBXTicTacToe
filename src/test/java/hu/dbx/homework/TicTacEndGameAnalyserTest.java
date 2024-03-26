package hu.dbx.homework;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TicTacEndGameAnalyserTest {

    @Test
    void testWon0On3x3() {
        String rawData = "0X0X0X0X0";
        int markersToWin = 3;
        TicTacEndGameAnalyser analyser = new TicTacEndGameAnalyser(rawData, markersToWin);
        AnalyserResult result = analyser.calculateEndGameResult();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(AnalyserResult.GameStatusEnum.DECIDED_ENDGAME, result.getStatus());
        Assertions.assertTrue(result.isPlayer2Won());
        Assertions.assertFalse(result.isPlayer1Won());
        Assertions.assertFalse(result.isDraw());
    }

    @Test
    void testWon0On4x4() {
        String rawData = "0X0 X0X 0X0     ";
        int markersToWin = 3;
        TicTacEndGameAnalyser analyser = new TicTacEndGameAnalyser(rawData, markersToWin);
        AnalyserResult result = analyser.calculateEndGameResult();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(AnalyserResult.GameStatusEnum.DECIDED_ENDGAME, result.getStatus());
        Assertions.assertTrue(result.isPlayer2Won());
        Assertions.assertFalse(result.isPlayer1Won());
        Assertions.assertFalse(result.isDraw());
    }

    @Test
    void testWonXOn3x3() {
        String rawData = "X0X0X0X0X";
        int markersToWin = 3;
        TicTacEndGameAnalyser analyser = new TicTacEndGameAnalyser(rawData, markersToWin);
        AnalyserResult result = analyser.calculateEndGameResult();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(AnalyserResult.GameStatusEnum.DECIDED_ENDGAME, result.getStatus());
        Assertions.assertTrue(result.isPlayer1Won());
        Assertions.assertFalse(result.isPlayer2Won());
        Assertions.assertFalse(result.isDraw());
    }

    @Test
    void testWonXOn4x4() {
        String rawData = "X0X 0X0 X0X     ";
        int markersToWin = 3;
        TicTacEndGameAnalyser analyser = new TicTacEndGameAnalyser(rawData, markersToWin);
        AnalyserResult result = analyser.calculateEndGameResult();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(AnalyserResult.GameStatusEnum.DECIDED_ENDGAME, result.getStatus());
        Assertions.assertTrue(result.isPlayer1Won());
        Assertions.assertFalse(result.isPlayer2Won());
        Assertions.assertFalse(result.isDraw());
    }

    @Test
    void testDrawOn3x3() {
        String rawData = "0X00XXX0X";
        int markersToWin = 3;
        TicTacEndGameAnalyser analyser = new TicTacEndGameAnalyser(rawData, markersToWin);
        AnalyserResult result = analyser.calculateEndGameResult();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(AnalyserResult.GameStatusEnum.DECIDED_ENDGAME, result.getStatus());
        Assertions.assertFalse(result.isPlayer1Won());
        Assertions.assertFalse(result.isPlayer2Won());
        Assertions.assertTrue(result.isDraw());
    }

    @Test
    void testDrawOn4x4() {
        String rawData = "0X0 0XX X0X     ";
        int markersToWin = 3;
        TicTacEndGameAnalyser analyser = new TicTacEndGameAnalyser(rawData, markersToWin);
        AnalyserResult result = analyser.calculateEndGameResult();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(AnalyserResult.GameStatusEnum.DECIDED_ENDGAME, result.getStatus());
        Assertions.assertFalse(result.isPlayer1Won());
        Assertions.assertFalse(result.isPlayer2Won());
        Assertions.assertTrue(result.isDraw());
    }

    @Test
    void testToBigMarkersToWin() {
        String rawData = "0X00XXX0X";
        int markersToWin = 6;
        TicTacEndGameAnalyser analyser = new TicTacEndGameAnalyser(rawData, markersToWin);
        AnalyserResult result = analyser.calculateEndGameResult();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(AnalyserResult.GameStatusEnum.CANNOT_DECIDED_DATA_PARAMETER_ERROR, result.getStatus());
        Assertions.assertFalse(result.isPlayer1Won());
        Assertions.assertFalse(result.isPlayer2Won());
        Assertions.assertFalse(result.isDraw());
    }

    @Test
    void testToLowMarkersToWin() {
        String rawData = "0X00XXX0X";
        int markersToWin = 2;
        TicTacEndGameAnalyser analyser = new TicTacEndGameAnalyser(rawData, markersToWin);
        AnalyserResult result = analyser.calculateEndGameResult();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(AnalyserResult.GameStatusEnum.CANNOT_DECIDED_DATA_PARAMETER_ERROR, result.getStatus());
        Assertions.assertFalse(result.isPlayer1Won());
        Assertions.assertFalse(result.isPlayer2Won());
        Assertions.assertFalse(result.isDraw());
    }

    @Test
    void testTwoPlayerWonInTheData() {
        String rawData = "000XXX0X0";
        int markersToWin = 3;
        TicTacEndGameAnalyser analyser = new TicTacEndGameAnalyser(rawData, markersToWin);
        AnalyserResult result = analyser.calculateEndGameResult();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(AnalyserResult.GameStatusEnum.CANNOT_DECIDED_GAME_PLAY_ERROR, result.getStatus());
        Assertions.assertFalse(result.isPlayer1Won());
        Assertions.assertFalse(result.isPlayer2Won());
        Assertions.assertFalse(result.isDraw());
    }

    @Test
    void testInvalidCharacterInData() {
        String rawData = "0X0AXXX0X";
        int markersToWin = 3;
        TicTacEndGameAnalyser analyser = new TicTacEndGameAnalyser(rawData, markersToWin);
        AnalyserResult result = analyser.calculateEndGameResult();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(AnalyserResult.GameStatusEnum.CANNOT_DECIDED_DATA_PARAMETER_ERROR, result.getStatus());
        Assertions.assertFalse(result.isPlayer1Won());
        Assertions.assertFalse(result.isPlayer2Won());
        Assertions.assertFalse(result.isDraw());
    }

    @Test
    void testNullTableData() {
        String rawData = null;
        int markersToWin = 3;
        TicTacEndGameAnalyser analyser = new TicTacEndGameAnalyser(rawData, markersToWin);
        AnalyserResult result = analyser.calculateEndGameResult();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(AnalyserResult.GameStatusEnum.CANNOT_DECIDED_DATA_PARAMETER_ERROR, result.getStatus());
        Assertions.assertFalse(result.isPlayer1Won());
        Assertions.assertFalse(result.isPlayer2Won());
        Assertions.assertFalse(result.isDraw());
    }

    @Test
    void testToSmallTableData() {
        String rawData = "0X";
        int markersToWin = 3;
        TicTacEndGameAnalyser analyser = new TicTacEndGameAnalyser(rawData, markersToWin);
        AnalyserResult result = analyser.calculateEndGameResult();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(AnalyserResult.GameStatusEnum.CANNOT_DECIDED_DATA_PARAMETER_ERROR, result.getStatus());
        Assertions.assertFalse(result.isPlayer1Won());
        Assertions.assertFalse(result.isPlayer2Won());
        Assertions.assertFalse(result.isDraw());
    }

}
