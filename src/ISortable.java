import java.util.Comparator;

public interface ISortable<T>
{
    void bubbleSort(Comparator<T> comparator);

    void insertionSort(Comparator<T> comparator);

    void mergeSort(Comparator<T> comparator);
}
