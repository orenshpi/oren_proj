package hw1;
/**
 * public class AVLNode
 * <p>
 * This class represents an AVLTree with integer keys and boolean values.
 * <p>
 * IMPORTANT: do not change the signatures of any function (i.e. access modifiers, return type, function name and
 * arguments. Changing these would break the automatic tester, and would result in worse grade.
 * <p>
 * However, you are allowed (and required) to implement the given functions, and can add functions of your own
 * according to your needs.
 * 
 */

public class AVLTree {
	AVLNode root;
	AVLNode min;
	AVLNode max;
	int size=0;
	int cnt_balance_delete=0;

    /**
     * This constructor creates an empty AVLTree. 
     * complexity=O(1)
     */
    public AVLTree(){
    	this.root=null;
        
    }

    /**
     * public boolean empty()
     * <p>
     * returns true if and only if the tree is empty, 
     * complexity=O(1)
     */ 
    
    public boolean empty() { 
        if (this.root==null){
        	return true;
        	
        }
        return false;
    }

    /**
     * public boolean search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     * complexity O(log(n))
     */
    public Boolean search(int k) {
        AVLNode Node=root;
        while (Node!=null){
        	if (k==Node.getKey()) {
        		return Node.getValue();
        		
        	}
        	if (k<Node.key) {
        		Node=Node.getLeft();
        	}
        	else {
        		Node=Node.getRight();
        	}
        }
        return null;
    }

    /**
     * public int insert(int k, boolean i)
     * <p>
     * inserts an item with key k and info i to the AVL tree.
     * the tree must remain valid (keep its invariants).
	 * returns the number of nodes which require rebalancing operations (i.e. promotions or rotations).
	 * This always includes the newly-created node.
     * returns -1 if an item with key k already exists in the tree.
     * complexity O(log(n))
     */
    public int insert(int k, boolean i) {
    	AVLNode Node=new AVLNode(k,i);  
    	Node.setLeft(Node.VirtualAVLNode());
    	Node.setRight(Node.VirtualAVLNode());
    	Node.xor=Node.info;
    	this.size+=1;
    	if(this.size()==1){
    		this.root=Node;
    		this.max=Node;
    		this.min=Node;
    		return 1;
    	}
    	
    	
    	if(search(k)!=null) {
    		return -1;
    	}
    	
    	AVLNode Node_to_compare=this.root;
    	while(Node_to_compare.getValue()!=null) { // searching where to insert Node
    		if(Node.getKey()<Node_to_compare.getKey()) {
    			if(!Node_to_compare.left.isRealNode()) {
    				break;
    			}
    			Node_to_compare=Node_to_compare.left;
    		}
    		else{
    			if(!Node_to_compare.right.isRealNode()) {
        			break;
    			}
    			Node_to_compare=Node_to_compare.right;
    			
    			
    		}
    	}
    	Node.setParent(Node_to_compare); //inserting Node as a leaf
    	if(Node.parent.key>Node.key) {
    		Node.parent.setLeft(Node);
    			
    		}
    		else {
    			Node.parent.setRight(Node);
    			
    			
    		}
        
        
        
    	set_succ_and_pred_insert(Node); //updating successor and predecessor
        int cnt1=update_heights_and_BF(Node); //updating heights and BF 
        
        AVLNode aid_node= Node;
        int cnt=1;
        while(aid_node!=null  && Math.abs(aid_node.BF)<2) { //getting to the first node with BF 2,-2 
        	cnt++;
        	aid_node=aid_node.getParent();
        }
        if(aid_node!=null) {
        	rotations(aid_node);
        	update_heights_and_BF(aid_node);
        	update_xor_for_ancestors(Node);
        
        	return cnt;
        }
        update_xor_for_ancestors(Node);
        return cnt1;
        
       
    }
    
  // complexity O(logn)
  //updating all of Node's ancestor's xor in case Node's info is true
        void update_xor_for_ancestors(AVLNode Node) {  
        	
        	AVLNode ancestor=Node.parent;
        	while(ancestor!=null) {
        		update_xor_for_Node(ancestor);
        		
        		ancestor=ancestor.parent;
        	}
        		
        	}
        		
        		
        	
        	
        	
      // complexity : O(1)
      //updating Node's xor;
        
