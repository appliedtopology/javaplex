function [optimum_value, chain_map, coefficients] =  find_integral_point(candidate_map, homotopies, initial_optimum)
    repititions = 10;
    random_coefficients = zeros(repititions, size(homotopies));
    for i = 1:repititions
        [optimum_value, chain_map, random_coefficients(i)] = choose_random_corner_point(cycle_sum, homotopies, initial_optimum);
    end
end