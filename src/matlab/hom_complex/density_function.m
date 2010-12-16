function [f,g] = density_function(D,sigma,x)
% DENSITY_FUNCTION N-dimensional (symmetric) gaussian density estimation
% function. This returns -1 times the function value so that minimizing it
% actually finds the maximimum.
%
% INPUT:    D           n x d matrix of n d-dimensional points
%           sigma       standard deviation
%           x           function argument
%
% OUTPUT:   f           the scalar function value at x
%           g           the vector gradient value at x

    n = size(D,1);

    % Compute the value of the function at x
    f = 0; g = x*0;
    for i=1:n
        y = D(i,:);
        f = f + evalfun(x,y,sigma);
        g = g + evalgrad(x,y,sigma);
    end

    % Compute the gradient if it is needed
    if nargout > 1
    end 

end

function g = evalgrad(x,y,sigma) 
    g = (x-y)*exp(-norm(x-y)^2/(2*sigma^2))/(sqrt(2*pi)*sigma^3);
end

function f = evalfun(x,y,sigma)
    f = -exp(-norm(x-y)^2/(2*sigma^2))/sqrt(2*pi*sigma^2);
end