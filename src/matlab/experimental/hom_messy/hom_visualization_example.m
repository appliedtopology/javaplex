
clc; clear; close all;

domain_size = 4;
codomain_size = 3;

% create the domain and codomain simplicial complexes
domain_stream = examples.SimplexStreamExamples.getCircle(domain_size);
codomain_stream = examples.SimplexStreamExamples.getCircle(codomain_size);

domain_points = examples.PointCloudExamples.getEquispacedCirclePoints(domain_size);
codomain_points = examples.PointCloudExamples.getEquispacedCirclePoints(codomain_size) + 2;

% obtain the parameterization
[cycle_sum, homotopies] = hom_parameterization(domain_stream, codomain_stream);

% do not optimize - just draw the dumb chain map
PlexViewer.drawMapping(domain_stream, domain_points, codomain_stream, codomain_points, cycle_sum);