        void update_xor_for_Node(AVLNode Node) { 	
        	
        int count=0;
        if(!Node.is_leaf()) {
        
       	
       	if(Node.left.isRealNode() ) {
       		if(Node.left.xor==true) {
       		count++;
       		}
       		if(Node.left.info==true && (!Node.left.is_leaf())) {
       			count++;
       		}
       		
       	}
       	if(Node.right.isRealNode()) {
       		if(Node.right.xor==true) {
       		count++;
       		}
       		if(Node.right.info==true && (!Node.right.is_leaf())) {
       			count++;
       		}
       		
       	}
        	
        if(count%2==1) {
        	Node.xor=true;
        }
        else {
        	Node.xor=false;
        }
        }
        else {
        	Node.xor=Node.info;
        }
        }
        
        
        
    
    // complexity O(logn)
    // set successor_and_predecessor for a new Node in the tree;
    
   void set_succ_and_pred_insert(AVLNode Node){

       if(this.min.key>Node.key) { //checking if Node is the new minimum
       	Node.successor=min;
       	min.predecessor=Node;
       	this.min=Node;
       	
       	
       }
       
       else if(this.max.key<Node.key) { //checking if Node is the new maximum
       	Node.predecessor=max;
       	max.successor=Node;
       	this.max=Node;
       	
       }
       else { // Node is not the new minimum or maximum
    	 AVLNode ancestor=Node.getParent();
    	 while(ancestor.key<Node.key) {
    		 ancestor=ancestor.getParent();
    		 
    	 }
    	 Node.successor=ancestor;
    	 Node.predecessor=ancestor.predecessor;
    	 Node.predecessor.successor=Node;
    	 ancestor.predecessor=Node;
    	 
    	 
       }
       
       
    	   
    	   
    	   
       }
   
   // complexity O(1)
   // receiving a node with BF 2,-2, checking what rotations are needed and performing them
   
   void rotations(AVLNode Node){
	   if(Node.BF==-2) {
		   if(Node.right.BF==-1 || Node.right.BF==0) {
			   left_rotation(Node);
		   }
		   else { //Node.right.BF==1
			   right_rotation(Node.right);
			   left_rotation(Node);
			   
		   }
	   }
		   else {  //Node.BF==2
			   if(Node.left.BF==1 || Node.left.BF==0) {
				   right_rotation(Node);
			   
		   }
			   else { //Node.right.BF==-1
				   left_rotation(Node.left);
				   right_rotation(Node);
			   }
			   
	   }
	   }
   
  
   
	   
   
   
   
       
// complexity O(logn)       
// returns the number of Nodes that changed height during insertion
// update all heights and Balance Factors of ancestors of Node during insertion of Node 
   int update_heights_and_BF(AVLNode Node) {
	   int cnt=1;
	   AVLNode ancestor=Node.parent;
	   int prev_height=ancestor.getHeight();
	   if(ancestor.getKey()<Node.getKey()) {
		   ancestor.setHeight(Math.max(ancestor.left.getHeight(), ancestor.getRight().getHeight()+1));
		   
	   }
	   else{
		   ancestor.setHeight(Math.max(ancestor.getLeft().getHeight()+1, ancestor.right.getHeight()));
		   
		   
	   }
	   if(prev_height!=ancestor.height) { // if ancestor changed his height
		   cnt++;
	   }
	   ancestor.BF=ancestor.left.getHeight()-ancestor.right.getHeight();
	   while(ancestor!=this.root ) { 
		 
		ancestor=ancestor.getParent();
		prev_height=ancestor.getHeight();
		ancestor.setHeight(Math.max(ancestor.left.getHeight(),ancestor.right.getHeight())+1);
		 if(prev_height!=ancestor.height) { // if ancestor changed his height
			   cnt++;
		 }
		ancestor.BF=ancestor.left.getHeight()-ancestor.right.getHeight();  
		
	   }
	   AVLNode parent=Node.parent;
	   if(parent.left.isRealNode()) {
	   parent.left.BF=parent.left.left.height-parent.left.right.height;
	   }
	   if(parent.right.isRealNode()){
	   parent.right.BF=parent.right.left.height-parent.right.right.height;
	   
	   } 
	   return cnt;
	    
    }
    
