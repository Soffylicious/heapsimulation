package memory;

public class FreeBlock {
    private int segmentLength;
    private int address;

    FreeBlock(int segmentLength, int address) {
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
