% This script prepares the javaplex library for use

clc; clear all; close all;
clear import;

javaaddpath('./lib/javaplex.jar');

cd './utility';
addpath(pwd);
cd '..';

import edu.stanford.math.plex4.*;