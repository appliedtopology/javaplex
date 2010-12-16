function distances = pdist2(points)
    n = size(points, 1);
    distances = zeros(n * (n - 1) /2, 1);
    index = 1;
    for i = 1:n
        for j = i+1:n
            distances(index) = sqrt(sum((points(i, :) - points(j, :)).^2));
            index = index + 1;
        end
    end
end