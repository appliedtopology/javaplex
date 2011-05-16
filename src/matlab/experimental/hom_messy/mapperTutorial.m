clc; clear; close all;

%% Generate a circle in 2 dimensions (300 points in 2 dimensions)
X = randn(300, 2) * [2 0; 0 0.2];
%Y = randn(300, 2) * [0.2 0; 0 2];
%X = [X; Y];
%X = X./(sqrt(sum(X.*X,2))*ones(1, 2));

%% Find the Inter-point Distance Matrix
d = L2_distance(X',X',1);

%% Find the filter, here we use the distance from the first point
eccFilter = d(1, :);
scatter(X(:,1), X(:,2), 1000, eccFilter, '.');
axis equal;

%% Parameters for Mapper
filterSamples = 5;
overlapPct = 50;

%% Run Mapper
[adja, nodeInfo, levelIdx] = mapper(d, eccFilter, 1/filterSamples,...
                                                        overlapPct);
                                         
%% Prepare inputs for GraphViz
% For each node of the output graph, find the size (~ cardinality of the
% cluster) and the average function value of points in the cluster.
label{1} = sprintf('Dataset Name   : test');
label{2} = sprintf('Filter Samples : %d', filterSamples);
label{3} = sprintf('Overlap Pct    : %0.2f', overlapPct);

for i=1:length(nodeInfo)
    ecc(i) = nodeInfo{i}.filter;
    setSize(i) = length(nodeInfo{i}.set);
end

%%
stream = create_quotient_complex(adja, ecc);


%% Generate the input to Graphviz
writeDotFile(sprintf('t1.dot'), adja, ecc, setSize, label);

%% Execute Graphviz
system(sprintf('\"C:\\Program Files (x86)\\Graphviz2.22\\bin\\neato.exe\" -Tpng t1.dot -o t1.png'));
figure;
imshow('t1.png')