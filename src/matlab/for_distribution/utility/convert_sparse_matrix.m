function matlab_sparse_matrix = convert_sparse_matrix(sparse_primitivelib_matrix)
% matlab_sparse_matrix = convert_sparse_matrix(formal_sum, matrix_converter)
%
% INPUTS:
% sparse_primitivelib_matrix: an object of type DoubleSparseMatrix or
% IntSparseMatrix
%
% OUTPUTS:
% matlab_sparse_matrix: a matlab sparse matrix equivalent of the java
% sparse matrix

    import edu.stanford.math.plex4.*;
    
    m = sparse_primitivelib_matrix.getNumRows();
    n = sparse_primitivelib_matrix.getNumColumns();
    i = sparse_primitivelib_matrix.getRows()' + 1;
    j = sparse_primitivelib_matrix.getColumns()' + 1;    
    s = sparse_primitivelib_matrix.getValues()';
    matlab_sparse_matrix = sparse(m, n);
    for k = 1:length(i)
        matlab_sparse_matrix(i(k), j(k)) = s(k);
    end
end