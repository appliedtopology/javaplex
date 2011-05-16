function [interpolated_points] = compute_interpolated_points(mapping, domain_points, codomain_points)

[I_0, domain_dimension] = size(domain_points);
[J_0, codomain_dimension] = size(codomain_points);

vertex_mapping = mapping(1:J_0, 1:I_0);

interpolated_points = vertex_mapping' * codomain_points;

%interpolated_points = mod(interpolated_points, 2 * pi);

end