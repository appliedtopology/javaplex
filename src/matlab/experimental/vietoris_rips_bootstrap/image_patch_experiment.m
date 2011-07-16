function [barcodes] = image_patch_experiment(T, k_min, k_max, k_step, max_filtration_value)

import edu.stanford.math.plex4.*;

label = 'n50000Dct';
datafile = sprintf('%s.mat', label);

load(datafile, 'n50000Dct');
pointsRange = n50000Dct;
size(pointsRange);

max_dimension = 1;

i_min = 0;
i_max = (k_max - k_min) / k_step;

indices_cell_array = cell(i_max + 1, 0);

cache_file_prefix = sprintf('cached_density_ranks/%s', label);


parfor i = i_min:i_max
    k = k_min + i * k_step;
    indices = get_core_subset_cached(pointsRange, k, T, cache_file_prefix);
    indices_cell_array{i + 1} = indices;
end

list = java.util.ArrayList();

for i = i_min:i_max
    indices = indices_cell_array{i + 1};
    indices = utility.ArrayUtility.makeMonotone(indices);
    list.add(indices);
end


bootstrapper = homology.zigzag.bootstrap.VietorisRipsBootstrapper(pointsRange, max_filtration_value, max_dimension, list);
barcodes = bootstrapper.performBootstrap();

transformer = homology.filtration.IdentityConverter.getInstance();
filtration_value_intervals = transformer.transform(barcodes);

options.file_format = 'eps';
options.filename = sprintf('%s-samples-%d-%d-%d-%d-%1.3f.%s', label, k_min, k_max, k_step, T, max_filtration_value, options.file_format);
options.caption = sprintf('Image Patch Data with Density Filtration: k_{min} = %d, k_{max} = %d, k_{step} = %d, T = %d', k_min, k_max, k_step, T);


plot_barcodes(filtration_value_intervals, options);


end