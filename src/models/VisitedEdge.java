package models;
public class VisitedEdge {
    private Node from, to;
    public VisitedEdge(Node from, Node to) { this.from = from; this.to = to; }
    public Node getFrom() { return from; }
    public Node getTo() { return to; }
}