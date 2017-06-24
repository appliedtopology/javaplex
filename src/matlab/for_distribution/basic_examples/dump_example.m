% This script demonstrates the use of plex on a simple "house" example

clc; clear; close all;
import edu.stanford.math.plex4.*;

max_dimension = 2;
max_filtration_value = 4;
num_divisions = 1000;

% create the set of points
point_cloud = examples.PointCloudExamples.getHouseExample();

% create a Vietoris-Rips stream 
stream = api.Plex4.createVietorisRipsStream(point_cloud, max_dimension + 1, max_filtration_value, num_divisions);


%% Dump Example:

iterator = stream.iterator();

while (iterator.hasNext())
  % the next line will print the current simplex
  simplex = iterator.next()
  % here you can do whatever is needed with the simplex
  filtration_value = stream.getFiltrationValue(simplex)
end

