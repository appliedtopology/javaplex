clc; clear; close all;
% Generating Cycles
% 1 ([4,5]->[4,5]) + -1 ([4,5]->[0,5]) + 1 ([4,5]->[0,1]) + 1 ([4,5]->[2,3]) + 1 ([4,5]->[3,4]) + 1 ([4,5]->[1,2])
% 1 ([0]->[0]) + 1 ([1]->[0]) + 1 ([4]->[0]) + 1 ([3]->[0]) + 1 ([5]->[0]) + 1 ([2]->[0])
% Cycle Sum
% 1 ([4,5]->[4,5]) + -1 ([4,5]->[0,5]) + 1 ([0]->[0]) + 1 ([4,5]->[0,1]) + 1 ([4,5]->[2,3]) + 1 ([1]->[0]) + 1 ([4,5]->[3,4]) + 1 ([4]->[0]) + 1 ([5]->[0]) + 1 ([3]->[0]) + 1 ([2]->[0]) + 1 ([4,5]->[1,2])
% Homotopies
% 1 ([4,5]->[0,1]) + -1 ([5]->[1]) + 1 ([0,5]->[0,1]) + 1 ([5]->[0])
% 1 ([4,5]->[0,5]) + -1 ([5]->[5]) + 1 ([0,5]->[0,5]) + 1 ([5]->[0])
% 1 ([0,5]->[1,2]) + -1 ([5]->[2]) + 1 ([5]->[1]) + 1 ([4,5]->[1,2])
% -1 ([5]->[3]) + 1 ([4,5]->[2,3]) + 1 ([5]->[2]) + 1 ([0,5]->[2,3])
% 1 ([0,5]->[3,4]) + 1 ([5]->[3]) + 1 ([4,5]->[3,4]) + -1 ([5]->[4])
% 1 ([4,5]->[4,5]) + 1 ([0,5]->[4,5]) + 1 ([5]->[4]) + -1 ([5]->[5])
% 1 ([3,4]->[0,1]) + -1 ([4,5]->[0,1]) + 1 ([4]->[0]) + -1 ([4]->[1])
% -1 ([4,5]->[0,5]) + 1 ([3,4]->[0,5]) + 1 ([4]->[0]) + -1 ([4]->[5])
% 1 ([3,4]->[1,2]) + -1 ([4]->[2]) + 1 ([4]->[1]) + -1 ([4,5]->[1,2])
% -1 ([4,5]->[2,3]) + 1 ([4]->[2]) + -1 ([4]->[3]) + 1 ([3,4]->[2,3])
% -1 ([4]->[4]) + 1 ([3,4]->[3,4]) + -1 ([4,5]->[3,4]) + 1 ([4]->[3])
% 1 ([4]->[4]) + -1 ([4,5]->[4,5]) + -1 ([4]->[5]) + 1 ([3,4]->[4,5])
% -1 ([3]->[1]) + -1 ([3,4]->[0,1]) + 1 ([2,3]->[0,1]) + 1 ([3]->[0])
% -1 ([3,4]->[0,5]) + 1 ([2,3]->[0,5]) + -1 ([3]->[5]) + 1 ([3]->[0])
% -1 ([3,4]->[1,2]) + 1 ([3]->[1]) + -1 ([3]->[2]) + 1 ([2,3]->[1,2])
% 1 ([3]->[2]) + -1 ([3]->[3]) + -1 ([3,4]->[2,3]) + 1 ([2,3]->[2,3])
% 1 ([2,3]->[3,4]) + -1 ([3]->[4]) + -1 ([3,4]->[3,4]) + 1 ([3]->[3])
% 1 ([3]->[4]) + 1 ([2,3]->[4,5]) + -1 ([3,4]->[4,5]) + -1 ([3]->[5])
% -1 ([2]->[1]) + 1 ([1,2]->[0,1]) + -1 ([2,3]->[0,1]) + 1 ([2]->[0])
% 1 ([1,2]->[0,5]) + -1 ([2]->[5]) + -1 ([2,3]->[0,5]) + 1 ([2]->[0])
% 1 ([1,2]->[1,2]) + 1 ([2]->[1]) + -1 ([2]->[2]) + -1 ([2,3]->[1,2])
% 1 ([2]->[2]) + -1 ([2]->[3]) + -1 ([2,3]->[2,3]) + 1 ([1,2]->[2,3])
% 1 ([1,2]->[3,4]) + -1 ([2,3]->[3,4]) + 1 ([2]->[3]) + -1 ([2]->[4])
% 1 ([1,2]->[4,5]) + -1 ([2,3]->[4,5]) + -1 ([2]->[5]) + 1 ([2]->[4])
% -1 ([1,2]->[0,1]) + 1 ([1]->[0]) + -1 ([1]->[1]) + 1 ([0,1]->[0,1])
% -1 ([1,2]->[0,5]) + -1 ([1]->[5]) + 1 ([0,1]->[0,5]) + 1 ([1]->[0])
% -1 ([1,2]->[1,2]) + -1 ([1]->[2]) + 1 ([1]->[1]) + 1 ([0,1]->[1,2])
% 1 ([1]->[2]) + -1 ([1]->[3]) + 1 ([0,1]->[2,3]) + -1 ([1,2]->[2,3])
% -1 ([1,2]->[3,4]) + 1 ([0,1]->[3,4]) + 1 ([1]->[3]) + -1 ([1]->[4])
% 1 ([0,1]->[4,5]) + -1 ([1,2]->[4,5]) + -1 ([1]->[5]) + 1 ([1]->[4])
% 1 ([0]->[0]) + -1 ([0]->[1]) + -1 ([0,5]->[0,1]) + -1 ([0,1]->[0,1])
% 1 ([0]->[0]) + -1 ([0,1]->[0,5]) + -1 ([0,5]->[0,5]) + -1 ([0]->[5])
% -1 ([0,5]->[1,2]) + 1 ([0]->[1]) + -1 ([0]->[2]) + -1 ([0,1]->[1,2])
% -1 ([0]->[3]) + 1 ([0]->[2]) + -1 ([0,1]->[2,3]) + -1 ([0,5]->[2,3])
% -1 ([0,1]->[3,4]) + -1 ([0,5]->[3,4]) + 1 ([0]->[3]) + -1 ([0]->[4])
% -1 ([0,1]->[4,5]) + -1 ([0,5]->[4,5]) + 1 ([0]->[4]) + -1 ([0]->[5])
% Domain Basis
% 0: [0]
% 1: [1]
% 2: [2]
% 3: [3]
% 4: [4]
% 5: [5]
% 6: [0,1]
% 7: [0,5]
% 8: [1,2]
% 9: [2,3]
% 10: [3,4]
% 11: [4,5]
% Codomain Basis
% 0: [0]
% 1: [1]
% 2: [2]
% 3: [3]
% 4: [4]
% 5: [5]
% 6: [0,1]
% 7: [0,5]
% 8: [1,2]
% 9: [2,3]
% 10: [3,4]
% 11: [4,5]
domain_dimension = 12;
codomain_dimension = 12;
homotopies_dimension = 36;
domain_vertices = 6;
codomain_vertices = 6;
tmp_m = 12;
tmp_n = 12;
tmp_i = [1, 1, 1, 1, 1, 1, 7, 8, 9, 10, 11, 12];
tmp_j = [1, 2, 3, 4, 5, 6, 12, 12, 12, 12, 12, 12];
tmp_s = [1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 1.0];
cycle_sum = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

