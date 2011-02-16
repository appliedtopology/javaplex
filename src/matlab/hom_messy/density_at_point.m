function [ density ] = density_at_point(cluster_center,cluster_density,point)
%DENSITY_AT_POINT Summary of this function goes here
%   Detailed explanation goes here
if (cluster_density==0)
    density = 0;
    return;
end

s = cluster_density;
coef = 1/sqrt(2*pi*s^2);
density = coef*exp(-norm(cluster_center - point)^2 / (2*s^2));
