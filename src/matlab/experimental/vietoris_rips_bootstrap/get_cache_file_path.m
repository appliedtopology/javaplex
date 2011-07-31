function file_path = get_cache_file_path(dataset, filter_label, theta)
    path = '../../../../data/natural_images';
    cache_file_prefix = sprintf('%s/cached_density_ranks/%s-%s', path, dataset, filter_label);
    file_path = sprintf('%s-indices-%f.mat', cache_file_prefix, theta);
end