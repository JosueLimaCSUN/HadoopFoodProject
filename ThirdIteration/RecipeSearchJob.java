import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class RecipeSearchJob {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("Usage: RecipeSearchJob <input path> <output path> <search terms (comma-separated)>");
            System.exit(-1);
        }

        Configuration conf = new Configuration();
        conf.set("search.terms", args[2]);

	conf.setInt("mapreduce.map.cpu.vcores",4);
	conf.setInt("mapreduce.recude.cpu.vcores",4);


        Path outputPath = new Path(args[1]);
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);
            System.out.println("Deleted existing output path: " + outputPath);
        }

        Job job = Job.getInstance(conf, "Recipe Search");
        job.setJarByClass(RecipeSearchJob.class);

        job.setMapperClass(RecipeMapper.class);
        job.setReducerClass(RecipeReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, outputPath);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}


