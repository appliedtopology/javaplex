clc; clear; close all;

m = 10;
n = 1000;

B = randn(m, n);
C = randn(m, n);
x = randn(n*n, 1);

tic()
y_1 = fast_kron_mult(B, C, x);
toc()

tic()
y_2 = kron(B,C)*x;
toc()

max(abs(y_1 - y_2))