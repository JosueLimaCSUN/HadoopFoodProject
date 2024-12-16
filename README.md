# HadoopFoodProject
Final for COMP 535 Hadoop

Refering to Hadoop documentation may be necessary to understand some of the filesystem commands needed.

Setup steps:

1) Have Hadoop 3.4.0 or later installed.
2) Ensure the Hadoop environment is running. hadoop/sbin/start-all.sh can be executed to launch all necessary processes.
3) Move any necessary input files to HDFS. hdfs dfs -put [local/path/to/source] [/destination]
4) Compile the class files to ensure they have been built with the compatible version of hadoop libraries for your platform. javac -classpath $(hadoop classpath) -d classes RecipeMapper.java RecipeReducer.java RecipeSearchJob.java
5) Create the jar file. jar -cvf RecipeSearch.jar -C classes/ .

Running the RecipeSearchJob:

Note: The output path and file name will be the primary way to discern which job those results correspond to so name them thoughtfully. I.e. /output/milk_egg_search_1 if the ingredients being searched for are milk and eggs and this is your first search of that batch

hadoop jar RecipeSearch.jar RecipeSearchJob [/hdfs/abs/path/input] [/hdfs/abs/path/output]
ex: hadoop jar RecipeSearch.jar RecipeSearchJob /input/recipes_data.csv /output/results

Retrieving the Results:

Note: The output on hdfs will be an untyped file and could be multiple files with enough results. Name the local file(s) you copy them to thoughtfully or you may have to change the file extensions after the copy.

hdfs dfs -get [/hdfs/path/to/program/output] [local/path/to/copy/output]
ex: hdfs dfs -get /output/bacon_search_1/part-r-00000 ./csvoutput/bacon_search_1.csv
