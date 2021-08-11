import java.util.Random;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public  class Galaxy extends Application {
	
	// X and Y values
	
	public static int xCoord = 9;												// Max x coordinates
	public static int yCoord = 7;												// Max y coordinates	
	public static long seed ;													// Seed
	
	
	public class Model{

		
		// Grid
		
		private boolean [][] shipGrid ;
		private boolean [][] flagGrid;
		
		
		// ship
												
		private int shipFound = 0;												// how many ships are Found
		private int shipProximity = 0 ;											// ship on sight
		
		
		// Turn count
		
		private int turns = 0;													// current turns
		private int endTurns = 13;												// End
		

		// Constructor. Initializes Object
	
		public Model() {
			
			shipGrid = new boolean[xCoord][yCoord];									// grid width (xCoord) and height (yCoord)
			flagGrid = new boolean [xCoord][yCoord];
			setSeed();
			setGrid();															// Initiates a grid when Object is made
			
		}

		
		//setting up the grid
		
		public void setGrid() {
			
			turns = 0; 															// Resets turns = 0  when Initializing a new grid
			endTurns = 13;
			shipFound = 0;														// Resets found ships = 0  when Initializing a new grid
			
			for (int column = 0 ; column < yCoord ; column ++) {	
				
				for (int row = 0 ; row < xCoord ; row ++) {
					
					shipGrid [row][column] = false;
					flagGrid [row][column] = false;
					
				}
			
			}
			
			setShips();															// Setting ships after a false grid (setting ship tiles true)
			
		}
		 
		
		// Ship in tile
		
		public void setShips() {
					
			Random seeding = new Random(seed);									// Random with seed Parameter 
			int nrLoops = 4;													// Number of ships
							
			while (nrLoops > 0 ) {
						
				int xtemp = seeding.nextInt(xCoord);
				int ytemp = seeding.nextInt(yCoord);
		
				if(shipGrid [xtemp][ytemp] != true){								// Only sets ship if there is no ship 
								
					shipGrid [xtemp][ytemp] = true;
					nrLoops--;													// Only counts down if ship is set
					
				}
								
		}
						
			
		}
				
				
		// returns Tile value
		
		public boolean getTile(int row , int column){
			
			return shipGrid[row][column];
			
		}
		

		// Get ships in sight
		
		public int getShipProximity(int x, int y) {
			
				shipProximity = 0; 											// counter reset 
				int temp  ;													// temporary counter

				for (int row = x ; row < xCoord ; row ++) {					// 0°
					
					if (getTile(row,y)) {									// y does not change
						shipProximity ++;
						break;
					}
				}
				
				for (int column = y ; column >= 0 ; column --) {			// 90°
					
					if (getTile(x,column))	{								// x does not change
												
						shipProximity ++;
						break;
					}
				}
				
				for (int row = x ; row >= 0  ; row --) {					// 180°   >= Reaching for index 0
					
					if (getTile(row,y)) {									// y does not change
						shipProximity ++;
						break;
					}
				}
				
				
				for (int column = y ; column < yCoord ; column ++) {		// 270°
					
					if (getTile(x,column)) {								// x does not change
						shipProximity ++;
						break;
					}
				}
				
				
				temp = x;													// Initialize temp new after loop
				
				for (int column = y ; column >= 0   ; column --)   {		// 45°  
					
				
					
					if (getTile(temp,column) && temp < xCoord) {
						shipProximity ++;
						break;
					}
					
					
					if (temp < xCoord -1){
						temp++;												// add x coordinates
					}
					else {
						break;
					}
					
					
																			
				}
				
				
				temp = x;
				
				for (int column = y ; column >= 0 ; column --)  {			// check 135°
					
					if (getTile(temp,column) ) {							// iterate over x , y ( same values)
						shipProximity ++;
						break;
					}
					
					if (temp > 0){
						
						temp--;												// add x coordinates
						
					}
					else {
						break;
					}
					
				}
				
				temp = x;
				
				for (int column = y ; column < yCoord ; column ++)  {		// check 225°
					
					if (getTile(temp,column) ) {							// Can't run over 0
						shipProximity ++;
						break;
					}
					
					if (temp > 0){
						
						temp--;												// add x coordinates
						
					}
					else {
						break;
					}
					
																			
				}
				
				temp = x;
				
				for (int column = y ; column < yCoord ; column ++)  {		// check 315°
					
					
					if (getTile(temp,column)) {
						shipProximity ++;
						break;
					}
					
					if (temp < xCoord -1){
						
						temp++;												// add x coordinates
						
					}
					else {
						break;
					}
				
				}
				

			return shipProximity;

		}
		
		
		// set Seed with a long input
		
		public void setSeed(long seed) {
			
			Galaxy.seed = seed;
			
		}
		
		
		// set random Seed
		
		public void setSeed() {												// setSeed Overload ( no argument )
			
			Random random = new Random ();
			
			Galaxy.seed = Long.valueOf(random.nextInt());
			
		}
		
	}
	
	
	public class View{
		
		
		// Display
		
		private Stage stage;
		private Model model;
		private Button [][] button ;
		
		BorderPane borderPane = new BorderPane();							// BorderPane
		GridPane gridPane = new GridPane ();								// Button Grid
		GridPane xGrid = new GridPane();									// x Display
		GridPane yGrid = new GridPane();									// y Display
		GridPane bottomGrid = new GridPane();								// Bottom Display
		Label messageLabel = new Label();									// Winning Update
		Button seedButton = new Button("Seed:");							// Seed Label
		TextField seeding = new TextField();								// Text field
		
		
		// View Constructor
		
		public View (Model model, Stage stage) {							
			
			this.stage = stage;
			this.model = model;
			
			button = new Button [xCoord][yCoord];
			
			setInterface();													// Interface Initialization
			
			
			// Alignment
			
			 borderPane.setCenter(gridPane);								// Grid in the middle	 
			 gridPane.setAlignment(Pos.CENTER);
			 
	
			 borderPane.setTop(xGrid);										// x Axis Labeling 
			 xGrid.setAlignment(Pos.TOP_RIGHT);
			 
			 
			 borderPane.setLeft(yGrid);										// y Axis Labeling 
			 yGrid.setAlignment(Pos.CENTER_RIGHT);
			 
			 
			 seedButton.setPrefSize(60,25);									// Seeding Label
			 bottomGrid.add(seedButton, 0, 0);
			 bottomGrid.setAlignment(Pos.CENTER);
			 
			 
			 seeding.setPrefSize(90,25);									// Text field
			 bottomGrid.add(seeding, 1, 0);
			 bottomGrid.setAlignment(Pos.CENTER);
			 
			
			 messageLabel.setPrefSize(90, 25);								// Message Update
			 bottomGrid.add(messageLabel, 2 , 0);
			 borderPane.setBottom(bottomGrid);
			 bottomGrid.setAlignment(Pos.CENTER);
			 
			 
			 Scene scene = new Scene(borderPane, 300, 270);													// Scene Initialization (x,y)
			 scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm()); 				// Removing focus color
			 getStage().setScene(scene);																	// Display view
			
		}
			
		
		// Setting up a new Interface
		
		public void setInterface() {
			
			gridPane.getChildren().retainAll();								// Clears Button Grid+
			seeding.setPromptText(String.valueOf(Galaxy.seed) );			// Prompt for current seed
		
			
			for (int column = 0 ; column < yCoord ; column ++) {			// Grid of Buttons	
				
				for (int row = 0 ; row < xCoord ; row ++) {
					
					Button buttons = new Button ();
					buttons.setPrefSize(30, 30);
					button [row][column] = buttons;
					gridPane.add(buttons, row , column);

				}
				
			}
			

			for (int row = 1 ; row <= xCoord ; row ++) {					// x Axis labeling
				Label messageLabel = new Label();
				messageLabel.setPrefSize(30, 30);
				xGrid.add(messageLabel, row , 0);
				messageLabel.setText( "   " + (char) (row + 64));			// ASCII value A = 65
			
			}
			
			
			for (int column = 1 ; column <= yCoord ; column ++) {			// y Axis labeling
				Label messageLabel = new Label();
				messageLabel.setPrefSize(30, 30);
				yGrid.add(messageLabel, 0 , column -1 );
				messageLabel.setText( "   "+String.valueOf(column));
			}
		}
			
		
		// Returns stage
		
		public Stage getStage() {	
			
		    return stage;
		    
		 }
		
		
		// Returns Button
		
		public Button getButton(int x , int y) {	
			
		    return button[x][y];
		    
		}
				
		
		//Reveal tile information
		
		public void revealTile(int x , int y) {							
			
			if (model.getTile(x, y) == true ) {
				
				button[x][y].setText("🚀");
				model.turns ++;
				model.endTurns++;
				model.shipFound ++;											// ship count + 1 when discovered & no turn count +1 because of extra turn
			
			}
			
			else if (model.getTile(x, y) == false) {
				
				button[x][y].setText(String.valueOf(model.getShipProximity(x, y)));
				
				if (String.valueOf(model.getShipProximity(x, y)).equals("0")) {
					
					drawFor0(x,y);
					
				}
				
				
				model.turns++;												// turn count +1
				
				
			}
			
		}
		
		
		// Winning condition
		
		public void update() {												
			
			if ( model.shipFound == 4) {					// Can't win if you lost
				
				messageLabel.setText( " " +model.turns + "/" + model.endTurns + " You Win!");										
				
				disableGrid();
				
			}
			
			else if ( model.turns >= model.endTurns) {		// Can't lose if you won
				
				messageLabel.setText( " "+ model.turns + "/" + model.endTurns + " You Lose!");
				
				disableGrid();
				

			}
			 
			
		}
		
		
		// Return message Label
		
		public Label getMessageLabel() {									
			
			return messageLabel;
			
		 }
		
		
		// Disables grid (stops game)
		
		public void disableGrid() {											
		
			for (int column = 0 ; column < yCoord ; column ++) {			
				
				for (int row = 0 ; row < xCoord ; row ++) {
					
					button [row][column].setDisable(true);
					
				}
			}
			
		}
		
		
		// Getting Seed from text field
		
		public String getSeed() {											
			
			System.out.println(seeding.getText() );
			return seeding.getText() ;
			
		}
		
		
		// draws if shipProximity is 0
		
		public void drawFor0(int x, int y) {
			
			int temp  ;														// temporary counter

			for (int row = x + 1 ; row < xCoord ; row ++) {					// 0°
				
				if(!button[row][y].isDisabled()) {							// If button already revealed don't write over it
				button[row][y].setText("🏴");
				model.flagGrid[row][y] = true;
				}
			}
			
			for (int column = y - 1 ; column >= 0 ; column --) {			// 90°
				
				if(!button[x][column].isDisabled()) {
				button[x][column].setText("🏴");
				model.flagGrid[x][column] = true;
				
				}
			}
			
			for (int row = x - 1; row >= 0  ; row --) {						// 180°   >= Reaching for index 0
				
				if(!button[row][y].isDisabled()) {
				button[row][y].setText("🏴");
				model.flagGrid[row][y] = true;
				
				}
				
			}
			
			
			for (int column = y + 1 ; column < yCoord ; column ++) {		// 270°
				
				if(!button[x][column].isDisabled()) {
				button[x][column].setText("🏴");
				model.flagGrid[x][column] = true;
				
				}
			}
			
			
			temp = x;														// Initialize temp new after loop
			
			for (int column = y - 1 ; column >= 0   ; column --)   {		// 45°  
	
				if (temp < xCoord -1){
					temp++;													// add x coordinates
				}
				else {
					break;
				}
				
				if ( temp   < xCoord) {
					if(!button[temp ][column].isDisabled()) {
					button[temp ][column].setText("🏴");
					model.flagGrid[temp ][column] = true;
					}
				}
																		
			}
			
			
			temp = x;
			
			for (int column = y - 1 ; column >= 0 ; column --)  {			// check 135°
				
				if (temp >= 0){
					
					temp--;												// add x coordinates
					
				}
				else {
					break;
				}
				
				if ( temp  >= 0 ) {
					if(!button[temp ][column].isDisabled()) {
						
					button[temp][column].setText("🏴");
					model.flagGrid[temp][column] = true;
					
					}
				}
				
				
			}
			
			temp = x;
			
			for (int column = y + 1 ; column < yCoord ; column ++)  {		// check 225°
				
				if (temp >= 0){
					
					temp--;													// add x coordinates
					
				}
				else {
					break;
				}
				
				if ( temp  >= 0 ) {
					
					if(!button[temp ][column].isDisabled()) {
						
					button[temp][column].setText("🏴");
					model.flagGrid[temp][column] = true;
					
					}
					
				}
				
																			
			}
			
			temp = x;
			
			for (int column = y + 1 ; column < yCoord ; column ++)  {		// check 315°
				
				if (temp < xCoord -1){
					
					temp++;												// add x coordinates
					
				}
				else {
					break;
				}
				if ( temp  <= xCoord - 1) {
					
					if(!button[temp ][column].isDisabled()) {
						
						button[temp][column].setText("🏴");
						model.flagGrid[temp][column] = true;
						
					}
				}
				
			}

		}
		
	}


	public class Control {
		
		private  Model model;
		private View view;
		 
		public Control (Model model, View view) {
			 
			this.model = model;
			this.view = view;
			
			view.messageLabel.setText( "   " + String.valueOf(model.turns) + "/"  +String.valueOf(model.endTurns) + " Steps.");
			controlGame();																								// Initializes control
			
			 
			
			//  Seeding Process
			view.seedButton.setOnAction	(event	   -> 	{	if(view.getSeed().equals("")) {model.setSeed();view.messageLabel.setText("");}			// Restart the game with a new seed
			 												else {
			 												try{
			 												model.setSeed(Long.valueOf( view.getSeed()));
			 												view.messageLabel.setText("");} catch(Exception e) {
			 												model.setSeed();
			 												view.messageLabel.setText("   Wrong Seed!");}};				// New Seed
			 												model.setGrid();											// New grid with new ships
			 												view.setInterface();										// Delete old and get new Interface
			 												controlGame(); } ); 										// Set controls for new Button grid
			 
			
			view.seeding.setOnKeyPressed( event 	-> {  	if (event.getCode() == KeyCode.ENTER) {
															if(view.getSeed().equals("")) {model.setSeed();view.messageLabel.setText("");}			// Restart the game with a new seed
															else {
															try { model.setSeed(Long.valueOf(view.getSeed()));view.messageLabel.setText("");} catch (Exception wrongSeed){
															System.out.println(wrongSeed);
															model.setSeed();
															view.messageLabel.setText("   Wrong Seed!");}};				// New Seed
															model.setGrid();											// New grid with new ships
															view.setInterface();										// Delete old and get new Interface
															controlGame();} } );
			
			
		}
		 
		
		// Setting up controls for the Game
		
		public void controlGame() {
		
		for (int column = 0 ; column < yCoord ; column ++) {	
				 
			final int c = column;
					
				for (int row = 0 ; row < xCoord ; row ++) {
						
					final int r = row;
						
					view.getButton( r, c ).setOnMousePressed ( event -> {	if(event.getButton() == MouseButton.PRIMARY) {
																			view.revealTile(r , c);
																			view.messageLabel.setText( "   " + String.valueOf(model.turns) + "/"  +String.valueOf(model.endTurns) + " Steps.");
																			view.update(); 
																			view.getButton(r,c).setDisable(true);}
					
																			else if (event.getButton() == MouseButton.SECONDARY && model.flagGrid[r][c] != true) {		// Set flag Button
																			view.button[r][c].setText("🏴");
																			model.flagGrid[r][c] = true;}
					
																			else if (event.getButton() == MouseButton.SECONDARY && model.flagGrid[r][c] == true) {		// Remove flag Button
																			view.button[r][c].setText("");
																			model.flagGrid[r][c] = false;};});  
					
				}
				
		}
			 
		System.out.println("Seed: " + Galaxy.seed);					// Seed in console for Copy and Paste 
		view.getStage().show();
		
		}

	}
	
	
	@Override
	public void start(Stage Game) throws Exception {
		
		Game.setTitle("Darkstorm Galaxy");
		Model model = new Model();
	    View view = new View(model, Game);
	    new Control(model, view);

	}

	public static void main(String[] args) {
	
		launch(args);
		
	}
	
}