    //complexity O(1)
    //right rotation - receiving the first Node with BF=2,-2 after insertion when right rotation is needed
   
    void right_rotation(AVLNode Node) {
    	AVLNode left_Node=Node.left; 
    	Node.setLeft(left_Node.right);
    	left_Node.right.setParent(Node);
    		
    	
    	AVLNode Node_parent=Node.getParent(); 
    	Node.setParent(left_Node);
    	left_Node.setRight(Node);
    	
    	if(this.root==Node) {
    		this.root=left_Node;
    		left_Node.setParent(null);
    	}
    	else {
    	
    		if(left_Node.getKey()<Node_parent.getKey()) { //if Node was his parent's left son
    		Node_parent.setLeft(left_Node);
    	}
    	else { //if Node was his parent's right son
    	Node_parent.setRight(left_Node);	
    	
    	}
    	left_Node.setParent(Node_parent);
    	}
    
    	Node.height=Math.max(Node.right.height, Node.left.height)+1;
    	left_Node.height=Math.max(left_Node.left.height,Node.height)+1;
    	Node.BF=Node.left.height-Node.right.height;
    	update_xor_for_Node(Node);
    	update_xor_for_Node(Node.parent);
    	
    	
    }
   // complexity O(1) 
  //left rotation - receiving the first Node with BF=2,-2 after insertion when left rotation is needed
    void left_rotation(AVLNode Node) {
    	AVLNode right_Node=Node.right; 
    	Node.setRight(right_Node.left);
    	right_Node.left.setParent(Node);
    		
    	
    	AVLNode Node_parent=Node.getParent(); 
    	Node.setParent(right_Node);
    	right_Node.setLeft(Node);
    	
    	if(this.root==Node) {
    		this.root=right_Node;
    		right_Node.setParent(null);
    	}
    	else {
    	
    		if(right_Node.getKey()<Node_parent.getKey()) { //if Node was his parent's left son
    		Node_parent.setLeft(right_Node);
    	}
    	else { //if Node was his parent's right son
    	Node_parent.setRight(right_Node);	
    	
    	}
    	right_Node.setParent(Node_parent);
    	}
    	
    
    	Node.height=Math.max(Node.left.height, Node.right.height)+1;
    	right_Node.height=Math.max(right_Node.right.height,Node.height)+1;
    	Node.BF=Node.left.height-Node.right.height;
    	
    	update_xor_for_Node(Node);
    	update_xor_for_Node(Node.parent);
    	
    	
    }
    
    	
    /**
     * public int delete(int k)
     * <p>
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of nodes which required rebalancing operations (i.e. demotions or rotations).
     * returns -1 if an item with key k was not found in the tree.
     * time complexity: O(log(n))
     */
    public int delete(int k) {
    	cnt_balance_delete=0;
    	
       if(search(k)==null) {
    	   return -1;
       }
    	if(size==1) {
    		root=null;
    		min=null;
    		max=null;
    		size=0;
    		return 0;
    	}
    	AVLNode Node=find_Node(k);
    	set_succ_and_pred_delete(Node);
    	choose_delete(Node);
    	eliminate(Node);
    	size--;
    	
    	
    	
    	return cnt_balance_delete;
    	
       
        
    }
    
    // complexity O(logn)
    //update all heights and Balance Factors of ancestors of Node during deletion of Node 
    void update_heights_and_BF_2(AVLNode Node){ 
    	
    if(Node!=root) {
    	int prev_height_parent=Node.parent.height;
    	
    	if(Node.key<Node.parent.key) { //Node is his parent's left son
    		if(Node.left.isRealNode()) {
    			Node.parent.height=Math.max(Node.left.height, Node.parent.right.height)+1;
    			
    		}
    		else {
    			Node.parent.height=Math.max(Node.right.height, Node.parent.right.height)+1;
    		}
    	}
    	else { //Node is his parent's right son
    		if(Node.left.isRealNode()) {
    			Node.parent.height=Math.max(Node.left.height, Node.parent.left.height)+1;
    		}
    		else {
    			Node.parent.height=Math.max(Node.right.height, Node.parent.left.height)+1;
    		}
    	}
    	if(prev_height_parent!=Node.parent.height) {
			cnt_balance_delete++;
		}
    	
    	Node.parent.BF=Node.parent.left.height-Node.parent.right.height;
    	
    	if(Node.parent!=root){
    	AVLNode ancestor=Node.parent;
    	while(ancestor!=root) {
    		ancestor=ancestor.parent;
    		int prev_height=ancestor.height;
    		ancestor.height=Math.max(ancestor.right.height, ancestor.left.height)+1;
    		if(prev_height!=ancestor.height) {
    			cnt_balance_delete++;
    		}
    		ancestor.BF=ancestor.left.height-ancestor.right.height;
    		
    	}
    	
    }
    }
    }
    
