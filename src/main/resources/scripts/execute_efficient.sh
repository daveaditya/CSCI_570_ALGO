#!/bin/sh
filename="$1"
if [ [ -e $filename ]]; then
    java -jar algo_project_efficient.jar $filename
else
    echo "Please provide a file."
fi
