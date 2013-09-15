package controllers;

import java.util.ArrayList;
import java.util.List;

import play.mvc.Controller;
import play.mvc.Result;
import us.jubat.classifier.ClassifierClient;
import us.jubat.classifier.Datum;
import us.jubat.classifier.EstimateResult;
import us.jubat.classifier.TupleStringDatum;
import us.jubat.classifier.TupleStringDouble;
import us.jubat.classifier.TupleStringString;
import views.html.index;

public class Application extends Controller {

	public static final String HOST = "127.0.0.1";
	public static final int PORT = 9199;
	public static final int TIMEOUT = 5;

	/** Jubatus Application Name */
	public static final String NAME = "twitterReply";

	public static Result index() {
		return ok(index.render("Your new application is ready."));
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

	public static Result predictReply(String message) throws Exception{
		ClassifierClient client = new ClassifierClient(HOST, PORT, TIMEOUT);

		List<Datum> datalist = new ArrayList<Datum>();
		datalist.add(createDatum(message));

		String str = null;

		List<List<EstimateResult>> result = client.classify(NAME, datalist);
		if (result.size() > 0) {

			double max = 0;
			EstimateResult mostly = null;

			//分類結果の候補リスト
			for (EstimateResult er : result.get(0)) {
				//scoreが最大なものを取得
				if (er.score > max || max == 0) {
					max = er.score;
					mostly = er;
				}
			}

			if (mostly != null) {
				str = mostly.label;
			}
		}

		return ok(str);

	}

	public static Result addTrain(String message, String reply) throws Exception {

		List<TupleStringDatum> trains = new ArrayList<TupleStringDatum>();

		TupleStringDatum train = new TupleStringDatum();

		Datum datum = createDatum(message);

		train.first = reply;
		train.second = datum;

		trains.add(train);

		ClassifierClient client = new ClassifierClient(HOST, PORT, TIMEOUT);

		client.train(NAME, trains);

		return ok("add train");

	}

}
