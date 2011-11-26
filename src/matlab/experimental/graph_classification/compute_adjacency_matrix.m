function adjacency_matrix = compute_adjacency_matrix(graph)
    n = graph.getNumVertices();
    adjacency_matrix = zeros(n, n);

    for i = 1:n
        for j = (i+1):n
            if (graph.containsEdge(i-1, j-1))
                adjacency_matrix(i, j) = 1;
                adjacency_matrix(j, i) = 1;
            end
        end
    end
end