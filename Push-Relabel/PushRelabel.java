package hw12;

import java.util.ArrayList;

public class PushRelabel {

  private NetworkVertex[][] vMatrix;
  private ArrayList<Edge> edges;
  private ArrayList<Capacity> capacities;

  public void initializePreflow(int sourceIndex, NetworkVertex source) {
    for (NetworkVertex[] vMatrix1 : vMatrix) {
      for (int j = 0; j < vMatrix.length; j++) {
        vMatrix1[j].height = 0;
        vMatrix1[j].excessFlow = 0;
      }
    }

    for (Edge e :
            edges) {
      e.flow = 0;
    }

    source.height = vMatrix.length;
    for (NetworkVertex vertex :
            vMatrix[sourceIndex]) {
      Edge edge = findEdgeByVertex(source, vertex);
      Capacity capacity = findCapacityByVertex(source, vertex);
      edge.flow = capacity.value;
      vertex.excessFlow = capacity.value;
      source.excessFlow = source.excessFlow - capacity.value;
    }

  }

  public void push(NetworkVertex u, NetworkVertex v) {
    Edge edge = findEdgeByVertex(u, v);
    Capacity capacity = findCapacityByVertex(u, v);
    int delta = Math.min(u.excessFlow, capacity.value);

    if (edge != null) {
      edge.flow = edge.flow + delta;
    } else {
      Edge reverseEdge = findEdgeByVertex(v, u);
      reverseEdge.flow = reverseEdge.flow - delta;
    }
    u.excessFlow = u.excessFlow - delta;
    v.excessFlow = v.excessFlow + delta;
  }

  public void relabel(int vIndex, NetworkVertex u){
    int min = Integer.MAX_VALUE;
    for (NetworkVertex vertex :
            vMatrix[vIndex]) {
      Edge edge = findEdgeByVertex(u, vertex);
      if (edge != null) {
        if (min > vertex.height) {
          min = vertex.height;
        }
      }
    }
    u.height = 1 + min;
  }

  public Edge findEdgeByVertex(NetworkVertex formerVertex, NetworkVertex latterVertex) {
    for (Edge edge :
            edges) {
      if (edge.formerVertex == formerVertex && edge.latterVertex == latterVertex) {
        return edge;
      }
    }
    return null;
  }

  public Capacity findCapacityByVertex(NetworkVertex formerVertex, NetworkVertex latterVertex) {
    for (Capacity capacity :
            capacities) {
      if (capacity.formerVertex == formerVertex && capacity.latterVertex == latterVertex) {
        return capacity;
      }
    }
    return null;
  }

  public static void main(String args[]){
    PushRelabel pushRelabelObj = new PushRelabel();

    for (NetworkVertex[] vMatrix1 : pushRelabelObj.vMatrix) {
      for (int j = 0; j < pushRelabelObj.vMatrix.length; j++) {
        vMatrix1[j] = new NetworkVertex();
        vMatrix1[j] = new NetworkVertex();
      }
    }
    pushRelabelObj.capacities = new ArrayList<>();
  }
}
