function subsets = get_binary_subsets(n)
    size = 2^n;
    subsets = zeros(size, n);
    for i = 1:(size - 1)
        k = 1;
        for j = 1:n
            subsets(i + 1, j) = bitand(i, k) / k;
            k = k * 2;
        end
    end
end