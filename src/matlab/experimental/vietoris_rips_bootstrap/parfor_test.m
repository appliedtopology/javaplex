clc; clear all; close all;

n = 100;
k = 2000;

%%
tic();
for i = 1:n
    values = randn(k, k);
end
toc()

%%
tic();
parfor i = 1:n
    values = randn(k, k);
end
toc()
