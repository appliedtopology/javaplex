
clc; clear; close all;

domain_size = 9;
codomain_size = 7;

% create the domain and codomain simplicial complexes
domain_stream = examples.SimplexStreamExamples.getTetrahedron();
codomain_stream = examples.SimplexStreamExamples.getOctahedron();

domain_points = examples.PointCloudExamples.getTetrahedronVertices();
codomain_points = examples.PointCloudExamples.getOctahedronVertices() + 1;

%domain_points = examples.PointCloudExamples.getEquispacedCirclePoints(domain_size);
%codomain_points = examples.PointCloudExamples.getEquispacedCirclePoints(codomain_size) + 2;

% obtain the parameterization
[cycle_sum, homotopies] = hom_parameterization(domain_stream, codomain_stream);

% setup the LP
[f, A, b, Aeq, beq, lb, ub] = create_max_lp(cycle_sum, homotopies);

[x,fval,exitflag,output,lambda] = linprog(f,A,b,Aeq,beq,lb,ub);

map = compute_mapping(cycle_sum, homotopies, x(1:size(homotopies, 1)))

% do not optimize - just draw the dumb chain map
PlexViewer.drawMapping(domain_stream, domain_points, codomain_stream, codomain_points, map);