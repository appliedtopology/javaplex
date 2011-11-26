function normalized_laplacian = compute_normalized_laplacian(laplacian)
    n = size(laplacian, 1);
    normalized_laplacian = zeros(n, n);
    
    for i = 1:n
        d_i = laplacian(i, i);
        if (laplacian(i, i) ~= 0)
            normalized_laplacian(i, i) = 1;
        end
        for j = (i+1):n
            if (laplacian(i, j) ~= 0)
                normalized_laplacian(i, j) = laplacian(i, j) / (sqrt(laplacian(i, i)) * sqrt(laplacian(j, j)));  
                normalized_laplacian(j, i) = normalized_laplacian(i, j);
            end
        end
    end
    
end