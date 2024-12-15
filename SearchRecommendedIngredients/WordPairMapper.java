import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.conf.Configuration;

import java.io.IOException;

public class WordPairMapper extends Mapper<Object, Text, Text, IntWritable> {

    private String targetIngredient;
    private final static IntWritable one = new IntWritable(1);
    private Text ingredient = new Text();

    @Override
    protected void setup(Context context) {
        Configuration conf = context.getConfiguration();
        targetIngredient = conf.get("target.ingredient", "").toLowerCase();
    }

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        if (fields.length >= 3) {
            String ingredientsRaw = fields[1].replaceAll("[\\[\\]\"]", "").toLowerCase();
            String[] ingredients = ingredientsRaw.split(",");

            boolean containsTarget = false;

            // Check if the target ingredient exists
            for (String ing : ingredients) {
                if (ing.trim().contains(targetIngredient)) {
                    containsTarget = true;
                    break;
                }
            }

            // Emit other ingredients if the target is present
            if (containsTarget) {
                for (String ing : ingredients) {
                    String cleaned = ing.trim();
                    if (!cleaned.isEmpty() && !cleaned.equals(targetIngredient)) {
                        ingredient.set(cleaned);
                        context.write(ingredient, one);
                    }
                }
            }
        }
    }
}