homotopies = cell(homotopies_dimension, 1);
tmp_m = 12;
tmp_n = 12;
tmp_i = [1, 2, 7, 7];
tmp_j = [6, 6, 8, 12];
tmp_s = [1.0, -1.0, 1.0, 1.0];
homotopies{ 1} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [1, 6, 8, 8];
tmp_j = [6, 6, 8, 12];
tmp_s = [1.0, -1.0, 1.0, 1.0];
homotopies{ 2} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [2, 3, 9, 9];
tmp_j = [6, 6, 8, 12];
tmp_s = [1.0, -1.0, 1.0, 1.0];
homotopies{ 3} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [3, 4, 10, 10];
tmp_j = [6, 6, 8, 12];
tmp_s = [1.0, -1.0, 1.0, 1.0];
homotopies{ 4} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [4, 5, 11, 11];
tmp_j = [6, 6, 8, 12];
tmp_s = [1.0, -1.0, 1.0, 1.0];
homotopies{ 5} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [5, 6, 12, 12];
tmp_j = [6, 6, 8, 12];
tmp_s = [1.0, -1.0, 1.0, 1.0];
homotopies{ 6} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [1, 2, 7, 7];
tmp_j = [5, 5, 11, 12];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 7} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [1, 6, 8, 8];
tmp_j = [5, 5, 11, 12];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 8} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [2, 3, 9, 9];
tmp_j = [5, 5, 11, 12];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 9} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [3, 4, 10, 10];
tmp_j = [5, 5, 11, 12];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 10} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [4, 5, 11, 11];
tmp_j = [5, 5, 11, 12];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 11} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [5, 6, 12, 12];
tmp_j = [5, 5, 11, 12];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 12} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [1, 2, 7, 7];
tmp_j = [4, 4, 10, 11];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 13} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [1, 6, 8, 8];
tmp_j = [4, 4, 10, 11];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 14} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [2, 3, 9, 9];
tmp_j = [4, 4, 10, 11];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 15} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [3, 4, 10, 10];
tmp_j = [4, 4, 10, 11];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 16} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [4, 5, 11, 11];
tmp_j = [4, 4, 10, 11];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 17} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [5, 6, 12, 12];
tmp_j = [4, 4, 10, 11];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 18} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [1, 2, 7, 7];
tmp_j = [3, 3, 9, 10];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 19} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [1, 6, 8, 8];
tmp_j = [3, 3, 9, 10];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 20} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [2, 3, 9, 9];
tmp_j = [3, 3, 9, 10];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 21} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [3, 4, 10, 10];
tmp_j = [3, 3, 9, 10];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 22} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [4, 5, 11, 11];
tmp_j = [3, 3, 9, 10];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 23} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [5, 6, 12, 12];
tmp_j = [3, 3, 9, 10];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 24} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [1, 2, 7, 7];
tmp_j = [2, 2, 7, 9];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 25} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [1, 6, 8, 8];
tmp_j = [2, 2, 7, 9];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 26} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [2, 3, 9, 9];
tmp_j = [2, 2, 7, 9];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 27} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [3, 4, 10, 10];
tmp_j = [2, 2, 7, 9];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 28} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [4, 5, 11, 11];
tmp_j = [2, 2, 7, 9];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 29} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [5, 6, 12, 12];
tmp_j = [2, 2, 7, 9];
tmp_s = [1.0, -1.0, 1.0, -1.0];
homotopies{ 30} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [1, 2, 7, 7];
tmp_j = [1, 1, 7, 8];
tmp_s = [1.0, -1.0, -1.0, -1.0];
homotopies{ 31} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [1, 6, 8, 8];
tmp_j = [1, 1, 7, 8];
tmp_s = [1.0, -1.0, -1.0, -1.0];
homotopies{ 32} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [2, 3, 9, 9];
tmp_j = [1, 1, 7, 8];
tmp_s = [1.0, -1.0, -1.0, -1.0];
homotopies{ 33} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [3, 4, 10, 10];
tmp_j = [1, 1, 7, 8];
tmp_s = [1.0, -1.0, -1.0, -1.0];
homotopies{ 34} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [4, 5, 11, 11];
tmp_j = [1, 1, 7, 8];
tmp_s = [1.0, -1.0, -1.0, -1.0];
homotopies{ 35} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

