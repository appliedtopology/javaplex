K = homotopies_dimension;
I = codomain_dimension;
J = domain_dimension;

%{
x = [c_1 ... c_K, s_11 ... s_IJ, t_1 ... t_J, u_1 ... u_I, t, u]
%}

num_variables = K + I*J + I + J + 2;
num_constraints = 2 * (I*J + I + J) + 1;

A = sparse(num_constraints, num_variables);
b = sparse(num_constraints, 1);
f = sparse(num_variables, 1);

for i = 1:I
    for j = 1:J
        s_index = K + flatten(i, j, I, J);
        constraint_index = 2 * flatten(i, j, I, J) - 1;
        A(constraint_index, s_index) = -1;
        A(constraint_index + 1, s_index) = -1;
    end
end

for k = 1:K
    [i_indices, j_indices, values] = find(homotopies{k});
    count = length(i_indices);
    for index = 1:count
        i = i_indices(index);
        j = j_indices(index);
        v = values(index);
        constraint_index = 2 * flatten(i, j, I, J) - 1;
        A(constraint_index, k) = v;
        A(constraint_index + 1, k) = -v;
    end
end

[i_indices, j_indices, values] = find(cycle_sum);
count = length(i_indices);
for index = 1:count
    i = i_indices(index);
    j = j_indices(index);
    v = values(index);
    s_index = K + flatten(i, j, I, J);
    constraint_index = 2 * flatten(i, j, I, J) - 1;
    b(constraint_index) = -v;
    b(constraint_index + 1) = v;
end

constraint_index = 2 * I * J + 1;

for j = 1:J
    t_j_index = K + I*J + j;
    A(constraint_index, t_j_index) = -1;
    for i = 1:I
        s_ij_index = K + flatten(i, j, I, J);
        A(constraint_index, s_ij_index) = 1;
    end
    constraint_index = constraint_index + 1;
end

for i = 1:I
    u_i_index = K + I*J + J + i;
    A(constraint_index, u_i_index) = -1;
    for j = 1:J
        s_ij_index = K + flatten(i, j, I, J);
        A(constraint_index, s_ij_index) = 1;
    end
    constraint_index = constraint_index + 1;
end

t_index = K + I*J + J + I + 1;
u_index = t_index + 1;

for j = 1:J
    t_j_index = K + I*J + j;
    A(constraint_index, t_j_index) = 1;
    A(constraint_index, t_index) = -1;
    constraint_index = constraint_index + 1;
end

for i = 1:I
    u_i_index = K + I*J + J + i;
    A(constraint_index, u_i_index) = 1;
    A(constraint_index, u_index) = -1;
    constraint_index = constraint_index + 1;
end

A(constraint_index, t_index) = 1;
A(constraint_index, u_index) = 1;
b(constraint_index) = fval;

f(t_index) = 1;
f(u_index) = 1;

for k = 1:K
    %f(k) = randn();
end

large = 100;

Aeq = [];
beq = [];
lb = -large * ones(num_variables, 1);
ub = large * ones(num_variables, 1);

for k = 1:K
    lb(k) = 0;
    ub(k) = 1;
end