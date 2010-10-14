function [constraint, ceq] = positivity_constraint(c, candidate_map, homotopies)

    Q = compute_chain_map(c, candidate_map, homotopies);
    constraint = sum(sum(-min(Q, 0)));
    ceq = [];
    %constraint = [];