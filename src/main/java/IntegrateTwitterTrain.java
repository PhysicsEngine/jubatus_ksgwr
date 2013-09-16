import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import us.jubat.classifier.ClassifierClient;
import us.jubat.classifier.Datum;
import us.jubat.classifier.TupleStringDatum;
import us.jubat.classifier.TupleStringDouble;
import us.jubat.classifier.TupleStringString;


public class IntegrateTwitterTrain implements Secret{

	public static final String HOST = "127.0.0.1";
	public static final int PORT = 9199;
	public static final int TIMEOUT = 5;

	/** Jubatus Application Name */
	public static final String NAME = "twitterReply";

	public static ClassifierClient client;
	static{
		try{
		client = new ClassifierClient(HOST, PORT, TIMEOUT);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static final Datum createDatum(String message) {
		Datum datum = new Datum();
		datum.string_values = new ArrayList<TupleStringString>();
		//使わなくても初期化必須
		datum.num_values = new ArrayList<TupleStringDouble>();

		//素性値はデータ値のみ
		TupleStringString tss = new TupleStringString();
		tss.first = "message";
		tss.second = message;

		datum.string_values.add(tss);

		return datum;
	}

	public static List<TupleStringDatum> createOneTrain(String message,String reply) {
		List<TupleStringDatum> trains = new ArrayList<TupleStringDatum>();
		TupleStringDatum train = new TupleStringDatum();

		Datum datum = createDatum(message);

		train.first = reply;
		train.second = datum;

		trains.add(train);
		return trains;
	}

	 static class MyStatusListener implements StatusListener {



	    	Writer writer;
	    	Twitter twitter;

	    	public MyStatusListener(Writer writer,Twitter twitter) {
	    		this.writer = writer;
	    		this.twitter = twitter;
	    	}

	        public void onStatus(Status status) {
	            String reply = status.getText();
	            //reply include and japan
	            if(reply.matches("^@[a-zA-Z0-9]+[ 　].*$") && status.getUser().getLang().equals("ja")) {
	            	try{
	            		Status statusMessage = twitter.showStatus(status.getInReplyToStatusId());
	            		String message = statusMessage.getText();
	            		StringBuilder sb = new StringBuilder()
	            			.append(statusMessage.getText().replaceAll("\n", ""))
	            			.append("\t")
	            			.append(message)
	            			.append("\n");
	            		writer.append(sb.toString());
	            		writer.flush();
	            		client.train(NAME, createOneTrain(message, reply));
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

	public static void main(String[] args) throws Exception{
		BufferedWriter bw = new BufferedWriter(new FileWriter("train.log"));

		Configuration configuration = new ConfigurationBuilder().setOAuthConsumerKey(CONSUMER_KEY)
                .setOAuthConsumerSecret(CONSUMER_SECRET)
                .setOAuthAccessToken(ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET)
                .build();

        TwitterStream twStream = new TwitterStreamFactory(configuration).getInstance();
        Twitter twitter = new TwitterFactory(configuration).getInstance();
        twStream.addListener(new MyStatusListener(bw,twitter));

        // フィルターを設定する
        FilterQuery filter = new FilterQuery();
        filter.track(new String[]{"@"});
        twStream.filter(filter);

        twStream.sample();
	}

}
