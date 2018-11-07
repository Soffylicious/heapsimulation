package memory;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This memory model allocates memory cells based on the first-fit method. 
 * 
 * @author "Johan Holmberg, Malmö university"
 * @since 1.0
 */
public class FirstFit extends Memory {

	private LinkedList<Node> freeSegmentList;
	private HashMap<Pointer, Integer> blockMap;
	/**
	 * Initializes an instance of a first fit-based memory.
	 * 
	 * @param size The number of cells.
	 */
	public FirstFit(int size) {
		super(size);
		this.freeSegmentList = new LinkedList();
		this.freeSegmentList.addFirst(new Node(size,0));
		this.blockMap = new HashMap<>();
	}

	/**
	 * Allocates a number of memory cells. 
	 * 
	 * @param size the number of cells to allocate.
	 * @return The address of the first cell.
	 */
	@Override
	public Pointer alloc(int size) {
		for (int i = 0; i < freeSegmentList.size(); i++) {
			if(freeSegmentList.get(i).getSegmentLength() >= size) {
				Pointer pointer = new Pointer(freeSegmentList.get(i).getAddress(), this);
				pointer.pointAt(freeSegmentList.get(i).getAddress());
				blockMap.put(pointer, size);
				if ( freeSegmentList.get(i).getSegmentLength() == size) {
					freeSegmentList.remove(i);
				} else {
					int newSize = freeSegmentList.get(i).getSegmentLength() - size;
					int newAddress = freeSegmentList.get(i).getAddress() + size;
					freeSegmentList.get(i).setSegmentLength(newSize);
					freeSegmentList.get(i).setAddress(newAddress);
				}
				return pointer;
			}
		}
		return null;
	}
	
	/**
	 * Releases a number of data cells
	 * 
	 * @param p The pointer to release.
	 */
	@Override
	public void release(Pointer p) {
		int sizeToRelease = blockMap.get(p);					//Get block size of Pointer that we want to release
		Node newNode = new Node(sizeToRelease, p.pointsAt());

		//Put the new node in the "right" place in the list
		int i = 0;
		while(p.pointsAt() > freeSegmentList.get(i).getAddress()) {
			i++;
		}
		freeSegmentList.add(i, newNode);					// Create a new mark for empty space size in memory

//		Node previousNode, nextNode;
//		if(freeSegmentList.get(i-1) != null) {
//			previousNode = freeSegmentList.get(i-1);
//		}
//		if(freeSegmentList.get(i+1) != null) {
//			nextNode = freeSegmentList.get(i+1);
//		}
//		// Now we have to de-fragment the memory
//		// To do so, we have to check previous free address and next free address
//
//		// Check if there is free space before and after or only before the newly
//		// freed space. If it is true, merge space
//		if( ( previousNode.getAddress() + (previousNode.getSegmentLength()-1)) == (newNode.getAddress()-1) ) {
//			if((previousNode.getAddress() == (newNode.getAddress() + newNode.getSegmentLength())) ) {
//				// Both space before and after is free
//				previousNode.setSegmentLength(previousNode.getSegmentLength() + nextNode.getSegmentLength() + newNode.getSegmentLength());
//				freeSegmentList.remove(newNode);
//				freeSegmentList.remove(nextNode);
//			} else {
//				//Only space before is free
//				previousNode.setSegmentLength(previousNode.getSegmentLength() + newNode.getSegmentLength());
//				freeSegmentList.remove(newNode);
//			}
//		}
//		// If only space after is free
//		if(previousNode.getAddress() == (newNode.getAddress() + newNode.getSegmentLength())) {
//			newNode.setSegmentLength(nextNode.getSegmentLength() + newNode.getSegmentLength());
//			freeSegmentList.remove(nextNode);
//		}
	}
	
	/**
	 * Prints a simple model of the memory. Example:
	 * 
	 * |    0 -  110 | Allocated
	 * |  111 -  150 | Free
	 * |  151 -  999 | Allocated
	 * | 1000 - 1024 | Free
	 */
	@Override
	public void printLayout() {
		// TODO Implement this!
	}
	
	/**
	 * Compacts the memory space.
	 */
	public void compact() {
		// TODO Implement this!
	}
}
