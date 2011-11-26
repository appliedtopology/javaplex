function distance_matrix = pairwise_row_distances(matrix)
    n = size(matrix, 1);
    k = size(matrix, 2);
    distance_matrix = zeros(n, n);
    
    for i = 1:n
        for j = (i+1):n
            distance = norm(matrix(i, :) - matrix(j, :));
            distance_matrix(i, j) = distance;
            distance_matrix(j, i) = distance;
        end
    end
end