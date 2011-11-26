function [Y] = spectral_embedding(graph, k)

laplacian = double(edu.stanford.math.plex4.graph.utility.GraphUtility.getLaplacianMatrix(graph));
normalized_laplacian = compute_normalized_laplacian(laplacian);

[V, D] = eig(normalized_laplacian);

last_eigenvectors = V(:, 1:k);

Y = zeros(size(last_eigenvectors, 1), size(last_eigenvectors, 2));

for i=1:size(last_eigenvectors, 1)
    n = sqrt(sum(last_eigenvectors(i, :) .^ 2));
    if (n ~= 0)
        Y(i,:) = last_eigenvectors(i, :) ./ n;
        %Y(i,:) = last_eigenvectors(i, :);
    end
end

end