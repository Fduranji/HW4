/*******************************************************
 * BinaryTree.java
 * Homewokr #4 Part 2, this program implements self 
 *    balancing binary search tree. 
 *
 * Program uses the file "AreaCodes.txt" whihc is included
 *    in the same folder
 * It includes an insert(), search() and delete()
 *
 * For insert() and delete() the balancing is taken care
 *    of in their respected methods.  
 *
 * Part 3 of the Homework is also included, down below it
 *    starts on line 490
 *
 * The search and delete methods take in strind data in the
 *    form of DMA,REGION,STATE for the second part of HW
 *
 * The third part of homework, the string data is in the form
 *    of REGION,STATE,DMA
 *
 * Extra credit is included at the end of the program.
 *******************************************************/
 
 import java.util.Scanner;
 import java.io.*;
 
 /* Class Node */
 class Node 
 {  
     Node left, right;
     String data;
     int height;
 
     /* Constructor */
     public Node() 
     {
         left = null;
         right = null;
         height = 0;
     }

     /* Constructor */
     public Node(String data) 
     {
         left = null;
         right = null;
         this.data = data;
         height = 0;
     }     
 }
 
 /* Class BalanceSearchTree */
 class BalanceSearchTree 
 {
     private Node root;   
     private int count = 0;  
 
     /* Constructor */
     public BalanceSearchTree() 
     {
         root = null;
     }
 
     /* Function to check if tree is empty */
     public boolean isEmpty() 
     {
         return root == null;
     }
 
     /* Make the tree logically empty */
     public void clear() 
     {
         root = null;
     }

     /* Function to insert data */
     public void insert(String data) 
     {
         root = insert(data, root);
     }

     /* Function to get height of node */
     private int height(Node t ) 
     {
         return t == null ? -1 : t.height;
     }

     /* Function to max of left/right node */
     private int max(int lhs, int rhs) 
     {
         return lhs > rhs ? lhs : rhs;
     }

     /* Function to insert data recursively */
     private Node insert(String x, Node t) 
     {
         if (t == null)  t = new Node(x);
         else if (x.compareTo(t.data) < 0) 
         {
             t.left = insert( x, t.left );
             if (height( t.left ) - height( t.right ) == 2)
                 if (x.compareTo(t.left.data) < 0)
                 {
                     //count++;
                     t = rotateWithLeftChild( t );
                     
                 }
                 else
                 {
                     //count++;
                     t = doubleWithLeftChild( t );
                 }    
                     
         }
         else if (x.compareTo(t.data) > 0) 
         {
             t.right = insert( x, t.right );
             if (height( t.right ) - height( t.left ) == 2)
                 if (x.compareTo(t.right.data) > 0)
                 {
                     //count++;
                     t = rotateWithRightChild( t );
                 }
                 else
                 {
                     //count++;
                     t = doubleWithRightChild( t );
                 }
         }
         else
           ;  // Duplicate; do nothing

         t.height = max( height( t.left ), height( t.right ) ) + 1;
         return t;
     }
     
     private Node minValueNode(Node node)
     {   
         Node current = node;
  
         /* loop down to find the leftmost leaf */
         while (current.left != null)
            current = current.left;
  
         return current;
     }
 
     // Get Balance factor of node N
     private int getBalance(Node N)
     {
         if (N == null)
             return 0;
         return height(N.left) - height(N.right);
    }    
    
    public void delete(String data)
    { 
       root = delete(data, root);
    }
    
    private Node delete(String data, Node x)
    {
       // STEP 1: PERFORM STANDARD BST DELETE
       if (x == null)
          return x;
    
       // If the key to be deleted is smaller than
       // the root's key, then it lies in left subtree
       if (data.compareTo(x.data) < 0)
          x.left = delete(data, x.left);
    
       // If the key to be deleted is greater than the
       // root's key, then it lies in right subtree
       else if (data.compareTo(x.data) > 0)
          x.right = delete(data, x.right);
    
       // if key is same as root's key, then this is the node
       // to be deleted
       else
       {
       // node with only one child or no child
          if ((x.left == null) || (x.right == null))
          {
                   Node temp = null;
                   if (temp == x.left)
                       temp = x.right;
                   else
                       temp = x.left;
    
                   // No child case
                   if (temp == null)
                   {
                       temp = x;
                       x = null;
                   }
                   else   // One child case
                       x = temp; // Copy the contents of
                                    // the non-empty child
           }
           else
           {
                   // node with two children: Get the inorder
                   // successor (smallest in the right subtree)
                   Node temp = minValueNode(x.right);
    
                   // Copy the inorder successor's data to this node
                   x.data = temp.data;
    
                   // Delete the inorder successor
                   x.right = delete(temp.data, x.right);
           }
       }
    
       // If the tree had only one node then return
       if (x == null)
          return x;
    
       // STEP 2: UPDATE HEIGHT OF THE CURRENT NODE
       x.height = max(height(x.left), height(x.right)) + 1;
    
       // STEP 3: GET THE BALANCE FACTOR OF THIS NODE (to check whether
       //  this node became unbalanced)
       int balance = getBalance(x);
    
       // If this node becomes unbalanced, then there are 4 cases
       // Left Left Case
       if (balance > 1 && getBalance(x.left) >= 0)
          return rotateWithRightChild(x);
    
       // Left Right Case
       if (balance > 1 && getBalance(x.left) < 0)
       {
          x.left = rotateWithLeftChild(x.left);
          return rotateWithRightChild(x);
       }
    
       // Right Right Case
       if (balance < -1 && getBalance(x.right) <= 0)
          return rotateWithLeftChild(x);
    
       // Right Left Case
       if (balance < -1 && getBalance(x.right) > 0)
       {
          x.right = rotateWithRightChild(x.right);
          return rotateWithLeftChild(x);
       }
    
       return x;
    }//delete


     /* Rotate binary tree node with left child */     
     private Node rotateWithLeftChild(Node k2) 
     {
         count++;
         Node k1 = k2.left;
         k2.left = k1.right;
         k1.right = k2;
         k2.height = max( height( k2.left ), height( k2.right ) ) + 1;
         k1.height = max( height( k1.left ), k2.height ) + 1;
         return k1;
     }
 
     /* Rotate binary tree node with right child */
     private Node rotateWithRightChild(Node k1) 
     {
         count++;
         Node k2 = k1.right;
         k1.right = k2.left;
         k2.left = k1;
         k1.height = max( height( k1.left ), height( k1.right ) ) + 1;
         k2.height = max( height( k2.right ), k1.height ) + 1;
         return k2;
     }

     /**
      * Double rotate binary tree node: first left child
      * with its right child; then node k3 with new left child */
     private Node doubleWithLeftChild(Node k3) 
     {
         count++;
         k3.left = rotateWithRightChild( k3.left );
         return rotateWithLeftChild( k3 );
     }

     /**
      * Double rotate binary tree node: first right child
      * with its left child; then node k1 with new right child */      
     private Node doubleWithRightChild(Node k1) 
     {
         count++;
         k1.right = rotateWithLeftChild( k1.right );
         return rotateWithRightChild( k1 );
     }  
  
     /* Functions to count number of nodes */
     public int countNodes() 
     {
         return countNodes(root);
     }

     private int countNodes(Node r) 
     {
         if (r == null)  return 0;
         else 
         {
             int l = 1;
             l += countNodes(r.left);
             l += countNodes(r.right);
             return l;
         }
     }
     
     public int countBalance()
     {
         return countBalance(root);
     }
     
     private int countBalance(Node x)
     {
         return count;    
     }

     /* Functions to search for an element */
     public boolean search(String val) {
         return search(val, root);
     }
     
   private boolean search(String data, Node x)
   {
      boolean found = false;
      
      if(x == null)
      {
         //System.out.println("Sorry, tree is empty, root Node = null");
         return found;
      }
      while ((x != null) && !found)
      {
         String foundData = x.data;
         if(data.compareTo(foundData) < 0) 
         {
            x = x.left;
         }
         else if( data.compareTo(foundData) > 0)
         {
            x = x.right;
         }
         else
         {
            found = true;
            break;
         }
         found = search(data, x);
      }
      return found;
   }     

     /* Function for inorder traversal */
     public void inorder() 
     {
         inorder(root);
     }

     private void inorder(Node r) 
     {
         if (r != null) 
         {
             inorder(r.left);
             System.out.println(r.data +" ");
             inorder(r.right);
         }
     }

     /* Function for preorder traversal */
     public void preorder() 
     {
         preorder(root);
     }

     private void preorder(Node r) 
     {
         if (r != null) 
         {
             System.out.println(r.data +" ");
             preorder(r.left);             
             preorder(r.right);
         }
     }

     /* Function for postorder traversal */
     public void postorder() 
     {
         postorder(root);
     }

     private void postorder(Node r) 
     {
         if (r != null) 
         {
             postorder(r.left);             
             postorder(r.right);
             System.out.println(r.data +" ");
         }
     }
 }
 
 /* Class BalanceSearchTreeTest */
