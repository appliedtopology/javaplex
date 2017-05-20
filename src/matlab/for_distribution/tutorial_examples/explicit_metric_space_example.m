% This script demonstrates the use of the explicit metric space - Section
% 4.2

clc; clear; close all;
import edu.stanford.math.plex4.*;

%% House example

% create distance matrix
distances = [0,2,sqrt(8),2,sqrt(10); 2,0,2,sqrt(8),sqrt(10); sqrt(8),2,0,2,sqrt(2); 2,sqrt(8),2,0,sqrt(2); sqrt(10),sqrt(10),sqrt(2),sqrt(2),0]

% create the metric space
m_space = metric.impl.ExplicitMetricSpace(distances);

% again, remember that indexing is 0-based!
m_space.distance(0, 2)