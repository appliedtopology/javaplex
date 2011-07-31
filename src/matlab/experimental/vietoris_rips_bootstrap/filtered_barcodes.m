function [barcodes, stream_size] = filtered_barcodes(dataset, filter_label, T, theta, S, max_filtration_value, filter_function, display_plot, use_sequential_maxmin)

import edu.stanford.math.plex4.*;

    if (~exist('display_plot', 'var'))
        display_plot = true;
    end
    
    if (~exist('use_sequential_maxmin', 'var'))
        use_sequential_maxmin = true;
    end
    
    datafile_path = get_data_file_path(dataset);
    load(datafile_path, dataset);
    point_cloud = eval(dataset);
    
    dimension = 1;
    num_divisions = 1000;

    core_indices = get_core_subset_cached(point_cloud, theta, T, filter_function, dataset, filter_label);
    core_subset = point_cloud(core_indices, :);
    
    if (use_sequential_maxmin)
        landmark_selector = api.Plex4.createMaxMinSelector(core_subset, S);
    else
        landmark_selector = api.Plex4.createRandomSelector(core_subset, S);
    end
    
    sample_indices = landmark_selector.getLandmarkPoints() + 1;
    subsampled_core = core_subset(sample_indices, :);
     
    % create a Vietoris-Rips stream 
    stream = api.Plex4.createVietorisRipsStream(subsampled_core, dimension + 1, max_filtration_value, num_divisions);
    stream_size = stream.getSize();

    % get the default persistence algorithm
    persistence = api.Plex4.getDefaultSimplicialAlgorithm(dimension + 1);

    % compute intervals
    barcodes = persistence.computeIntervals(stream);

    options.file_format = 'png';
    options.filename = sprintf('outputs/%s-%s-%f-%d-%1.3f.%s', dataset, filter_label, theta, T, max_filtration_value, options.file_format);
    options.caption = sprintf('Barcodes for VR(%s[theta=%f, T=%d], %2.3f) (S=%d)', dataset, theta, T, max_filtration_value, S);

    options.max_filtration_value = max_filtration_value;
    plot_barcodes(barcodes, options);

    if (~display_plot)
        close;
    end

end

