function [barcodes] = image_patch_experiment(T, k_min, k_max, k_step, S, max_filtration_value, use_sequential_maxmin)

    import edu.stanford.math.plex4.*;

    if (~exist('use_sequential_maxmin', 'var'))
        use_sequential_maxmin = true;
    end
    
    path = '../../../../data/natural_images';
    label = 'n50000Dct';
    datafile = sprintf('%s/%s.mat', path, label);

    load(datafile, label);
    point_cloud = n50000Dct;

    max_dimension = 1;

    i_min = 0;
    i_max = (k_max - k_min) / k_step;

    indices_cell_array = cell(i_max + 1, 0);

    cache_file_prefix = sprintf('%s/cached_density_ranks/%s', path, label);
   

    for i = i_min:i_max
        k = k_min + i * k_step;
        core_indices = get_core_subset_cached(point_cloud, k, T, cache_file_prefix);
        core_subset = point_cloud(core_indices, :);
        
        utility.RandomUtility.initializeWithSeed(0);
        
        if (use_sequential_maxmin)
            landmark_selector = api.Plex4.createMaxMinSelector(core_subset, S);
        else
            landmark_selector = api.Plex4.createRandomSelector(core_subset, S);
        end
        
        sample_indices = landmark_selector.getLandmarkPoints() + 1;
        
        indices_cell_array{i + 1} = core_indices(sample_indices);
    end

    list = java.util.ArrayList();

    for i = i_min:i_max
        indices = indices_cell_array{i + 1} - 1;
        indices = utility.ArrayUtility.makeMonotone(indices);
        list.add(indices);
    end

    bootstrapper = homology.zigzag.bootstrap.VietorisRipsBootstrapper(point_cloud, max_filtration_value, max_dimension, list);
    barcodes = bootstrapper.performBootstrap();

    transformer = homology.filtration.IdentityConverter.getInstance();
    filtration_value_intervals = transformer.transform(barcodes);

    options.file_format = 'eps';
    options.filename = sprintf('%s-samples-%d-%d-%d-%d-%1.3f.%s', label, k_min, k_max, k_step, T, max_filtration_value, options.file_format);
    options.caption = sprintf('Image Patch Data with Density Filtration: k_{min} = %d, k_{max} = %d, k_{step} = %d, T = %d', k_min, k_max, k_step, T);


    plot_barcodes(filtration_value_intervals, options);

end