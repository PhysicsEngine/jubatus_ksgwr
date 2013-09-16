import java.util.ArrayList;
import java.util.List;

import us.jubat.classifier.ClassifierClient;
import us.jubat.classifier.Datum;
import us.jubat.classifier.TupleStringDatum;
import us.jubat.classifier.TupleStringDouble;
import us.jubat.classifier.TupleStringString;

public class Client {
	public static final String HOST = "localhost";
	public static final int PORT = 9199;
	public static final String NAME = "test";
	public static final int TIMEOUT = 5;

	/*
	public void start() throws Exception {
		RecommenderClient r = new RecommenderClient(HOST, PORT, 5);
		Datum d = new Datum();
		d.string_values = new ArrayList<TupleStringString>();
		d.num_values = new ArrayList<TupleStringDouble>();

		TupleStringDouble tuple1, tuple2, tuple3;

		// user01
		tuple1 = new TupleStringDouble();
		tuple1.first = "movie_A";
		tuple1.second = 5;
		d.num_values.add(tuple1);

		tuple2 = new TupleStringDouble();
		tuple2.first = "movie_B";
		tuple2.second = 2;
		d.num_values.add(tuple2);

		tuple3 = new TupleStringDouble();
		tuple3.first = "movie_C";
		tuple3.second = 3;
		d.num_values.add(tuple3);

		r.update_row(NAME, "user01", d);

		// user02
		tuple1 = new TupleStringDouble();
		tuple1.first = "movie_A";
		tuple1.second = 2;
		d.num_values.add(tuple1);

		tuple2 = new TupleStringDouble();
		tuple2.first = "movie_B";
		tuple2.second = 5;
		d.num_values.add(tuple2);

		tuple3 = new TupleStringDouble();
		tuple3.first = "movie_C";
		tuple3.second = 1;
		d.num_values.add(tuple3);

		r.update_row(NAME, "user02", d);

		// user03
		tuple1 = new TupleStringDouble();
		tuple1.first = "movie_A";
		tuple1.second = 5;
		d.num_values.add(tuple1);

		tuple2 = new TupleStringDouble();
		tuple2.first = "movie_B";
		tuple2.second = 1;
		d.num_values.add(tuple2);

		tuple3 = new TupleStringDouble();
		tuple3.first = "movie_C";
		tuple3.second = 4;
		d.num_values.add(tuple3);

		r.update_row(NAME, "user03", d);

		List<String> rows = r.get_all_rows(NAME);
		for (String id : rows) {
			List<TupleStringFloat> result;
			result = r.similar_row_from_id(NAME, id, 3);
			System.out.println(id + " is similar to: ");
			System.out.print("  ");
			for (TupleStringFloat tuple : result) {
				System.out.print(tuple.first + " (" + tuple.second + "), ");
			}
			System.out.println();
		}
	}*/

	private static final Datum createDatum(String message) {
		Datum datum = new Datum();
		datum.string_values = new ArrayList<TupleStringString>();
		datum.num_values = new ArrayList<TupleStringDouble>();

		//素性値はデータ値のみ
		TupleStringString tss = new TupleStringString();
		tss.first = "message";
		tss.second = message;

		datum.string_values.add(tss);

		return datum;
	}

	public static void main(String[] args) throws Exception {
		//new Client().start();
		//System.exit(0);

		String message="ただいま";
		String reply="おかえり";

		List<TupleStringDatum> trains = new ArrayList<TupleStringDatum>();

		TupleStringDatum train = new TupleStringDatum();

		Datum datum = createDatum(message);

		train.first = reply;
		train.second = datum;

		trains.add(train);

		ClassifierClient client = new ClassifierClient(HOST, PORT, TIMEOUT);

		client.train(NAME, trains);

		System.out.println("train");
	}
}