    // complexity O(logn)
    // choose which delete is needed by checking Node's number of sons
    void choose_delete(AVLNode Node) { 
    	if(Node.is_leaf()) {
    		delete_0(Node);
    	}
    	else if(Node.left.isRealNode() && Node.right.isRealNode()) {
    		size++; // two objects are being deleted, and we want the size to decrease only by one
    		delete_2(Node);
    		
    	}
    	
    	else {
    		delete_1(Node);
    		
    	}
	
    }
    
    // complexity O(logn)
    // delete a Node with 0 sons
    void delete_0(AVLNode Node) { 
    	
    	if(Node.parent.key<Node.key) { // if Node is his parent's right son
    		Node.parent.setRight(Node.right);
    	}
    	else { // if Node is his parent's left son
    		Node.parent.setLeft(Node.left);
    	}
    	update_heights_and_BF_2(Node);
    	rotations_after_delete(Node);
    	
    }
    
    // complexity O(logn)
    //delete a Node with 1 sons
    void delete_1(AVLNode Node) { 
    	if(Node.key!=root.key) {
    	if(Node.parent.key>Node.key) { // if Node is his parent's left son
    		if(Node.left.isRealNode()) { //if Node has a left son 
    			Node.parent.setLeft(Node.left);
    		}
    		else { //if Node has a right son
    			Node.parent.setLeft(Node.right);
    			
    		}
    	}
    	else { //if Node is his parent's right son
    		if(Node.left.isRealNode()) { //if Node has a left son 
    			Node.parent.setRight(Node.left);
    			
    		}
    		else {//if Node has a right son
    			Node.parent.setRight(Node.right);
    		}
    	}
    	}
    		Node.left.setParent(Node.parent);
    		Node.right.setParent(Node.parent);
    		update_heights_and_BF_2(Node);
    		rotations_after_delete(Node);		
    }
    	
    // complexity O(logn)	
    // delete a Node with 2 sons
    void delete_2(AVLNode Node) { 
    	
    	AVLNode Node_succ=Node.successor;
    	Node.info=Node.successor.info;
    	delete(Node.successor.key);
    	if(Node.key!=root.key) { // if Node is not the root;
    	if(Node.key<Node.parent.key) {// if Node is his parent's left son
    		Node.parent.setLeft(Node_succ);
    	}
    	else { // if Node is his parent's right son
    		Node.parent.setRight(Node_succ);
    		
    	}
    	}
    	else { //Node is the root
    		root=Node.successor;
    	}
    	Node_succ.height=Node.height;
    	Node_succ.BF=Node.BF;
    	Node_succ.setParent(Node.parent);
    	Node_succ.setLeft(Node.left);
    	Node_succ.setRight(Node.right);
    	Node.left.setParent(Node_succ);
    	Node.right.setParent(Node_succ);
    	Node.successor.predecessor=Node_succ;
    	Node.predecessor.successor=Node.successor;
    	Node_succ.xor=Node.xor;
   
    }
    
    // complexity O(logn)
    // perform needed rotations from Node to the root and update heights, bf and xor accordingly
    void rotations_after_delete(AVLNode Node){ 
    	
    	AVLNode ancestor=Node;
    	
    	while(ancestor!=null) {
    		int prev_height=ancestor.height;
    		if(Math.abs(ancestor.BF)== 2) {
    			rotations(ancestor);
    		}
    		
    		
    		if(ancestor!=null) {
    			
    			ancestor.height=Math.max(ancestor.left.height, ancestor.right.height)+1;
    			if(prev_height!=ancestor.height) {
    				cnt_balance_delete++;
    			}
    			
    			ancestor.BF=ancestor.left.height-ancestor.right.height;
    			update_xor_for_Node(ancestor);
    		
    		if(ancestor.parent!=null) {
    			int prev_height2=ancestor.parent.height;
    			ancestor.parent.height=Math.max(ancestor.parent.left.height, ancestor.parent.right.height)+1;
    			if(prev_height2!=ancestor.parent.height) {
    				cnt_balance_delete++;
    			}
    			ancestor.parent.BF=ancestor.parent.left.height-ancestor.parent.right.height;
    			update_xor_for_Node(ancestor.parent);
    			}
    		}
    		ancestor=ancestor.parent;
    	}
    	
    
    	
    }
    
