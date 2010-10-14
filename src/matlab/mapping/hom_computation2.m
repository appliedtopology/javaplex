clc; close all;
v = randn(1, homotopies_dimension);

cvx_begin
    variable c(homotopies_dimension);
    variable P(codomain_dimension, domain_dimension);
    variable P2(domain_dimension, domain_dimension) symmetric;
    
    Q = cycle_sum' * cycle_sum;
    for i = 1:homotopies_dimension
        Q = Q + c(i) * (homotopies(:, :, i)' * cycle_sum + cycle_sum' * homotopies(:, :, i));
        for j = 1:homotopies_dimension
            Q = Q + c(i) * c(j) * homotopies(:, :, i)' * homotopies(:, :, i);
        end
    end
    
    minimize sum(sum(square(P2 - Q)))
    subject to
        P == Q
        c <= 1
        c >= 0
cvx_end

initial_optimum = cvx_optval
c
P = full(P)
max(sum(abs(P))) + max(sum(abs(P')))

(P' * P)