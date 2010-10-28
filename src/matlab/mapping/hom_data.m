clc; clear; close all;
% Generating Cycles
% 1 ([1,2]->[1,2]) + 1 ([1,2]->[0,1]) + -1 ([1,2]->[0,2])
% 1 ([0]->[0]) + 1 ([1]->[0]) + 1 ([2]->[0])
% Cycle Sum
% 1 ([1,2]->[1,2]) + 1 ([1,2]->[0,1]) + -1 ([1,2]->[0,2]) + 1 ([0]->[0]) + 1 ([1]->[0]) + 1 ([2]->[0])
% Homotopies
% 1 ([2]->[1]) + 1 ([0,2]->[0,1]) + 1 ([1,2]->[0,1]) + -1 ([2]->[0])
% 1 ([2]->[2]) + 1 ([0,2]->[0,2]) + 1 ([1,2]->[0,2]) + -1 ([2]->[0])
% 1 ([1,2]->[1,2]) + -1 ([2]->[1]) + 1 ([2]->[2]) + 1 ([0,2]->[1,2])
% -1 ([1,2]->[0,1]) + -1 ([1]->[0]) + 1 ([1]->[1]) + 1 ([0,1]->[0,1])
% 1 ([1]->[2]) + 1 ([0,1]->[0,2]) + -1 ([1,2]->[0,2]) + -1 ([1]->[0])
% -1 ([1,2]->[1,2]) + 1 ([1]->[2]) + -1 ([1]->[1]) + 1 ([0,1]->[1,2])
% -1 ([0,2]->[0,1]) + -1 ([0]->[0]) + 1 ([0]->[1]) + -1 ([0,1]->[0,1])
% -1 ([0,1]->[0,2]) + -1 ([0]->[0]) + -1 ([0,2]->[0,2]) + 1 ([0]->[2])
% -1 ([0]->[1]) + 1 ([0]->[2]) + -1 ([0,2]->[1,2]) + -1 ([0,1]->[1,2])
% Domain Basis
% 0: [0]
% 1: [1]
% 2: [2]
% 3: [0,1]
% 4: [0,2]
% 5: [1,2]
% Codomain Basis
% 0: [0]
% 1: [1]
% 2: [2]
% 3: [0,1]
% 4: [0,2]
% 5: [1,2]
domain_dimension = 6;
codomain_dimension = 6;
homotopies_dimension = 9;
domain_vertices = 3;
codomain_vertices = 3;
tmp_m = 6;
tmp_n = 6;
tmp_i = [1, 1, 1, 4, 5, 6];
tmp_j = [1, 2, 3, 6, 6, 6];
tmp_s = [1.0, 1.0, 1.0, 1.0, -1.0, 1.0];
cycle_sum = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

homotopies = cell(homotopies_dimension, 1);
tmp_m = 6;
tmp_n = 6;
tmp_i = [1, 2, 4, 4];
tmp_j = [3, 3, 5, 6];
tmp_s = [-1.0, 1.0, 1.0, 1.0];
homotopies{ 1} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 6;
tmp_n = 6;
tmp_i = [1, 3, 5, 5];
tmp_j = [3, 3, 5, 6];
tmp_s = [-1.0, 1.0, 1.0, 1.0];
homotopies{ 2} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 6;
tmp_n = 6;
tmp_i = [2, 3, 6, 6];
tmp_j = [3, 3, 5, 6];
tmp_s = [-1.0, 1.0, 1.0, 1.0];
homotopies{ 3} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 6;
tmp_n = 6;
tmp_i = [1, 2, 4, 4];
tmp_j = [2, 2, 4, 6];
tmp_s = [-1.0, 1.0, 1.0, -1.0];
homotopies{ 4} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 6;
tmp_n = 6;
tmp_i = [1, 3, 5, 5];
tmp_j = [2, 2, 4, 6];
tmp_s = [-1.0, 1.0, 1.0, -1.0];
homotopies{ 5} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 6;
tmp_n = 6;
tmp_i = [2, 3, 6, 6];
tmp_j = [2, 2, 4, 6];
tmp_s = [-1.0, 1.0, 1.0, -1.0];
homotopies{ 6} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 6;
tmp_n = 6;
tmp_i = [1, 2, 4, 4];
tmp_j = [1, 1, 4, 5];
tmp_s = [-1.0, 1.0, -1.0, -1.0];
homotopies{ 7} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 6;
tmp_n = 6;
tmp_i = [1, 3, 5, 5];
tmp_j = [1, 1, 4, 5];
tmp_s = [-1.0, 1.0, -1.0, -1.0];
homotopies{ 8} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 6;
tmp_n = 6;
tmp_i = [2, 3, 6, 6];
tmp_j = [1, 1, 4, 5];
tmp_s = [-1.0, 1.0, -1.0, -1.0];
homotopies{ 9} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

domain_aw_maps = sparse(domain_dimension^2, domain_dimension);
codomain_aw_maps = sparse(codomain_dimension^2, codomain_dimension);
domain_aw_maps(1, 1) = 1;
domain_aw_maps(8, 2) = 1;
domain_aw_maps(15, 3) = 1;
domain_aw_maps(4, 4) = 1;
domain_aw_maps(20, 4) = 1;
domain_aw_maps(27, 5) = 1;
domain_aw_maps(5, 5) = 1;
domain_aw_maps(33, 6) = 1;
domain_aw_maps(12, 6) = 1;
codomain_aw_maps(1, 1) = 1;
codomain_aw_maps(8, 2) = 1;
codomain_aw_maps(15, 3) = 1;
codomain_aw_maps(4, 4) = 1;
codomain_aw_maps(20, 4) = 1;
codomain_aw_maps(27, 5) = 1;
codomain_aw_maps(5, 5) = 1;
codomain_aw_maps(33, 6) = 1;
codomain_aw_maps(12, 6) = 1;
