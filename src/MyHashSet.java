import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class MyHashSet<T> implements IDataStructure<T>, ISortable<T>, ILinearSearchable<T>
{
    private final Set<T> set = new LinkedHashSet<>();

    @Override
    public String getName()
    {
        return "HashSet";
    }

    @Override
    public void insert(T item)
    {
        set.add(item);
    }

    @Override
    public void delete(T item)
    {
        set.remove(item);
    }

    @Override
    public boolean contains(T item)
    {
        return set.contains(item);
    }

    @Override
    public List<T> getAll()
    {
        return new ArrayList<>(set);
    }

    @Override
    public void bubbleSort(Comparator<T> comparator)
    {
        List<T> list = new ArrayList<>(set);
        int n = list.size();

        for (int i = 0; i < n - 1; i++)
        {
            for (int j = 0; j < n - i - 1; j++)
            {
                if (comparator.compare(list.get(j), list.get(j + 1)) > 0)
                {
                    T temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }

        set.clear();
        set.addAll(list);
    }

    @Override
    public void insertionSort(Comparator<T> comparator)
    {
        List<T> list = new ArrayList<>(set);

        for (int i = 1; i < list.size(); i++)
        {
            T key = list.get(i);
            int j = i - 1;

            while (j >= 0 && comparator.compare(list.get(j), key) > 0)
            {
                list.set(j + 1, list.get(j));
                j--;
            }

            list.set(j + 1, key);
        }

        set.clear();
        set.addAll(list);
    }

    @Override
    public void mergeSort(Comparator<T> comparator)
    {
        List<T> list = new ArrayList<>(set);
        list.sort(comparator);
        set.clear();
        set.addAll(list);
    }

    @Override
    public List<T> linearSearch(Function<T, String> extractor, String term)
    {
        List<T> results = new ArrayList<>();

        for (T item : set)
        {
            if (extractor.apply(item).equalsIgnoreCase(term))
            {
                results.add(item);
            }
        }

        return results;
    }

    @Override
    public List<T> binarySearch(Function<T, String> extractor, String term, Comparator<T> comparator)
    {
        List<T> list = new ArrayList<>(set);
        list.sort(comparator);

        int left = 0, right = list.size() - 1;
        while (left <= right)
        {
            int mid = (left + right) / 2;
            int cmp = term.compareToIgnoreCase(extractor.apply(list.get(mid)));
            if (cmp == 0)
            {
                List<T> results = new ArrayList<>();
                int i = mid;
                while (i >= 0 && term.equalsIgnoreCase(extractor.apply(list.get(i))))
                {
                    results.add(list.get(i));
                    i--;
                }
                i = mid + 1;
                while (i < list.size() && term.equalsIgnoreCase(extractor.apply(list.get(i))))
                {
                    results.add(list.get(i));
                    i++;
                }
                return results;
            }
            else if (cmp < 0)
            {
                right = mid - 1;
            }
            else
            {
                left = mid + 1;
            }
        }
        return new ArrayList<>();
    }
}