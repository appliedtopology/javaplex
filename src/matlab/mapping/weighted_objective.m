function v = weighted_objective(P, W)
    v1 = max(sum(abs(W * P))) + max(sum(abs((W * P)')));
    v2 = sum(max(abs(W * P))) + sum(max(abs((W * P)')));
    v3 = sum(sum(abs(W * P))) + sum(sum(abs((W * P)')));
    v = v1;