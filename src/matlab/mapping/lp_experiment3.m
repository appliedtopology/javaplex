clc; close all; clear;
tic()
hom_data;
fval = 99999;
create_lp_data3;

[x,fval,exitflag,output,lambda] = linprog(f,A,b,Aeq,beq,lb,ub);
map = compute_chain_map(x(1:K), cycle_sum, homotopies);
map = (abs(map) > 1e-3) .* map
max(sum(map)) + max(sum(map'))
v = randn(num_variables, 1);
b(I + J + 1) = (fval);
[x,fval,exitflag,output,lambda] = linprog(v,A,b,Aeq,beq,lb,ub);

map = compute_chain_map((x(1:K)), cycle_sum, homotopies);
map = (abs(map) > 1e-3) .* map
dlmwrite('corner_point.txt', full(map));
fval

toc()
sum(map);
sum(map');
max(sum(map)) + max(sum(map'))