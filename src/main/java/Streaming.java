
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class Streaming implements Secret{

    static class MyStatusListener implements StatusListener {

    	Writer writer;

    	public MyStatusListener(Writer writer) {
    		this.writer = writer;
    	}

        public void onStatus(Status status) {
            String message = status.getText();
            //reply include and japan
            if(message.matches("^@[a-zA-Z0-9]+[ 　].*$") && status.getUser().getLang().equals("ja")) {
            	try{

            		StringBuilder sb = new StringBuilder()
            			.append(status.getInReplyToUserId())
            			.append("\t")
            			.append(status.getInReplyToStatusId())
            			.append("\t")
            			.append(message)
            			.append("\n");
            		writer.append(sb.toString());
            		System.out.print(sb.toString());
            	}catch(Exception e){
            		e.printStackTrace();
            	}
            }

        }

        public void onDeletionNotice(StatusDeletionNotice sdn) {

        }

        public void onTrackLimitationNotice(int i) {

        }

        public void onScrubGeo(long lat, long lng) {

        }

        public void onException(Exception excptn) {

        }

		public void onStallWarning(StallWarning warning) {
			// TODO 自動生成されたメソッド・スタブ

		}

    }

    public static void main(String[] args) throws Exception {

    	File file = new File("replydata");
    	BufferedWriter bw = new BufferedWriter(new FileWriter(file));

        Configuration configuration = new ConfigurationBuilder().setOAuthConsumerKey(CONSUMER_KEY)
                .setOAuthConsumerSecret(CONSUMER_SECRET)
                .setOAuthAccessToken(ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET)
                .build();

        TwitterStream twStream = new TwitterStreamFactory(configuration).getInstance();
        twStream.addListener(new MyStatusListener(bw));

        // フィルターを設定する
        FilterQuery filter = new FilterQuery();
        filter.track(new String[]{"@"});
        twStream.filter(filter);

        twStream.sample();

        Thread.sleep(1000*60*1);

        twStream.shutdown();
        bw.close();
    }
}