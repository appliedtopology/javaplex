function g = gradient(f, x, h)
    if (nargin < 3)
        h = 1e-5;
    end
    h_inv = 1 / h;
    [m, n] = size(x);
    g = zeros(m, n);
    f_x = f(x);
    for i = 1:m
        for j = 1:n
            x_new = x;
            x_new(i, j) = x_new(i, j) + h;
            g(i, j) = f(x_new) - f_x;
        end
    end
    g = g * h_inv;
end