% This script shows a sublevelset persistence example - Section 7.6

clc; clear; close all;
import edu.stanford.math.plex4.*;

%% Example: Random input
filename='random';
M=rand(100,100);
intervals_sub = sublevelset_persistence(M,filename)
