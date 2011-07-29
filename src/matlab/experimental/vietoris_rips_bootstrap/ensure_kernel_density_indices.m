function ensure_kernel_density_indices(T, sigma_min, sigma_max, sigma_step)

import edu.stanford.math.plex4.*;

path = '../../../../data/natural_images';
label = 'n50000Dct';
datafile = sprintf('%s/%s.mat', path, label);

load(datafile, label);
pointsRange = n50000Dct;
size(pointsRange);

T_cache = T;

i_min = 0;
i_max = (sigma_max - sigma_min) / sigma_step;
i_max = (floor(i_max));
cache_file_prefix = sprintf('%s/cached_density_ranks/%s-kernel', path, label);

density_estimator = @(points, sigma) gaussian_kernel_densities(points, points, sigma);

parfor i = i_min:i_max
    k = sigma_min + i * sigma_step;
    indices = get_core_subset_cached(pointsRange, k, T, cache_file_prefix, T_cache, density_estimator);
end

