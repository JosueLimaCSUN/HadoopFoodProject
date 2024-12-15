import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class RecipeSearchJob extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = getConf();

        // Set up the job
        Job job = Job.getInstance(conf, "Recipe Search by Onion");
        job.setJarByClass(RecipeSearchJob.class);

        // Set the Mapper and Reducer classes
        job.setMapperClass(RecipeMapper.class);
        job.setReducerClass(RecipeReducer.class);

        // Set the output key and value classes
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // Set input and output paths
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // Wait for job to complete
        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

        if (otherArgs.length != 2) {
            System.err.println("Usage: RecipeSearchJob <in> <out>");
            System.exit(2);
        }

        // Run the job using ToolRunner
        int exitCode = ToolRunner.run(conf, new RecipeSearchJob(), otherArgs);
        System.exit(exitCode);
    }
}
