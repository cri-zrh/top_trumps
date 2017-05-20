import java.sql.*;

/*
 * Manages the connection to the database
 */
public class DatabaseConnection {

  private Connection connection = null;

  public DatabaseConnection(){
    Connect();
  }

  /** Method establishes connection to DB
 * @return connection object
 */
public Connection Connect(){

    String dbname = "TopTrumps";
    String username = "postgres";
    String password = "Croft12345";

    try
    {
    connection =
    DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname,username, password);
    }
    catch (SQLException e)
    {
    System.err.println("Connection failed!");
    e.printStackTrace();

    }
    if (connection != null)
    {
    System.out.println("Connection successful");
    }
    else
    {
    System.err.println("Failed to make connection!");
    }
    return connection;
  }

  public void Close()
  {
    try
    {
    connection.close();
    System.out.println("Connection closed");
    }
    catch (SQLException e)
    {
    e.printStackTrace();
    System.out.println("Failed to close connection!");
    }
  }

  /**
 * @param query - Takes in the select queries which need to be executed
 * @param col
 * @return string containing the results retrieved from the database
 */
public String executeQuery(String query, String col)
  {
    StringBuilder stringBuilder = new StringBuilder();

    Statement stmt = null;

    try {
    stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery(query);

    //the next method of ResultSet allows you to iterate through the results
    while (rs.next())
      {
    // the getString method of the ResultSet object allows you to access the value for the given column name for the current row in the result set as a String.
            stringBuilder.append(rs.getString(col));
            }
    }
    catch (SQLException e ) {
    e.printStackTrace();
    System.err.println("error executing query " + query);
    }
    return stringBuilder.toString();
  }
}