tmp_m = 12;
tmp_n = 12;
tmp_i = [5, 6, 12, 12];
tmp_j = [1, 1, 7, 8];
tmp_s = [1.0, -1.0, -1.0, -1.0];
homotopies{ 36} = sparse(tmp_i, tmp_j, tmp_s, tmp_m, tmp_n);

domain_aw_maps = sparse(domain_dimension^2, domain_dimension);
codomain_aw_maps = sparse(codomain_dimension^2, codomain_dimension);
domain_aw_maps(1, 1) = 1;
domain_aw_maps(14, 2) = 1;
domain_aw_maps(27, 3) = 1;
domain_aw_maps(40, 4) = 1;
domain_aw_maps(53, 5) = 1;
domain_aw_maps(66, 6) = 1;
domain_aw_maps(7, 7) = 1;
domain_aw_maps(74, 7) = 1;
domain_aw_maps(8, 8) = 1;
domain_aw_maps(90, 8) = 1;
domain_aw_maps(99, 9) = 1;
domain_aw_maps(21, 9) = 1;
domain_aw_maps(112, 10) = 1;
domain_aw_maps(34, 10) = 1;
domain_aw_maps(125, 11) = 1;
domain_aw_maps(47, 11) = 1;
domain_aw_maps(60, 12) = 1;
domain_aw_maps(138, 12) = 1;
codomain_aw_maps(1, 1) = 1;
codomain_aw_maps(14, 2) = 1;
codomain_aw_maps(27, 3) = 1;
codomain_aw_maps(40, 4) = 1;
codomain_aw_maps(53, 5) = 1;
codomain_aw_maps(66, 6) = 1;
codomain_aw_maps(7, 7) = 1;
codomain_aw_maps(74, 7) = 1;
codomain_aw_maps(8, 8) = 1;
codomain_aw_maps(90, 8) = 1;
codomain_aw_maps(99, 9) = 1;
codomain_aw_maps(21, 9) = 1;
codomain_aw_maps(112, 10) = 1;
codomain_aw_maps(34, 10) = 1;
codomain_aw_maps(125, 11) = 1;
codomain_aw_maps(47, 11) = 1;
codomain_aw_maps(60, 12) = 1;
codomain_aw_maps(138, 12) = 1;
