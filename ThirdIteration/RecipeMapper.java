import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.conf.Configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RecipeMapper extends Mapper<LongWritable, Text, Text, Text> {

    private List<String> searchTerms; // List of search terms
    private Text csvKey = new Text(); // Recipe title
    private Text csvValue = new Text(); // Ingredients and directions as CSV row

    @Override
    protected void setup(Context context) {
        Configuration conf = context.getConfiguration();
        String terms = conf.get("search.terms", ""); // Search terms as a comma-separated list
        searchTerms = Arrays.asList(terms.toLowerCase().split(","));
    }

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); // Safely split CSV fields

        if (fields.length >= 3) {
            String title = fields[0].replaceAll("\"", "").trim();
            String ingredients = fields[1].replaceAll("\"", "").toLowerCase().trim();
            String directions = fields[2].replaceAll("\"", "").trim();

            // Check if all search terms are present in the ingredients
            boolean allTermsPresent = searchTerms.stream()
                                                .allMatch(term -> ingredients.contains(term.trim()));

            if (allTermsPresent) {
                // Format the output as a clean CSV row
                csvKey.set(title);
                csvValue.set(String.format("\"%s\",\"%s\"", ingredients, directions));
                context.write(csvKey, csvValue);
            }
        }
    }
}

