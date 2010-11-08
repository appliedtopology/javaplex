function mapping = compute_mapping(cycle_sum, homotopies, coefficients)
    mapping = cycle_sum;
    K = length(coefficients);
    for k = 1:K
        mapping = mapping + coefficients(k) * homotopies{k};
    end
end