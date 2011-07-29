function [densities] = kernel_density(points, width)
    [densities, xi] = ksdensity(points, points, 'kernel', 'normal', 'width', width);
end

