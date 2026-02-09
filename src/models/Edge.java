package models;
import java.util.Objects;
public class Edge {
    private String from, to;
    public Edge(String from, String to) {
        this.from = from;
        this.to = to;
    }
    public String getFrom() { return from; }
    public String getTo() { return to; }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge e = (Edge) o;
        return (Objects.equals(from, e.from) && Objects.equals(to, e.to)) ||
               (Objects.equals(from, e.to) && Objects.equals(to, e.from));
    }
    @Override
    public int hashCode() { return Objects.hash(from) + Objects.hash(to); }
}