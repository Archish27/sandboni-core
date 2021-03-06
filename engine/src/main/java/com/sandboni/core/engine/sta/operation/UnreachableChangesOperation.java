package com.sandboni.core.engine.sta.operation;

import com.sandboni.core.engine.sta.graph.Edge;
import com.sandboni.core.engine.sta.graph.vertex.Vertex;
import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;

import java.util.Set;

import static com.sandboni.core.engine.common.StreamHelper.emptyIfFalse;
import static com.sandboni.core.engine.sta.graph.vertex.VertexInitTypes.END_VERTEX;
import static com.sandboni.core.engine.sta.graph.vertex.VertexInitTypes.START_VERTEX;

public class UnreachableChangesOperation extends AbstractGraphOperation<MapResult<String, Set<String>>> {

    UnreachableChangesOperation() {
    }

    @Override
    public MapResult<String, Set<String>> execute(Graph<Vertex, Edge> graph) {
        ShortestPathAlgorithm<Vertex, Edge> algorithm = new BellmanFordShortestPath<>(graph);
        return new MapResult<>(emptyIfFalse(graph.containsVertex(END_VERTEX) && graph.containsVertex(START_VERTEX),
                () -> graph.edgesOf(END_VERTEX).stream()
                        .map(Edge::getTarget)
                        .filter(v -> algorithm.getPath(v, START_VERTEX) == null))
                .stream().collect(toMapActorAction));
    }
}
