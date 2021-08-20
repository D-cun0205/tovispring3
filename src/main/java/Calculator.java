import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

    public Integer calcSum(String filepath) throws IOException {
        System.out.println(filepath);
        BufferedReader br = new BufferedReader(new FileReader("/Users/sanghunpark/Desktop/sideProject/teeee.txt"));
        Integer sum = 0;
        String line = null;
        while((line = br.readLine()) != null) {
            sum += Integer.valueOf(line);
        }

        System.out.println(sum);
        br.close();
        return sum;
    }
}
