%% 
clc; clear; close all;
import edu.stanford.math.plex4.*;

distances = [0,2,sqrt(8),2,sqrt(10); 2,0,2,sqrt(8),sqrt(10); sqrt(8),2,0,2,sqrt(2); 2,sqrt(8),2,0,sqrt(2); sqrt(10),sqrt(10),sqrt(2),sqrt(2),0];
m_space = metric.impl.ExplicitMetricSpace(distances);
max_dimension = 3;
max_filtration_value = 4;

stream = api.Plex4.createVietorisRipsStream(m_space, max_dimension, max_filtration_value);
stream.finalizeStream();

persistence = api.Plex4.getDefaultSimplicialAlgorithm(max_dimension);
filtration_value_intervals = persistence.computeIntervals(stream);

%%

num_landmark_points = 3;
maxmin_selector = api.Plex4.createMaxMinSelector(m_space, num_landmark_points);

maxmin_selector.getLandmarkPoints()

% Computes R = max distance from a point to the set of landmarks
maxmin_selector.getMaxDistanceFromPointsToLandmarks()
