import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

public class RecipeReducer extends Reducer<Text, Text, Text, Text> {

    private Text result = new Text();

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        // Here we just combine the values into one, assuming each key has only one associated value
        StringBuilder outputValue = new StringBuilder();
        
        // Aggregate all values for the current recipe title (if multiple values per title)
        for (Text val : values) {
            outputValue.append(val.toString()).append("\n");
        }
        
        // Set the result to the aggregated output
        result.set(outputValue.toString());
        // Write the result to the context (the key is the title, and value is ingredients + directions)
        context.write(key, result);
    }
}
