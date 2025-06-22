import java.util.List;
import java.util.function.Function;
import java.util.Comparator;

public interface ILinearSearchable<T>
{
    List<T> linearSearch(Function<T, String> extractor, String term);

    List<T> binarySearch(Function<T, String> extractor, String term, Comparator<T> comparator);
}