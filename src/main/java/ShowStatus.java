import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class ShowStatus implements Secret{

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO 自動生成されたメソッド・スタブ
		Configuration configuration = new ConfigurationBuilder().setOAuthConsumerKey(CONSUMER_KEY)
                .setOAuthConsumerSecret(CONSUMER_SECRET)
                .setOAuthAccessToken(ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET)
                .build();

		Twitter twitter = new TwitterFactory(configuration).getInstance();

		BufferedReader br = new BufferedReader(new FileReader("replydata"));
		BufferedWriter bw = new BufferedWriter(new FileWriter("traindata"));

		while(br.ready()){
			String[] fields = br.readLine().split("\t");
			if(fields.length>2) {
				try{
					Long statusId = Long.parseLong(fields[1]);
					if(statusId>0){
						Status status = twitter.showStatus(statusId);
						StringBuilder sb = new StringBuilder()
							.append(fields[2])
							.append("\t")
							.append(status.getText().replaceAll("\n", ""))
							.append("\n");
						System.out.println(sb.toString());
						bw.write(sb.toString());
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		br.close();
		bw.close();
	}

}
