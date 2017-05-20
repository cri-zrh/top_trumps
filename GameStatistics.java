import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class GameStatistics {

  public GameStatistics() {
  }


  /**
 * @param gameWinner - the game winner's player index
 * @param noRound  - Tot. number of rounds played in the game
 * @param noDraws - Tot. number of draws in the game
 * @param humanRoundWins - Tot. number of rounds won by human
 * @param CPURoundWins - Tot. aggregated number of round won by the CPU
 * This method takes in its parameters from GameLogic class, and inserts them into the DB.
 */
public void save(int gameWinner, int noRound, int noDraws, int humanRoundWins, int CPURoundWins) {

    DatabaseConnection dbm = new DatabaseConnection();
    Connection ctn = dbm.Connect();
    Statement stmt = null;

    String gameWinnerString = null;

    // Assigning the winner's name based on player's index. If the index = 0, then the player is human, else CPU.
    if (gameWinner == 0) {
      gameWinnerString = " 'human' ";
    }
    else {
      gameWinnerString = " 'CPU' ";
    }

    String query = "INSERT INTO game(rounds, draws, winner, human_roundW, cpu_roundW) VALUES( "+
        noRound + "," +
        noDraws + "," +
        gameWinnerString + "," +
        humanRoundWins + "," + // rounds won by human player
        CPURoundWins + ");"; //rounds won by CPU players

    System.out.println(query);

    try
    {
      stmt = ctn.createStatement();
      stmt.executeUpdate(query);
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      System.err.println("Error executing insert statement " + query);
    }
  }

  }
