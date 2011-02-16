function points = graph_embedding(adjacency_matrix, dimension)
    n = size(adjacency_matrix, 1);
    distances = floyd_warshall(adjacency_matrix);
    [Y, eigvals] = cmdscale(distances);
    points = zeros(n, dimension);
    width = size(Y, 2);
    points(:, 1:dimension) = Y(:, min(width, 1:dimension));
end