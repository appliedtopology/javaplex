function value = default_objective(coefficients, cycle_sum, homotopies)
% This function returns the maximum 1-norm of the rows and columns - it
% corresponds to the 1-norm relaxation of the bisimpliciality objective
% function
    mapping = compute_mapping(cycle_sum, homotopies, coefficients);
    value = max(sum(abs(mapping))) + max(sum(abs(mapping')));
end