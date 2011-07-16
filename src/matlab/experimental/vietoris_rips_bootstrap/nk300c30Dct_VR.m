clc; clear; close all;

load nk300c30Dct

% Select 100 random points
rp = randperm(15000);
point_cloud = nk300c30Dct(rp(1:100), :);

max_dimension = 3;
num_divisions = 500;
max_filtration_value = 1.5;

% create a Vietoris-Rips  stream
stream = api.Plex4.createVietorisRipsStream(point_cloud, max_dimension, max_filtration_value, num_divisions);

num_simplices = stream.getSize()

% get the default persistence algorithm
persistence = api.Plex4.getDefaultSimplicialAlgorithm(max_dimension);

% compute intervals
intervals = persistence.computeIntervals(stream);

%% create the barcode plots
options.filename = 'nk300c30Dct_VR';
options.max_filtration_value = max_filtration_value;
options.max_dimension = max_dimension - 1;
plot_barcodes(intervals, options);

% plot a projection of the dataset onto the first two DCT basis vectors
figure
hold on
plot(nk300c30Dct(:,1), nk300c30Dct(:,2), '.'), axis equal
plot(point_cloud(:,1), point_cloud(:,2), '.r'), axis equal