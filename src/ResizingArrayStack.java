// ResizingArrayStack.java: wenlong
// Desciption: grow and shrink array, don't require client to provide capacity
// This class gives push, pop, peel and size methods
//
// Notice: too expensive for push and pop
//         * need to copy all item to a new array
//         * inserting first N items takes time proportional to 1+2+ ..+N ~ N2/2
// So, ensure that array resizing happens infrequently
//
// Performance analysis:
//         *Starting from an empty stack, any sequence of N push and pop operations
//          take time proportional to N
//         *with linked-list implementation, each operation takes constant time in the worst time
//          Also, use extra time and space (reference) to deal with the links.
//
//          with resizing-array implentatin, each operation takes constant amortized time and less wasted space;
//          probably faster implementation of each operation.   
//
// Test:
//  >more tobe.txt
//  to be or not to - be - - that - - - is
//  >java ResizingArrayStack <tobe.txt
//  to be not that or be (2 left on stack)
//
//import java.util.Iterator;
import java.util.NoSuchElementException;

public class ResizingArrayStack<Item> {
    private Item[] s; //array of items
    private int N;        // number of elements on stack

    // create an empty stack
    public ResizingArrayStack(){  //constructor
        // make an Object array with a length of 2
        s = (Item[]) new Object[2]; 
    }

    public boolean isEmpty()
    {
        return N == 0;
        
    }
    public int size()
    {
        return N;
    }

    //cost of inserting first N items
    //N + (2 + 4 + 8 + ... + N) ~ 3N
    private void resize(int capacity){
        assert capacity >= N;
        Item[] temp = (Item[]) new Object[capacity];
        
        for (int i=0; i<N; i++){
             temp[i] = s[i];
        }
        s = temp;
    }
    
    // repeated doubling
    // if array is full, create a new array of twice the size, and copy items
    public void push(Item item){
        if(N == s.length)
            resize(2 * s.length);
                
        s[N++] = item;   // increment top and insert item
    }
    
    public Item pop() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        
        Item item = s[--N]; // access the item and decrement top
        s[N] = null;    // avoid loitering
        //if we halve size of array when array is one half,
        //like push-pop-push-pop, proportion to N, so,
        //halve size of array when array is one-quarter full, in this way,
        //resizing doesn't happen that often
        if(N>0 && N == s.length/4)
            resize(s.length/2);

        return item;
    }

    // Test
    public static void main(String[] args)
    {
        ResizingArrayStack<String> s = new ResizingArrayStack<String>();
        while(!StdIn.isEmpty()){
            String item = StdIn.readString();
            if(!item.equals("-"))
                s.push(item);
            else if(!s.isEmpty())
                StdOut.print(s.pop() + " ");
        }
        StdOut.println("(" + s.size() + " left on stack)");
        
    }
    
}  // end class stackx
