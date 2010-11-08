% This script shows an example of computing the parameterization of the
% space of chain maps

clc; clear; close all;

% create the domain and codomain simplicial complexes
domain_stream = examples.SimplexStreamExamples.getTetrahedron();
codomain_stream = examples.SimplexStreamExamples.getOctahedron();

% obtain the parameterization
[cycle_sum, homotopies] = hom_parameterization(domain_stream, codomain_stream)

K = size(homotopies, 1);
I = domain_stream.getSize();
J = codomain_stream.getSize();
v = randn(1, K);

cvx_begin
    variable c(K);
    variable Q(J, I);
    minimize max(sum(abs(Q))) + max(sum(abs(Q')));
    subject to
        Q == compute_mapping(cycle_sum, homotopies, c);
        Q >= 0;
        c >= 0;
        c <= 1;
cvx_end

compute_mapping(cycle_sum, homotopies, c)
initial_optimum = cvx_optval;


cvx_begin
    variable c(K);
    variable Q(J, I);
    minimize norm(Q(3:6, 3:6), 1);
    subject to
        Q == compute_mapping(cycle_sum, homotopies, c);
        max(sum(abs(Q))) + max(sum(abs(Q'))) <= initial_optimum;
        Q >= 0;
        c >= 0;
        c <= 1;
cvx_end

compute_mapping(cycle_sum, homotopies, c)
second_optimum = cvx_optval;

cvx_begin
    variable c(K);
    variable Q(J, I);
    minimize v * c;
    subject to
        Q == compute_mapping(cycle_sum, homotopies, c);
        max(sum(abs(Q))) + max(sum(abs(Q'))) <= initial_optimum;
        %norm(Q(3:6, 3:6), 1) <= second_optimum;
        Q >= 0;
        c >= 0;
        c <= 1;
cvx_end

compute_mapping(cycle_sum, homotopies, c)