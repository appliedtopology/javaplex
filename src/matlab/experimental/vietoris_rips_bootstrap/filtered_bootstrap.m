function [barcodes, indices] = filtered_bootstrap(dataset, filter_label, T, theta_values, S, max_filtration_value, filter_function, display_plot, use_sequential_maxmin)
    import edu.stanford.math.plex4.*;

    if (~exist('use_sequential_maxmin', 'var'))
        use_sequential_maxmin = true;
    end
    
    if (~exist('display_plot', 'var'))
        display_plot = true;
    end
    
    datafile_path = get_data_file_path(dataset);
    load(datafile_path, dataset);
    point_cloud = eval(dataset);

    max_dimension = 1;
    indices_cell_array = zeros(length(theta_values), S);

    i = 1;
    for theta = theta_values
        core_indices = get_core_subset_cached(point_cloud, theta, T, filter_function, dataset, filter_label);
        core_subset = point_cloud(core_indices, :);
        
        utility.RandomUtility.initializeWithSeed(0);
        
        if (use_sequential_maxmin)
            landmark_selector = api.Plex4.createMaxMinSelector(core_subset, S);
        else
            landmark_selector = api.Plex4.createRandomSelector(core_subset, S);
        end
        
        sample_indices = (landmark_selector.getLandmarkPoints() + 1);
        
        indices_cell_array(i, :) = core_indices(sample_indices);
        indices_cell_array(i, :) = utility.ArrayUtility.makeMonotone(indices_cell_array(i, :));
        
        i = i + 1;
    end
    
    theta_min = min(theta_values);
    theta_max = max(theta_values);

    list = java.util.ArrayList();

    for i = 1:length(theta_values)
        indices = int32(indices_cell_array(i, :) - 1);
        list.add(indices);
    end

    bootstrapper = homology.zigzag.bootstrap.VietorisRipsBootstrapper(point_cloud, max_filtration_value, max_dimension, list);
    barcodes = bootstrapper.performBootstrap();

    transformer = homology.filtration.IdentityConverter.getInstance();
    filtration_value_intervals = transformer.transform(barcodes);

    options.file_format = 'eps';
    options.filename = sprintf('outputs/%s-%s-samples-%1.3f-%1.3f-%d-%d-%1.3f.%s', dataset, filter_label, theta_min, theta_max, T, S, max_filtration_value, options.file_format);
    options.caption = sprintf('Bootstrap for %s with %s filter: theta_{min} = %1.3f, theta_{max} = %1.3f, T = %d, S = %d', dataset, filter_label, theta_min, theta_max, T, S);


    plot_barcodes(filtration_value_intervals, options);
    
    if (~display_plot)
        close;
    end
    
    indices = indices_cell_array;
end

