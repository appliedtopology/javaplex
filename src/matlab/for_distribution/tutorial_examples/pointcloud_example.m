% This script demonstrates the use of various point clouds - Section 4.1

clc; clear; close all;
import edu.stanford.math.plex4.*;

%% House Example

% create the set of points
point_cloud = [-1,0; 1,0; 1,2; -1,2; 0,3]

% various examples of metric space methods
m_space = metric.impl.EuclideanMetricSpace(point_cloud);
% NB: javaPlex uses 0-based arrays!
m_space.getPoint(0)
m_space.getPoint(2)
m_space.distance(m_space.getPoint(0), m_space.getPoint(2))

%% Figure 8 Example

% create the set of points
point_cloud = examples.PointCloudExamples.getRandomFigure8Points(1000);

figure
scatter(point_cloud(:,1), point_cloud(:,2), '.')
axis equal

%% Torus Example

% create the set of points
point_cloud = examples.PointCloudExamples.getRandomTorusPoints(2000, 1, 2);

figure
scatter3(point_cloud(:,1), point_cloud(:,2), point_cloud(:,3), '.')
axis equal
view(60,40)

%% Sphere Product Example

% create the set of points
% The following gets 1000 points on the torus S^1 x S^1 in R^4
% One can use the same to get uniformly random points on S^k x ... x S^k
% (any finite product of k-spheres)
point_cloud = examples.PointCloudExamples.getRandomSphereProductPoints(1000, 1, 2);

figure
scatter(point_cloud(:,3), point_cloud(:,4), '.')
axis equal
