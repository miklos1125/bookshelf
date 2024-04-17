package bookshelf;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;


public class Book {
    
    private SimpleStringProperty title, author, publisher;
    private SimpleStringProperty yearOfPublishing, numberOfPages;
    private SimpleIntegerProperty ID;
    private SimpleBooleanProperty available;
    
    Book(String title, String author){
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.publisher = new SimpleStringProperty();
        this.yearOfPublishing = new SimpleStringProperty();
        this.numberOfPages = new SimpleStringProperty();
        this.ID = new SimpleIntegerProperty();
        this.available = new SimpleBooleanProperty(true);
    }
    
    @Override
    public String toString(){
        String toReturn = title.get() +" ("+ author.get() +")";
        return toReturn;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public String getPublisher() {
        return publisher.get();
    }

    public void setPublisher(String publisher) {
        this.publisher.set(publisher);
    }

    public String getYearOfPublishing() {
        String toReturn = yearOfPublishing != null ? yearOfPublishing.get() : "";
        return toReturn;
    }

    public void setYearOfPublishing(int yearOfPublishing) {
        String yearString = yearOfPublishing != 0 ? String.valueOf(yearOfPublishing) : "";
        this.yearOfPublishing.set(yearString);
    }

    public int getID() {
        return ID.get();
    }
    
    public void setID(int id){
        this.ID.set(id);
    }
    
    public String getNumberOfPages() {
        String toReturn = numberOfPages != null ? numberOfPages.get() : "";
        return toReturn;
    }

    public void setNumberOfPages(int numberOfPages) {
        String numString = numberOfPages >0 ? String.valueOf(numberOfPages) : "";
        this.numberOfPages.set(numString);
    }

    public boolean isAvailable() {
        return available.get();
    }

    public void setAvailable(boolean available) {
        this.available.set(available);
    }
}
