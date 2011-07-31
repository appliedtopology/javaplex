function indices = get_core_subset_cached(points, theta, T, filter_function, dataset, filter_label, T_cache)
    
    num_points = size(points, 1);

    filename = get_cache_file_path(dataset, filter_label, theta);
    
    if (exist(filename, 'file'))
        load (filename, 'indices');
        
        if (length(indices) >= T)
            indices = indices(1:T);
            display(sprintf('get_core_subset_cached: Retrieved %d indices for k=%f from file %s', T, theta, filename));
            return;
        end
    end
    
    if (~exist('T_cache'))
        T_cache = min(num_points, T * 3);
    end
    
    filtration_values = filter_function(points, theta);
    indices = coreSubset(filtration_values, T_cache);
    save(filename, 'indices');
    
    display(sprintf('get_core_subset_cached: Cached %d indices for k=%f in file %s', T_cache, theta, filename));
    
    indices = indices(1:T);
end
