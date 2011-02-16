function matrix = to_matlab_matrix(list_of_sums, stream)
    converter = edu.stanford.math.primitivelib.autogen.formal_sum.DoubleVectorConverter(stream);
    matrix = 0;
    iterator = list_of_sums.iterator();
    while (iterator.hasNext())
        sum = iterator.next();
        sparse_vector_object = converter.toSparseVector(sum);
        if (matrix == 0)
            n = list_of_sums.size();
            m = sparse_vector_object.getLength();
            matrix = sparse(m, n);
        end
        i = sparse_vector_object.getIndices()' + 1;
        s = sparse_vector_object.getValues()';
        j = 
    end
    
end