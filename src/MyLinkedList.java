import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;


public class MyLinkedList<T> implements IDataStructure<T>, ISortable<T>, ILinearSearchable<T>
{
    private class Node
    {
        T data;
        Node next;

        Node(T data)
        {
            this.data = data;
        }
    }

    private Node head;

    @Override
    public String getName()
    {
        return "LinkedList";
    }

    @Override
    public void insert(T item)
    {
        Node node = new Node(item);

        if (head == null)
        {
            head = node;
        }
        else
        {
            Node current = head;

            while (current.next != null)
            {
                current = current.next;
            }

            current.next = node;
        }
    }

    @Override
    public void delete(T item)
    {
        if (head == null)
        {
            return;
        }

        if (head.data.equals(item))
        {
            head = head.next;
            return;
        }

        Node current = head;

        while (current.next != null && !current.next.data.equals(item))
        {
            current = current.next;
        }

        if (current.next != null)
        {
            current.next = current.next.next;
        }
    }

    @Override
    public boolean contains(T item)
    {
        Node current = head;

        while (current != null)
        {
            if (current.data.equals(item))
            {
                return true;
            }

            current = current.next;
        }

        return false;
    }

    @Override
    public List<T> getAll()
    {
        List<T> list = new ArrayList<>();
        Node current = head;

        while (current != null)
        {
            list.add(current.data);
            current = current.next;
        }

        return list;
    }

    private int size()
    {
        int count = 0;
        Node current = head;

        while (current != null)
        {
            count++;
            current = current.next;
        }

        return count;
    }

    @Override
    public void bubbleSort(Comparator<T> comparator)
    {
        int n = size();

        for (int i = 0; i < n - 1; i++)
        {
            Node current = head;

            for (int j = 0; j < n - i - 1; j++)
            {
                if (comparator.compare(current.data, current.next.data) > 0)
                {
                    T temp = current.data;
                    current.data = current.next.data;
                    current.next.data = temp;
                }

                current = current.next;
            }
        }
    }

    @Override
    public void insertionSort(Comparator<T> comparator)
    {
        if (head == null || head.next == null)
        {
            return;
        }

        Node sorted = null;
        Node current = head;

        while (current != null)
        {
            Node next = current.next;
            sorted = insertSorted(sorted, current, comparator);
            current = next;
        }

        head = sorted;
    }

    private Node insertSorted(Node sorted, Node node, Comparator<T> comparator)
    {
        if (sorted == null || comparator.compare(sorted.data, node.data) >= 0)
        {
            node.next = sorted;
            return node;
        }

        Node current = sorted;

        while (current.next != null && comparator.compare(current.next.data, node.data) < 0)
        {
            current = current.next;
        }

        node.next = current.next;
        current.next = node;

        return sorted;
    }

    @Override
    public void mergeSort(Comparator<T> comparator)
    {
        head = mergeSortList(head, comparator);
    }

    private Node mergeSortList(Node node, Comparator<T> comparator)
    {
        if (node == null || node.next == null)
        {
            return node;
        }

        Node mid = getMid(node);
        Node right = mid.next;
        mid.next = null;

        Node leftSorted = mergeSortList(node, comparator);
        Node rightSorted = mergeSortList(right, comparator);

        return merge(leftSorted, rightSorted, comparator);
    }

    private Node getMid(Node node)
    {
        Node slow = node;
        Node fast = node.next;

        while (fast != null && fast.next != null)
        {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }

    private Node merge(Node left, Node right, Comparator<T> comparator)
    {
        Node dummy = new Node(null);
        Node tail = dummy;

        while (left != null && right != null)
        {
            if (comparator.compare(left.data, right.data) <= 0)
            {
                tail.next = left;
                left = left.next;
            }
            else
            {
                tail.next = right;
                right = right.next;
            }

            tail = tail.next;
        }

        tail.next = (left != null) ? left : right;

        return dummy.next;
    }

    @Override
    public List<T> linearSearch(Function<T, String> extractor, String term)
    {
        List<T> results = new ArrayList<>();
        Node current = head;

        while (current != null)
        {
            if (extractor.apply(current.data).equalsIgnoreCase(term))
            {
                results.add(current.data);
            }

            current = current.next;
        }

        return results;
    }

    @Override
    public List<T> binarySearch(Function<T, String> extractor,
                                String term,
                                Comparator<T> comparator)
    {
        // First, sort the linked list in place:
        mergeSort(comparator);

        // Now retrieve the sorted data:
        List<T> list = getAll();

        int left = 0, right = list.size() - 1;
        while (left <= right)
        {
            int mid = (left + right) / 2;
            int cmp = term.compareToIgnoreCase(
                    extractor.apply(list.get(mid))
            );

            if (cmp == 0)
            {
                // collect all equal items around mid
                List<T> results = new ArrayList<>();

                int i = mid;
                while (i >= 0 && term.equalsIgnoreCase(
                        extractor.apply(list.get(i))))
                {
                    results.add(list.get(i--));
                }

                i = mid + 1;
                while (i < list.size() && term.equalsIgnoreCase(
                        extractor.apply(list.get(i))))
                {
                    results.add(list.get(i++));
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

        return Collections.emptyList();
    }
}