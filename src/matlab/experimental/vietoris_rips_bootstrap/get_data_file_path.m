function file_path = get_data_file_path(dataset)
    path = '../../../../data/natural_images';
    file_path = sprintf('%s/%s.mat', path, dataset);
end