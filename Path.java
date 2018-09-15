


public class Path implements Comparable<Path> {
    private double weight;
    private Node end;
    private int height;


    Path(Node end, int height) {
        this.end = end;
        this.weight = end.getWeight();
        this.height = height;
    }


    @Override
    public int compareTo(Path that) {
        return Double.compare(this.weight, that.weight);
    }


    @Override
    public String toString() {
        return "Path{" + "weight=" + weight + ", end="
                + end + ", height=" + height + '}';
    }


    public int[] path() {
        Node current = end;
        int[] path = new int[height];

        for (int i = height - 1; i >= 0; i--) {
            path[i] = current.getX();
            current = current.getParent();
        }

        return path;
    }


}
