import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.conf.Configuration;  // Import for accessing user input
import java.io.IOException;

public class RecipeMapper extends Mapper<LongWritable, Text, Text, Text> {

    private Text recipeTitle = new Text();
    private Text recipeData = new Text();
    private String searchTerm = "";  // Store the user input search term

    @Override
    protected void setup(Context context) {
        // Retrieve the search term from the job configuration
        Configuration conf = context.getConfiguration();
        searchTerm = conf.get("search.term", "").toLowerCase();  // Default is an empty string
    }

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // Convert the input value (line) to a string
        String line = value.toString();

        // Split the line into fields using regular expression to handle quoted commas
        String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        // Ensure that we have at least the necessary fields: title, ingredients, and directions
        if (fields.length >= 3) {
            String title = fields[0].replaceAll("\"", "").trim();
            String ingredients = fields[1].replaceAll("\"", "").trim();
            String directions = fields[2].replaceAll("\"", "").trim();

            // Search for the user-provided search term in the ingredients (case-insensitive)
            if (!searchTerm.isEmpty() && ingredients.toLowerCase().contains(searchTerm)) {
                recipeTitle.set(title);

                // Combine ingredients and directions
                recipeData.set("Ingredients: " + ingredients + " Directions: " + directions);

                // Emit title and recipe data
                context.write(recipeTitle, recipeData);
            }
        }
    }
}

