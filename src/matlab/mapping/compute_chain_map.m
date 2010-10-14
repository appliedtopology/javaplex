function Q = compute_chain_map(c, candidate_map, homotopies)
    homotopies_dimension = size(homotopies, 1);
    Q = candidate_map;
    for i = 1:homotopies_dimension
        Q = Q + c(i) * homotopies{i};
    end