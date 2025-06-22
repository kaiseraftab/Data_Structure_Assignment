import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.util.List;

public class BookLoader
{
    public static List<Book> loadBooks(String filePath) throws Exception
    {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(filePath))
        {
            return gson.fromJson(reader, new TypeToken<List<Book>>() {}.getType());
        }
    }
}
