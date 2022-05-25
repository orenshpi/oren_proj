package hw2;
import java.util.Random;

/*
You must NOT change the signatures of classes/methods in this skeleton file.
You are required to implement the methods of this skeleton file according to the requirements.
You are allowed to add classes, methods, and members as required.
 */

/**
 * This class represents a graph that efficiently maintains the heaviest neighborhood over edge addition and
 * vertex deletion.
 *
 */
public class Graph {
	hash_table hash;
    static max_heap binary_max_heap;
    int NumNodes;
    int NumEdges;
    
    /**
     * Initializes the graph on a given set of nodes. The created graph is empty, i.e. it has no edges.
     * You may assume that the ids of distinct nodes are distinct.
     *
     * @param nodes - an array of node objects
     */
    // complexity=O(N)
    public Graph(Node [] nodes){
    	hash = new hash_table(nodes.length);
    	binary_max_heap=new max_heap(nodes.length+1);
    	for(int i=0; i<nodes.length; i++) {
    		if(nodes[i]!=null) {
    		hash.insert(nodes[i]);
    		NumNodes++;
    		nodes[i].index_in_heap=i+1;
    		binary_max_heap.arr[i+1]=nodes[i];
    		}
    	}
    	for(int i=1; i<(nodes.length+1)/2; i++) {
    		if(nodes[0]!=null) {
    		binary_max_heap.heapify_down(binary_max_heap.arr[(nodes.length+1)/2-i]);
    	}
    	}
    }

    /**
     * This method returns the node in the graph with the maximum neighborhood weight.
     * Note: nodes that have been removed from the graph using deleteNode are no longer in the graph.
     * @return a Node object representing the correct node. If there is no node in the graph, returns 'null'.
     */
    // complexity=O(1)
    public Node maxNeighborhoodWeight(){
        if(getNumNodes()==0) {
        	return null;
        }
        return binary_max_heap.max();
    }

    /**
     * given a node id of a node in the graph, this method returns the neighborhood weight of that node.
     *
     * @param node_id - an id of a node.
     * @return the neighborhood weight of the node of id 'node_id' if such a node exists in the graph.
     * Otherwise, the function returns -1.
     */
    // complexity=O(1)
    public int getNeighborhoodWeight(int node_id){
    	if(!hash.is_in_table(node_id)) {
    		return -1;
    	}
        Node node=hash.find_in_table(node_id);
        return node.neighborhood_sum;
        
    }

    /**
     * This function adds an edge between the two nodes whose ids are specified.
     * If one of these nodes is not in the graph, the function does nothing.
     * The two nodes must be distinct; otherwise, the function does nothing.
     * You may assume that if the two nodes are in the graph, there exists no edge between them prior to the call.
     *
     * @param node1_id - the id of the first node.
     * @param node2_id - the id of the second node.
     * @return returns 'true' if the function added an edge, otherwise returns 'false'.
     */
    // complexity=O(logn)
    public boolean addEdge(int node1_id, int node2_id){
        if(hash.is_in_table(node1_id) && hash.is_in_table(node2_id) ) {
        	Node node1=hash.find_in_table(node1_id);
        	Node node2=hash.find_in_table(node2_id);
        	cell cell1=new cell(node1);
        	cell cell2=new cell(node2);
        	cell1.parallel_edge=cell2;
        	cell2.parallel_edge=cell1;
        	node1.add_edge_to_node(cell2);
        	node2.add_edge_to_node(cell1);
        	NumEdges++;
        	return true;
        }
        return false;
    }

    /**
     * Given the id of a node in the graph, deletes the node of that id from the graph, if it exists.
     *
     * @param node_id - the id of the node to delete.
     * @return returns 'true' if the function deleted a node, otherwise returns 'false'
     */
    // complexity=O((d(v)+1)*logn), d(v) - v's rank
    public boolean deleteNode(int node_id){
    	if(hash.is_in_table(node_id)) {
    	
        Node node=hash.find_in_table(node_id);
        NumEdges-=node.neighbors.size;
        cell c_edge=node.neighbors.head;
        for(int i=0; i<node.neighbors.size; i++) {
        	c_edge.node.delete_edge_from_node(c_edge.parallel_edge);
        	c_edge=c_edge.next;
        }
        
        binary_max_heap.delete(node);
        hash.delete(node);
        
        NumNodes--;
        return true;
        
        
    	}
        return false;
    }
    
    // returns the number of nodes currently in the graph
    // complexity=O(1)
    public int getNumNodes() {
    	return NumNodes;
    	
    }
    
    // returns the number of edges currently in the graph
    // complexity=O(1)
    public int getNumEdges() {
    	return NumEdges;
    	
    }


    /**
     * This class represents a node in the graph.
     */
    public static class Node{
    	int index_in_heap;
        public int weight;
		public int id;
		public LinkedList neighbors;
		public int neighborhood_sum = weight;
		

