package kabasuji.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import kabasuji.controller.moves.SelectPieceMove;
import kabasuji.model.Board;
import kabasuji.model.Bullpen;
import kabasuji.model.Kabasuji;
import kabasuji.model.Piece;
import kabasuji.view.BullpenView;
import kabasuji.view.JLabelIcon;
import kabasuji.view.PieceView;
import kabasuji.view.PlayLevelPanel;
import kabasuji.view.ZoomPanel;
import misc.MusicPlayer;

/**
 * Controller for Selecting Piece in bullpen; Selects piece then displays it in
 * zoom Panel.
 * 
 * When the button is pressed to attempt to select piece, the model will update
 * the selected piece and the gui will reflect the changes
 * 
 * @author jwu
 *
 */
public class SelectPieceBullpenController extends MouseAdapter {

	/* Top Level Model */
	Kabasuji kabasuji;
	/* Bullpen */
	Bullpen bullpen;
	/* Board */
	Board board;
	/* List of pieces in bullpen */
	ArrayList<Piece> pieces;
	/* Selected piece */
	Piece selectedPiece;

	/* piece number */
	int numPiece;

	/* Top Level Boundary */
	PlayLevelPanel panel;
	/* Bullpenview */
	BullpenView bullpenview;
	/* ZoomPanel */
	ZoomPanel zoompanel;
	/* Piece view object in bullpen */
	JLabelIcon pieceicon;
	/* Piece view array */
	PieceView[] pieceviews;
	/* filename of zoompanel */
	String fnzoom;
	/* original filename of pieceview */
	String fnpiece;

	/**
	 * Constructor for SelectPieceBullpenController
	 * 
	 * @param kabasuji
	 * @param panel
	 * @param pieceicon
	 *            the piece view object on the boardview
	 * @param numPiece
	 *            the number of the relative piece
	 */
	public SelectPieceBullpenController(Kabasuji kabasuji, PlayLevelPanel panel, JLabelIcon pieceicon, int numPiece) {
		this.kabasuji = kabasuji;
		this.bullpen = kabasuji.getSelectedLevel().getBullpen();
		this.board = kabasuji.getSelectedLevel().getBoard();
		this.panel = panel;
		this.bullpenview = panel.getBullpenView();
		this.pieceviews = bullpenview.getPieceView();
		this.pieceicon = pieceicon;
		this.zoompanel = panel.getZoomPanel();
		this.fnzoom = zoompanel.getFileName();
		this.fnpiece = pieceicon.getFileName();
		this.numPiece = numPiece;
		this.selectedPiece = kabasuji.getSelectedLevel().getBullpen().getPieces().get(numPiece);
	}

	/**
	 * Whenever mouse is pressed (left button), attempt to select object. This
	 * is a GUI controller.
	 */
	public void mousePressed(MouseEvent me) {
		// selecting piece toggle
		if (board.getSelectedPiece() == null) {
			if (bullpen.getSelectedPiece() == null) {
				SelectPieceMove spm = new SelectPieceMove(selectedPiece);
				spm.execute(kabasuji);
				pieceicon.setImg("generalhoverbutton.png");
				new MusicPlayer("selectpiecebullpen.wav").setVolume(-20);
			} else if (bullpen.getSelectedPiece() == selectedPiece) {
				SelectPieceMove spm = new SelectPieceMove(null);
				spm.execute(kabasuji);
				pieceicon.setImg(fnpiece);
				new MusicPlayer("selectpiecebullpen.wav").setVolume(-20);
			}
		}
	}
	/**
	 * Mouse Enter displays the piece view object on the zoompanel.
	 */
	public void mouseEntered(MouseEvent e) {
		// displays enlarged piece on zoom panel upon entering
		if (bullpen.getSelectedPiece() == null && board.getSelectedPiece() == null) {
			// update zoompanel to show selectedPiece
			zoompanel.displayPieceView(selectedPiece);
		}

	}
	/**
	 * Mouse Exit removes the pieceview object from the zoompanel.
	 */
	public void mouseExited(MouseEvent e) {
		// sets the panel back to empty if no piece is selected before leaving
		// image
		if (bullpen.getSelectedPiece() == null && board.getSelectedPiece() == null) {
			zoompanel.removeAll();
			zoompanel.repaint();
		}
	}
}
