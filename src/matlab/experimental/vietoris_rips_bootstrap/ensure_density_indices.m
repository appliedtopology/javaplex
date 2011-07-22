function ensure_density_indices(T, k_min, k_max, k_step)

import edu.stanford.math.plex4.*;

label = 'n50000Dct';
datafile = sprintf('%s.mat', label);

load(datafile, 'n50000Dct');
pointsRange = n50000Dct;
size(pointsRange);

T_cache = T;

i_min = 0;
i_max = (k_max - k_min) / k_step;

cache_file_prefix = sprintf('cached_density_ranks/%s', label);

parfor i = i_min:i_max
    k = k_min + i * k_step;
    indices = get_core_subset_cached(pointsRange, k, T, cache_file_prefix, T_cache);
end

