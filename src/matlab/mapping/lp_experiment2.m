clc; close all; clear;

hom_data;
fval = 2000;
create_lp_data2;

[x, fval, exitflag] = linprog(f,A,b,Aeq,beq,lb,ub);
compute_chain_map(x(1:K), cycle_sum, homotopies)
fval

round_two = 1;
create_lp_data2;
fval = 1;
%v(r) = -1;
[x, fval_unused, exitflag] = linprog(f,A,b,Aeq,beq,lb,ub);
compute_chain_map(x(1:K), cycle_sum, homotopies)
fval_unused