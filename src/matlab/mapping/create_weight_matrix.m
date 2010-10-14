function W = create_weight_matrix(codomain_size, codomain_vertices)
    W = sparse(codomain_size, codomain_size);
    for i = 1:codomain_size
        W(i, i) = 1;
    end
    for i = 1:codomain_vertices
        W(i, i) = 2;
    end