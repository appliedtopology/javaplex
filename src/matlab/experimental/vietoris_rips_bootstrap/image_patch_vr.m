function [barcodes, stream_size] = image_patch_vr(T, k, S, max_filtration_value, display_plot, use_sequential_maxmin)

    import edu.stanford.math.plex4.*;

    if (~exist('display_plot', 'var'))
        display_plot = true;
    end
    
    if (~exist('use_sequential_maxmin', 'var'))
        use_sequential_maxmin = true;
    end

    path = '../../../../data/natural_images';
    label = 'n50000Dct';
    datafile = sprintf('%s/%s.mat', path, label);

    load(datafile, label);
    point_cloud = n50000Dct;
    size(point_cloud);
    
    dimension = 1;
    num_divisions = 1000;

    cache_file_prefix = sprintf('%s/cached_density_ranks/%s', path, label);
    core_indices = get_core_subset_cached(point_cloud, k, T, cache_file_prefix);

    core_subset = point_cloud(core_indices, :);
    
    if (use_sequential_maxmin)
        landmark_selector = api.Plex4.createMaxMinSelector(core_subset, S);
    else
        landmark_selector = api.Plex4.createRandomSelector(core_subset, S);
    end
    
    sample_indices = landmark_selector.getLandmarkPoints() + 1;
    subsampled_core = core_subset(sample_indices, :);
    subsampled_core2 = point_cloud(core_indices(sample_indices), :);
    
    
    % create a Vietoris-Rips stream 
    stream = api.Plex4.createVietorisRipsStream(subsampled_core, dimension + 1, max_filtration_value, num_divisions);
    stream_size = stream.getSize();

    % get the default persistence algorithm
    persistence = api.Plex4.getDefaultSimplicialAlgorithm(dimension + 1);

    % compute intervals
    barcodes = persistence.computeIntervals(stream);

    options.file_format = 'png';
    options.filename = sprintf('outputs/%s-%d-%d-%1.3f.%s', label, k, T, max_filtration_value, options.file_format);
    options.caption = sprintf('Barcodes for VR(%s[k=%d, T=%d], %2.3f) (S=%d)', label, k, T, max_filtration_value, S);

    options.max_filtration_value = max_filtration_value;
    h = plot_barcodes(barcodes, options);

    if (~display_plot)
        close;
    end

end