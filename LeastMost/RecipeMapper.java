import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class RecipeMapper extends Mapper<Object, Text, Text, IntWritable> {

    private Text recipeTitle = new Text();
    private IntWritable ingredientCount = new IntWritable();

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); // Split safely for CSV

        if (fields.length >= 3) {
            String title = fields[0].replaceAll("\"", "").trim();
            String ingredients = fields[1].replaceAll("\"", "").trim();

            // Count the number of ingredients (split by commas)
            int count = ingredients.split(",").length;

            // Emit the recipe title and the count of ingredients
            recipeTitle.set(title);
            ingredientCount.set(count);
            context.write(recipeTitle, ingredientCount);
        }
    }
}

