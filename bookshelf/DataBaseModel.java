package bookshelf;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataBaseModel {
    
    final private String URL = "jdbc:h2:./data/mydata";
    final private String user = "Librarian";
    final private String password = "bibliotheque";
    
    private Connection connection = null;
    private PreparedStatement prepState = null;
    private Statement statement = null;
    private DatabaseMetaData dbmetadata = null;
    
    private boolean connected;
    
    DataBaseModel(){
        try{
            connection = DriverManager.getConnection(URL, user, password);
            //System.out.println("Connection OK");
            statement = connection.createStatement();
            dbmetadata = connection.getMetaData();
            ResultSet rs = dbmetadata.getTables(null, "PUBLIC", "BOOKS", null);
            if (!rs.next()){
                System.out.println("Table creation...");
                String sql = "CREATE TABLE Books(ID int NOT NULL "
                        + "AUTO_INCREMENT, Title varchar(100), Author varchar(100), "
                        + "Publisher varchar(100), Year_published int, Pages int, "
                        + "PRIMARY KEY(ID));";
                statement.execute(sql);
            }
            connected = true;
        }catch(SQLException e){
            e.printStackTrace();
            connected = false;
        }
    }
    
    public boolean isConnected(){
        return connected;
    }
    
    public void closeConnection(){
        try{
            statement.close();
            connection.close();
            //System.out.println("Connection closed.");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void upload(Book book){
        String sql = "INSERT INTO Books(Title, Author, Publisher, Year_published, Pages) "
                + "VALUES(?, ?, ?, ?, ?);"; 
        try{
            prepState = connection.prepareStatement(sql);
            String title = book.getTitle();
            prepState.setString(1, title);
            String author = book.getAuthor();
            prepState.setString(2, author);
            String publisher = book.getPublisher();
            prepState.setString(3, publisher);
            try{
                int year = Integer.parseInt(book.getYearOfPublishing());
                prepState.setInt(4, year);
            }catch(NumberFormatException nfe){
                prepState.setInt(4, 0);
            }
            try{
                int page = Integer.parseInt(book.getNumberOfPages());
                prepState.setInt(5, page);
            }catch(NumberFormatException nfe){
                prepState.setInt(5, 0);
            }
//            boolean available = book.isAvailable();
//            prepState.setBoolean(6, available);
            prepState.execute();
            prepState.close();
        } catch (SQLException e){
            System.out.println("Uploading failure.");
            System.out.println(""+e);
        }
    }
    
    public ObservableList<Book> getList(){
        ObservableList<Book> listOfBooks = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Books";
        try{
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                int id = rs.getInt(1);
                String title = rs.getString(2);
                String author = rs.getString(3);
                String publisher = rs.getString(4);
                int year = rs.getInt(5);
                int pages = rs.getInt(6);
                //boolean available = rs.getBoolean(7);
                Book actualBook = new Book(title, author);
                actualBook.setID(id);
                actualBook.setPublisher(publisher);
                actualBook.setYearOfPublishing(year);
                actualBook.setNumberOfPages(pages);
                //actualBook.setAvailable(available);
                listOfBooks.add(actualBook);
                //System.out.println(actualBook.toString());
            }
        } catch(SQLException e){
            System.out.println("Listing faliure.");
        }
        return listOfBooks;
    }
}
