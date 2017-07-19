package swen221.monopoly.testing;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

import swen221.monopoly.*;
import swen221.monopoly.Player.Token;
import swen221.monopoly.locations.ColourGroup;
import swen221.monopoly.locations.Location;
import swen221.monopoly.locations.Property;
import swen221.monopoly.locations.SpecialArea;
import swen221.monopoly.locations.Station;
import swen221.monopoly.locations.Street;
import swen221.monopoly.locations.Utility;

public class MonopolyTests {
	// this is where you must write your tests; do not alter the package, or the
	// name of this file. An example test is provided for you.

	@Test
	public void testValidBuyProperty_1() {
		// Construct a "mini-game" of Monopoly and with a single player. The
		// player attempts to buy a property. We check that the right amount has
		// been deducted from his/her balance, and that he/she now owns the
		// property and vice-versa.
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Park Lane", 1500);
			game.buyProperty(player);
			assertEquals(1150, player.getBalance());
			assertEquals("Park Lane", player.iterator().next().getName());
			Street street = (Street) game.getBoard().findLocation("Park Lane");
			assertEquals(player, street.getOwner());
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	// ----------------------GameOfMonopoly tests-----------------------------

	@Test
	public void testMovePlayerPassGo() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Park Lane", 100);
			game.movePlayer(player, 4);
			// check player collect $200 when passing GO
			assertEquals(300, player.getBalance());
			// check player is on right location after move
			assertEquals("Old Kent Road", player.getLocation().getName());
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Check money isnt deposited into account when player doesnt pass go
	 */
	@Test
	public void testDoesntPassGo() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Bow Street", 100);
			game.movePlayer(player, 4);
			assertEquals(100, player.getBalance());
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Check the right amount of rent is collected
	 */
	@Test
	public void testMovePlayerCollectRent() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1000);
			Player owner = setupMockPlayer(game, "Bow Street", 1000);
			game.buyProperty(owner);
			game.movePlayer(owner, 2);
			game.movePlayer(player, 5);
			assertEquals(986, player.getBalance());
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	
	@Test(expected = AssertionError.class)
	public void testBuyPropertyWrongLoc() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Free Parking", 1000);
			game.buyProperty(player);
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void testBuyPropertyAlreadyOwned() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1000);
			Player owner = setupMockPlayer(game, "Bow Street", 1000);
			game.buyProperty(owner);
			game.movePlayer(owner, 3);
			game.movePlayer(player, 5);
			game.buyProperty(player);
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void testBuyPropertyInsufficientFunds() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 10);
			game.buyProperty(player);
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testBuyProperty() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1000);
			game.buyProperty(player);
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void tesSellPropertyWrongLocation() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Free Parking", 1000);
			game.sellProperty(player, player.getLocation());
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void testSellPropertyAlreadyOwned() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1000);
			Player owner = setupMockPlayer(game, "Bow Street", 1000);
			game.buyProperty(owner);
			game.movePlayer(owner, 3);
			game.movePlayer(player, 5);
			game.sellProperty(player, player.getLocation());
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void testSellPropertyNotOwned() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1000);
			game.sellProperty(player, player.getLocation());
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void testSellPropertyWithMortgage() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1000);
			game.buyProperty(player);
			game.mortgageProperty(player, player.getLocation());
			game.sellProperty(player, player.getLocation());
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testSellProperty() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1000);
			game.buyProperty(player);
			game.sellProperty(player, player.getLocation());
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void testMortgagePropertyWrongLocation() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Park Lane", 1000);
			game.buyProperty(player);
			game.movePlayer(player, 3);
			game.mortgageProperty(player, player.getLocation());
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void testMortgagePropertyAlreadyOwned() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1000);
			Player owner = setupMockPlayer(game, "Bow Street", 1000);
			game.buyProperty(owner);
			game.movePlayer(owner, 3);
			game.movePlayer(player, 5);
			game.mortgageProperty(player, player.getLocation());
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void testMortgagePropertyAlreadyMortgaged() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1000);
			game.buyProperty(player);
			game.mortgageProperty(player, player.getLocation());
			game.mortgageProperty(player, player.getLocation());
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testMortgageProperty() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1000);
			game.buyProperty(player);
			game.mortgageProperty(player, player.getLocation());
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void testUnmortgagePropertyWrongLocation() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Park Lane", 1000);
			game.buyProperty(player);
			game.movePlayer(player, 3);
			game.unmortgageProperty(player, player.getLocation());
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void testUnmortgagePropertyAlreadyOwned() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1000);
			Player owner = setupMockPlayer(game, "Bow Street", 1000);
			game.buyProperty(owner);
			game.movePlayer(owner, 3);
			game.movePlayer(player, 5);
			game.unmortgageProperty(player, player.getLocation());
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testUnmortgageProperty() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1000);
			game.buyProperty(player);
			game.mortgageProperty(player, player.getLocation());
			game.unmortgageProperty(player, player.getLocation());
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void testUnmortgagePropertyNotMortgaged() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1000);
			game.buyProperty(player);
			game.unmortgageProperty(player, player.getLocation());
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void testUnmortgagePropertyInsufficientFunds() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 141);
			game.buyProperty(player);
			game.mortgageProperty(player, player.getLocation());
			game.unmortgageProperty(player, player.getLocation());
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void testBuildHousesWrongLocation() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Free Parking", 141);
			Location loc = player.getLocation();
			game.buildHouses(player, loc, 3);
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void testBuildHousesMortgagedStreet() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1400);
			Location loc = player.getLocation();
			game.buyProperty(player);
			game.mortgageProperty(player, loc);
			game.buildHouses(player, loc, 3);
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void testBuildHousesDoesntOwnStreet() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1400);
			Location loc = player.getLocation();
			game.buyProperty(player);
			game.movePlayer(player, 2);
			game.buyProperty(player);
			game.buildHouses(player, loc, 3);
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void testBuildHousesNoSpace() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 10000);
			Location loc = player.getLocation();
			game.buyProperty(player);
			game.movePlayer(player, 2);
			game.buyProperty(player);
			game.movePlayer(player, 1);
			game.buyProperty(player);
			game.buildHouses(player, loc, 3);
			game.buildHouses(player, loc, 3);
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void testBuildHousesInsufficientMoney() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 450);
			Location loc = player.getLocation();
			game.buyProperty(player);
			game.movePlayer(player, 2);
			game.buyProperty(player);
			game.movePlayer(player, 1);
			game.buyProperty(player);
			game.buildHouses(player, loc, 2);
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testBuildHouses() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1000);
			Location loc = player.getLocation();
			game.buyProperty(player);
			game.movePlayer(player, 2);
			game.buyProperty(player);
			game.movePlayer(player, 1);
			game.buyProperty(player);
			game.buildHouses(player, loc, 2);
			Street s = (Street) loc;
			assertEquals(2, s.getHouses());
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void testBuildHotelNotLocation() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Free Parking", 450);
			Location loc = player.getLocation();
			game.buildHotel(player, loc);
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void testBuildHotelMortgagedStreet() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 450);
			Location loc = player.getLocation();
			game.buyProperty(player);
			game.mortgageProperty(player, loc);
			game.buildHotel(player, loc);
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void testBuildHotelDoesntOwnStreet() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 450);
			Location loc = player.getLocation();
			game.buyProperty(player);
			game.buildHotel(player, loc);
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void testBuildHotelNoSpace() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1000);
			game.buyProperty(player);
			game.movePlayer(player, 2);
			game.buyProperty(player);
			game.movePlayer(player, 1);
			game.buyProperty(player);
			Location loc = player.getLocation();
			game.buildHouses(player, loc, 4);
			game.buildHotel(player, loc);
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void testBuildHotelSecondHotel() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 2000);
			game.buyProperty(player);
			game.movePlayer(player, 2);
			game.buyProperty(player);
			game.movePlayer(player, 1);
			game.buyProperty(player);
			Location loc = player.getLocation();
			game.buildHouses(player, loc, 5);
			game.buildHotel(player, loc);
			game.buildHotel(player, loc);
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = AssertionError.class)
	public void testBuildHotelInsuffiecientFunds() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1000);
			game.buyProperty(player);
			game.movePlayer(player, 2);
			game.buyProperty(player);
			game.movePlayer(player, 1);
			game.buyProperty(player);
			Location loc = player.getLocation();
			game.buildHouses(player, loc, 5);
			game.buildHotel(player, loc);
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	// ----------------------Players tests-----------------------------

	@Test
	public void testPlayerGetToken() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1000);
			Token t = player.getToken();
			assertEquals(Player.Token.ScottishTerrier, t);
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPlayerBuy() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1000);
			Player owner = setupMockPlayer(game, "Bow Street", 1000);
			game.buyProperty(owner);
			game.movePlayer(owner, 1);
			game.movePlayer(player, 5);
			Property p = (Property) player.getLocation();
			player.buy(p);
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPlayerSell() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1000);
			Player owner = setupMockPlayer(game, "Bow Street", 1000);
			game.buyProperty(owner);
			game.movePlayer(owner, 1);
			game.movePlayer(player, 5);
			Property p = (Property) player.getLocation();
			player.sell(p);
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	// ----------------------ColorGroup tests-----------------------------

	@Test
	public void testColorGroupGetColor() {
		GameOfMonopoly game = new GameOfMonopoly();
		ColourGroup c = new ColourGroup("Red", 150);
		assertEquals("Red", c.getColour());
	}

	// ----------------------Property tests-----------------------------

	@Test
	public void testPropertyUnmortgage() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1000);
			game.buyProperty(player);
			Property p = (Property) player.getLocation();
			p.unmortgage();
			assertEquals(false, p.isMortgaged());
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	// ----------------------SpecialArea tests-----------------------------
	
	@Test(expected = RuntimeException.class)
	public void testSpecialAreaGetOwner() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1000);
			SpecialArea s = new SpecialArea("test");
			s.getOwner();
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}

	}
	
	@Test(expected = RuntimeException.class)
	public void testSpecialAreaGetRent() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Go", 1000);
			SpecialArea s = (SpecialArea) player.getLocation();
			s.getRent();
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}

	}
	// ----------------------Station tests-----------------------------

	@Test
	public void testStationGetRent() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game,"Fenchurch St. Station", 1000);
			game.buyProperty(player);
			Property p = (Property) player.getLocation();
			Station s = (Station) p;
			assertEquals(25, s.getRent(1));
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}

	}
	
	// ----------------------Street tests-----------------------------

	@Test
	public void testStreetGetRent() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Pall Mall", 1000);
			game.buyProperty(player);
			game.movePlayer(player, 2);
			game.buyProperty(player);
			game.movePlayer(player, 1);
			game.buyProperty(player);
			Player other = setupMockPlayer(game, "Pall Mall", 1000);
			Street s = (Street) other.getLocation();
			assertEquals(20, s.getRent(2));
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}
	
	// ----------------------Utility tests-----------------------------

	@Test
	public void testUtilityGetRent() {
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Electric Company", 1000);
			game.buyProperty(player);
			game.movePlayer(player, 4);
			Player other = setupMockPlayer(game, "Electric Company", 1000);
			Utility s = (Utility) other.getLocation();
			assertEquals(8, s.getRent(2));
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}
	
	// ----------------------Board tests-----------------------------

	@Test
	public void testGetStartLocation() {
		GameOfMonopoly game = new GameOfMonopoly();
		assertEquals("Go", game.getBoard().getStartLocation().getName());

	}

	@Test
	public void testFindLocation() {
		GameOfMonopoly game = new GameOfMonopoly();
		Location start = game.getBoard().findLocation("Oxford Street");
		Location end = game.getBoard().findLocation("Park Lane");
		assertEquals(end, game.getBoard().findLocation(start, 5));
	}
	
	@Test
	public void testFindLocationNull() {
		GameOfMonopoly game = new GameOfMonopoly();
		Location start = game.getBoard().findLocation("Test");
		assertEquals(null, start);
	}
	
	
	/**
	 * Setup a mock game of monopoly with a player located at a given location.
	 */
	private Player setupMockPlayer(GameOfMonopoly game, String locationName, int balance)
			throws GameOfMonopoly.InvalidMove {
		Board board = game.getBoard();
		Location location = board.findLocation(locationName);
		return new Player("Dave", Player.Token.ScottishTerrier, balance, location);
	}
}
