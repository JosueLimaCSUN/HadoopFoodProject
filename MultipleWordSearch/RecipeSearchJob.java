import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class RecipeSearchJob {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("Usage: RecipeSearchJob <input path> <output path> <search term>");
            System.exit(-1);
        }

        // Create a Hadoop Configuration and pass the search term
        Configuration conf = new Configuration();
        conf.set("search.term", args[2]);  // Set search term as a configuration parameter

        Job job = Job.getInstance(conf, "Recipe Search");
        job.setJarByClass(RecipeSearchJob.class);

        // Set Mapper and Reducer classes
        job.setMapperClass(RecipeMapper.class);
        job.setReducerClass(RecipeReducer.class);

        // Set output key and value types
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // Input and output paths
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // Run the job
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

