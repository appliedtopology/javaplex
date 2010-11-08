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
    domain_converter = matrix_converter.getDomainRepresentation();
    codomain_converter = matrix_converter.getCodomainRepresentation();
    m = codomain_converter.getDimension();
    n = domain_converter.getDimension();
    i = sparse_matrix_object.getRows()' + 1;
    j = sparse_matrix_object.getColumns()' + 1;    
    s = sparse_matrix_object.getValues()';
    matlab_sparse_matrix = sparse(m, n);
    for k = 1:length(i)
        matlab_sparse_matrix(j(k), i(k)) = s(k);
    end
end