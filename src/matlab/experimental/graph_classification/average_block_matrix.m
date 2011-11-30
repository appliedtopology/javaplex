function matrix = average_block_matrix(A, I, J)
    m = size(A, 1);
    n = size(A, 2);
    block_size_1 = m / I;
    block_size_2 = n / J;
    
    matrix = zeros(I, J);
    
    for i = 1:I
        for j = 1:J
            sum = 0;
            for s = 1:block_size_1
                for t = 1:block_size_2
                    sum = sum + A((i-1) * block_size_1 + s, (j-1) * block_size_2 + t);
                end
            end
            matrix(i, j) = sum / (block_size_1 * block_size_2);
        end
    end
end