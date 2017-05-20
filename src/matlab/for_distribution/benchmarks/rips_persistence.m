%% Rips-complex benchmark example
% Note: make sure that you give matlab enough heap space to work with

clc; clear; close all;
import edu.stanford.math.plex4.*;

max_dimension = 4;
max_filtration_value = 0.3;
num_divisions = 10;
num_points = 220;

% create the set of points
point_cloud = examples.PointCloudExamples.getRandomFigure8Points(num_points);

algorithms = api.PersistenceAlgorithmInterface.getAllSimplicialAbsoluteHomologyAlgorithms(max_dimension);

filtration_limit = 0.5;
num_sizes = 5;
num_algorithms = algorithms.size();
max_filtration_values = linspace(0, filtration_limit, num_sizes);

times = zeros(num_algorithms, num_sizes);
sizes = zeros(num_sizes, 1);

for f = 1:num_sizes
    max_filtration_value = max_filtration_values(f);
    stream = api.Plex4.createVietorisRipsStream(point_cloud, max_dimension + 1, max_filtration_value, num_divisions);
    sizes(f) = stream.getSize();
    
    for i = 1:num_algorithms
        algorithm = algorithms.get(i - 1);
        algorithm.toString();
        tic();
        filtration_index_intervals = algorithm.computeIntervals(stream);
        times(i, f) = toc();
    end
end

%% Produce plot

algorithm_names = java_array('java.lang.String', num_algorithms);

for i = 1:num_algorithms
    algorithm = algorithms.get(i - 1);
    algorithm_names(i) = algorithm.toString();
end

h = figure;
plot(sizes, times, '-o');

legend(cell(algorithm_names));
grid on;

xlabel('Number of Simplices');
ylabel('Execution Time (s)');
