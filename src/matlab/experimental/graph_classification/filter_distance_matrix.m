function distances = filter_distance_matrix(distances)
    threshold = 1e10;
    [r, c] = find(distances>threshold);
    for i = 1:length(r)
        distances(r(i), c(i)) = 0;
    end
    
    max_value = max(max(distances));
    
    for i = 1:length(r)
        distances(r(i), c(i)) = 0 * max_value;
    end
end