clc; clear; close all;

path = '../../../../data/natural_images';
label = 'nk300c30Dct';
datafile = sprintf('%s/%s.mat', path, label);
load(datafile, label);

point_cloud = nk300c30Dct;


max_dimension = 3;
num_landmark_points = 50;
nu = 1;
num_divisions = 500;

% create a sequential maxmin landmark selector
landmark_selector = api.Plex4.createMaxMinSelector(point_cloud, num_landmark_points);
landmarks = point_cloud(landmark_selector.getLandmarkPoints() + 1, :);

R = landmark_selector.getMaxDistanceFromPointsToLandmarks()
max_filtration_value = R / 4;

% create a lazy witness stream
stream = streams.impl.LazyWitnessStream(landmark_selector.getUnderlyingMetricSpace(), landmark_selector, max_dimension, max_filtration_value, nu, num_divisions);
stream.finalizeStream()
num_simplices = stream.getSize()

% get the default persistence algorithm
persistence = api.Plex4.getDefaultSimplicialAlgorithm(max_dimension);

% compute the intervals
intervals = persistence.computeIntervals(stream);

%% create the barcode plots
options.filename = 'nk300c30Dct_LW1';
options.max_filtration_value = max_filtration_value;
options.max_dimension = max_dimension - 1;
plot_barcodes(intervals, options);

% plot a projection of the dataset onto the first two DCT basis vectors
figure
hold on
plot(point_cloud(:,1), point_cloud(:,2), '.'), axis equal
plot(landmarks(:,1), landmarks(:,2), '.r'), axis equal


