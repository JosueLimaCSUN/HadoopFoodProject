import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class RecipeReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    private Text minRecipe = new Text("Recipe with Least Ingredients:");
    private Text maxRecipe = new Text("Recipe with Most Ingredients:");
    private IntWritable minCount = new IntWritable(Integer.MAX_VALUE);
    private IntWritable maxCount = new IntWritable(Integer.MIN_VALUE);

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        for (IntWritable value : values) {
            int count = value.get();

            // Check for minimum count
            if (count < minCount.get()) {
                minCount.set(count);
                minRecipe.set("Least: " + key.toString());
            }

            // Check for maximum count
            if (count > maxCount.get()) {
                maxCount.set(count);
                maxRecipe.set("Most: " + key.toString());
            }
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        // Emit the recipes with the least and most ingredients
        context.write(minRecipe, minCount);
        context.write(maxRecipe, maxCount);
    }
}

