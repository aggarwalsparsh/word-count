
import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SPARSH
 */
public class wordCount
{
  static class myMapper extends Mapper<Object,Text,Text,IntWritable>
  
  {

        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException
        {
         StringTokenizer tokens=new StringTokenizer(value.toString());  
         while(tokens.hasMoreTokens())
         {
             String s=tokens.nextToken();
             context.write(new Text(s),new IntWritable(1));
         }//To change body of generated methods, choose Tools | Templates.
        }
      
  }
  static class myReducer extends Reducer<Text,IntWritable,Text,IntWritable>
  {

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException 
        {
          int sum=0;
          for(IntWritable x:values)
          {
              int y=x.get();
              sum=sum+y;
              
          }//T
          context.write(key, new IntWritable(sum));
        }

  }
    public static void main(String[] args) throws Exception
    {
      Job j=new Job(new Configuration(),"WC");  
      j.setJarByClass(wordCount.class);
      j.setMapperClass(myMapper.class);
      j.setCombinerClass(myReducer.class);
      j.setReducerClass(myReducer.class);
      j.setMapOutputKeyClass(Text.class);
      j.setMapOutputValueClass(IntWritable.class);
      j.setOutputKeyClass(Text.class);
      j.setOutputValueClass(IntWritable.class);
      Path pin=new Path(args[0]);
      Path pout=new Path(args[1]);
      FileInputFormat.addInputPath(j, pin);
      FileOutputFormat.setOutputPath(j, pout);
      System.exit(j.waitForCompletion(true)?0:1);
      
      
    }
}
