package levelbuilder.view;

import levelbuilder.controller.BuilderReleaseBoardController;
import kabasuji.model.Board;
import kabasuji.model.Builder;
import kabasuji.model.Tile;
import kabasuji.view.JLabelIcon;


@SuppressWarnings("serial")
public class BuilderReleaseBoardView extends BuilderBoardView {

	/** The board.*/
	Board board;
	/** High level builder class*/
	Builder builder;
	/** The application.*/
	TopLevelApplicationBuilder app;
	/** JLabel that contains it.*/
	JLabelIcon panel;
	
	/** The tiles in the board */
	Tile[][] tiles;
	/** Number of rows */
	int row;
	/** Number of columns */
	int col;
	
	/** Labels for the tiles */
	JLabelIcon[] tile;

	/** Square matrix length */
	int sqmatrixlength;
	/** Side side length */
	int tilesidelength;
	/** Tile side length, scaled. */
	int tilesidescaled;

	/** Scaling offset */
	int offset;

	/** clickMode 0 does hint/invalidate,  clickMode 1 places release tiles. */
	int clickMode = 0; 
	/** Which number to add */
	int numToAdd = 1;
	/** Which color to add */
	int colorToAdd = 1;

	/**
	 * Constructs a release board view.
	 * @param board The board for the viewer to display
	 * @param panel The display panel to display
	 * @param builder The builder to retreive info from
	 * @param app the high level application
	 */
	public BuilderReleaseBoardView(Board board, JLabelIcon panel, Builder builder, TopLevelApplicationBuilder app) {
		super(board, panel, builder, app, 2);
		System.out.println("Initializing the builder release board view.");
		this.board = board;
		this.panel = panel;
		this.builder = builder;
		this.app = app;
		this.tiles = board.getTiles();

		this.row = board.getTiles().length+2;
		this.col = board.getTiles()[0].length+2;
		this.tile = new JLabelIcon[row * col];

		// this is the largest length of the tile matrix
		// finds the smallest tile length
		
		if (row > col) {
			sqmatrixlength = row;
			this.tilesidelength = (int) (panel.getSize().getWidth() / sqmatrixlength);
		} else {
			sqmatrixlength = col;
			this.tilesidelength = (int) (panel.getSize().getHeight() / sqmatrixlength);
		}

		// scaling + offset to fit the container panel;
		tilesidescaled = (int) (tilesidelength * 0.99);
		offset = (int) ((tilesidelength - tilesidescaled) / 2);
		
		// sets the bounds of the boardview within the panel container
		setBounds(0, 0, (int) panel.getSize().getWidth(), (int) panel.getSize().getHeight());
		updateBoard();
	}
	
	
	/**
	 * display the tiles on the container panel and scales them to fit
	 * row/col
	 * includes centering as well
	 */
	public void updateBoard(){
		try{
			panel.removeAll();
			for (int i = 0; i < col-2; i++) {
				for (int j = 0; j < row-2; j++) {
					System.out.print("Tile found: ");
					if(tiles[i][j].getNumber() == 0)//If this is not a release tile.
					{
						if (tiles[i][j].isHint()) {					
							displayTile(i,j,"tile.png");
							JLabelIcon hint = new JLabelIcon("hint.png", tile[i * row + j].getWidth(), tile[i * row + j].getWidth());
							tile[i * row + j].add(hint);
						}
						else if(!tiles[i][j].isValid())
						{
							displayTile(i,j,"general1button.png");
							System.out.println("Painting  it invalid.");
						}
						else //if(tiles[j][i].isValid())
						{
							displayTile(i,j,"tile.PNG");						
							System.out.println("Painting  it normal.");
						}
						tile[i*row+j].addMouseListener(new BuilderReleaseBoardController(board, tile[i*row+j], tiles[i][j], builder, app, clickMode, numToAdd, colorToAdd, this));
					}
					else//A release tile
					{
						System.out.println("Found a release tile to print.");
						String tileToDisplay = "releasetile";
						if (!tiles[i][j].isHint()) {
							System.out.println("Making a non-hint release tile.");
							if(tiles[i][j].getColor() == 1)
							{
								tileToDisplay = tileToDisplay + "1_";
							}
							else if(tiles[i][j].getColor()  == 2)
							{
								tileToDisplay = tileToDisplay + "2_";
							}
							else if(tiles[i][j].getColor() == 3)
							{
								tileToDisplay = tileToDisplay + "3_";
							}
							
							tileToDisplay = tileToDisplay + Integer.toString(tiles[i][j].getNumber()) + ".PNG";
							displayTile(i,j,tileToDisplay);
							tile[i*row+j].setFileName(tileToDisplay);
						}
						else if(!tiles[i][j].isValid())
						{
							displayTile(i,j,"general1button.png");
						}
						else //It is valid and also a hint.
						{						
							System.out.println("Making a hint release tile.");
	
							if(tiles[i][j].getColor() == 1)
							{
								tileToDisplay = tileToDisplay + "1_";
							}
							else if(tiles[i][j].getColor()  == 2)
							{
								tileToDisplay = tileToDisplay + "2_";
							}
							else if(tiles[i][j].getColor() == 3)
							{
								tileToDisplay = tileToDisplay + "3_";
							}
							tileToDisplay = tileToDisplay + Integer.toString(tiles[i][j].getNumber()) + "_h.PNG";
							System.out.println("File:" + tileToDisplay);
							displayTile(i,j,tileToDisplay);
							tile[i*row+j].setFileName(tileToDisplay);
							
						}
						tile[i*row+j].addMouseListener(new BuilderReleaseBoardController(board, tile[i*row+j], tiles[i][j], builder, app, clickMode, numToAdd, colorToAdd, this));
					}
				}
			}
			panel.repaint();
		}
		catch(NullPointerException e)
		{
			System.out.println("Null pointer caught, update function called before constructor.");
		}
	}
	
	
	/**
	 * Displays a tile.
	 * @param i i location of tile
	 * @param j j location of tile
	 * @param pic The icon to be displayed. (file name.)
	 */
	public void displayTile(int i,int j, String pic){
		tile[i * row + j] = new JLabelIcon(pic, tilesidescaled, tilesidescaled);
		tile[i * row + j].setLocation((int) (tilesidelength * (j+1 + (sqmatrixlength - row) / 2)) + offset,
				(int) (tilesidelength * (i+1 + (sqmatrixlength - col) / 2)) + offset);
		tile[i*row+j].setImg(pic);
		panel.add(tile[i * row + j]);
	}

	/**
	 * Sets edit mode to the given number.
	 * @param number Number to set edit mode to.
	 */
	public void setEditMode(int number) {
		clickMode = number;
	}

	 /**
	  * Returns the edit mode of the current release level.
	  * @return
	  */
	public int getEditMode() {
		return clickMode;
	}

	/**
	 * Returns the number value of the currently selected color. 
	 * @return
	 */
	public int getColor() {
		return colorToAdd;
	}

	/**
	 * Returns the number value selected to add release values.
	 * @return
	 */
	public int getNumber() {
		return numToAdd;
	}

	/**
	 * Sets number for editing
	 * @param number Number to add
	 */
	public void setNumber(int number) {
		numToAdd = number;
	}
	public void setColor(int color) {
		colorToAdd = color;
	}
	
	/**
	 * Sets the tiles in the viewer.
	 * @param t Tiles to set.
	 */
	public void setTiles(Tile[][] t)
	{
		tiles = t;
	}
	
	/**
	 * Returns the label icons for the tiles in the board.
	 * @return JLabelIcon array for tiles.
	 */
	public JLabelIcon[] getTiles() {
		return tile;
	}
	
}