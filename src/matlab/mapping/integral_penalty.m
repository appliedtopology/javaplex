function p = integral_penalty(X)
    p = sum(sum(X.^2 .* (X - 1).^2));