import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashSet;

public class RecipeReducer extends Reducer<Text, Text, Text, Text> {

    private Text csvRow = new Text();

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        HashSet<String> uniqueValues = new HashSet<>();

        for (Text value : values) {
            uniqueValues.add(value.toString());
        }

        for (String value : uniqueValues) {
            csvRow.set(String.format("\"%s\",\"%s\"", key.toString(), value));
            context.write(null, csvRow);
        }
    }
}

