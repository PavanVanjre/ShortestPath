//Pavan Vanjre Ravindranath
// Student Id - 801352266
package heap;

public class MinHeap {
    HeapNode[] heap;
    int size;
     int capacity;

    public MinHeap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.heap = new HeapNode[capacity];
    }



    public void insert(String name, Float distance) {
        if (size == capacity) {
            System.out.println("Heap is full. Cannot insert more elements.");
            return;
        }

        HeapNode newNode = new HeapNode(name, distance);
        heap[size] = newNode;
        size++;

        heapifyUp(size - 1);
    }

    public HeapNode extractMin() {
        if (isEmpty()) {
            System.out.println("Heap is empty. Cannot extract minimum element.");
            return null;
        }

        HeapNode minNode = heap[0];
        heap[0] = heap[size - 1];
        size--;

        heapifyDown(0);

        return minNode;
    }

    private void heapifyUp(int index) {
        int parentIndex = (index - 1) / 2;

        while (index > 0 && heap[index].distance < heap[parentIndex].distance) {
            swap(index, parentIndex);
            index = parentIndex;
            parentIndex = (index - 1) / 2;
        }
    }

    private void heapifyDown(int index) {
        int leftChild = 2 * index + 1;
        int rightChild = 2 * index + 2;
        int smallest = index;

        if (leftChild < size && heap[leftChild].distance < heap[smallest].distance) {
            smallest = leftChild;
        }

        if (rightChild < size && heap[rightChild].distance < heap[smallest].distance) {
            smallest = rightChild;
        }

        if (smallest != index) {
            swap(index, smallest);
            heapifyDown(smallest);
        }
    }

    private void swap(int i, int j) {
        HeapNode temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void display() {
        for (int i = 0; i < size; i++) {
            System.out.println(heap[i].name + " - " + heap[i].distance);
        }
    }

    public HeapNode getHeapNode(String nodeName) {
        for (HeapNode node : heap) {
            if (node.name.equals(nodeName)) {
                 return node;
            }
        }
        return null;
    }
    public void updateNodeDistance(String nodeName, Float updateDistance) {
        for(int i =0;i< heap.length;i++) {
            if(heap[i].name.equals(nodeName)){
                heap[i].distance = updateDistance;
                heapifyUp(i);
            }
        }
    }
}
