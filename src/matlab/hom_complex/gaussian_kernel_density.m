function density = gaussian_kernel_density(x, points, sigma)
    density = 0;
    n = size(points, 1);
    dimension = size(points, 2);
    for point = points'
        contribution = 1;
        for i = 1:dimension
            contribution = contribution * normpdf(point(i) - x(i), 0, sigma);
        end
        density = density + contribution;
    end
    density = density / n;
end
