function matlab_sparse_matrix = to_sparse_matlab_matrix(formal_sum, matrix_converter)
% matlab_sparse_matrix = to_sparse_matlab_matrix(formal_sum, matrix_converter)
%
% INPUTS:
% formal_sum: an object of type DoubleSparseFormalSum<ObjectObjectPair<M, N>>
% matrix_converter: the object of type DoubleMatrixConverter<M, N>
%
% OUTPUTS:
% matlab_sparse_matrix: a sparse matrix which represents the same linear
% transformation as the given formal sum

    import edu.stanford.math.plex4.*;
    
    sparse_matrix_object = matrix_converter.toSparseMatrix(formal_sum);
    matlab_sparse_matrix = convert_sparse_matrix(sparse_matrix_object);
end