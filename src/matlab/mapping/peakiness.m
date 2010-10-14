function x = peakiness(c, candidate_map, homotopies)
    Q = compute_chain_map(c, candidate_map, homotopies);

    %x = 1 ./ sum(sum(Q.^2));
    %x = sum(sum(integral_penalty(Q)));
    
    x = -sum(sum(Q.^2)) / sum(sum(abs(Q)));