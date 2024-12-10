import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class RecipeMapper extends Mapper<LongWritable, Text, Text, Text> {

    private Text recipeTitle = new Text();
    private Text recipeData = new Text();

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // Convert the input value (line) to a string
        String line = value.toString();

        // Split the line into fields using regular expression to handle quoted commas
        // We split by commas that are not inside quotes
        String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        // Ensure that we have at least the necessary fields: title, ingredients, and directions
        if (fields.length >= 3) {
            // Remove leading/trailing quotes from the fields
            String title = fields[0].replaceAll("\"", "").trim();  // Clean quotes from title
            String ingredients = fields[1].replaceAll("\"", "").trim();  // Clean quotes from ingredients
            String directions = fields[2].replaceAll("\"", "").trim();  // Clean quotes from directions

            // Search for the word "onion" in the ingredients (case-insensitive)
            if (ingredients.toLowerCase().contains("egg")) {
                recipeTitle.set(title);

                // Combine the ingredients and directions into one string to pass to the reducer
                recipeData.set("Ingredients: " + ingredients + " Directions: " + directions);

                // Output title, ingredients, and directions
                context.write(recipeTitle, recipeData);
            }
        }
    }
}
