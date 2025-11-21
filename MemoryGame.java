/////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: Memory Match Game
// Course: CS 300 Fall 2020
//
// Author: Khyati Gupta
// Email: kgupta44@wisc.edu
// Lecturer: Mouna Kacem
//
///////////////////////// ALWAYS CREDIT OUTSIDE HELP //////////////////////////
//
// Persons: Aabha Mishra (mousePressed() help)
// Online Sources: NONE
//
///////////////////////////////////////////////////////////////////////////////

import java.io.File;
import processing.core.PApplet;
import processing.core.PImage;

/*
 * This class implements a card matching memory game and uses the Processing library to build the
 * Graphical User Interface and interact with the player. In this memory game, 12 cards are first
 * laid face down on a surface. Each turn, the player selects two faced down cards, which are then
 * flipped over and if they match, they remain faced up. Otherwise, they are both flipped back to
 * face down. When the player successfully turns all the cards up, the program will declare him a
 * Winner of the game.
 * 
 * @author khyat
 *
 */

public class MemoryGame {

	// Congratulations message
	private final static String CONGRA_MSG = "CONGRATULATIONS! YOU WON!";
	// Cards not matched message
	private final static String NOT_MATCHED = "CARDS NOT MATCHED. Try again!";
	// Cards matched message
	private final static String MATCHED = "CARDS MATCHED! Good Job!";
	// 2D-array which stores cards coordinates on the window display
	private final static float[][] CARDS_COORDINATES = new float[][] { { 170, 170 }, { 324, 170 }, { 478, 170 },
			{ 632, 170 }, { 170, 324 }, { 324, 324 }, { 478, 324 }, { 632, 324 }, { 170, 478 }, { 324, 478 },
			{ 478, 478 }, { 632, 478 } };
	// Array that stores the card images filenames
	private final static String[] CARD_IMAGES_NAMES = new String[] { "ball.png", "redFlower.png", "yellowFlower.png",
			"apple.png", "peach.png", "shark.png" };

	private static PApplet processing; // PApplet object that represents the graphic display window
	private static Card[] cards; // one dimensional array of cards
	private static PImage[] images; // array of images of the different cards
	private static Card selectedCard1; // First selected card
	private static Card selectedCard2; // Second selected card
	private static boolean winner; // boolean evaluated true if the game is won, and false otherwise
	private static int matchedCardsCount; // number of cards matched so far in one session of the game
	private static String message; // Displayed message to the display window

	/*
	 * Defines the initial environment properties of this game as the program starts
	 * 
	 * @return
	 */
	public static void setup(PApplet processing) {

		images = new PImage[6];
		MemoryGame.processing = processing;

		// load ball.png image file as PImage object and store its reference into
		// images[0]
		images[0] = processing.loadImage("images" + File.separator + CARD_IMAGES_NAMES[0]);
		// load redFlower.png image file as PImage object and store its reference into
		// images[1]
		images[1] = processing.loadImage("images" + File.separator + CARD_IMAGES_NAMES[1]);
		// load yellowFlower.png image file as PImage object and store its reference
		// into images[2]
		images[2] = processing.loadImage("images" + File.separator + CARD_IMAGES_NAMES[2]);
		// load apple.png image file as PImage object and store its reference into
		// images[3]
		images[3] = processing.loadImage("images" + File.separator + CARD_IMAGES_NAMES[3]);
		// load peach.png image file as PImage object and store its reference into
		// images[4]
		images[4] = processing.loadImage("images" + File.separator + CARD_IMAGES_NAMES[4]);
		// load shark.png image file as PImage object and store its reference into
		// images[5]
		images[5] = processing.loadImage("images" + File.separator + CARD_IMAGES_NAMES[5]);

		startNewGame();
	}

	/*
	 * Initializes the Game
	 * 
	 * @return
	 */
	public static void startNewGame() {

		selectedCard1 = null;
		selectedCard2 = null;
		matchedCardsCount = 0;
		winner = false;
		message = "";

		cards = new Card[CARDS_COORDINATES.length];
		int[] mixedUp = Utility.shuffleCards(cards.length);
		for (int i = 0; i < CARDS_COORDINATES.length; i++) {
			cards[i] = new Card(images[mixedUp[i]], CARDS_COORDINATES[i][0], CARDS_COORDINATES[i][1]);
			cards[i].setVisible(false);

		}
	}

	/*
	 * Callback method called each time the user presses a key
	 * 
	 * @return
	 */
	public static void keyPressed() {

		char keyPressed = processing.key;
		if (keyPressed == 'N' || keyPressed == 'n')
			startNewGame();

	}

