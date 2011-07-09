function indices = get_core_subset_cached(points, k, T, label, T_cache)
    
    num_points = size(points, 1);

    if (~exist('T_cache'))
        T_cache = min(num_points, T * 3);
    end

    filename = sprintf('%s-indices-%d.mat', label, k);

    if (exist(filename, 'file'))
        load (filename, 'indices');
        
        if (length(indices) >= T)
            indices = indices(1:T);
            display(sprintf('get_core_subset_cached: Retrieved %d indices for k=%d from file %s', T, k, filename));
            return;
        end
    end
       
    density = kDensitySlow(points, k);
    indices = coreSubset(density, T_cache);
    save(filename, 'indices');
    
    display(sprintf('get_core_subset_cached: Cached %d indices for k=%d in file %s', T_cache, k, filename));
    
    indices = indices(1:T);
end