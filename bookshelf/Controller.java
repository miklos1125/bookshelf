package bookshelf;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class Controller implements Initializable {
    
    private ObservableList<Book> listOfBooks;
       
    @FXML
    ToggleGroup operation;
    @FXML
    RadioButton listButton, addButton, modButton;
    @FXML
    Pane listPane, addPane, modPane;
    
    //listPane:
    @FXML
    TableView mainTable;
    @FXML
    TableColumn idColumn, titleColumn, authorColumn,
            publisherColumn, yearColumn, pagesColumn;
    @FXML
    Label placeholder;
    
    //addPane:
    @FXML
    Button addToLibButton;
    @FXML
    TextField titleInput, authorInput, publisherInput, yearInput, pagesInput; 
        
    @Override
    public void initialize(URL url, ResourceBundle rb){
        setRadioButtons();
        setListOfBooks();
        setTable();

    }
    
    @FXML
    private void addToLibButtonAction(ActionEvent event) {
        String title = titleInput.getText().trim();
        String author = authorInput.getText().trim();
        if (author.equals("") || author.toLowerCase().equals("unknown")){
            author = "unknown";
        }
        if (title.equals("")){
            return;
        } else {
            Book actualBook = new Book(title, author);
            String publisher = publisherInput.getText().trim();
            actualBook.setPublisher(publisher);
            try{
                int year, pages;
                if(! yearInput.getText().equals("")){
                    year = Integer.parseInt(yearInput.getText());
                } else {
                    year = 0;
                }
                if (year < 0) throw new NumberFormatException();
                actualBook.setYearOfPublishing(year);
                
                if(! pagesInput.getText().equals("")){
                    pages = Integer.parseInt(pagesInput.getText());
                } else {
                    pages = 0;
                }
                if (pages < 0) throw new NumberFormatException();
                actualBook.setNumberOfPages(pages);
            }catch(NumberFormatException e){
                System.out.println(e.getMessage());
                System.out.println("Felugró ablak: Wrong number format!");
                return;
            }
            DataBaseModel dbmodel = new DataBaseModel();
            if (dbmodel.isConnected()){
                dbmodel.upload(actualBook);
            } else {
                System.out.println("Could not connect to database.");
                return;
            }
            dbmodel.closeConnection();
            TextField []inputs = {titleInput, authorInput, publisherInput, 
                                                        yearInput, pagesInput};
            for(TextField tf: inputs){
                tf.clear();
            }
        }
    }
    
    private void setListOfBooks(){
        DataBaseModel dbmodel = new DataBaseModel();
        if (dbmodel.isConnected()){
            listOfBooks = dbmodel.getList();
        } else {
            String s = "Could not connect to database.";
            System.out.println(s);
            placeholder.setText(s);
        }
        dbmodel.closeConnection();
    }
    
    private void setTable(){
//        TableColumn availableColumn = new TableColumn("available");
//        availableColumn.setMinWidth(30);
//        availableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new BooleanStringConverter()));
//        availableColumn.setCellValueFactory(new PropertyValueFactory<Book, Boolean>("available"));
//        mainTable.getColumns().add(availableColumn);
        
        idColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        idColumn.setCellValueFactory(new PropertyValueFactory<Book, Integer>("ID"));
               
        titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        titleColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        
        authorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        authorColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        
        publisherColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        publisherColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("publisher"));
        
        yearColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        yearColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("yearOfPublishing"));
        
        pagesColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        pagesColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("numberOfPages"));
        
        mainTable.setItems(listOfBooks);
    }
    
    
    private void setRadioButtons(){
    operation.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            @Override
            public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n){
                RadioButton rb = (RadioButton)operation.getSelectedToggle();
                String s = rb.getId();
                switch (s){
                    case "listButton":  setListOfBooks();
                                        mainTable.setItems(listOfBooks);
                                        addPane.setVisible(false);
                                        modPane.setVisible(false);
                                        listPane.setVisible(true);
                                        break;
                    case "addButton":   addPane.setVisible(true);
                                        modPane.setVisible(false);
                                        listPane.setVisible(false);
                                        break;
                    case "modButton":  addPane.setVisible(false);
                                        modPane.setVisible(true);
                                        listPane.setVisible(false);
                                        break;
                }
            }
        });
    }
}