		/**
         * Creates a new node object, given its id and its weight.
         * @param id - the id of the node.
         * @param weight - the weight of the node.
         */
		// complexity=O(1)
        public Node(int id, int weight){
            this.id = id;
            this.weight = weight;
            this.neighbors=new LinkedList();
            this.neighborhood_sum=this.weight;
        }

        /**
         * Returns the id of the node.
         * @return the id of the node.
         */
        // complexity=O(1)
        public int getId(){
            return this.id;
        }

        /**
         * Returns the weight of the node.
         * @return the weight of the node.
         */
        // complexity=O(1)
        public int getWeight(){
            return this.weight;
        }
        
        // add an edge to node's neighbors list, and update it's neighborhood's weight accordingly
        // complexity=O(logn)
        void add_edge_to_node(cell c) {
        	neighbors.insert_cell(c);
        	binary_max_heap.increase_key(this, c.node.weight);
        }
        
        // delete an edge from node, and update it's neighborhood's weight accordingly
        // complexity=O(logn)
        void delete_edge_from_node(cell c) {
        	neighbors.delete_cell(c);
        	binary_max_heap.decrease_key(this,c.node.weight);
        }
        
        
        
        
    }
    
    
    /**
     * This class represents a node's neighbors in the graph.
     */
    
    
    
    // cell for the LinkedList
    static class cell {
		cell next;
		cell prev;
		cell parallel_edge;
		Node node;
		
	//constructor for cell	
	// complexity=O(1)
	public cell(Node node) {
		this.node=node;
	}
	}
    
    
    // a doubly linked list
    public static class LinkedList{
    	cell head;
    	cell last;
    	public int size = 0;
    	
    	
    	
    	
    	// insert a cell to the LinkedList
    	// complexity=O(1)
    	void insert_cell(cell c) {
    		if(this.size==0) {
    			head=c;
    			last=c;
    		}
    		else {
    			last.next=c;
    			c.prev=last;
    			last=c;
    			
    		}
    		size++;
    	
    		
    		
    	}
    	// delete a cell in the LinkedList
    	// complexity=O(1)
    	void delete_cell(cell c) {
    		
    		if(size==1) {
    			head=null;
    			last=null;
    		}
    		
    		else if(last==c) {
    			last=c.prev;
    			c.prev.next=null;
    			
    			
    		}
    		
    		else if(head==c) {
    			head=head.next;
    			head.prev=null;
    		}
    		
    		else {
    			c.prev.next=c.next;
    			c.next.prev=c.prev;
    		}
    		c.next=null;
    		c.prev=null;
    		size--;
    	}
    	
    	// check if a node is in the list, given his id
    	// complexity=O(d), d=list.size
    	public boolean is_in(int id) {
    		cell c=head;
    		while (c!=null){
    			if(c.node.getId()==id) {
    				return true;
    			}
    			c=c.next;
    				
    			}
    		return false;
    		}
    	
    	// pre - is_in(id)==true
    	// find node in the list 
    	// complexity=O(d), d=list.size
    	public Node find(int id) {
    		cell c=head;
    		while (c.node.getId()!=id){
    			c=c.next;
    		}
    				return c.node;
    
    	}
    	
    	
    	
    	
    	// adding a Node to the LinkedList at the end
    	// complexity=O(1)
    	public void insert_node(Node node) {
    		cell c= new cell(node);
    	
    		if(this.size==0) {
    			head=c;
    			last=c;
    		}
    		else {
    			last.next=c;
    			c.prev=last;
    			last=c;
    			
    		}
    		size++;
    	}
    	
    	// pre - is_in(node)==true
    	// deleting Node from the LinkedList
    	// complexity=O(d)
    	public void delete_node(Node node) {
    		
    		cell c=head;
    		while(c.node!=node) {
    			c=c.next;
    			
    		}
    		if(c==head) {
    			head=c.next;
    			if(head!=null) {
    			head.prev=null;
    		}
    		}
    		else if(c==last) {
    			last=c.prev;
    			if(last!=null) {
    			last.next=null;
    			
    			}
    			
    		}
    		else {
    			if(c.prev!=null) {
    		
    		c.prev.next=c.next;
    			}
    			if(c.next!=null) {
    		c.next.prev=c.prev;
    			}
    		}
    		c.next=null;
    		c.prev=null;
    		size--;
    		
    	}
    	
    }
    //hash_table with chaining
    class hash_table{
    	LinkedList[] table;
    	int a;
    	int b;
    	int p=10^9+9;
    	
    //hash_table constructor
    // complexity=O(1)
    public hash_table(int n) {
    	table = new LinkedList[n];
    	Random rand=new Random();
    	a=rand.nextInt(p-1)+1;
    	b=rand.nextInt(p);
    	
    }
    
