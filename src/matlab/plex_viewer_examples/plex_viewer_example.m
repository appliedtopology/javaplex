clc; clear; close all;

num_points = 5;

points = examples.PointCloudExamples.getEquispacedCirclePoints(num_points);
stream = api.Plex4.createVietorisRipsStream(points, 2, 0.4);
stream.finalizeStream();

PlexViewer.drawSimplexStream(stream, points);
