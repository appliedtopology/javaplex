function [d] = smoothness_objective(map, domain_points, codomain_points, distance_function, edge_list)
    interpolated_points = compute_interpolated_points(map, domain_points, codomain_points);
    num_edges = size(edge_list, 1);
    d = 0;
    for k = 1:num_edges
        i = edge_list(k, 1) + 1;
        j = edge_list(k, 2) + 1;
        d = d + distance_function(interpolated_points(i, :), interpolated_points(j, :))^2;
    end
    d = d + 0 * sum(sum(abs(map).^2));
end