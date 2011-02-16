function density = gaussian_kernel_density(x, points, sigma)
    density = 0;
    n = size(points, 1);
    dimension = size(points, 2);
    multiplier = 1 / (sigma * sqrt(2 * pi));
    exp_multiplier = 1 / (2 * sigma * sigma);
    for point = points'
        contribution = 1;
        for i = 1:dimension
            contribution = contribution * exp(-exp_multiplier * (point(i) - x(i))^2) * multiplier;
        end
        density = density + contribution;
    end
    density = density / n;
end
