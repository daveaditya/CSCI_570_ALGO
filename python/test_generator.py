import argparse
import os
import random
from os import path

random.seed(42)

ALPHABETS = "ACGT"

def main(output_location, number_of_cases):

  # If the location does not exists, create it
  if not path.isdir(output_location):
    os.mkdir(output_location)

  len_of_zero_padding = len(str(number_of_cases))

  # Create test files as much as the number of cases
  for i in range(number_of_cases):

    # Create a file with input cases
    with open(f"{output_location}/input{i:0>{len_of_zero_padding}}.txt", "w", encoding="utf-8") as file:

      for _ in range(2):
        # Write first string
        file.write(f"{''.join(random.sample(ALPHABETS, len(ALPHABETS)))}\n")

        # Write indices
        length = len(ALPHABETS)
        for _ in range(random.randint(0, 100)):
          index = random.randint(0, length % 100)
          length += index
          file.write(f"{index}\n")
      # We're done

  print(f"Generation of test files done at :: {output_location}")



if __name__ == "__main__":

  parser = argparse.ArgumentParser()
  parser.add_argument("-ol", "--output-location", type=str, default="./output", help="Location of the direrctory where to output the test files. Do not end with a slash.")
  parser.add_argument("-n", "--number-of-cases", type=int, default=16, help="Number of test files to generate.")
  args = parser.parse_args()
  
  main(args.output_location, args.number_of_cases)