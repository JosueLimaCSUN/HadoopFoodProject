import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordPairDriver {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("Usage: WordPairDriver <input path> <output path> <target ingredient>");
            System.exit(-1);
        }

        Configuration conf = new Configuration();
        conf.set("target.ingredient", args[2]); // Pass the target ingredient

        Job job = Job.getInstance(conf, "Ingredient Pairing Finder");
        job.setJarByClass(WordPairDriver.class);

        job.setMapperClass(WordPairMapper.class);
        job.setReducerClass(WordPairReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

