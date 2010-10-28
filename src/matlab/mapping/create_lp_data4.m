K = homotopies_dimension;
I = codomain_dimension;
J = domain_dimension;

%{
x = [c_1 ... c_K, a_11 ... a_IJ, r_1 ... r_I, l_1 ... l_J]
a_ij = sum_k c_k H_ij^k + H_ij^0
r_i = sum_i a_ij (sum over i'th row)
l_j = sum_j a_ij (sum over j'th col)

r >= r_i (all i)
l >= l_j (all j)

Equality constraints:

for i, j = 1, 1 ... I, J:
    a_ij = sum_k sum_k c_k H_ij^k + H_ij^0

for i = 1 ... I:
    r_i = sum_i a_ij

for j = 1 ...  J:
    l_j = sum_j a_ij

Inequality constraints:

for i = 1 ... I:
    r_i <= ceil_row_sums_i

for i = 1 ... I:
    r_i >= floor_row_sums_i

for j = 1 ... J:
    l_j <= ceil_col_sums_i

for j = 1 ... J:
    l_j >= floor_col_sums_i

sum_i r_i <= total_sum

ub/lb constraints:
for i, j = 1, 1 ... I, J:
    a_ij >= 0

minimize max_i r_i + max_j l_j

%}

num_variables = K + I*J + I + J;

num_equality_constraints = I*J + I + J;
num_inequality_constraints = I + I + J + J + 1;

Aeq = sparse(num_equality_constraints, num_variables);
beq = sparse(num_equality_constraints, 1);
A = sparse(num_inequality_constraints, num_variables);
b = sparse(num_inequality_constraints, 1);
f = randn(num_variables, 1);
lb = sparse(num_variables, 1);
ub = sparse(num_variables, 1);

for i = 1:I
    for j = 1:J
        a_ij_index = K + flatten(i, j, I, J);
        equality_constraint_index = flatten(i, j, I, J);
        Aeq(equality_constraint_index, a_ij_index) = -1;
    end
end

for k = 1:K
    [i_indices, j_indices, values] = find(homotopies{k});
    count = length(i_indices);
    for index = 1:count
        i = i_indices(index);
        j = j_indices(index);
        v = values(index);
        equality_constraint_index = flatten(i, j, I, J);
        Aeq(equality_constraint_index, k) = v;
    end
end

[i_indices, j_indices, values] = find(cycle_sum);
count = length(i_indices);
for index = 1:count
    i = i_indices(index);
    j = j_indices(index);
    v = values(index);
    equality_constraint_index = flatten(i, j, I, J);
    beq(equality_constraint_index) = -v;
end


for i = 1:I
    r_i_index = K + I*J + i;
    equality_constraint_index = I*J + i;
    Aeq(equality_constraint_index, r_i_index) = -1;
    for j = 1:J
        a_ij_index = K + flatten(i, j, I, J);
        Aeq(equality_constraint_index, a_ij_index) = 1;
    end
end

for j = 1:J
    l_j_index = K + I*J + I + j;
    equality_constraint_index = I*J + I + j;
    Aeq(equality_constraint_index, l_j_index) = -1;
    for i = 1:I
        a_ij_index = K + flatten(i, j, I, J);
        Aeq(equality_constraint_index, a_ij_index) = 1;
    end
end

for i = 1:I
    r_i_index = K + I*J + i;
    inequality_constraint_index = i;
    A(inequality_constraint_index, r_i_index) = 1;
    b(inequality_constraint_index) = ceil_row_sums(i);
end

for i = 1:I
    r_i_index = K + I*J + i;
    inequality_constraint_index = I + i;
    A(inequality_constraint_index, r_i_index) = -1;
    b(inequality_constraint_index) = -floor_row_sums(i);
end

for j = 1:J
    l_j_index = K + I*J + I + j;
    inequality_constraint_index = I + I + j;
    A(inequality_constraint_index, l_j_index) = 1;
    b(inequality_constraint_index) = ceil_col_sums(j);
end

for j = 1:J
    l_j_index = K + I*J + I + j;
    inequality_constraint_index = I + I + J + j;
    A(inequality_constraint_index, l_j_index) = -1;
    b(inequality_constraint_index) = floor_col_sums(j);
end

inequality_constraint_index = num_inequality_constraints;
b(inequality_constraint_index) = total_sum;
for i = 1:I
    r_i_index = K + I*J + i;
    A(inequality_constraint_index, r_i_index) = 1;
end

for index = 1:num_variables
    lb(index) = -inf;
    ub(index) = inf;
end

for index = 1:K
    lb(index) = 0;
    ub(index) = 1;
end

for i = 1:I
    for j = 1:J
        a_ij_index = K + flatten(i, j, I, J);
        lb(a_ij_index) = 0;
        ub(a_ij_index) = 1;
    end
end
