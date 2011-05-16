% This script displays a visualization of a Vietoris-Rips complex
% 
% KEY COMMANDS:
% arrow keys - rotate camera
% pg up/down - zoom in/out
% numpad 8/2 - increase/decrease filtration index

clc; clear; close all;

num_points = 200;

points = examples.PointCloudExamples.getGaussianPoints(num_points, 3);
stream = api.Plex4.createVietorisRipsStream(points, 2, 0.5);
stream.finalizeStream();

PlexViewer.drawSimplexStream(stream, points);
