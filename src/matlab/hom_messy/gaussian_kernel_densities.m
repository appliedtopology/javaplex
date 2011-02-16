function densities = gaussian_kernel_densities(points, reference_points, sigma)
    n = size(points, 1);
    densities = zeros(n, 1);
    for i = 1:n
        densities(i) = gaussian_kernel_density(points(i, :), reference_points, sigma);
    end
end