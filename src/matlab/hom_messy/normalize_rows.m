function result = normalize_rows(matrix)
    result = diag(1.0 ./ sum(abs(matrix), 2)) * matrix;