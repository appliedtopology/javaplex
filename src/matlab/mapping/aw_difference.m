function result = aw_difference(map, sigma, domain_aw_maps, codomain_aw_maps)
    %{
    map = abs(map)
    tensor_map = kron(map, map);
    aw_sigma = domain_aw_maps * sigma
    f_sigma = map * sigma
    aw_f_sigma = codomain_aw_maps * f_sigma
    f_aw_sigma = tensor_map * aw_sigma
    result = aw_f_sigma - f_aw_sigma
    %}
    Q = cell(2, 1);
    Q{1} = map;
    Q{2} = map;
    result = codomain_aw_maps * (map * sigma) - kronmult(Q, (domain_aw_maps * sigma));
end