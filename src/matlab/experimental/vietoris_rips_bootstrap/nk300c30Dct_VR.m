clc; clear; close all;

path = '../../../../data/natural_images';
label = 'nk300c30Dct';
datafile = sprintf('%s/%s.mat', path, label);
load(datafile, label);


% Select 100 random points
%rp = randperm(15000);
%point_cloud = nk300c30Dct(rp(1:100), :);

% Select 100 max-min points
S = 100;
landmark_selector = api.Plex4.createMaxMinSelector(nk300c30Dct, S);
indices = landmark_selector.getLandmarkPoints() + 1;
point_cloud = nk300c30Dct(indices, :);

max_dimension = 3;
num_divisions = 500;
max_filtration_value = 1.1;

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

intervals.getInfiniteIntervals()

%% plot a projection of the dataset onto the first two DCT basis vectors
figure
hold on
plot(nk300c30Dct(:,1), nk300c30Dct(:,2), '.'), axis equal
plot(point_cloud(:,1), point_cloud(:,2), '.r'), axis equal