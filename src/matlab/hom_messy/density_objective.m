function density = density_objective(map, domain_points, codomain_points, codomain_densities)
    interpolated_points = compute_interpolated_points((map), domain_points, codomain_points);
    
    [I_0, domain_dimension] = size(domain_points);
    [J_0, codomain_dimension] = size(codomain_points);
    
    vertex_mapping = map(1:J_0, 1:I_0);
    %domain_densities = vertex_mapping' * codomain_densities;
    domain_densities = gaussian_kernel_densities(interpolated_points, codomain_points, 0.6);
    
    total_distance = pdist2(interpolated_points);
    lambda = 0.01;
    
    density = -(sum(domain_densities) + lambda * sum(total_distance));
    %density = -min(domain_densities);
end