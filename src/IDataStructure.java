import java.util.List;

public interface IDataStructure<T>
{
    void insert(T item);

    void delete(T item);

    boolean contains(T item);

    List<T> getAll();

    String getName();
}
