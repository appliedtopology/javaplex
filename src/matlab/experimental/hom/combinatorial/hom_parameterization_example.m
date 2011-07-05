% This script shows an example of computing the parameterization of the
% space of chain maps

clc; clear; close all;

% create the domain and codomain simplicial complexes
domain_stream = examples.SimplexStreamExamples.getTetrahedron();
codomain_stream = examples.SimplexStreamExamples.getTetrahedron();

% obtain the parameterization
[cycle_sum, homotopies] = hom_parameterization(domain_stream, codomain_stream);

cycle_sum