public class BinaryTree 
{
    public static void main(String[] args)throws IOException
    {            
        
      int insertCount = 0;  
      
      Scanner keyboard = new Scanner(System.in);
      //System.out.print("Enter filename: ");
      String filename = "AreaCodes.txt"; //keyboard.nextLine();
      
      File file = new File(filename);
      
      //Delimiter used for commas and new line
      Scanner readFile = new Scanner(file).useDelimiter("[,\n]");
   
      /* Creating object of BalanceSearchTree */
      BalanceSearchTree dmaTree = new BalanceSearchTree();
      
      String number = "";
      String city = "";
      String state = "";
      
      long startTime = System.nanoTime();
      
      String text = "";
      while(readFile.hasNext())
      {
         number = readFile.next();
         city = readFile.next();
         state = readFile.next();
         text = number + "," + city + "," + state;
         text = text.replace("\n", "").replace("\r", "");
         dmaTree.insert(text);       
         insertCount++;
      } 
      
      long endTime = System.nanoTime();
      
      double duration = (endTime - startTime) / 1000000.0;
    
      System.out.println("After data is inserted time is: " + duration + " milliseconds");
      
      System.out.println("Number of Nodes inserted: " + insertCount);
      
      System.out.println("Number of times the tree was balanced: " + dmaTree.countBalance());

      System.out.println();
      
      String search = "500,Portland-Auburn,ME";
      
      System.out.println("Searching for '500,Portland-Auburn,ME'");
      if(dmaTree.search("500,Portland-Auburn,ME"))
      {
         System.out.println("Found\n");
      }
      else
      {
         System.out.println("Not Found\n");
      }
      
      System.out.print("Pre order : \n");
      dmaTree.preorder();
      
      System.out.println();
      
      System.out.println("Deleting Node '507,Savannah,GA'");
      dmaTree.delete("507,Savannah,GA");
      
      System.out.println("Searching for '507,Savannah,GA'");
      if(dmaTree.search("507,Savannah,GA"))
      {
         System.out.println("Found\n");
      }
      else
      {
         System.out.println("Not Found\n");
      }      

      System.out.print("Pre order after deleting : \n");
      dmaTree.preorder();
      
      readFile.close();
      
      System.out.println();
      
      /**
         Region is now the Key
         Part 3 of Homework
      */
      
      System.out.println(">>Region is used as the key here<<\n");      
      
      int count = 0;
      
      Scanner board = new Scanner(System.in);
      
      String nameOfFile = "AreaCodes.txt";
      
      File regionFile = new File(nameOfFile);
      
      Scanner rdFile = new Scanner(regionFile).useDelimiter("[,\n]");
      
      BalanceSearchTree regionTree = new BalanceSearchTree();
      
      String dma = "";
      String region = "";
      String st = "";
      
      long start = System.nanoTime();
      
      String combine = "";
      while(rdFile.hasNext())
      {
         dma = rdFile.next();
         region = rdFile.next();
         st = rdFile.next();
         combine = region + "," + st + "," + dma;
         combine = combine.replace("\n", "").replace("\r", "");
         regionTree.insert(combine);
         count++;      
      }
      
      long end = System.nanoTime();
      
      double regionDuration = (end - start) / 1000000.0;
    
      System.out.println("After data is inserted time is: " + regionDuration + " milliseconds");
      
      System.out.println("Number of Nodes inserted: " + count);
      
      System.out.println("Number of times the tree was balanced: " + regionTree.countBalance());

      System.out.println(); 
           
      search = "Cleveland,OH,510";
      
      System.out.println("Searching for 'Cleveland,OH,510'");
      if(regionTree.search("Cleveland,OH,510"))
      {
         System.out.println("Found\n");
      }
      else
      {
         System.out.println("Not Found\n");
      }
      
      System.out.print("Pre order : \n");
      regionTree.preorder();
      
      System.out.println();
      
      System.out.println("Deleting Node 'Cleveland,OH,510'");
      regionTree.delete("Cleveland,OH,510");
      
      System.out.println("Searching for 'Cleveland,OH,510'");
      if(regionTree.search("Cleveland,OH,510"))
      {
         System.out.println("Found\n");
      }
      else
      {
         System.out.println("Not Found\n");
      }      

      System.out.print("Pre order after deleting : \n");
      regionTree.preorder();
      
      readFile.close();    
      
      System.out.println();
      
      /**
         EXTRA CREDIT
      */
      
      System.out.println(">>BOTH LISTS IN IN ORDER<<");
      
      System.out.print(">>DMA In order : \n");
      dmaTree.inorder();
      System.out.println();
      
      System.out.print(">>REGION In order : \n");
      regionTree.inorder();  
    }
}