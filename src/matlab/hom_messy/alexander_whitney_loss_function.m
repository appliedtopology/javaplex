function z = alexander_whitney_loss_function(map, domain_aw_matrix, codomain_aw_matrix, loss_function)
    domain_size = size(domain_aw_matrix, 2);
    codomain_size = size(domain_aw_matrix, 2);
    z = 0;
    for j = 1:domain_size
        LHS = codomain_aw_matrix * map(:, j);
        RHS = fast_kron_mult(map, map, domain_aw_matrix(:, j));
        z = z + loss_function(LHS - RHS);
    end
end