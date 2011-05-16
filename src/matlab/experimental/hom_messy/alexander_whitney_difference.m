function d = alexander_whitney_difference(map, domain_aw_matrix, codomain_aw_matrix)
    domain_size = size(domain_aw_matrix, 2);
    codomain_size = size(domain_aw_matrix, 2);
    RHS = zeros(codomain_size^2, domain_size);
    for j = 1:domain_size
        RHS(:, j) = fast_kron_mult(map, map, domain_aw_matrix(:, j));
    end
    LHS = codomain_aw_matrix * map;
    
    d = LHS - RHS;
end