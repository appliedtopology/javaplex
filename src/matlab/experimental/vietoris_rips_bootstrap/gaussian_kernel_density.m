function density = gaussian_kernel_density(x, points, sigma)
    n = size(points, 1);
    dimension = size(points, 2);
    
    multiplier = (sigma * sqrt(2 * pi)) ^ (-dimension);
    exp_multiplier = -1 / (2 * sigma * sigma);
    
    squared_difference = (repmat(x, n, 1) - points) .^ 2;
    exp_factor = sum(exp(exp_multiplier * sum(squared_difference, 2)));
    
    density = multiplier * exp_factor / n;
end
