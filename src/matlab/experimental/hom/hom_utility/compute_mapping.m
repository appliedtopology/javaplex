function mapping = compute_mapping(cycle_sum, homotopies, coefficients)
% This function computes the representative chain map by adding homotopies
% to the cycle sum with the given coefficients.

    mapping = cycle_sum;
    K = length(coefficients);
    for k = 1:K
        mapping = mapping + coefficients(k) * homotopies{k};
    end
end