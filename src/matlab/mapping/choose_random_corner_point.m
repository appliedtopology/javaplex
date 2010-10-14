function [optimum_value, chain_map, coefficients] = choose_random_corner_point(candidate_map, homotopies, initial_optimum)

    homotopies_dimension = size(homotopies, 1);
    codomain_dimension = size(candidate_map, 1);
    domain_dimension = size(candidate_map, 2);

    v = randn(1, homotopies_dimension);

    cvx_begin
        variable c(homotopies_dimension);
        variable P(codomain_dimension, domain_dimension);
        
        minimize v * c
        subject to
            original_objective(P) <= initial_optimum
            P == compute_chain_map(c, candidate_map, homotopies);
            c <= 1
            c >= -1
            %P >= 0
            %P <= 1
    cvx_end

    optimum_value = cvx_optval;
    chain_map = P;
    coefficients = c;