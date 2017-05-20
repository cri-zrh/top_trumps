import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/*
 * ReportGUI opens a new window which displays the game statistics. In addition, it writes the report
 * into a txt file.
*/
public class ReportGUI extends JFrame implements ActionListener  {

  // instance variables
  private String reportContent;
  private DatabaseConnection dbConnection;
  private JTextArea reportDisplay;
  private FileWriter fileWriter;
  private JButton saveTxtReport;

  /**
 * Method defines the look of the report GUI
 */
public ReportGUI() {
    // Adding GUI components
  JFrame reportF = new JFrame();
  reportF.setTitle("Game Results");
  reportF.setSize(500,110);
  reportF.setLocation(200,150);

    reportDisplay = new JTextArea();
    dbConnection = new DatabaseConnection();

    // Calls the method to build the report content
    buildReport();

    // Adds the text area to the report frame and makes it visible
    reportDisplay.setEditable(false);
    reportDisplay.setText(reportContent);
    saveTxtReport = new JButton("Save Report");

    JPanel reportPan = new JPanel();
    JPanel buttonPan = new JPanel();

    reportPan.add(reportDisplay);
    buttonPan.add(saveTxtReport);

    reportF.add(reportPan, "North");
    reportF.add(buttonPan, "South");

    saveTxtReport.addActionListener(this);
    reportF.setVisible(true);
  }

  /**
 * Method creates a String containing the information for the game statistics report.
 */
private void buildReport() {
    StringBuilder stringBuilder = new StringBuilder();

    // Header of report
    stringBuilder.append("Games\tCPU wins\t"
        + "Human wins\tDraw avg\tMax rounds\n");
    stringBuilder.append("=============================================\n");

    // shows the number of overall games
    stringBuilder.append(dbConnection.executeQuery("SELECT COUNT(game_id) FROM game", "count"));
    stringBuilder.append("\t");
    // shows the number of times the computer won
    stringBuilder.append(dbConnection.executeQuery("SELECT COUNT(game_id) FROM game WHERE winner = 'CPU'", "count"));
    stringBuilder.append("\t");
    // shows the number of times the human player won
    stringBuilder.append(dbConnection.executeQuery("SELECT COUNT(game_id) FROM game WHERE winner = 'human'", "count"));
    stringBuilder.append("\t");
    // shows the average number of draws per game
    stringBuilder.append((int)Float.parseFloat(dbConnection.executeQuery("SELECT AVG(draws) FROM game", "avg")));
    stringBuilder.append("\t");
    // shows the maximum number of rounds played
    stringBuilder.append(dbConnection.executeQuery("SELECT MAX(rounds) FROM game", "max"));

    // Converts stringBuilder to a String
    reportContent = stringBuilder.toString();
  }

  /**
 * Method writes out the report to text file.
 *
 */
private void reportToFile() {
    try {
    fileWriter = new FileWriter("GameStats.txt");
    fileWriter.write(reportContent);
    fileWriter.close();
    }
    catch (IOException ioe) {
      System.err.println("IO Exception thrown while trying to write to file GameStats.txt");
    }
  }

public void actionPerformed(ActionEvent e) {
  if(e.getSource() == saveTxtReport){
      // Calls the method to create a text file that contains the game statistics.
      reportToFile();
  }
}

}
