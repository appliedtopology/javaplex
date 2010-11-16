function y = fast_kron_mult(B, C, x)
% y = fast_kron_mult(B, C, x)
%
% This function executes the operation y = kron(B *C) * x asymptotically faster
% by instead calculating C * X * B', where X is a reshaped matrix version
% of x.
% 
% INPUTS:
% B: a matrix of size m x n
% C: a matrix of size m x n
% x: a vector of length n^2
%
% OUTPUTS:
% the vector which is equal to y = kron(B, C) * x
    m = size(B, 1);
    n = size(B, 2);
    y = reshape(C*reshape(x,n,n)*B',m*m,1);
end