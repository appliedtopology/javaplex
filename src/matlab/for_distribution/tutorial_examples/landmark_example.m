% This script shows the difference between randomized and maxmin landmark
% selection - Section 5.2

clc; clear; close all;
import edu.stanford.math.plex4.*;

%% Figure 8 Example

% initialize the point cloud
point_cloud = examples.PointCloudExamples.getRandomFigure8Points(1000);

% create the landmark selectors
num_landmark_points = 100;
random_selector = api.Plex4.createRandomSelector(point_cloud, num_landmark_points);
maxmin_selector = api.Plex4.createMaxMinSelector(point_cloud, num_landmark_points);

% extract the subset of landmark points from the original point cloud
% Note: we need to increment the indices by 1 since Java uses 0-based
% arrays
random_points = point_cloud(random_selector.getLandmarkPoints() + 1, :);
maxmin_points = point_cloud(maxmin_selector.getLandmarkPoints() + 1, :);

% Plot the landmark points
subplot(1, 2, 1);
scatter(random_points(:,1), random_points(:, 2), '+');
title('Random landmark selection');

subplot(1, 2, 2);
scatter(maxmin_points(:,1), maxmin_points(:, 2), '+');
title('Maxmin landmark selection');

print('-depsc', 'landmarks.eps');
system('epstopdf landmarks.eps')
