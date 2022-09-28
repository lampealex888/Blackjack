/**
* The RandIndexQueue class manages a queue
* that wraps around itself and dynamically 
* increases in capacity as necessary.
*
* @author  Alex Lampe
* @since   1-4-2022 
*/
import java.util.Random;

public class RandIndexQueue<T> implements MyQ<T>, Indexable<T>, Shufflable {
    private T [] queue;
    private int size;
    private int moves = 0;
    private int frontIndex = -1;
    private int backIndex = -1;

    public RandIndexQueue(int initSize) {
        queue = (T []) new Object[initSize];
        size = 0;
    }

    public RandIndexQueue(RandIndexQueue<T> old)
    {
        queue = (T []) new Object[old.capacity()];
        size = 0;
        backIndex = -1;
        for (int i = 0; i < old.capacity(); i++)
        {
            queue[i] = old.get(i);
            backIndex++;
            size++;
        }
        frontIndex = 0;
    }

    public void enqueue(T newEntry)
    {
        // Check if the queue is full
        if (size == queue.length)
        {
            T [] newqueue = (T []) new Object[queue.length*2];
            backIndex = -1;
            for (int i = 0; i < queue.length; i++)
            {
                newqueue[i] = queue[(frontIndex+i) % (queue.length)];
                backIndex++;
            }
            frontIndex = 0;
            queue = newqueue;
        }
        // Check if the queue is empty
        if (frontIndex == -1) {
            frontIndex++;
        }
        // Check if the backIndex is at the back of the array
        if (backIndex == queue.length - 1) {
            backIndex = 0;
        } else {
            backIndex++;
        }
        queue[backIndex] = newEntry;
        moves++;
        size++;
    }
    public T dequeue()
    {
        // Check if the queue is empty
        if (size > 0)
        {
            T elem;
            // Check if the frontIndex is at the back of the array
            if (frontIndex == queue.length - 1) {
                elem = queue[frontIndex];
                queue[frontIndex] = null;
                frontIndex = 0;
            } else {
                elem = queue[frontIndex];
                queue[frontIndex] = null;
                frontIndex++;
            }
            size--;
            moves++;
            return elem;
        }
        else
            throw new EmptyQueueException("The queue is empty");
    }
  
    public T getFront()
    {
        if (size > 0)
            return queue[frontIndex];
        else
            throw new EmptyQueueException("The queue is empty!");
    }
  
    public boolean isEmpty()
    {
        return size == 0;
    }
  
    public void clear()
    {
        queue = (T []) new Object[queue.length];
        size = 0;
        frontIndex = -1;
        backIndex = -1;
    }

    public int size()
    {
        return size;
    }
	public int capacity()
    {
        return queue.length;
    }

	public int getMoves()
    {
        return moves;
    }

	public void setMoves(int val)
    {
        moves = val;
    }

    public T get(int i)
    {
        return queue[(frontIndex+i) % queue.length];
    }

	public void set(int i, T item)
    {
        queue[(frontIndex+i) % queue.length] = item;
    }
	
    public void shuffle()
    {
        // Adopted the random method by Sumit Ghosh from geeksforgeeks.org 
        // https://www.geeksforgeeks.org/shuffle-a-given-array-using-fisher-yates-shuffle-algorithm/
        Random random = new Random();
        for (int i = size - 1; i > 0; i--)
        {
            int j = (random.nextInt(i+1) + frontIndex) % queue.length;
            
            T temp = queue[(frontIndex+i) % queue.length];
            queue[(frontIndex+i) % queue.length] = queue[j];
            queue[j] = temp;
        }
    }

    public boolean equals(RandIndexQueue<T> rhs)
    {
        //T [] newqueue = (T []) new Object[size];
        //System.arraycopy((T []) rhs, 0, newqueue, 0, rhs.size);
        //return Arrays.equals(queue, newqueue);
        for (int i = 0; i < rhs.size(); i++)
        {
            if (rhs.get(i) != queue[(frontIndex+i) % queue.length]) return false;
        }
        return true;
    }

    public String toString() {
        String txt = "";
        for (int i = 0; i < size; i++) {
            if (queue[(frontIndex+i) % queue.length] != null) txt = txt + queue[(frontIndex+i) % queue.length] + " ";
        }
        return txt;
    }
}