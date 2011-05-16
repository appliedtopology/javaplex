function distances = floyd_warshall(adjacency_matrix)
    n = size(adjacency_matrix, 1);
    distances = zeros(n, n);
    
    for i = 1:n
        for j = 1:n
            if (adjacency_matrix(i, j) ~= 0)
                distances(i, j) = adjacency_matrix(i, j);
            else
                distances(i, j) = 10;
            end
        end
    end
    
    for k = 1:n
        for i = 1:n
            for j = 1:n
                distances(i, j) = min(distances(i, j), distances(i, k) + distances(k, j));
            end
        end
    end
    
    for i = 1:n
        distances(i, i) = 0;
    end
end