#!/bin/sh
echo "Please enter the input filename:"
read filename

if [ -f "$filename" ]; then
    java -jar algo_project_efficient.jar $filename
else
    echo "$filename does not exist."
fi
