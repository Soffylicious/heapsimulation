package memory;

public class Node {
    private int segmentLength;
    private int address;

    Node(int segmentLength, int address) {
        this.segmentLength = segmentLength;
        this.address = address;
    }

    public int getSegmentLength() {
        return segmentLength;
    }

    public void setSegmentLength(int segmentLength) {
        this.segmentLength = segmentLength;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }
}
