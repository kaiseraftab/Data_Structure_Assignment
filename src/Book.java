import com.google.gson.annotations.SerializedName;

public class Book
{
    @SerializedName("Title")
    private String title;

    @SerializedName("Author")
    private String author;

    @SerializedName("Year")
    private int year;

    @SerializedName("Genre")
    private String genre;

    @SerializedName("Publisher")
    private String publisher;

    public Book(String title, String author, int year, String genre, String publisher)
    {
        this.title = title;
        this.author = author;
        this.year = year;
        this.genre = genre;
        this.publisher = publisher;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public String getGenre()
    {
        return genre;
    }

    public void setGenre(String genre)
    {
        this.genre = genre;
    }

    public String getPublisher()
    {
        return publisher;
    }

    public void setPublisher(String publisher)
    {
        this.publisher = publisher;
    }

    @Override
    public String toString()
    {
        return title + " by " + author + " (" + year + ") - " + genre + ", " + publisher;
    }
}