	/*
	 * Callback method draws continuously this application window display
	 * 
	 * @return
	 */
	public static void draw() {

		// Set the color used for the background of the Processing window
		processing.background(245, 255, 250); // Mint cream color
		for (int i = 0; i < cards.length; i++) {
			if (cards[i] != null)
				cards[i].draw();
		}
		displayMessage(message);
	}

	/*
	 * Checks whether the mouse is over a given Card
	 * 
	 * @return true if the mouse is over the a card in the cards array, false
	 * otherwise
	 */
	public static boolean isMouseOver(Card card) {

		// computing start and end bounds of x and y coordinates
		float startX = (float) (card.getX() - (0.5 * card.getWidth()));
		float endX = (float) (card.getX() + (0.5 * card.getWidth()));

		float startY = (float) (card.getY() - (0.5 * card.getHeight()));
		float endY = (float) (card.getY() + (0.5 * card.getHeight()));

		if (processing.mouseX < endX && processing.mouseX > startX && processing.mouseY < endY
				&& processing.mouseY > startY) {

			return true;
		}

		return false;
	}

	/*
	 * Checks whether the two selected cards are null or not, if they have not been
	 * matched yet or if all cards are matched and performs functions relative to
	 * these conditions
	 * 
	 * @return
	 */
	public static void mousePressed() {
		// when the two selected cards are not null
		if (selectedCard1 != null && selectedCard2 != null) {
			// if the two cards are already matched
			if (selectedCard1.isMatched() && selectedCard2.isMatched()) {
				selectedCard1.deselect();
				selectedCard2.deselect();
				selectedCard1 = null;
				selectedCard2 = null;
			}
			// if the two cards have not been matched yet
			else if (!selectedCard1.isMatched() && !selectedCard2.isMatched()) {
				selectedCard1.deselect();
				selectedCard2.deselect();
				selectedCard1.setVisible(false);
				selectedCard2.setVisible(false);
				selectedCard1 = null;
				selectedCard2 = null;
			}
		}

		// assigning card values into selectedCard variables
		if (selectedCard1 == null || selectedCard2 == null) {
			mousePressedHelper();
		}

		// if cards selected aren't null and they match
		if (selectedCard1 != null && selectedCard2 != null && !selectedCard1.isMatched() && !selectedCard2.isMatched()
				&& matchingCards(selectedCard1, selectedCard2)) {
			selectedCard1.setMatched(true);
			selectedCard2.setMatched(true);
			message = MATCHED;
			matchedCardsCount = matchedCardsCount + 2;

			// checking if all cards are matched
			if (matchedCardsCount == 12) {
				winner = true;
				if (selectedCard1 != null) {
					selectedCard1.deselect();
				}
				if (selectedCard2 != null) {
					selectedCard2.deselect();
				}
				message = CONGRA_MSG;
				return;
			}

		}

		// if cards selected do not match
		else if (selectedCard1 != null && selectedCard2 != null && !selectedCard1.isMatched()
				&& !selectedCard2.isMatched() && !matchingCards(selectedCard1, selectedCard2)) {
			message = NOT_MATCHED;
		}
	}

	/*
	 * Helper method for assigning card values to selectedCard variables, when
	 * either is null
	 * 
	 * @return
	 */
	public static void mousePressedHelper() {
		for (int i = 0; i < cards.length; i++) {
			if (isMouseOver(cards[i])) {

				cards[i].setVisible(true);
				cards[i].select();

				if (selectedCard1 == null) {
					selectedCard1 = cards[i];
				} else {
					selectedCard2 = cards[i];
				}
				break;
			}
		}
	}

	/*
	 * Checks whether two cards match or not
	 * 
	 * @param card1 reference to the first card
	 * 
	 * @param card2 reference to the second card
	 * 
	 * @return true if card1 has the same image as card2
	 */
	public static boolean matchingCards(Card card1, Card card2) {
		// if image references for both cards match
		if (card1.getImage().equals(card2.getImage()))
			return true;

		return false;

	}

	/*
	 * Displays a given message to the display window
	 * 
	 * @param message to be displayed to the display window
	 * 
	 * @return
	 */
	public static void displayMessage(String message) {

		processing.fill(0);
		processing.textSize(20);
		processing.text(message, processing.width / 2, 50);
		processing.textSize(12);

	}

	public static void main(String[] args) {

		Utility.startApplication(); // starts the application

	}

}
