import math
import argparse
import os
from os import path
import pandas as pd
import matplotlib.pyplot as plt
from matplotlib.lines import Line2D
import seaborn as sns
sns.set()

sns.set_style("whitegrid")

def plot(df: pd.DataFrame, output_location: str):
  figure = plt.figure(figsize=(10, 6))
  sns.lineplot(x='length_of_input', y='time_basic', color="b", marker="o", data=df)
  sns.lineplot(x='length_of_input', y='time_memory_efficient', marker="o", color="g", data=df)
  plt.title("Input Length vs Time", fontsize=15)
  plt.xlabel ("Length of Input")
  plt.ylabel ("Time (seconds)")
  plt.legend(handles=[ Line2D([], [], color="blue", label="Basic Version") ,Line2D([], [], color="green", label="Memory Efficient Version")])
  plt.savefig(f"{output_location}/time_plot.jpeg")

  figure = plt.figure(figsize=(10, 6))
  sns.lineplot(x='length_of_input', y='memory_basic', data=df, color="b", marker="o")
  sns.lineplot(x='length_of_input', y='memory_memory_efficient', data=df, color="g", marker="o")
  plt.title("Input Length vs Memory", fontsize=15)
  plt.xlabel ("Length of Input")
  plt.ylabel ("Memory (kilobyte)")
  plt.legend(handles=[ Line2D([], [], color="blue", label="Basic Version") ,Line2D([], [], color="green", label="Memory Efficient Version")])
  plt.savefig(f"{output_location}/memory_plot.jpeg")


def main(input_location: str, output_location: str, output_directory: str):

  # If the input location does not exists or has no files, end the program
  if not path.isdir(input_location):
    print("The input location is not valid. Please retry with different location.")
    exit(1)

  if len(os.listdir(input_location)) == 0:
    print("No files are present in the input directory. Please check the directory and try again.")
    exit(1)

  # Create output location if it is not already present
  if not path.isdir(output_location):
    os.mkdir(output_location)

  mn_values = list()

  # Create list of input files
  input_files = [ f"{input_location}/{file_name}" for file_name in sorted(os.listdir(input_location))]

  # Create list of final string lengths m*n
  for input_file in input_files:

    with open(input_file, mode="r", encoding="utf-8") as basic_file:

      lines_in_basic_file = basic_file.readlines()

      length_of_first_base_string = len(lines_in_basic_file[0].strip())
      lines_for_first_base_string = 0

      index = 1
      while True:
        if lines_in_basic_file[index].strip().isdigit():
          lines_for_first_base_string += 1
        else:
          break
        index += 1

      length_of_second_base_string = len(lines_in_basic_file[index].strip())
      lines_for_second_base_string = 0

      index += 1
      while True:
        if lines_in_basic_file[index].strip().isdigit():
          lines_for_second_base_string += 1
        index += 1
        if index == len(lines_in_basic_file):
          break

      mn_values.append(math.pow(2, lines_for_first_base_string) + length_of_first_base_string * math.pow(2, lines_for_second_base_string) * length_of_second_base_string)
    # Completed calculating mn values for all input files

  # Create list of time in seconds
  # Create list of memory in KB (Kilobytes)
  time_values_basic = list()
  time_values_memory_efficient = list()
  memory_values_basic = list()
  memory_values_memory_efficient = list()

  # Create list of output files
  basic_output_files = [ f"{output_location}/basic/{file_name}" for file_name in sorted(os.listdir(f"{output_location}/basic"))]
  memory_efficient_output_files = [ f"{output_location}/memory_efficient/{file_name}" for file_name in sorted(os.listdir(f"{output_location}/memory_efficient"))]

  for files in zip(basic_output_files, memory_efficient_output_files):

    with open(files[0], mode="r", encoding="utf-8") as basic_file, open(files[1], mode="r", encoding="utf-8") as memory_efficient_file:
      lines_in_basic_file = basic_file.readlines()
      time_values_basic.append(float(lines_in_basic_file[3]))
      memory_values_basic.append(float(lines_in_basic_file[4]))

      lines_in_memory_efficient_file = memory_efficient_file.readlines()
      time_values_memory_efficient.append(float(lines_in_memory_efficient_file[3]))
      memory_values_memory_efficient.append(float(lines_in_memory_efficient_file[4]))

  # Create dataframe from the collected data
  df = pd.DataFrame({
    "length_of_input": mn_values,
    "time_basic": time_values_basic,
    "time_memory_efficient": time_values_memory_efficient,
    "memory_basic": memory_values_basic,
    "memory_memory_efficient": memory_values_memory_efficient
  })

  # Plot
  plot(df, output_directory)

  print("Plotting Done!")


if __name__ == "__main__":

  parser = argparse.ArgumentParser()
  parser.add_argument("-in", "--input-location", type=str, default="./../input", help="Location of the directory that contains the input files")
  parser.add_argument("-op", "--output-location", type=str, default="./../output", help="Location of the directory that contains the output files")
  parser.add_argument("-ol", "--output-directory", type=str, default="./../plots", help="Location where plots needs to be saved")
  args = parser.parse_args()

  main(args.input_location, args.output_location, args.output_directory)