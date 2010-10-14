function v = original_objective(P)
    v1 = max(sum(abs(P))) + max(sum(abs(P')));
    v2 = sum(max(abs(P))) + sum(max(abs(P')));
    v3 = sum(sum(abs(P))) + sum(sum(abs(P')));
    v = v1;