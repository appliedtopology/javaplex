clc; close all;
v = randn(1, homotopies_dimension);

cvx_begin
    variable c(homotopies_dimension);
    variable P(codomain_dimension, domain_dimension);
    
    Q = cycle_sum;
    for i = 1:homotopies_dimension
        Q = Q + c(i) * homotopies(:, :, i);
    end
    
    minimize sum(sum(abs(P))) + 0.0 * norm(c, 1);
    subject to
        P == Q
        c <= 1
        c >= 0
        P >= 0
cvx_end

initial_optimum = cvx_optval
c
P = full(P)
max(sum(abs(P))) + max(sum(abs(P')))