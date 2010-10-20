function [yn,indices] = totally_unimodular_test(A)
% tum(A) -- see if the matrix A is totally unimodular
% returns true (1) if so and false (0) if not.
%
% An optional second return argument is a 2-by-k matrix giving the row
% indices (top) and column indices (bottom) of a square submatrix that
% demonstrates that this matrix is not totally unimodular (or [] if the
% matrix is totally unimodular).
% 
% This works OK on matrices that aren't too large.
% Author: Edward Scheinerman (ers@jhu.edu)

[r,c] = size(A);
yn = true;
n = min(r,c);
indices = [];

for k=1:n
	rows = nchoosek(1:r,k); % all k-subsets of the rows of A
	cols = nchoosek(1:c,k); % all k-subsets of the cols of A
	
	nrows = size(rows,1);  % r choose k
	ncols = size(cols,1);  % c choose k
	
	% iterate over all submatrices of A formed by taking k rows and k
	% columns; these matrices (called B below) should have determinant
	% equal to 1, -1, or 0.
	
	for i=1:nrows
		for j=1:ncols
			rowidx = rows(i,:);
			colidx = cols(j,:);
			B = A(rowidx,colidx);
			dB = det(B);
			if ~( (dB==1) || (dB==-1) || (dB==0) )
				yn = false;
				%% disp(['Bad submatrix with determinant = ',num2str(dB),' found']);
				%% disp(B);
				indices = [rowidx;colidx];
				return;
			end
		end
	end
end
