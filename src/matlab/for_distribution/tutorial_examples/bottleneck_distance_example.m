% This script shows the bottleneck distance example

%% Bottleneck Distance Example

clc; clear; close all;
import edu.stanford.math.plex4.*;

% get new ExplicitSimplexStreams
streamA = api.Plex4.createExplicitSimplexStream();
streamB = api.Plex4.createExplicitSimplexStream();

% construct complex A
streamA.addVertex(1, 0);
streamA.addVertex(2, 0);
streamA.addVertex(3, 0);
streamA.addVertex(4, 0);
streamA.addVertex(5, 1);
streamA.addElement([1, 2], 0);
streamA.addElement([2, 3], 0);
streamA.addElement([3, 4], 0);
streamA.addElement([4, 1], 0);
streamA.addElement([3, 5], 2);
streamA.addElement([4, 5], 3);
streamA.addElement([3, 4, 5], 7);
streamA.finalizeStream();

% construct complex B
streamB.addVertex(1, 0);
streamB.addVertex(2, 0);
streamB.addVertex(3, 0);
streamB.addVertex(4, 0);
streamB.addVertex(5, 2);
streamB.addElement([1, 2], 0);
streamB.addElement([2, 3], 0);
streamB.addElement([3, 4], 0);
streamB.addElement([4, 1], 0);
streamB.addElement([3, 5], 2);
streamB.addElement([4, 5], 2);
streamB.addElement([3, 4, 5], 10);
streamB.finalizeStream();

% get persistence algorithm over Z/2Z
persistence = api.Plex4.getModularSimplicialAlgorithm(3, 2);

% compute and print the intervals
intervalsA = persistence.computeIntervals(streamA)
intervalsB = persistence.computeIntervals(streamB)

% compute the bottleneck distances
intervalsA_dim0=intervalsA.getIntervalsAtDimension(0);
intervalsB_dim0=intervalsB.getIntervalsAtDimension(0);
bottleneck_distance_dim0 = edu.stanford.math.plex4.bottleneck.BottleneckDistance.computeBottleneckDistance(intervalsA_dim0,intervalsB_dim0)
intervalsA_dim1=intervalsA.getIntervalsAtDimension(1);
intervalsB_dim1=intervalsB.getIntervalsAtDimension(1);
bottleneck_distance_dim1 = edu.stanford.math.plex4.bottleneck.BottleneckDistance.computeBottleneckDistance(intervalsA_dim1,intervalsB_dim1)






