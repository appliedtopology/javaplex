function variance = variance_objective(P, codomain_vertices, domain_vertices)
    
    V = P(1:codomain_vertices, 1:domain_vertices);
    mean = ones(codomain_vertices, 1) * sum(V .* ([1:codomain_vertices]' * ones(1, domain_vertices)));
    
    %%variance = sum(sum(square(V - ones(domain_vertices, 1) * mean(V))));
    %variance = sum(sum(V .* square([1:codomain_vertices]' * ones(1, domain_vertices) - mean)));
    variance = sum(sum(V .* square([1:codomain_vertices]' * ones(1, domain_vertices))));
end