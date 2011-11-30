function [graph] = create_graph_from_edgelist(edges)
    min_index = min(min(edges));
    max_index = max(max(edges));
    
    offset = -min_index;
    range = max_index - min_index + 1;
    
    graph = edu.stanford.math.plex4.graph.UndirectedListGraph(range);
    
    for k = 1:size(edges, 1)
        i = edges(k, 1) + offset;
        j = edges(k, 2) + offset;
        graph.addEdge(i, j);
    end
    
end

