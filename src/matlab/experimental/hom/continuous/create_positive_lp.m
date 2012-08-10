function [f, A, b, Aeq, beq, lb, ub] = create_positive_lp(cycle_sum, homotopies, opt_max_constraint, opt_f)

    K = size(homotopies, 1);
    [I, J] = size(homotopies{1});

    %{
    x = [c_1 ... c_K, a_11 ... a_IJ, r_1 ... r_I, l_1 ... l_J, r, l]
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
        r >= r_i

    for j = 1 ...  J:
        l >= l_j

    r + l <= fval

    ub/lb constraints:
    for i, j = 1, 1 ... I, J:
        a_ij >= 0

    minimize max_i r_i + max_j l_j

    %}

    num_variables = K + I*J + I + J + 2;

    num_equality_constraints = I*J + I + J;
    num_inequality_constraints = I + J + 1;

    Aeq = sparse(num_equality_constraints, num_variables);
    beq = sparse(num_equality_constraints, 1);
    A = sparse(num_inequality_constraints, num_variables);
    b = sparse(num_inequality_constraints, 1);
    f = sparse(num_variables, 1);
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

    r_index = K + I*J + I + J + 1;
    l_index = K + I*J + I + J + 2;

    for i = 1:I
        r_i_index = K + I*J + i;
        inequality_constraint_index = i;
        A(inequality_constraint_index, r_index) = -1;
        A(inequality_constraint_index, r_i_index) = 1;
    end
    
    K = size(homotopies, 1);
    [I, J] = size(homotopies{1});

    for j = 1:J
        l_j_index = K + I*J + I + j;
        inequality_constraint_index = I + j;
        A(inequality_constraint_index, l_index) = -1;
        A(inequality_constraint_index, l_j_index) = 1;
    end

    for index = 1:num_variables
        lb(index) = -inf;
        ub(index) = inf;
    end

    for index = 1:K
        lb(index) = -1;
        ub(index) = 1;
    end

    for i = 1:I
        for j = 1:J
            a_ij_index = K + flatten(i, j, I, J);
            lb(a_ij_index) = 0;
            ub(a_ij_index) = 10;
        end
    end

    if (exist('opt_max_constraint', 'var'))
        inequality_constraint_index = I + J + 1;
        A(inequality_constraint_index, l_index) = 1;
        A(inequality_constraint_index, r_index) = 1;
        b(inequality_constraint_index) = opt_max_constraint;
    end
    
    f(r_index) = 1;
    f(l_index) = 1;
    
    if (exist('opt_f', 'var'))
        f(1:length(opt_f)) = opt_f;
    end
end