    // complexity O(1)
    // detach Node from the tree 
    void eliminate(AVLNode Node) { 
    	Node.parent=null;
    	Node.left=null;
    	Node.right=null;
    }
    	
    	
    	
    	
    	
    
    
    
    // complexity O(1)
    // update successor and predecessor during deletion
    void set_succ_and_pred_delete(AVLNode Node) {
    	if(min.key==Node.key) {
    		min=Node.successor;
    		Node.successor.predecessor=null;
    	
    	}
    	else if(max.key==Node.key) {
    		max=Node.predecessor;
    		Node.predecessor.successor=null;
   	
    	 }
    	else {
    		Node.successor.predecessor=Node.predecessor;
    		Node.predecessor.successor=Node.successor;
    	}
    	 
    	 
    	
    }
    
    
    
    
    
    
    	 
    	 
    	 
    	 
    	 
    /**
     * public Boolean min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     * time_complexity: O(1)
     */
    public Boolean min() {
    	if(empty()) {
    		return null;
    	}
        return min.getValue();
        }

    /**
     * public Boolean max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     * time_complexity: O(1)
     */
    public Boolean max() {
    	if(empty()) {
    		return null;
    	}
        return max.getValue();
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     * time_complexity: O(n)
     */
    public int[] keysToArray() {
        int[] arr = new int[this.size];
        AVLNode Node=min;
        int i=0;
        while (Node!=null) {
        	arr[i]=Node.getKey();
        	i++;
        	Node=Node.successor;
        }
        return arr;
        
    }
    
   

    /**
     * public boolean[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     * time_complexity: O(n)
     */
    public boolean[] infoToArray() {
    	 boolean[] arr = new boolean[this.size];
         AVLNode Node=min;
         int i=0;
         while (Node!=null) {
         	arr[i]=Node.getValue();
         	i++;
         	Node=Node.successor;
         }
         return arr;
    }
    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     * time_complexity: O(1)
     */
    public int size() {
        return size;
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     * complexity O(1)
     */
    public AVLNode getRoot() {
        return this.root;
    }

    /**
     * public boolean prefixXor(int k)
     *
     * Given an argument k which is a key in the tree, calculate the xor of the values of nodes whose keys are
     * smaller or equal to k.
     *
     * precondition: this.search(k) != null
     * time_complexity: O(log(n))
     *
     */
    public boolean prefixXor(int k){
    	AVLNode Node=find_Node(k);
    	int cnt=0;
    	while(Node!=null) {
    		if(Node.key<=k && xor_addition(Node)) {
    			cnt++;
    		}
    	
    		Node=Node.parent;	
    	}
    	
    	if(cnt%2==1) {
    		return true;
    		
    	}
    	return false;
        
    }
    
  // complexity O(1) 
  //check Node's and Node's left subtree's xor
    public boolean xor_addition(AVLNode Node) { 
    	if(!Node.left.isRealNode()) {
    		return Node.info;
    	}
    	int cnt=0;
    	
    	if(Node.info==true) {
    		cnt++;
    	}
    	if(Node.left.info==true) {
    		cnt++;
    	}
    	if(!Node.left.is_leaf()) {
    		if(Node.left.xor==true) {
    		    cnt++;
    		}
    	}
    	if(cnt%2==1) {
    		return true;
    	}
    	return false;
    	
    }
    
  // complexity O(logn)
  //find a Node with key k
    AVLNode find_Node(int k) { 
    	AVLNode Node=this.root;
    	while(Node.key!=k) {
    		if(Node.key<k) {
    			Node=Node.right;
    		}
    		else {
    			Node=Node.left;
    		}
    	}
    	return Node;
    	
    }

    /**
     * public AVLNode successor
     *
     * given a node 'node' in the tree, return the successor of 'node' in the tree (or null if successor doesn't exist)
     *
     * @param node - the node whose successor should be returned
     * @return the successor of 'node' if exists, null otherwise
     * complexity O(1)
     */
    public AVLNode successor(AVLNode node){
        return node.successor;
    }

    /**
     * public boolean succPrefixXor(int k)
     *
     * This function is identical to prefixXor(int k) in terms of input/output. However, the implementation of
     * succPrefixXor should be the following: starting from the minimum-key node, iteratively call successor until
     * you reach the node of key k. Return the xor of all visited nodes.
     *
     * precondition: this.search(k) != null
     * time_complexity: O(n)
     */
    public boolean succPrefixXor(int k){
    	AVLNode Node=this.min;
    	int cnt=0;
    	while(Node!=null && Node.key<=k) {
    		if(Node.getValue()==true) {
    			cnt++;
    		}
    		Node=Node.successor;
    		
    	}
    	if(cnt%2==0) {
    		return false;
    	}
    		
    	return true;
    	
    	
    	
    
    }


    /**
     * public class AVLNode
     * <p>
     * This class represents a node in the AVL tree.
     * <p>
     * IMPORTANT: do not change the signatures of any function (i.e. access modifiers, return type, function name and
     * arguments. Changing these would break the automatic tester, and would result in worse grade.
     * <p>
     * However, you are allowed (and required) to implement the given functions, and can add functions of your own
     * according to your needs.
     */
    public class AVLNode {
    	int key;
    	Boolean info;
    	AVLNode left;
    	AVLNode right;
    	AVLNode parent=null;
    	int height=0;
    	AVLNode successor;
    	AVLNode predecessor;
    	int BF;
    	Boolean xor;
    	
    	
    	//AVLNode constructor, complexity=O(1)
    	public AVLNode(int key, Boolean info) {
    		this.key=key;
    		this.info=info;
    		this.xor=this.info;
    		
    		
    	}
    	
    	//return a virtual AVLNode, complexity=O(1)
    	 public AVLNode VirtualAVLNode() {
    		AVLNode Node=new AVLNode(-1,null);
    		Node.setHeight(-1);
    		return Node;
    		
    		
    	}

        //returns node's key (for virtual node return -1), complexity=O(1)
    	 public int getKey() {
             if(!isRealNode()) {
             	return -1;
             
             }
             return this.key;
         }

        //returns node's value [info] (for virtual node return null), complexity=O(1)
    	 public Boolean getValue() {
         	if(!isRealNode()) {
             	return null;
             }
             return this.info;
         } 
         
    

        //sets left child, complexity=O(1)
        public void setLeft(AVLNode node) {
            this.left=node;
        }

        //returns left child (if there is no left child return null), complexity=O(1)
        public AVLNode getLeft() {
        	
            return this.left;
        }

        //sets right child, complexity=O(1)
        public void setRight(AVLNode node) {
        	 this.right=node;
        }

        //returns right child (if there is no right child return null), complexity=O(1)
        public AVLNode getRight() {
        	
            return this.right;
        }

        //sets parent, complexity=O(1)
        public void setParent(AVLNode node) {
            this.parent=node;
        }

        //returns the parent (if there is no parent return null), complexity=O(1)
        public AVLNode getParent() {
            return this.parent;
            }
        

        // Returns True if this is a non-virtual AVL node, complexity=O(1)
        public boolean isRealNode() {
            if(this.info==null) {
            	return false;
            }
            return true;
            	
            
        }

        // sets the height of the node, complexity=O(1)
        public void setHeight(int height) {
            this.height=height;
        }
        
        //check if Node is a leaf, complexity=O(1)
        Boolean is_leaf() {
        	if(this.isRealNode()) {
        	
        		if(!this.right.isRealNode()&& (!this.left.isRealNode())) {
        			return true;
        		}
        	}
        		return false;
       
        }

        // Returns the height of the node (-1 for virtual nodes), complexity=O(1)
        public int getHeight() {
            if(isRealNode()) {
            	return this.height;
            }
            return -1;
            	
            
        }
    }
    
     
      
      
     
}


