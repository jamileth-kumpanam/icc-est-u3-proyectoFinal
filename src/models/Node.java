package models;
public class Node {
    private String id;
    private int x, y;
    private boolean blocked;
    public Node(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.blocked = false;
    }
    public String getId() { return id; }
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isBlocked() { return blocked; }
    public void setBlocked(boolean blocked) { this.blocked = blocked; }
}