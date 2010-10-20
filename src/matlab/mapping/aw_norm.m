function result = aw_norm(map, domain_aw_maps, codomain_aw_maps, domain_vertices, codomain_vertices)
    [codomain_dimension, domain_dimension] = size(map);
    result = 0;
    for i = 1:domain_vertices
        sigma = sparse(domain_dimension, 1);
        sigma(i) = 1;
        result = result + norm(aw_difference(map, sigma, domain_aw_maps, codomain_aw_maps), 1);
    end
    for i = 1:codomain_vertices
        sigma = sparse(codomain_dimension, 1);
        sigma(i) = 1;
        result = result + norm(aw_difference(map', sigma, codomain_aw_maps, domain_aw_maps), 1);
    end
end