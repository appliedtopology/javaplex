function [optimum_value, chain_map, coefficients] = minimize_variance(candidate_map, homotopies, codomain_vertices, domain_vertices)

    homotopies_dimension = size(homotopies, 1);
    codomain_dimension = size(candidate_map, 1);
    domain_dimension = size(candidate_map, 2);

    cvx_begin
        variable c(homotopies_dimension);
        variable P(codomain_dimension, domain_dimension);

        minimize variance_objective(P', domain_vertices, codomain_vertices);
        subject to
            P == compute_chain_map(c, candidate_map, homotopies);
            %ones(1, codomain_vertices) * P(1:codomain_vertices, 1:domain_vertices) == ones(1, domain_vertices);
            P >= 0
            %c <= 1
            %c >= 0
    cvx_end

    optimum_value = cvx_optval;
    chain_map = P;
    coefficients = c;