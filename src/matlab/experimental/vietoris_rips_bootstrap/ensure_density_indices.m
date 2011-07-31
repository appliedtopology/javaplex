function ensure_density_indices(theta_values, dataset, filter_label, filter_function)

    import edu.stanford.math.plex4.*;

    datafile_path = get_data_file_path(dataset);
    load(datafile_path, dataset);
    point_cloud = eval(dataset);

    T = size(point_cloud, 1);
    T_cache = T;
    
    for theta = theta_values
        get_core_subset_cached(point_cloud, theta, T, filter_function, dataset, filter_label, T_cache);
    end

end