function X = kron(A,B)
%KRON Kronecker product.
%   kron(A,B) returns the Kronecker product of two matrices A and B, of 
%   dimensions I-by-J and K-by-L respectively. The result is an I*J-by-K*L
%   block matrix in which the (i,j)-th block is defined as A(i,j)*B.

%   Version: 03/10/10
%   Authors: Laurent Sorber (Laurent.Sorber@cs.kuleuven.be)

[I J] = size(A);
[K L] = size(B);
if ~issparse(A) && ~issparse(B)
    A = reshape(A,[1 I 1 J]);
    B = reshape(B,[K 1 L 1]);
    X = reshape(bsxfun(@times,A,B),[I*K J*L]);
else
    [ia,ja,sa] = find(A);
    [ib,jb,sb] = find(B);
    ix = bsxfun(@plus,K*(ia-1).',ib);
    jx = bsxfun(@plus,L*(ja-1).',jb);
    if islogical(sa) && islogical(sb)
        X = sparse(ix,jx,bsxfun(@and,sb,sa.'),I*K,J*L);
    else
        X = sparse(ix,jx,double(sb)*double(sa.'),I*K,J*L);
    end
end
