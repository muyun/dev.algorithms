//BST.java: wenlong
//Description:* A BST(binary search tree) is a binary tree where each node has a Comparable key (and the value)
//             and satisfies the tree restriction
//            * combines the flexibility of insertion in a linked list with the efficiency of search in an ordered array
//
//Performance:
//            * all operations is h (height of BST, proportional to logN if keys inserted in random order);
//            * but the order iteration is N
//            *
//            * TODO:(done) Insertions and serch in a BST built from N random keys require ~2lnN compares on the average
//
//            * BST search cost for random keys to be about 39% higher than that for binary search, the extra cost is well worthwhile,
//            * the cost of inserting a new key is expected to be logarithmic
//
//-----------------------------------------------
package base;

import java.util.NoSuchElementException;
//import java.util.Queue;


//assume that Key is an Object because we use it to invoke compareTo() or equals()
public class BST<Key extends Comparable<Key>, Value>
{
    private Node root;  // root of BST

    private class Node {
        private Key key;   // sorted by key
        private Value val; //
        private Node left;  // the left link points to a BST for items with smaller keys
        private Node right;  // the right link points to a BST for items with larger keys
        private int count;  //number of nodes in the subtree rooted at the node

        public Node(Key key, Value val, int N)
        {
            this.key = key;
            this.val = val;
            this.count = N;
        }
        
    }

    public int size()
    {
        // return N;  // N is defined as a element of Node
        return size(root);
    }

    private int size(Node x)
    {
        if(x == null) return 0;
        else return x.count;
    }

    public boolean isEmpty()
    {
        return size() == 0;
    }

    //return value associated with the given key, or null if no such key exists
    public Value get(Key key)
    {
        return get(root, key);
    }

    private Value get(Node x, Key key)
    {
        if(x == null) return null;
        int cmp = key.compareTo(x.key);
        if(cmp < 0)
            return get(x.left, key);
        else if (cmp > 0)
            return get(x.right, key);
        else
            return x.val;
        
    }
                
    //insert key-value pair into BST, if key already exists, update with new value
    public void put(Key key, Value val)
    {
        //the node at the root of the tree corresponds to the first partitioning item in quicksort;
        // no keys to the left are larger, and no keys to the right are smaller and the subtrees are built recursively
        root = put(root, key, val);  //the root corresponds to the first partitioning item in quicksort
    }

    //the subtrees are built recursively, corresponding to quicksort's recursive subarray sorts
    private Node put(Node x, Key key, Value val)
    {
        if(x == null)
            return new Node(key, val, 1);  // return the ref to a new node, its left and right is null
        
        int cmp = key.compareTo(x.key);
        if(cmp < 0)
            x.left = put(x.left, key, val);  // x.left is null, after new Node, put this ref to the new node into x.left
        else if (cmp > 0)
            x.right = put(x.right, key, val);
        else  //cmp == 0
            x.val = val;  //reset the value
        
        x.count = size(x.left) + size(x.right) + 1;

        return x; //return the root
    }

    public Key min(){
        return min(root).key;
    }

    private Node min(Node x){
        if(x.left == null)
            return x;

        return min(x.left);
    }
    
    //floor of key is the largest key in the BST less than or equal to key
    public Key floor(Key key){
        Node x = floor(root, key);
        if(x == null)
            return null;

        return x.key;
    }

    private Node floor(Node x, Key key){
        if(x == null)
            return null;
        
        int cmp = key.compareTo(x.key);
        /*if(cmp == 0)
            return x;
        */
        if(cmp < 0)
            return floor(x.left, key);
        if(cmp > 0)
            return floor(x.right, key);
        
        return x; // cmp == 0;
    }

    //ceiling of key is the smallest key in the BST greater than or equal to key
   /*
    public int size(){
        return size(root);
    }

    private int size(Node x){
        if(x == null)
            return 0;
        return x.count;
    }
    */
    //rank: how many keys < k ?
    public int rank(Key key){
        return rank(key, root);
    }

    private int rank(Key key, Node x){
        //return num of keys less than x.key in the subtree rooted at x
        if (x == null)
            return 0;
        int cmp = key.compareTo(x.key);
        if(cmp < 0)
            return rank(key, x.left); // the nodes on the left is less than the Node
        else if(cmp > 0)
            return 1 + size(x.left) + rank(key, x.right);
        /*
        else if(cmp == 0)
            return size(x.left);
        */
        return size(x.left); //cmp == 0
    }
    
    //Iteration operation, it base on in-order traversal
    public Iterable<Key> keys(){
        Queue<Key> q = new Queue<Key>();
        inorder(root, q);
        return q;
    }

    //Inorder traversal of a BST yields keys in ascending order
    private void inorder(Node x, Queue<Key>  q){
        if(x == null)
            return;
        //put all the keys in the data structure on the queue in their natural order
        inorder(x.left, q); //traverse left subtree
        q.insert(x.key); //enqueue the key
        inorder(x.right, q);  //traverse right subtreex
    }

    //delete the min key
    public void deleteMin(){
        root = deleteMin(root);
    }
    private Node deleteMin(Node x){
        //* go left until finding a node with a null left link
        //* replace that node by its right link
        //* update subtree counts
        if(x.left == null)
            return x.right;
        
        x.left = deleteMin(x.left);
        x.count = 1 + size(x.left) + size(x.right);
        
        return x;
    }

    //Hibbard deletion
    //disadvantage: not symmetric, the tree is becoming much less balanced than it was
    //
    public void delete(Key key){
        root = delete(root, key);
    }

    private Node delete(Node x, Key key){
        if(x == null)
            return null;
        int cmp = key.compareTo(x.key);
        //serarch the key
        if (cmp < 0)
            x.left = delete(x.left, key);
        else if (cmp > 0)
            x.right = delete(x.right, key);
        else{
            //no right child
            // case 0, no child
            // case 1, only one child
            if(x.right == null)
                return x.left;
            
            // have a right child
            //case 2; have two children
            Node t = x;
            x = min(t.right); //find the min on the right
            x.right = deleteMin(t.right); //delete the min on the right and replace with successor
            x.left = t.left; //fix the links
        }

        x.count = size(x.left) + size(x.right) + 1;

        return x;
        
    }
        
}
