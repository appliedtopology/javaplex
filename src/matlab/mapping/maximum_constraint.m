function [constraint, ceq] = maximum_constraint(c, candidate_map, homotopies, initial_optimum)

    Q = compute_chain_map(c, candidate_map, homotopies);
    constraint = original_objective(Q) - initial_optimum;
    ceq = [];
    %constraint = [];