    // insert node to the hash_table
    // complexity=O(1)
    public void insert(Node node) {
    	int place=((a*node.getId()+b)%p)%table.length;
    	if(table[place]==null) {
    		table[place]= new LinkedList();
    	}
    	table[place].insert_node(node);
    	
    	
    }
    
    // pre - node is in the hash table
    // delete node from the hash table
    // complexity=O(1)
    public void delete(Node node) {
    	int place=((a*node.getId()+b)%p)%table.length;
    	table[place].delete_node(node);
    	
    }
    // check if node is in the hash table, given his id
    // complexity=O(1)
    public boolean is_in_table(int id) {
    	int place=((a*id+b)%p)%table.length;
    	if(table[place]!=null) {
    		
    	return table[place].is_in(id);
    	}
    	return false;
    	
    }
    
    // pre - is_in(id)==true
    // return node, given his id
    // complexity=O(1)
    public Node find_in_table(int id) {
    	int place=((a*id+b)%p)%table.length;
    	return table[place].find(id);
    	
    }
    
    
    
    }
    // max_binary_heap represented by an array
    class max_heap {
    	int last_item=0;
    	Node[] arr;
    	
    	
    	//constructor for max_heap
    	// complexity=O(1)
    	
    	public max_heap(int n){
    		arr=new Node[n];
    		this.last_item=n-1;
    		
    	}
    	
    	//insert a Node to the heap
    	// complexity=O(logn)
    	public void insert(Node node) {
    		arr[last_item+1]=node;
    		heapify_up(node);
    		last_item++;
    		
    		
    	}
    	
    	// returns the node with the maximal neighborhood_sum
    	// complexity=O(1)
    	public Node max() {
    		return arr[1];
    		
    	}
    	
    	// moving node up until his father's neighborhood_sum is bigger than his
    	// complexity=O(logn)
    	public void heapify_up(Node node) {
    		while(node.index_in_heap!=1 && node.neighborhood_sum>arr[node.index_in_heap/2].neighborhood_sum) {
    			int father_index_in_heap=node.index_in_heap/2;
        		Node father=arr[father_index_in_heap];
    			arr[node.index_in_heap]=father;
    			arr[father_index_in_heap]=node;
    			father.index_in_heap=node.index_in_heap;
    			node.index_in_heap=node.index_in_heap/2;
    			
    		}
    	}
    	//moving node down until he has no sons with bigger neighborhood_sum than his
    	// complexity=O(logn)
    	public void heapify_down(Node node) {
    		
    		while(node.index_in_heap*2+1<=last_item && 
    			(node.neighborhood_sum<arr[node.index_in_heap*2].neighborhood_sum
    			|| node.neighborhood_sum<arr[node.index_in_heap*2+1].neighborhood_sum)) {
    			
    			Node son=maximal_son(node);
    			int son_index_in_heap=son.index_in_heap;
    			arr[node.index_in_heap]=son;
    			arr[son.index_in_heap]=node;
    			son.index_in_heap=node.index_in_heap;
    			node.index_in_heap=son_index_in_heap;
    		}
    			
    			if(node.index_in_heap*2==last_item && node.neighborhood_sum<arr[node.index_in_heap*2].neighborhood_sum) {
    				
    				Node son=maximal_son(node);
        			int son_index_in_heap=son.index_in_heap;
        			arr[node.index_in_heap]=son;
        			arr[son.index_in_heap]=node;
        			son.index_in_heap=node.index_in_heap;
        			node.index_in_heap=son_index_in_heap;
    				
    			
    			
    		}
    	}
    	
    	// pre - node.index_in_heap*2>=arr.length
    	// check which son has the bigger neighborhood_sum
    	// complexity=O(1)
    	public Node maximal_son(Node node) {
    		if(node.index_in_heap*2==last_item) { // node has only one son
    			return arr[node.index_in_heap*2];
    		}
    		if(arr[node.index_in_heap*2].neighborhood_sum > arr[node.index_in_heap*2+1].neighborhood_sum) {
    			return arr[node.index_in_heap*2];
    		}
    		return arr[node.index_in_heap*2+1];
    		
    	}
    	
    	// delete node from the heap
    	// complexity=O(logn)
    	public void delete(Node node) {
    		arr[node.index_in_heap]=arr[last_item];
    		arr[node.index_in_heap].index_in_heap=node.index_in_heap;
    		arr[last_item]=null;
    		last_item--;
    		if(last_item+1!=node.index_in_heap) {
    			
    		
    		heapify_down(arr[node.index_in_heap]);
    		}
    		
    	}
    	
    	//increase the max_neighbors field of a node
    	// complexity=O(logn)
    	public void increase_key(Node node, int delta) {
    		node.neighborhood_sum+=delta;
    		heapify_up(node);
    	}
    	
    	//decrease the max_neighbors field of a node
    	// complexity=O(logn)
    	public void decrease_key(Node node, int delta) {
    		node.neighborhood_sum-=delta;
    		heapify_down(node);
    	}
    	
    
  
    
    }
}


