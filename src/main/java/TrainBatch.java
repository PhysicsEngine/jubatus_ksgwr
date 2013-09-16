import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import us.jubat.classifier.ClassifierClient;
import us.jubat.classifier.Datum;
import us.jubat.classifier.TupleStringDatum;
import us.jubat.classifier.TupleStringDouble;
import us.jubat.classifier.TupleStringString;


public class TrainBatch {

	public static final String HOST = "127.0.0.1";
	public static final int PORT = 9199;
	public static final int TIMEOUT = 5;

	/** Jubatus Application Name */
	public static final String NAME = "twitterReply";

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

	public List<TupleStringDatum> createOneTrain(String message,String reply) {
		List<TupleStringDatum> trains = new ArrayList<TupleStringDatum>();
		TupleStringDatum train = new TupleStringDatum();

		Datum datum = createDatum(message);

		train.first = reply;
		train.second = datum;

		trains.add(train);
		return trains;
	}

	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader("traindata"));

		List<TupleStringDatum> trains = new ArrayList<TupleStringDatum>();

		while (br.ready()) {
			String fields[] = br.readLine().split("\t");
			TupleStringDatum train = new TupleStringDatum();

			Datum datum = createDatum(fields[0]);

			train.first = fields[1];
			train.second = datum;

			trains.add(train);
		}

		ClassifierClient client = new ClassifierClient(HOST, PORT, TIMEOUT);

		client.train(NAME, trains);

		System.out.println("train data");

	}
}
