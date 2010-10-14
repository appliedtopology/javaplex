function [phi] = compute_cycle(phi_0, h_set, c)
    n = length(c);    
    phi = phi_0;
    for i = 1:n
        phi = phi + c(i) * h_set(:, :, i);
    end