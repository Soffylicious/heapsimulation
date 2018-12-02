package memory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * This memory model allocates memory cells based on the first-fit method.
 *
 * @author "Johan Holmberg, Malmö university"
 * @since 1.0
 * @author Sofia Larsson
 * @author Jessica Quach
 * @since 2.0
 */
public class FirstFit extends Memory {

    private LinkedList<FreeBlock> freeList;
    private HashMap<Pointer, Integer> blockMap;

    /**
     * Initializes an instance of a first fit-based memory.
     *
     * @param size The number of cells.
     */
    public FirstFit(int size) {
        super(size);
        this.freeList = new LinkedList<>();
        this.freeList.addFirst(new FreeBlock(size, 0));
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
        for (int i = 0; i < freeList.size(); i++) {
            if (freeList.get(i).getSegmentLength() >= size) {
                Pointer pointer = new Pointer(freeList.get(i).getAddress(), this);
                pointer.pointAt(freeList.get(i).getAddress());
                blockMap.put(pointer, size);
                if (freeList.get(i).getSegmentLength() == size) {
                    freeList.remove(i);
                } else {
                    int newSize = freeList.get(i).getSegmentLength() - size;
                    int newAddress = freeList.get(i).getAddress() + size;
                    freeList.get(i).setSegmentLength(newSize);
                    freeList.get(i).setAddress(newAddress);
                }
                return pointer;
            } else {
                System.out.println("Sorry, no space for that");
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
        int releasedSize = blockMap.get(p);    // Get block size of Pointer that we want to release
        int releasedAddress = p.pointsAt();    // Get the address at which the block begins
        blockMap.remove(p);
        FreeBlock newFreeBlock = new FreeBlock(releasedSize, releasedAddress);
        FreeBlock previousFreeBlock = null, nextFreeBlock = null;
        //Put the new node in the "right" place in the list

        // Find nodes that are before and after the new node
        if (!freeList.isEmpty()) {
            for (FreeBlock freeBlock : freeList) {
                if (releasedAddress < freeBlock.getAddress()) {
                    freeList.add(freeList.indexOf(freeBlock), newFreeBlock);
                    nextFreeBlock = freeBlock;
                    break;
                } else {
                    previousFreeBlock = freeBlock;
                }
            }
        } else {
            freeList.add(newFreeBlock);  // Make new node if the list is empty
        }

        // If there are nodes both BEFORE AND AFTER the new node
        if ((previousFreeBlock != null) && (nextFreeBlock != null)) {

            //If both before and after nodes can be merged
            if ((previousFreeBlock.getAddress() + previousFreeBlock.getSegmentLength()) == newFreeBlock.getAddress()) {
                if (((newFreeBlock.getAddress() + newFreeBlock.getSegmentLength()) == nextFreeBlock.getAddress())) {
                    previousFreeBlock.setSegmentLength(previousFreeBlock.getSegmentLength() + nextFreeBlock.getSegmentLength() + newFreeBlock.getSegmentLength());
                    freeList.remove(newFreeBlock);
                    freeList.remove(nextFreeBlock);
                } else {
                    previousFreeBlock.setSegmentLength(previousFreeBlock.getSegmentLength() + newFreeBlock.getSegmentLength());
                    freeList.remove(newFreeBlock);
                }
            }
            // If only space after is free
            if ((newFreeBlock.getAddress() + newFreeBlock.getSegmentLength()) == nextFreeBlock.getAddress()) {
                newFreeBlock.setSegmentLength(nextFreeBlock.getSegmentLength() + newFreeBlock.getSegmentLength());
                freeList.remove(nextFreeBlock);
            }

            // If there is only a node BEFORE the new node
        } else if (previousFreeBlock != null) {
            if ((previousFreeBlock.getAddress() + previousFreeBlock.getSegmentLength()) == newFreeBlock.getAddress()) {
                previousFreeBlock.setSegmentLength(previousFreeBlock.getSegmentLength() + newFreeBlock.getSegmentLength());
                freeList.remove(newFreeBlock);
            }
            // If there is only a node AFTER the new node
        } else if (nextFreeBlock != null) {
            if (((newFreeBlock.getAddress() + newFreeBlock.getSegmentLength()) == nextFreeBlock.getAddress())) {
                newFreeBlock.setSegmentLength(nextFreeBlock.getSegmentLength() + newFreeBlock.getSegmentLength());
                freeList.remove(nextFreeBlock);
            }
        }

    }

    /**
     * Prints a simple model of the memory. Example:
     * <p>
     * |    0 -  110 | Allocated
     * |  111 -  150 | Free
     * |  151 -  999 | Allocated
     * | 1000 - 1024 | Free
     */
    @Override
    public void printLayout() {
        Iterator it = blockMap.entrySet().iterator();
        int allocStart, allocEnd;

        System.out.println("Free");
        for (FreeBlock freeBlock : freeList) {
            System.out.println(freeBlock.getAddress() + " - " + (freeBlock.getAddress() + (freeBlock.getSegmentLength() - 1)) + " ");
        }
        System.out.println(" ");
        System.out.println("Allocated");
        if (it.hasNext()) {
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                allocStart = ((Pointer) pair.getKey()).pointsAt();
                allocEnd = allocStart + ((int) pair.getValue()) - 1;
                System.out.println(allocStart + " - " + allocEnd);
            }
        } else {
            System.out.println("No allocated memory space");
        }
    }

    /**
     * Compacts the memory space.
     */
    public void compact() {
        // TODO Implement this!
    }
}
