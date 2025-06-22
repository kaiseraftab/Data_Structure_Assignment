import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MyDictionary<K, V> implements IDataStructure<V>, ISortable<V>, ILinearSearchable<V>
{
    private final Map<K, V> map = new LinkedHashMap<>();
    private final Function<V, K> keyExtractor;

    public MyDictionary(Function<V, K> keyExtractor)
    {
        this.keyExtractor = keyExtractor;
    }

    @Override
    public String getName()
    {
        return "Dictionary";
    }

    @Override
    public void insert(V value)
    {
        map.put(this.keyExtractor.apply(value), value);
    }

    @Override
    public void delete(V value)
    {
        map.remove(this.keyExtractor.apply(value));
    }

    @Override
    public boolean contains(V value)
    {
        return map.containsKey(this.keyExtractor.apply(value));
    }

    @Override
    public List<V> getAll()
    {
        return new ArrayList<>(map.values());
    }

    @Override
    public void bubbleSort(Comparator<V> comparator)
    {
        List<V> list = new ArrayList<>(map.values());
        int n = list.size();

        for (int i = 0; i < n - 1; i++)
        {
            for (int j = 0; j < n - i - 1; j++)
            {
                if (comparator.compare(list.get(j), list.get(j + 1)) > 0)
                {
                    V temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }

        map.clear();
        for (V v : list)
        {
            map.put(this.keyExtractor.apply(v), v);
        }
    }

    @Override
    public void insertionSort(Comparator<V> comparator)
    {
        List<V> list = new ArrayList<>(map.values());

        for (int i = 1; i < list.size(); i++)
        {
            V key = list.get(i);
            int j = i - 1;

            while (j >= 0 && comparator.compare(list.get(j), key) > 0)
            {
                list.set(j + 1, list.get(j));
                j--;
            }

            list.set(j + 1, key);
        }

        map.clear();
        for (V v : list)
        {
            map.put(this.keyExtractor.apply(v), v);
        }
    }

    @Override
    public void mergeSort(Comparator<V> comparator)
    {
        List<V> list = new ArrayList<>(map.values());
        list.sort(comparator);
        map.clear();
        for (V v : list)
        {
            map.put(this.keyExtractor.apply(v), v);
        }
    }

    @Override
    public List<V> linearSearch(Function<V, String> extractor, String term)
    {
        List<V> results = new ArrayList<>();

        for (V value : map.values())
        {
            if (extractor.apply(value).equalsIgnoreCase(term))
            {
                results.add(value);
            }
        }

        return results;
    }

    @Override
    public List<V> binarySearch(Function<V, String> extractor, String term, Comparator<V> comparator)
    {
        List<V> list = new ArrayList<>(map.values());
        list.sort(comparator);

        int left = 0, right = list.size() - 1;
        while (left <= right)
        {
            int mid = (left + right) / 2;
            int cmp = term.compareToIgnoreCase(extractor.apply(list.get(mid)));
            if (cmp == 0)
            {
                List<V> results = new ArrayList<>();
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
