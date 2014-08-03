#!/bin/bash

pdflatex javaplex_tutorial
bibtex javaplex_tutorial
pdflatex javaplex_tutorial
pdflatex javaplex_tutorial

mkdir ../../dist/
cp javaplex_tutorial.pdf ../../dist/

./cleanup.sh

echo 'PDF output has been copied to ../../dist/javaplex_tutorial.pdf'