clc; close all;
v = rand(1, homotopies_dimension);

cvx_begin
    variable c(homotopies_dimension);
    variable P(codomain_dimension, domain_dimension);
    
    Q = cycle_sum;
    for i = 1:homotopies_dimension
        Q = Q + c(i) * homotopies(:, :, i);
    end
    
    minimize max(sum(abs(P))) + max(sum(abs(P')));
    subject to
        P == Q
        c <= 1
        c >= 0
        P >= 0
cvx_end

initial_optimum = cvx_optval
c
full(P)
max(sum(abs(P))) + max(sum(abs(P')))

lambda = 1;

cvx_begin
    variable c(homotopies_dimension);
    variable P(codomain_dimension, domain_dimension);
    
    Q = cycle_sum;
    for i = 1:homotopies_dimension
        Q = Q + c(i) * homotopies(:, :, i);
    end
    
    %minimize v * c + lambda * norm(c, 1);
    %maximize sum_smallest(sum(P), 2) + sum_smallest(sum(P'), 2)
    %minimize sum(sum(abs(P))) + lambda * norm(c, 1);
    %minimize norm(c, 1);
    minimize sum(sum(abs(P)))
    subject to
        max(sum(abs(P))) + max(sum(abs(P'))) <= initial_optimum
        P == Q
        c <= 1
        c >= 0
        P >= 0
cvx_end

initial_optimum
second_optimum = cvx_optval
c
full(P)
max(sum(abs(P))) + max(sum(abs(P')))

lambda = 0;
zero_entries = find(abs(c) <= 1e-8);

cvx_begin
    variable c(homotopies_dimension);
    variable P(codomain_dimension, domain_dimension);
    
    Q = cycle_sum;
    for i = 1:homotopies_dimension
        Q = Q + c(i) * homotopies(:, :, i);
    end
    
    %minimize sum(sum(abs(P)))
    %maximize sum_smallest(sum(P), 2) + sum_smallest(sum(P'), 2)
    %minimize norm(c, 1)
    %minimize sum(sum(abs(P))) + lambda * v * c
    minimize v * c
    subject to
        max(sum(abs(P))) + max(sum(abs(P'))) <= initial_optimum;
        sum(sum(abs(P))) <= second_optimum;
        %c(zero_entries) == 0
        P == Q
        c <= 1
        c >= 0
        P >= 0
cvx_end

initial_optimum
second_optimum = cvx_optval
c
full(P)
max(sum(abs(P))) + max(sum(abs(P')))

c = round(c);

Q = cycle_sum;
for i = 1:homotopies_dimension
    Q = Q + c(i) * homotopies(:, :, i);
end

full(Q)