package kabasuji.modelTests;

import kabasuji.model.*;
import junit.framework.TestCase;

public class LevelTest extends TestCase {

	protected void setUp() {
	}

	public void testLevels() {

		// make a new board
		Tile testTile1 = new Tile(false, true, 0, 0);
		Tile testTile2 = new Tile(false, true, 0, 0);
		Tile testTile3 = new Tile(false, false, 0, 0);
		Tile testTile4 = new Tile(false, false, 0, 0);
		Tile testTile5 = new Tile(false, false, 0, 0);
		Tile testTile6 = new Tile(false, false, 0, 0);
		Tile testTile7 = new Tile(false, true, 0, 0);
		Tile testTile8 = new Tile(false, true, 0, 0);

		Tile boardTile0_0 = new Tile(false, false, 0, 0);
		Tile boardTile0_1 = new Tile(false, false, 0, 0);
		Tile boardTile0_2 = new Tile(false, false, 0, 0);
		Tile boardTile0_3 = new Tile(false, false, 0, 0);
		Tile boardTile1_0 = new Tile(false, false, 0, 0);
		Tile boardTile1_1 = new Tile(false, false, 0, 0);
		Tile boardTile1_2 = new Tile(false, false, 0, 0);
		Tile boardTile1_3 = new Tile(false, false, 0, 0);
		Tile boardTile2_0 = new Tile(false, false, 0, 0);
		Tile boardTile2_1 = new Tile(false, false, 0, 0);
		Tile boardTile2_2 = new Tile(false, false, 0, 0);
		Tile boardTile2_3 = new Tile(false, false, 0, 0);
		Tile boardTile3_0 = new Tile(false, false, 0, 0);
		Tile boardTile3_1 = new Tile(false, false, 0, 0);
		Tile boardTile3_2 = new Tile(false, false, 0, 0);
		Tile boardTile3_3 = new Tile(false, false, 0, 0);

		Tile[][] piece1 = { { testTile1, testTile2 }, { testTile3, testTile4 } };
		Tile[][] piece2 = { { testTile5, testTile6 }, { testTile7, testTile8 } };
		Tile[][] board1 = { { boardTile0_0, boardTile0_1, boardTile0_2, boardTile0_3 },
				{ boardTile1_0, boardTile1_1, boardTile1_2, boardTile1_3 },
				{ boardTile2_0, boardTile2_1, boardTile2_2, boardTile2_3 },
				{ boardTile3_0, boardTile3_1, boardTile3_2, boardTile3_3 } };
		Piece testPiece2 = new Piece(piece2);

		PuzzleBoard puzzBoard1 = new PuzzleBoard(board1);

		// make a new bullpen
		Bullpen bullpen1 = new Bullpen();
		Tile[][] piece3 = { { testTile1, testTile2 }, { testTile2, testTile1 } };
		Piece testPiece1 = new Piece(piece1);
		bullpen1.addPiece(testPiece1);

		// make a new Puzzle Level Test
		PuzzleLevel puzzTest = new PuzzleLevel(puzzBoard1, bullpen1, 10);
		
		assertEquals(puzzTest.getStars(),0);
		
		assertEquals(puzzTest.isLocked(), false);
		
		assertEquals(puzzTest.canGoNextLevel(), false);
		
		puzzTest.setLocked(true);
		
		puzzTest.getStars();
		assertEquals(puzzTest.canGoNextLevel(), true);
		
		
		
	}
}
